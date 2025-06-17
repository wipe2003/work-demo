package com.wipe.permissionservice.pojo.enums;

import com.wipe.commonmodel.enums.EnumStatusCode;
import com.wipe.commonmodel.exception.ServiceException;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author wipe
 * @date 2025/6/16 下午4:22
 */
@Getter
@AllArgsConstructor
public enum EnumRole {

    /**
     * 超级管理员
     */
    SUPER_ADMIN(1, "super_admin"),

    /**
     * 管理员
     */
    ADMIN(3, "admin"),

    /**
     * 普通用户
     */
    USER(2, "user");

    private final Integer roleId;
    private final String roleCode;

    public static EnumRole fromCode(String roleCode) {
        for (EnumRole role : values()) {
            if (role.roleCode.equals(roleCode)) {
                return role;
            }
        }
        throw new ServiceException(EnumStatusCode.ERROR_PARAMS, "角色不存在: " + roleCode);
    }

}
