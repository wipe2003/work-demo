package com.wipe.permissionservice.controller;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wipe.commonmodel.AxiosResult;
import com.wipe.commonmodel.enums.EnumRole;
import com.wipe.commonmodel.enums.EnumStatusCode;
import com.wipe.commonmodel.util.ThrowUtil;
import com.wipe.commonmodel.model.domain.permission.UserRoles;
import com.wipe.commonmodel.model.dto.permission.UserRolePageRequest;
import com.wipe.permissionservice.aop.annotation.PermissionCheck;
import com.wipe.permissionservice.pojo.dto.UserRoleQueryRequest;
import com.wipe.permissionservice.service.PermissionService;
import com.wipe.permissionservice.service.UserRolesService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author wipe
 * @date 2025/6/16 下午4:26
 */
@RestController
@RequestMapping("/perm")
public class PermissionController {

    @Resource
    private PermissionService permissionService;

    @Resource
    private UserRolesService userRolesService;

    /**
     * 绑定默认角色
     *
     * @param userId 用户id
     * @return 绑定结果
     */
    @PostMapping("/bind_default")
    public AxiosResult<Boolean> bindDefaultRole(@RequestParam("userId") Long userId) {
        ThrowUtil.throwIf(userId == null, EnumStatusCode.ERROR_PARAMS, "用户id不能为空");
        permissionService.bindDefaultRole(userId);
        return AxiosResult.success();
    }

    /**
     * 获取角色码
     *
     * @return 角色码
     */
    @GetMapping("/role_code")
    public AxiosResult<String> roleCode(@RequestParam("userId") Long userId) {
        String userRoleCode = permissionService.getUserRoleCode(userId);
        return AxiosResult.success(userRoleCode);
    }

    /**
     * 超管调用
     * 升级为管理员
     *
     * @param userId 用户id
     * @return 升级结果
     */
    @PutMapping("/upgrade")
    @PermissionCheck(EnumRole.SUPER_ADMIN)
    public AxiosResult<Boolean> upgrade(@RequestParam("userId") Long userId) {
        ThrowUtil.throwIf(userId == null, EnumStatusCode.ERROR_PARAMS, "用户id不能为空");
        permissionService.upgradeToAdmin(userId);
        return AxiosResult.success(true);
    }


    /**
     * 超管调用
     * 降级为普通用户
     *
     * @param userId 用户id
     * @return 降级结果
     */
    @PutMapping("/downgrade")
    @PermissionCheck(EnumRole.SUPER_ADMIN)
    public AxiosResult<Boolean> downgrade(@RequestParam("userId") Long userId) {
        ThrowUtil.throwIf(userId == null, EnumStatusCode.ERROR_PARAMS, "用户id不能为空");
        permissionService.downgradeToUser(userId);
        return AxiosResult.success(true);
    }


    /**
     * 获取用户角色列表
     *
     * @param userRolePageRequest 请求参数
     * @return 用户角色列表
     */
    @PostMapping("/users")
    public AxiosResult<Page<UserRoles>> listUserRole(
            @RequestBody UserRolePageRequest userRolePageRequest) {
        // 参数校验
        ThrowUtil.throwIf(userRolePageRequest == null,
                EnumStatusCode.ERROR_PARAMS, "参数不能为空");
        String permissionCode = userRolePageRequest.getPermissionCode();
        Integer current = userRolePageRequest.getCurrent();
        Integer size = userRolePageRequest.getSize();
        boolean flag = StrUtil.isBlank(permissionCode) || ObjUtil.isNull(current) || ObjUtil.isNull(size);
        ThrowUtil.throwIf(flag, EnumStatusCode.ERROR_PARAMS, "参数不能为空");

        Page<UserRoles> page = new Page<>(current, size);
        // 转化枚举
        EnumRole role = EnumRole.fromCode(permissionCode);
        // 查询
        UserRoleQueryRequest queryRequest = new UserRoleQueryRequest();
        queryRequest.setRoleId(role.getRoleId());
        Page<UserRoles> rolesPage = userRolesService.getQueryWrapper(queryRequest).page(page);
        return AxiosResult.success(rolesPage);
    }
}
