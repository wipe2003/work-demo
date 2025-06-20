package com.wipe.userservice.pojo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * @author wipe
 * @date 2025/6/19 下午3:40
 * tip：正则表达式由 AI 生成
 * 允许为空字符串
 */
@Data
@ApiModel(description = "用户修改请求参数")
public class UserUpdateRequest implements Serializable {

    private static final long serialVersionUID = -1498993246189351283L;

    @ApiModelProperty(value = "用户Id")
    private Long userId;

    @ApiModelProperty(value = "邮箱")
    @Pattern(regexp = "^$|^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
            message = "请输入正确的邮箱地址")
    private String email;

    @ApiModelProperty(value = "手机号")
    @Pattern(regexp = "^$|^1[3-9]\\d{9}$", message = "请输入正确的手机号码")
    private String phone;
}
