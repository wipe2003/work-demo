package com.wipe.permissionservice.pojo.dto;

import lombok.Data;

/**
 * @author wipe
 * @date 2025/6/17 下午7:47
 */
@Data
public class UserRoleQueryRequest {

    private Long id;
    private Long userId;
    private Long roleId;
}
