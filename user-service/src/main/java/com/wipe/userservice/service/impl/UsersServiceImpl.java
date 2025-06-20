package com.wipe.userservice.service.impl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wipe.commonmodel.AxiosResult;
import com.wipe.commonmodel.constant.TopicConstant;
import com.wipe.commonmodel.enums.EnumStatusCode;
import com.wipe.commonmodel.exception.ServiceException;
import com.wipe.commonmodel.model.dto.BasePageRequest;
import com.wipe.commonmodel.util.ThrowUtil;
import com.wipe.userservice.manager.perm.responsibility.HandleByPermManager;
import com.wipe.userservice.mapper.UsersMapper;
import com.wipe.userservice.pojo.domain.User;
import com.wipe.userservice.pojo.dto.UserLoginRequest;
import com.wipe.userservice.pojo.dto.UserQueryRequest;
import com.wipe.userservice.pojo.dto.UserRegisterRequest;
import com.wipe.userservice.pojo.dto.UserResetPasswordRequest;
import com.wipe.userservice.pojo.vo.UserVo;
import com.wipe.userservice.rpc.perm.PermissionClient;
import com.wipe.userservice.service.UsersService;
import com.wipe.userservice.util.JwtUtil;
import com.wipe.userservice.util.UserContextHolder;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author 29770
 * @description 针对表【users】的数据库操作Service实现
 * @createDate 2025-06-16 16:03:54
 */
