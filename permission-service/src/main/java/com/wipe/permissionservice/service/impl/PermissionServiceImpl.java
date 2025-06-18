package com.wipe.permissionservice.service.impl;

import com.wipe.commonmodel.enums.EnumRole;
import com.wipe.commonmodel.enums.EnumStatusCode;
import com.wipe.commonmodel.model.domain.permission.UserRoles;
import com.wipe.commonmodel.util.ThrowUtil;
import com.wipe.permissionservice.mapper.PermissionMapper;
import com.wipe.permissionservice.pojo.dto.UserRoleQueryRequest;
import com.wipe.permissionservice.service.PermissionService;
import com.wipe.permissionservice.service.RolesService;
import com.wipe.permissionservice.service.UserRolesService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author wipe
 * @date 2025/6/18 下午5:53
 */
@Service
public class PermissionServiceImpl implements PermissionService {

    @Resource
    private RolesService rolesService;

    @Resource
    private UserRolesService userRolesService;

    @Resource
    private PermissionMapper permissionMapper;


    @Override
    public void bindDefaultRole(Long userId) {
        // 判断关系是否存在（一般新用户才会调用这个方法，这里可以不判断）
        UserRoleQueryRequest query = new UserRoleQueryRequest();
        query.setUserId(userId);
        boolean exists = userRolesService.getQueryWrapper(query).exists();
        ThrowUtil.throwIf(exists, EnumStatusCode.ERROR_OPERATION, "用户已经拥有角色");
        // 绑定关系
        UserRoles userRoles = new UserRoles();
        userRoles.setUserId(userId);
        // 这里也可以根据权限码查询出 id 后填写，我用枚举做了约定，直接填写
        userRoles.setRoleId(EnumRole.USER.getRoleId());
        boolean save = userRolesService.save(userRoles);
        ThrowUtil.throwIf(!save, EnumStatusCode.ERROR_OPERATION, "绑定角色失败");
    }

    @Override
    public String getUserRoleCode(Long userId) {
        return permissionMapper.getUserRoleCode(userId);
    }

    @Override
    public void upgradeToAdmin(Long userId) {

    }

    @Override
    public void downgradeToUser(Long userId) {

    }
}
