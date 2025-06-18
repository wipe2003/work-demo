package com.wipe.userservice.pojo.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * @author wipe
 * @date 2025/6/17 下午7:12
 */
@Data
public class UserRegisterRequest {

    @Pattern(regexp = "^[A-Za-z][A-Za-z0-9_]{3,9}$", message = "用户名长度应为 4-10，且以大小写字母开头")
    private String username;

    @Size(min = 6, max = 18, message = "密码长度应为 6-18")
    private String password;

    @NotBlank(message = "请输入确认密码")
    private String confirmPassword;
}
