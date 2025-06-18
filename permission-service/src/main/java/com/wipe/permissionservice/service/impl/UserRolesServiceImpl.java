package com.wipe.permissionservice.service.impl;


import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wipe.commonmodel.model.domain.permission.UserRoles;
import com.wipe.permissionservice.mapper.UserRolesMapper;
import com.wipe.permissionservice.pojo.dto.UserRoleQueryRequest;
import com.wipe.permissionservice.service.UserRolesService;
import org.springframework.stereotype.Service;

/**
 * @author 29770
 * @description 针对表【user_roles】的数据库操作Service实现
 * @createDate 2025-06-16 16:18:03
 */
@Service
public class UserRolesServiceImpl extends ServiceImpl<UserRolesMapper, UserRoles>
        implements UserRolesService {



    @Override
    public LambdaQueryChainWrapper<UserRoles> getQueryWrapper(UserRoleQueryRequest request) {
        LambdaQueryChainWrapper<UserRoles> wrapper = lambdaQuery();
        if (request == null) {
            return wrapper;
        }
        Long id = request.getId();
        Long userId = request.getUserId();
        Long roleId = request.getRoleId();

        wrapper.eq(ObjUtil.isNotNull(id), UserRoles::getId, id)
                .eq(ObjUtil.isNotNull(userId), UserRoles::getUserId, userId)
                .eq(ObjUtil.isNotNull(roleId), UserRoles::getRoleId, roleId);

        return wrapper;
    }
}
