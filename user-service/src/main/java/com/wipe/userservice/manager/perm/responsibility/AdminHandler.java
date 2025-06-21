package com.wipe.userservice.manager.perm.responsibility;

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
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wipe
 * @date 2025/6/18 下午3:43
 */
@Component
public class AdminHandler extends AbstractPermissionHandler {

    @Resource
    private PermissionClient permissionClient;


    public AdminHandler(UsersService usersService) {
        super(EnumRole.ADMIN.getRoleCode(), usersService);
    }

    /**
     * 查询所有普通用户
     * tip：
     * 因为规定一个用户只有一个角色
     * 所以这里可以设计冗余字段，冗余角色码，但不便于扩展（如一个用户多个角色）
     */
    @Override
    protected Page<User> listUser(int current, int size) {
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

    /**
     * 查询用户信息
     *
     * @param userId userId
     * @return 普通用户信息
     */
    @Override
    protected User userInfo(Long userId) {
        // 权限检查
        checkRoleIsUser(userId);
        // 查询用户信息
        return getUsersService().getById(userId);
    }

    @Override
    protected void modifyUserInfo(UserUpdateRequest userUpdateRequest) {
        checkRoleIsUser(userUpdateRequest.getUserId());
        User user = new User();
        BeanUtil.copyProperties(userUpdateRequest, user);
        getUsersService().updateById(user);
    }

    @Override
    protected void modifyPassword(UserResetPasswordRequest userResetPasswordRequest) {
        checkRoleIsUser(userResetPasswordRequest.getUserId());
        getUsersService().resetPasswordDirectly(userResetPasswordRequest);
    }

    /**
     * 校验目标用户角色是否为普通用户
     *
     * @param userId userId
     */
    private void checkRoleIsUser(Long userId) {
        AxiosResult<String> result = permissionClient.roleCode(userId);
        AxiosResult.check(result);
        String roleCode = result.getData();
        if (!EnumRole.USER.getRoleCode().equals(roleCode)) {
            throw new ServiceException(EnumStatusCode.ERROR_NO_AUTH);
        }
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
