package com.wipe.userservice.pojo.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author wipe
 * @date 2025/6/18 下午2:25
 */
@Data
public class UserLoginRequest implements Serializable {

    private static final long serialVersionUID = -1544826393056811050L;

    @NotBlank(message = "请输入用户名")
    private String username;

    @NotBlank(message = "请输入密码")
    private String password;
}
