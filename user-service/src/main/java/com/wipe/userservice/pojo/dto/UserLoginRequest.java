package com.wipe.userservice.pojo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author wipe
 * @date 2025/6/18 下午2:25
 */
@Data
@ApiModel(description = "用户登陆请求参数")
public class UserLoginRequest implements Serializable {

    private static final long serialVersionUID = -1544826393056811050L;

    @ApiModelProperty(value = "用户名", required = true)
    @NotBlank(message = "请输入用户名")
    private String username;

    @ApiModelProperty(value = "密码", required = true)
    @NotBlank(message = "请输入密码")
    private String password;
}
