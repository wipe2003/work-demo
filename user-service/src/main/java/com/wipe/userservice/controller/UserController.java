package com.wipe.userservice.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wipe.commonmodel.AxiosResult;
import com.wipe.commonmodel.model.dto.BasePageRequest;
import com.wipe.userservice.manager.perm.HandleByPermManager;
import com.wipe.userservice.pojo.domain.User;
import com.wipe.userservice.pojo.dto.UserResetPasswordRequest;
import com.wipe.userservice.pojo.dto.UserLoginRequest;
import com.wipe.userservice.pojo.dto.UserRegisterRequest;
import com.wipe.userservice.pojo.dto.UserUpdateRequest;
import com.wipe.userservice.pojo.vo.UserVo;
import com.wipe.userservice.rpc.perm.PermissionClient;
import com.wipe.userservice.service.UsersService;
import com.wipe.userservice.util.UserContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @author wipe
 * @date 2025/6/16 下午3:40
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UsersService usersService;

    @Resource
    private HandleByPermManager handleByPermManager;

    @Resource
    private PermissionClient permissionClient;


    /**
     * 用户注册
     *
     * @return userId
     */
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
    @GetMapping("/users")
    public AxiosResult<Page<UserVo>> users(
            @RequestParam("current") Integer current, @RequestParam("size") Integer size) {
        // 查询角色码
        Long userId = UserContextHolder.get().getUserId();
        String roleCode = permissionClient.roleCode(userId).getData();
        // 获取用户列表
        BasePageRequest pageRequest = new BasePageRequest(current, size);
        Page<UserVo> page = usersService.listUser(roleCode, pageRequest);
        return AxiosResult.success(page);
    }

    /**
     * 获取用户信息
     * 根据权限校验结果返回：普通用户仅自己，管理员所有普通用户，超管全部
     */
    @GetMapping("/{userId}")
    public AxiosResult<UserVo> userInfo(@PathVariable("userId") Long userId) {
        // 也可以将用户权限码存入 token 中，无需查数据库
        // 但如果中途用户权限码被修改，则会出问题
        Long id = UserContextHolder.get().getUserId();
        String roleCode = permissionClient.roleCode(id).getData();
        User userInfo = handleByPermManager.getUserInfo(roleCode, userId);
        return AxiosResult.success(UserVo.toUserVo(userInfo));
    }

    /**
     * 修改用户信息
     * 普通用户改自己，管理员改普通用户，超管改所有
     */
    @PutMapping("/{userId}")
    public AxiosResult<Boolean> updateUserInfo(
            @PathVariable("userId") Long userId,
            @RequestBody @Valid UserUpdateRequest userUpdateRequest) {
        userUpdateRequest.setUserId(userId);
        // 获取当前用户角色码
        Long id = UserContextHolder.get().getUserId();
        String roleCode = permissionClient.roleCode(id).getData();
        // 修改用户信息
        handleByPermManager.updateUserInfo(roleCode, userUpdateRequest);
        return AxiosResult.success(true);
    }

    /**
     * 重置密码
     */
    @PostMapping("/reset-password")
    public AxiosResult<Boolean> resetPassword(
            @RequestBody @Valid UserResetPasswordRequest userResetPasswordRequest) {
        Long currentUserId = UserContextHolder.get().getUserId();
        // 如果用户 Id 为空，则默认为当前用户
        if (userResetPasswordRequest.getUserId() == null) {
            userResetPasswordRequest.setUserId(currentUserId);
        }
        // 获取当前用户角色码
        String roleCode = permissionClient.roleCode(currentUserId).getData();
        handleByPermManager.resetPassword(roleCode, userResetPasswordRequest);
        return AxiosResult.success(true);
    }


}
