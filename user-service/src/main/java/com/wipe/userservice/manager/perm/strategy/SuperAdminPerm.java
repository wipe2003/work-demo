package com.wipe.userservice.manager.perm.strategy;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wipe.commonmodel.enums.EnumRole;
import com.wipe.userservice.pojo.domain.User;
import com.wipe.userservice.pojo.dto.UserResetPasswordRequest;
import com.wipe.userservice.pojo.dto.UserUpdateRequest;
import com.wipe.userservice.service.UsersService;
import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @author wipe
 * @date 2025/6/20 下午4:54
 */
@Data
@Component
public class SuperAdminPerm implements BasePermStrategy {

    private final String roleCode;

    private final UsersService usersService;

    public SuperAdminPerm(UsersService usersService) {
        this.usersService = usersService;
        this.roleCode = EnumRole.SUPER_ADMIN.getRoleCode();
    }

    @Override
    public String getRoleCode() {
        return this.roleCode;
    }

    @Override
    public Page<User> getUsers(int current, int size) {
        UsersService usersService = getUsersService();
        Page<User> page = new Page<>(current, size);
        return usersService.getQueryWrapper(null).page(page);
    }

    @Override
    public User getUserInfo(Long userId) {
        return getUsersService().getById(userId);
    }

    @Override
    public void updateUserInfo(UserUpdateRequest userUpdateRequest) {
        User user = new User();
        BeanUtil.copyProperties(userUpdateRequest, user);
        getUsersService().updateById(user);
    }

    @Override
    public void resetPassword(UserResetPasswordRequest userResetPasswordRequest) {
        getUsersService().resetPasswordDirectly(userResetPasswordRequest);
    }
}
