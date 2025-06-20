package com.wipe.userservice.pojo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * @author wipe
 * @date 2025/6/17 下午7:12
 */
@Data
@ApiModel(description = "用户注册请求参数")
public class UserRegisterRequest implements Serializable {

    private static final long serialVersionUID = -6352448320386017676L;

    @ApiModelProperty(value = "用户名", required = true,example = "以大小写字母开头，4-10位")
    @Pattern(regexp = "^[A-Za-z][A-Za-z0-9]{3,9}$", message = "长度应为 4-10，以大小写字母开头，允许数字、字母")
    private String username;

    @ApiModelProperty(value = "密码", required = true, example = "6-18位")
    @Pattern(regexp = "^(?!.*\\s).{6,18}$", message = "密码长度应为 6-18，不允许空格")
    private String password;

    @ApiModelProperty(value = "确认密码", required = true, example = "123456")
    @NotBlank(message = "请输入确认密码")
    private String confirmPassword;
}
