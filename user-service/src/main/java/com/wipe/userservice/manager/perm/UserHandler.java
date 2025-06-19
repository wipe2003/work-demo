package com.wipe.userservice.manager.perm;

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
import org.springframework.stereotype.Component;

/**
 * @author wipe
 * @date 2025/6/18 下午4:07
 */
@Component
public class UserHandler extends AbstractPermissionHandler {


    public UserHandler(UsersService usersService) {
        super(EnumRole.USER.getRoleCode(), usersService);
    }

    /**
     * 只查询自己
     */
    @Override
    protected Page<User> listUser(int current, int size) {
        Long userId = UserContextHolder.get().getUserId();
        UserQueryRequest query = new UserQueryRequest();
        query.setUserId(userId);
        Page<User> userPage = new Page<>(current, size);
        return getUsersService().getQueryWrapper(query).page(userPage);
    }

    @Override
    protected User userInfo(Long userId) {
        checkIsSelf(userId);
        return getUsersService().getById(userId);
    }

    @Override
    protected void modifyUserInfo(UserUpdateRequest userUpdateRequest) {
        checkIsSelf(userUpdateRequest.getUserId());
        User user = new User();
        BeanUtil.copyProperties(userUpdateRequest, user);
        getUsersService().updateById(user);
    }

    @Override
    protected void modifyPassword(UserResetPasswordRequest userResetPasswordRequest) {
        checkIsSelf(userResetPasswordRequest.getUserId());
        getUsersService().resetPassword(userResetPasswordRequest);
    }

    /**
     * 检查操作对象是否是自己
     *
     * @param userId userId
     */
    private void checkIsSelf(Long userId) {
        UserVo userVo = UserContextHolder.get();
        if (!userVo.getUserId().equals(userId)) {
            throw new ServiceException(EnumStatusCode.ERROR_NO_AUTH);
        }
    }

    @Override
    public int getOrder() {
        return 2;
    }
}
