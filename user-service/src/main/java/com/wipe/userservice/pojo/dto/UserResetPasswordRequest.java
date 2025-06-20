package com.wipe.userservice.pojo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * @author wipe
 * @date 2025/6/19 下午4:17
 * 重置密码
 */
@Data
@ApiModel(description = "用户重置密码请求参数")
public class UserResetPasswordRequest implements Serializable {

    private static final long serialVersionUID = -5286181758080174373L;

    @ApiModelProperty(value = "用户Id")
    private Long userId;

    @ApiModelProperty(value = "旧密码")
    private String oldPassword;

    @ApiModelProperty(value = "新密码", required = true)
    @Pattern(regexp = "^(?!.*\\s).{6,18}$", message = "密码长度应为 6-18，不允许空格")
    private String newPassword;

    @ApiModelProperty(value = "确认密码", required = true)
    @NotBlank(message = "请输入确认密码")
    private String confirmPassword;
}
