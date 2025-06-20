package com.wipe.userservice.manager.perm.strategy;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wipe.commonmodel.enums.EnumRole;
import com.wipe.commonmodel.enums.EnumStatusCode;
import com.wipe.commonmodel.exception.ServiceException;
import com.wipe.userservice.pojo.domain.User;
import com.wipe.userservice.pojo.dto.UserQueryRequest;
import com.wipe.userservice.pojo.dto.UserResetPasswordRequest;
import com.wipe.userservice.pojo.dto.UserUpdateRequest;
import com.wipe.userservice.pojo.vo.UserVo;
import com.wipe.userservice.service.UsersService;
import com.wipe.userservice.util.UserContextHolder;
import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @author wipe
 * @date 2025/6/20 下午4:54
 */
@Data
@Component
public class UserPerm implements BasePermStrategy {

    private final String roleCode;

    private final UsersService usersService;

    public UserPerm(UsersService usersService) {
        this.usersService = usersService;
        this.roleCode = EnumRole.USER.getRoleCode();
    }

    @Override
    public String getRoleCode() {
        return this.roleCode;
    }

    @Override
    public Page<User> getUsers(int current, int size) {
        Long userId = UserContextHolder.get().getUserId();
        UserQueryRequest query = new UserQueryRequest();
        query.setUserId(userId);
        Page<User> userPage = new Page<>(current, size);
        return getUsersService().getQueryWrapper(query).page(userPage);
    }

    @Override
    public User getUserInfo(Long userId) {
        checkIsSelf(userId);
        return getUsersService().getById(userId);
    }

    @Override
    public void updateUserInfo(UserUpdateRequest userUpdateRequest) {
        checkIsSelf(userUpdateRequest.getUserId());
        User user = new User();
        BeanUtil.copyProperties(userUpdateRequest, user);
        getUsersService().updateById(user);
    }

    @Override
    public void resetPassword(UserResetPasswordRequest userResetPasswordRequest) {
        checkIsSelf(userResetPasswordRequest.getUserId());
        getUsersService().resetPassword(userResetPasswordRequest);
    }

    private void checkIsSelf(Long userId) {
        UserVo userVo = UserContextHolder.get();
        if (!userVo.getUserId().equals(userId)) {
            throw new ServiceException(EnumStatusCode.ERROR_NO_AUTH);
        }
    }
}
