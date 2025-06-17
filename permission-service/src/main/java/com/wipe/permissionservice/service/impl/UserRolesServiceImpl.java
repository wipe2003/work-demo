package com.wipe.permissionservice.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wipe.permissionservice.pojo.domain.UserRoles;
import com.wipe.permissionservice.mapper.UserRolesMapper;
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

}
