package com.wipe.permissionservice.controller;

import com.wipe.permissionservice.pojo.domain.Roles;
import com.wipe.permissionservice.pojo.enums.EnumRole;
import com.wipe.permissionservice.service.RolesService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wipe
 * @date 2025/6/16 下午4:26
 */
@RestController
@RequestMapping("/perm")
public class PermissionController {

    @Resource
    private RolesService rolesService;

    @GetMapping("/index")
    public String index() {
        Roles s = new Roles();
        s.setRoleId(EnumRole.SUPER_ADMIN.getRoleId());
        s.setRoleCode(EnumRole.SUPER_ADMIN.getRoleCode());
        Roles a = new Roles();
        a.setRoleId(EnumRole.ADMIN.getRoleId());
        a.setRoleCode(EnumRole.ADMIN.getRoleCode());
        Roles u = new Roles();
        u.setRoleId(EnumRole.USER.getRoleId());
        u.setRoleCode(EnumRole.USER.getRoleCode());
        List<Roles> rolesList = new ArrayList<>();
        rolesList.add(s);
        rolesList.add(a);
        rolesList.add(u);
        rolesService.saveBatch(rolesList);
        return "index";
    }
}
