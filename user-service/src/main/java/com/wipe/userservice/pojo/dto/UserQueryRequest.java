package com.wipe.userservice.pojo.dto;

import lombok.Data;

/**
 * @author wipe
 * @date 2025/6/17 下午7:23
 * 查询请求
 */
@Data
public class UserQueryRequest {

    private Long userId;
    private String username;
    private String phone;
}
