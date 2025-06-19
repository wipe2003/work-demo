package com.wipe.userservice.pojo.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @author wipe
 * @date 2025/6/19 下午4:17
 * 重置密码
 */
@Data
public class UserResetPasswordRequest implements Serializable {

    private static final long serialVersionUID = -5286181758080174373L;

    private Long userId;

    @NotBlank(message = "请输入旧密码")
    private String oldPassword;

    @Size(min = 6, max = 18, message = "密码长度应为 6-18")
    private String newPassword;

    @NotBlank(message = "请输入确认密码")
    private String confirmPassword;
}
