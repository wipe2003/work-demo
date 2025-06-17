package com.wipe.userservice.controller;

import com.wipe.commonmodel.AxiosResult;
import com.wipe.commonmodel.enums.EnumStatusCode;
import com.wipe.commonmodel.exception.ServiceException;
import com.wipe.userservice.pojo.domain.User;
import com.wipe.userservice.rpc.LoggingClient;
import com.wipe.userservice.service.UsersService;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;

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
    private LoggingClient loggingClient;

    /**
     * 用户注册
     *
     * @return userId
     */
    @PostMapping("/register")
    public AxiosResult<Long> register() {
        return AxiosResult.success(1L);
    }

    /**
     * 用户登录
     *
     * @return jwt
     */
    @PostMapping("/login")
    public AxiosResult<String> login() {
        return AxiosResult.success("1L");
    }


    /**
     * 分页用户列表
     */
    @GetMapping("/users")
    public AxiosResult<String> users() {
        return AxiosResult.success("1L");
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

    @GetMapping("/test")
    public AxiosResult<String> test() {
        User user = new User();
        user.setUsername("wipe");
        user.setPassword("123");
        user.setEmail("123");
        user.setPhone("123");
        user.setGmtCreate(LocalDateTime.now());
        usersService.save(user);
        return AxiosResult.success("1L");
    }

    @GetMapping("/feign/test")
    public AxiosResult<String> feignTest() {
        String test = loggingClient.test();
        return AxiosResult.success(test);
    }

    @GetMapping("/test/seata")
    @GlobalTransactional
    public AxiosResult<String> testSeata() {
        User user = new User();
        user.setUsername("wipeeeeeee");
        user.setPassword("123");
        user.setEmail("123");
        user.setPhone("123");
        user.setGmtCreate(LocalDateTime.now());
        usersService.save(user);
        loggingClient.log();
        if (true) {
            throw new ServiceException(EnumStatusCode.ERROR_OPERATION, "测试异常");
        }
        return AxiosResult.success("1L");
    }
}
