package com.wipe.userservice.manager.perm;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wipe.commonmodel.enums.EnumRole;
import com.wipe.userservice.pojo.domain.User;
import com.wipe.userservice.pojo.dto.UserResetPasswordRequest;
import com.wipe.userservice.pojo.dto.UserUpdateRequest;
import com.wipe.userservice.service.UsersService;
import org.springframework.stereotype.Component;

/**
 * @author wipe
 * @date 2025/6/18 下午3:35
 */
@Component
public class SuperAdminHandler extends AbstractPermissionHandler {


    public SuperAdminHandler(UsersService usersService) {
        super(EnumRole.SUPER_ADMIN.getRoleCode(), usersService);
    }

    /**
     * 查询所有 用户
     */
    @Override
    protected Page<User> listUser(int current, int size) {
        UsersService usersService = getUsersService();
        Page<User> page = new Page<>(current, size);
        return usersService.getQueryWrapper(null).page(page);
    }

    /**
     * 查询用户信息
     *
     * @param userId userId
     * @return 用户信息
     */
    @Override
    protected User userInfo(Long userId) {
        return getUsersService().getById(userId);
    }

    /**
     * 修改用户信息
     *
     * @param userUpdateRequest 修改用户信息
     */
    @Override
    protected void modifyUserInfo(UserUpdateRequest userUpdateRequest) {
        User user = new User();
        BeanUtil.copyProperties(userUpdateRequest, user);
        getUsersService().updateById(user);
    }

    /**
     * 修改密码
     */
    @Override
    protected void modifyPassword(UserResetPasswordRequest userResetPasswordRequest) {
        getUsersService().resetPassword(userResetPasswordRequest);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
