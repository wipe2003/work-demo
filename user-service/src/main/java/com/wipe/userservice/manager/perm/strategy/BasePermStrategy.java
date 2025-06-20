package com.wipe.userservice.manager.perm.strategy;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wipe.userservice.pojo.domain.User;
import com.wipe.userservice.pojo.dto.UserResetPasswordRequest;
import com.wipe.userservice.pojo.dto.UserUpdateRequest;

/**
 * @author wipe
 * @date 2025/6/20 下午4:49
 * 策略模式接口
 */
public interface BasePermStrategy {

    /**
     * 获取角色权限码
     */
    String getRoleCode();

    /**
     * 获取用户分页列表
     *
     * @param current 当前页码
     * @param size    每页数量
     * @return 用户分页列表
     */
    Page<User> getUsers(int current, int size);

    /**
     * 获取用户信息
     *
     * @param userId 用户Id
     * @return 用户信息
     */
    User getUserInfo(Long userId);

    /**
     * 修改用户信息
     *
     * @param userUpdateRequest 修改用户信息
     */
    void updateUserInfo(UserUpdateRequest userUpdateRequest);

    /**
     * 重置密码
     *
     * @param userResetPasswordRequest 重置密码信息
     */
    void resetPassword(UserResetPasswordRequest userResetPasswordRequest);
}
