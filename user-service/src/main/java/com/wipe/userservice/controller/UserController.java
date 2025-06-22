package com.wipe.userservice.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wipe.commonmodel.AxiosResult;
import com.wipe.commonmodel.enums.EnumRole;
import com.wipe.commonmodel.enums.EnumStatusCode;
import com.wipe.commonmodel.exception.ServiceException;
import com.wipe.commonmodel.model.dto.BasePageRequest;
import com.wipe.userservice.aop.annotation.PermissionCheck;
import com.wipe.userservice.manager.perm.responsibility.HandleByPermManager;
import com.wipe.userservice.pojo.domain.User;
import com.wipe.userservice.pojo.dto.UserLoginRequest;
import com.wipe.userservice.pojo.dto.UserRegisterRequest;
import com.wipe.userservice.pojo.dto.UserResetPasswordRequest;
import com.wipe.userservice.pojo.dto.UserUpdateRequest;
import com.wipe.userservice.pojo.vo.UserVo;
import com.wipe.userservice.rpc.perm.PermissionClient;
import com.wipe.userservice.service.UsersService;
import com.wipe.userservice.util.UserContextHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @author wipe
 * @date 2025/6/16 下午3:40
 */
@Api(tags = "用户管理")
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UsersService usersService;

    @Resource
    private HandleByPermManager handleByPermManager;

//    @Resource
//    private PermStrategyManager permStrategyManager;

    @Resource
    private PermissionClient permissionClient;


    /**
     * 用户注册
     *
     * @return userId
     */
    @ApiOperation(value = "用户注册")
    @PostMapping("/register")
    public AxiosResult<Long> register(
            @RequestBody @Valid UserRegisterRequest userRegisterRequest) {
        Long userId = usersService.userRegister(userRegisterRequest);
        return AxiosResult.success(userId);
    }

    /**
     * 用户登录
     * tip：可将 jwt 存入 redis 保证凭证可控
     *
     * @return jwt
     */
    @ApiOperation(value = "用户登陆")
    @PostMapping("/login")
    public AxiosResult<String> login(
            @RequestBody @Valid UserLoginRequest loginRequest) {
        String token = usersService.userLogin(loginRequest);
        return AxiosResult.success(token);
    }


    /**
     * 分页用户列表
     * 根据权限校验结果返回：普通用户仅自己，管理员所有普通用户，超管全部
     */
    @ApiOperation(value = "分页用户列表：普通用户仅自己，管理员所有普通用户，超管全部")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页码", required = true),
            @ApiImplicitParam(name = "size", value = "每页数量", required = true)
    })
    @GetMapping("/users")
    public AxiosResult<Page<UserVo>> users(
            @RequestParam("current") Integer current, @RequestParam("size") Integer size) {
        // 查询角色码
        String roleCode = usersService.getCurrentRoleCode();
        // 获取用户列表
        BasePageRequest pageRequest = new BasePageRequest(current, size);
        Page<UserVo> page = usersService.listUser(roleCode, pageRequest);
        return AxiosResult.success(page);
    }

    /**
     * 获取用户信息
     * 根据权限校验结果返回：普通用户仅自己，管理员所有普通用户，超管全部
     */
    @ApiOperation(value = "获取用户信息：普通用户仅自己，管理员所有普通用户，超管全部")
    @ApiImplicitParam(name = "userId", value = "用户Id", required = true)
    @GetMapping("/{userId}")
    public AxiosResult<UserVo> userInfo(@PathVariable("userId") Long userId) {
        // 也可以将用户权限码存入 token 中，无需查数据库
        // 但如果中途用户权限码被修改，则会出问题
        String roleCode = usersService.getCurrentRoleCode();
        User userInfo = handleByPermManager.getUserInfo(roleCode, userId);
//        User userInfo = permStrategyManager.getUserInfo(userId);
        return AxiosResult.success(UserVo.toUserVo(userInfo));
    }

    /**
     * 修改用户信息
     * 普通用户改自己，管理员改普通用户，超管改所有
     */
    @ApiOperation(value = "修改用户信息：普通用户改自己，管理员改普通用户，超管改所有")
    @ApiImplicitParam(name = "userId", value = "用户Id", required = true)
    @PutMapping("/{userId}")
    public AxiosResult<Boolean> updateUserInfo(
            @PathVariable("userId") Long userId,
            @RequestBody @Valid UserUpdateRequest userUpdateRequest) {
        userUpdateRequest.setUserId(userId);
        // 获取当前用户角色码
        String roleCode = usersService.getCurrentRoleCode();
        // 修改用户信息
        handleByPermManager.updateUserInfo(roleCode, userUpdateRequest);
        return AxiosResult.success(true);
    }

    /**
     * 重置密码
     */
    @ApiOperation(value = "重置密码：普通用户重置自己，管理员重置普通用户，超管重置所有人")
    @PostMapping("/reset-password")
    public AxiosResult<Boolean> resetPassword(
            @RequestBody @Valid UserResetPasswordRequest userResetPasswordRequest) {
        Long currentUserId = UserContextHolder.get().getUserId();
        // 如果用户 Id 为空，则默认为当前用户
        if (userResetPasswordRequest.getUserId() == null) {
            userResetPasswordRequest.setUserId(currentUserId);
        }
        // 获取当前用户角色码
        AxiosResult<String> result = permissionClient.roleCode(currentUserId);
        AxiosResult.check(result);
        String roleCode = result.getData();
        handleByPermManager.resetPassword(roleCode, userResetPasswordRequest);
        return AxiosResult.success(true);
    }

    @PutMapping("/down-perm")
    @ApiOperation(value = "降级用户为普通用户(超管调用)")
    @ApiImplicitParam(name = "userId", value = "用户Id", required = true)
    @PermissionCheck(EnumRole.SUPER_ADMIN)
    public AxiosResult<Boolean> downPerm(@RequestParam("userId") Long userId) {
        if (UserContextHolder.get().getUserId().equals(userId)) {
            throw new ServiceException(EnumStatusCode.ERROR_OPERATION, "不能修改自己的权限");
        }
        AxiosResult<Boolean> result = permissionClient.downgrade(userId);
        AxiosResult.check(result);
        return AxiosResult.success(true);
    }

    @PutMapping("/up-perm")
    @ApiOperation(value = "升级用户为管理员(超管调用)")
    @ApiImplicitParam(name = "userId", value = "用户Id", required = true)
    @PermissionCheck(EnumRole.SUPER_ADMIN)
    public AxiosResult<Boolean> upPerm(@RequestParam("userId") Long userId) {
        if (UserContextHolder.get().getUserId().equals(userId)) {
            throw new ServiceException(EnumStatusCode.ERROR_OPERATION, "不能修改自己的权限");
        }
        AxiosResult<Boolean> result = permissionClient.upgrade(userId);
        AxiosResult.check(result);
        return AxiosResult.success(true);
    }


}
