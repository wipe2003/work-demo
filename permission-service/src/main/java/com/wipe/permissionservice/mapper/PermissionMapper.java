package com.wipe.permissionservice.mapper;

import org.apache.ibatis.annotations.Param;

/**
 * @author wipe
 * @date 2025/6/18 下午5:57
 */
public interface PermissionMapper {

    /**
     * 获取用户角色码
     * @param userId id
     * @return code
     */
    String getUserRoleCode(@Param("userId") Long userId);
}
