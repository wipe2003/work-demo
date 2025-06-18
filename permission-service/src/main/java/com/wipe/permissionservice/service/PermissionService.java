package com.wipe.permissionservice.service;

/**
 * @author wipe
 * @date 2025/6/18 下午5:52
 */
public interface PermissionService {

    /**
     * 绑定默认角色（普通用户）
     */
    void bindDefaultRole(Long userId);

    /**
     * 查询用户角色码
     *
     * @param userId 用户id
     * @return 角色码
     */
    String getUserRoleCode(Long userId);

    /**
     * 超管调用：升级用户为管理员
     *
     * @param userId 用户id
     */
    void upgradeToAdmin(Long userId);

    /**
     * 超管调用：降级用户为普通角色
     *
     * @param userId userId
     */
    void downgradeToUser(Long userId);
}