@Service
@Slf4j
public class UsersServiceImpl extends ServiceImpl<UsersMapper, User>
        implements UsersService {

    @Resource
    private RocketMQTemplate rocketMQTemplate;

    @Resource
    private PermissionClient permissionClient;

    @Resource
    private HandleByPermManager handleByPermManager;

//    @Resource
//    @Lazy
//    private PermStrategyManager permStrategyManager;

    @Resource
    private JwtUtil jwtUtil;

    @Override
    @Transactional(rollbackFor = Exception.class)
    @GlobalTransactional(rollbackFor = Exception.class)
    public Long userRegister(UserRegisterRequest userRegisterRequest) {
        String username = userRegisterRequest.getUsername();
        String password = userRegisterRequest.getPassword();
        String confirmPassword = userRegisterRequest.getConfirmPassword();
        // 判断用户是否存在
        UserQueryRequest userQueryRequest = new UserQueryRequest();
        userQueryRequest.setUsername(username);
        boolean exists = getQueryWrapper(userQueryRequest).exists();
        ThrowUtil.throwIf(exists, EnumStatusCode.ERROR_OPERATION, "用户已存在");
        // 比对密码
        ThrowUtil.throwIf(!password.equals(confirmPassword),
                EnumStatusCode.ERROR_OPERATION, "两次输入密码不一致");
        // 密码加密（可加盐）
        User user = new User();
        user.setUsername(username);
        user.setPassword(encodePassword(password));
        LocalDateTime now = LocalDateTime.now();
        user.setGmtCreate(now);
        // 保存用户信息
        ThrowUtil.throwIf(!save(user), EnumStatusCode.ERROR_OPERATION, "注册失败");
        AxiosResult<Boolean> result = permissionClient.bindDefaultRole(user.getUserId());
        AxiosResult.check(result);
        // 发送消息记录日志
        String format = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        Map<String, Object> payload = Map.of(
                "user_id", user.getUserId(),
                "action", "user_register:" + format,
                "detail", UserVo.toUserVo(user).toString()
        );
        rocketMQTemplate.asyncSend(TopicConstant.USER_REGISTER_TOPIC, payload,
                new SendCallback() {
                    @Override
                    public void onSuccess(SendResult sendResult) {
                        log.info("用户：{}，注册消息发送成功，msgId：{}",
                                user.getUserId(), sendResult.getMsgId());
                    }

                    @Override
                    public void onException(Throwable e) {
                        log.error("用户：{}，注册消息发送失败:{}", user.getUserId(), e.getMessage());
                    }
                });
        return user.getUserId();
    }

    @Override
    public String userLogin(UserLoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        // 判断用户是否存在
        UserQueryRequest query = new UserQueryRequest();
        query.setUsername(username);
        User user = getQueryWrapper(query).one();
        ThrowUtil.throwIf(ObjUtil.isNull(user), EnumStatusCode.ERROR_OPERATION, "用户不存在");
        // 比对密码
        ThrowUtil.throwIf(
                !encodePassword(password).equals(user.getPassword()),
                EnumStatusCode.ERROR_OPERATION, "密码错误");
        //生成 jwt
        UserVo userVo = UserVo.toUserVo(user);
        Map<String, Object> claims = BeanUtil.beanToMap(userVo);
        return jwtUtil.createJwt(claims);
    }

    @Override
    public Page<UserVo> listUser(String roleCode, BasePageRequest pageRequest) {
        // 获取用户列表
        Page<User> users = handleByPermManager.getUsers(
                roleCode, pageRequest.getCurrent(), pageRequest.getSize());
//        Page<User> users =
//                permStrategyManager.getUsers(pageRequest.getCurrent(), pageRequest.getSize());
        // 脱敏处理
        List<UserVo> collect = users.getRecords()
                .stream().map(UserVo::toUserVo)
                .collect(Collectors.toList());
        // 构造返回值
        Page<UserVo> userPage = new Page<>(pageRequest.getCurrent(), pageRequest.getSize());
        userPage.setRecords(collect);
        userPage.setTotal(users.getTotal());
        userPage.setSearchCount(users.searchCount());
        userPage.setMaxLimit(users.maxLimit());
        return userPage;
    }

    @Override
    public void resetPassword(UserResetPasswordRequest userResetPasswordRequest) {
        //参数校验
        String oldPassword = userResetPasswordRequest.getOldPassword();
        String newPassword = userResetPasswordRequest.getNewPassword();
        String confirmPassword = userResetPasswordRequest.getConfirmPassword();
        if (StrUtil.isBlank(oldPassword)) {
            throw new ServiceException(EnumStatusCode.ERROR_PARAMS, "请输入旧密码");
        }
        // 判断用户是否存在
        User user = getById(userResetPasswordRequest.getUserId());
        ThrowUtil.throwIf(ObjUtil.isNull(user), EnumStatusCode.ERROR_OPERATION, "用户不存在");
        // 比对密码
        ThrowUtil.throwIf(
                !encodePassword(oldPassword).equals(user.getPassword()),
                EnumStatusCode.ERROR_OPERATION, "密码错误");
        ThrowUtil.throwIf(oldPassword.equals(newPassword),
                EnumStatusCode.ERROR_OPERATION, "新密码不能与旧密码相同");
        ThrowUtil.throwIf(
                !newPassword.equals(confirmPassword),
                EnumStatusCode.ERROR_OPERATION, "两次输入密码不一致");
        //重新设置密码
        newPassword = encodePassword(newPassword);
        //保存密码
        boolean update = lambdaUpdate()
                .set(User::getPassword, newPassword)
                .eq(User::getUserId, user.getUserId())
                .update();
        ThrowUtil.throwIf(!update, EnumStatusCode.ERROR_OPERATION, "密码重置失败");
    }

    @Override
    public void resetPasswordDirectly(UserResetPasswordRequest userResetPasswordRequest) {
        // 判断用户存在
        Long userId = userResetPasswordRequest.getUserId();
        User user = getById(userId);
        ThrowUtil.throwIf(ObjUtil.isNull(user), EnumStatusCode.ERROR_OPERATION, "用户不存在");
        String newPassword = userResetPasswordRequest.getNewPassword();
        String confirmPassword = userResetPasswordRequest.getConfirmPassword();
        ThrowUtil.throwIf(!newPassword.equals(confirmPassword),
                EnumStatusCode.ERROR_OPERATION, "两次输入密码不一致");
        newPassword = encodePassword(newPassword);
        boolean update = lambdaUpdate()
                .set(User::getPassword, newPassword)
                .eq(User::getUserId, userId)
                .update();
        ThrowUtil.throwIf(!update, EnumStatusCode.ERROR_OPERATION, "密码重置失败");
    }

    @Override
    public String getCurrentRoleCode() {
        Long userId = UserContextHolder.get().getUserId();
        AxiosResult<String> result = permissionClient.roleCode(userId);
        AxiosResult.check(result);
        return result.getData();
    }

    @Override
    public void updateByUserId(User user) {
        User one = getById(user.getUserId());
        if (one == null) {
            throw new ServiceException(EnumStatusCode.ERROR_NOT_FOUND, "用户不存在");
        }
        ThrowUtil.throwIf(!updateById(user),
                EnumStatusCode.ERROR_OPERATION, "更新用户信息失败");
    }

    @Override
    public LambdaQueryChainWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest) {
        LambdaQueryChainWrapper<User> wrapper = lambdaQuery();
        if (userQueryRequest == null) {
            return wrapper;
        }
        Long userId = userQueryRequest.getUserId();
        String username = userQueryRequest.getUsername();
        String phone = userQueryRequest.getPhone();

        wrapper.eq(ObjUtil.isNotNull(userId), User::getUserId, userId);
        wrapper.eq(StrUtil.isNotBlank(username), User::getUsername, username);
        wrapper.eq(StrUtil.isNotBlank(phone), User::getPhone, phone);

        return wrapper;
    }


    private String encodePassword(String password) {
        ThrowUtil.throwIf(StrUtil.isBlank(password), EnumStatusCode.ERROR_PARAMS, "密码不能为空");
        return DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));
    }
}
