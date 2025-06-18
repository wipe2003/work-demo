package com.wipe.commonmodel.enums;

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
    SUPER_ADMIN(1L, "super_admin"),

    /**
     * 管理员
     */
    ADMIN(3L, "admin"),

    /**
     * 普通用户
     */
    USER(2L, "user");

    private final Long roleId;
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
