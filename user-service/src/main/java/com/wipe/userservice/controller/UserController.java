package com.wipe.userservice.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wipe.commonmodel.AxiosResult;
import com.wipe.commonmodel.model.dto.BasePageRequest;
import com.wipe.userservice.manager.perm.HandleByPermManager;
import com.wipe.userservice.pojo.dto.UserLoginRequest;
import com.wipe.userservice.pojo.dto.UserRegisterRequest;
import com.wipe.userservice.pojo.vo.UserVo;
import com.wipe.userservice.rpc.PermissionClient;
import com.wipe.userservice.service.UsersService;
import com.wipe.userservice.util.UserHolder;
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
     */
    @GetMapping("/users")
    public AxiosResult<Page<UserVo>> users(
            @RequestParam("current") Integer current, @RequestParam("size") Integer size) {
        // 查询角色码
        Long userId = UserHolder.get().getUserId();
        String roleCode = permissionClient.roleCode(userId).getData();
        // 获取用户列表
        BasePageRequest pageRequest = new BasePageRequest(current, size);
        Page<UserVo> page = usersService.listUser(roleCode, pageRequest);
        return AxiosResult.success(page);
    }

    /**
     * 获取用户信息
     */
    @GetMapping("/{userId}")
    public AxiosResult<String> userInfo(@PathVariable("userId") Long userId) {
        return AxiosResult.success("1L");
    }

    /**
     * 修改用户信息
     */
    @PutMapping("/{userId}")
    public AxiosResult<String> updateUserInfo(@PathVariable("userId") Long userId) {
        return AxiosResult.success("1L");
    }

    /**
     * 重置密码
     */
    @PostMapping("/reset-password")
    public AxiosResult<String> resetPassword() {
        return AxiosResult.success("1L");
    }


}
