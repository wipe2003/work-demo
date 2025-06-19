package com.wipe.userservice.manager.perm;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wipe.commonmodel.enums.EnumStatusCode;
import com.wipe.commonmodel.exception.ServiceException;
import com.wipe.userservice.pojo.domain.User;
import com.wipe.userservice.pojo.dto.UserResetPasswordRequest;
import com.wipe.userservice.pojo.dto.UserUpdateRequest;
import com.wipe.userservice.service.UsersService;
import lombok.Data;
import org.apache.http.util.Asserts;
import org.springframework.core.Ordered;

/**
 * @author wipe
 * @date 2025/6/18 下午3:20
 * @description 抽象权限检查处理器
 * 责任链模式（也可以使用策略模式替代）
 */
@Data
public abstract class AbstractPermissionHandler implements Ordered {

    /**
     * 下一个处理器
     */
    private AbstractPermissionHandler next;

    private UsersService usersService;

    /**
     * 权限码
     */
    private String permissionCode;

    public AbstractPermissionHandler(String permissionCode, UsersService usersService) {
        Asserts.notNull(permissionCode, "permissionCode");
        this.permissionCode = permissionCode;
        this.usersService = usersService;
    }


    /**
     * 检查是否匹配权限
     */
    protected boolean matchPermission(String permissionCode) {
        return this.permissionCode.equals(permissionCode);
    }

    /**
     * 根据角色的权限不同，返回不同用户列表
     */
    protected abstract Page<User> listUser(int current, int size);

    public final Page<User> getUsers(String permissionCode, int current, int size) {
        if (matchPermission(permissionCode)) {
            return listUser(current, size);
        }
        if (next != null) {
            return next.getUsers(permissionCode, current, size);
        }
        throw new ServiceException(EnumStatusCode.ERROR_NOT_FOUND, "权限不存在");
    }

    /**
     * 根据用户id查询用户信息
     *
     * @param userId userId
     * @return vo
     */
    protected abstract User userInfo(Long userId);

    public final User getUserInfo(String permissionCode, Long userId) {
        if (matchPermission(permissionCode)) {
            return userInfo(userId);
        }
        if (next != null) {
            return next.getUserInfo(permissionCode, userId);
        }
        throw new ServiceException(EnumStatusCode.ERROR_NOT_FOUND, "权限不存在");
    }

    /**
     * 修改用户信息
     *
     * @param userUpdateRequest 修改用户信息
     */
    protected abstract void modifyUserInfo(UserUpdateRequest userUpdateRequest);

    public final void updateUserInfo(String permissionCode, UserUpdateRequest userUpdateRequest) {
        if (matchPermission(permissionCode)) {
            modifyUserInfo(userUpdateRequest);
            return;
        }
        if (next != null) {
            next.updateUserInfo(permissionCode, userUpdateRequest);
        }
        throw new ServiceException(EnumStatusCode.ERROR_NOT_FOUND, "权限不存在");
    }

    /**
     * 重置密码
     *
     * @param userResetPasswordRequest req
     */
    protected abstract void modifyPassword(UserResetPasswordRequest userResetPasswordRequest);

    public final void resetPassword(String permissionCode, UserResetPasswordRequest userResetPasswordRequest) {
        if (matchPermission(permissionCode)) {
            modifyPassword(userResetPasswordRequest);
            return;
        }
        if (next != null) {
            next.resetPassword(permissionCode, userResetPasswordRequest);
        }
        throw new ServiceException(EnumStatusCode.ERROR_NOT_FOUND, "权限不存在");
    }
}
