package com.wipe.userservice.manager.perm.strategy;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wipe.commonmodel.AxiosResult;
import com.wipe.commonmodel.enums.EnumRole;
import com.wipe.commonmodel.enums.EnumStatusCode;
import com.wipe.commonmodel.exception.ServiceException;
import com.wipe.commonmodel.model.domain.permission.UserRoles;
import com.wipe.commonmodel.model.dto.permission.UserRolePageRequest;
import com.wipe.userservice.pojo.domain.User;
import com.wipe.userservice.pojo.dto.UserResetPasswordRequest;
import com.wipe.userservice.pojo.dto.UserUpdateRequest;
import com.wipe.userservice.rpc.perm.PermissionClient;
import com.wipe.userservice.service.UsersService;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wipe
 * @date 2025/6/20 下午4:54
 */
@Data
@Component
public class AdminPerm implements BasePermStrategy {

    private final String roleCode;

    private final UsersService usersService;

    private final PermissionClient permissionClient;

    public AdminPerm(UsersService usersService, PermissionClient permissionClient) {
        this.usersService = usersService;
        this.permissionClient = permissionClient;
        this.roleCode = EnumRole.ADMIN.getRoleCode();
    }

    @Override
    public String getRoleCode() {
        return this.roleCode;
    }

    @Override
    public Page<User> getUsers(int current, int size) {
        // 查询拥有普通用户角色的用户
        UserRolePageRequest pageRequest = new UserRolePageRequest();
        pageRequest.setCurrent(current);
        pageRequest.setSize(size);
        pageRequest.setPermissionCode(EnumRole.USER.getRoleCode());
        AxiosResult<Page<UserRoles>> result = permissionClient.listUserRole(pageRequest);
        AxiosResult.check(result);
        Page<UserRoles> userRolesPage = result.getData();
        // 聚合用户 id
        List<Long> ids = userRolesPage.getRecords()
                .stream().map(UserRoles::getUserId).collect(Collectors.toList());
        // 查询 用户
        List<User> users = getUsersService().listByIds(ids);
        Page<User> userPage = new Page<>(current, size);
        userPage.setRecords(users);
        userPage.setTotal(userRolesPage.getTotal());
        userPage.setSearchCount(userRolesPage.searchCount());
        userPage.setMaxLimit(userRolesPage.maxLimit());
        return userPage;
    }

    @Override
    public User getUserInfo(Long userId) {
        // 权限检查
        checkRoleIsUser(userId);
        // 查询用户信息
        return getUsersService().getById(userId);
    }

    @Override
    public void updateUserInfo(UserUpdateRequest userUpdateRequest) {
        checkRoleIsUser(userUpdateRequest.getUserId());
        User user = new User();
        BeanUtil.copyProperties(userUpdateRequest, user);
        getUsersService().updateById(user);
    }

    @Override
    public void resetPassword(UserResetPasswordRequest userResetPasswordRequest) {
        checkRoleIsUser(userResetPasswordRequest.getUserId());
        getUsersService().resetPasswordDirectly(userResetPasswordRequest);
    }


    private void checkRoleIsUser(Long userId) {
        AxiosResult<String> result = permissionClient.roleCode(userId);
        AxiosResult.check(result);
        String roleCode = result.getData();
        if (!EnumRole.USER.getRoleCode().equals(roleCode)) {
            throw new ServiceException(EnumStatusCode.ERROR_NO_AUTH);
        }
    }
}
