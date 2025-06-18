package com.wipe.permissionservice.service;


import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wipe.commonmodel.model.domain.permission.UserRoles;
import com.wipe.permissionservice.pojo.dto.UserRoleQueryRequest;

/**
* @author 29770
* @description 针对表【user_roles】的数据库操作Service
* @createDate 2025-06-16 16:18:03
*/
public interface UserRolesService extends IService<UserRoles> {


    /**
     * 获取查询条件
     *
     * @return wrapper
     */
    LambdaQueryChainWrapper<UserRoles> getQueryWrapper(UserRoleQueryRequest request);
}
