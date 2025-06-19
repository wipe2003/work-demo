package com.wipe.userservice.pojo.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author wipe
 * @date 2025/6/17 下午7:23
 * 查询请求
 */
@Data
public class UserQueryRequest implements Serializable {

    private static final long serialVersionUID = -934206093830368390L;

    private Long userId;
    private String username;
    private String phone;
}
