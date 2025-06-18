package com.wipe.userservice.pojo.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author wipe
 * @date 2025/6/18 下午2:25
 */
@Data
public class UserLoginRequest {

    @NotBlank(message = "请输入用户名")
    private String username;

    @NotBlank(message = "请输入密码")
    private String password;
}
