package com.wipe.permissionservice.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wipe.permissionservice.pojo.domain.Roles;
import com.wipe.permissionservice.mapper.RolesMapper;
import com.wipe.permissionservice.service.RolesService;
import org.springframework.stereotype.Service;

/**
 * @author 29770
 * @description 针对表【roles】的数据库操作Service实现
 * @createDate 2025-06-16 16:18:03
 */
@Service
public class RolesServiceImpl extends ServiceImpl<RolesMapper, Roles>
        implements RolesService {

}
