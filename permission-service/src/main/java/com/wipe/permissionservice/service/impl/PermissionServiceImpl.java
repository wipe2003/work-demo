package com.wipe.permissionservice.service.impl;

import cn.hutool.core.util.StrUtil;
import com.wipe.commonmodel.enums.EnumRole;
import com.wipe.commonmodel.enums.EnumStatusCode;
import com.wipe.commonmodel.exception.ServiceException;
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
        UserRoles one = userRolesService.getQueryWrapper(query).one();
        if (one != null) {
            one.setRoleId(EnumRole.USER.getRoleId());
            userRolesService.updateById(one);
            return;
        }
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
        String userRoleCode = permissionMapper.getUserRoleCode(userId);
        if (StrUtil.isBlank(userRoleCode)) {
            throw new ServiceException(EnumStatusCode.ERROR_PARAMS, "用户不存在");
        }
        return userRoleCode;
    }

    @Override
    public void upgradeToAdmin(Long userId) {
        boolean update = userRolesService.lambdaUpdate()
                .set(UserRoles::getRoleId, EnumRole.ADMIN.getRoleId())
                .eq(UserRoles::getUserId, userId)
                .update();
        ThrowUtil.throwIf(!update, EnumStatusCode.ERROR_OPERATION, "升级失败");
    }

    @Override
    public void downgradeToUser(Long userId) {
        boolean update = userRolesService.lambdaUpdate()
                .set(UserRoles::getRoleId, EnumRole.USER.getRoleId())
                .eq(UserRoles::getUserId, userId)
                .update();
        ThrowUtil.throwIf(!update, EnumStatusCode.ERROR_OPERATION, "降级失败");
    }
}
