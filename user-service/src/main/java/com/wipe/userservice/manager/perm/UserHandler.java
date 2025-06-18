package com.wipe.userservice.manager.perm;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wipe.commonmodel.enums.EnumRole;
import com.wipe.userservice.pojo.domain.User;
import com.wipe.userservice.pojo.dto.UserQueryRequest;
import com.wipe.userservice.service.UsersService;
import com.wipe.userservice.util.UserHolder;
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
        Long userId = UserHolder.get().getUserId();
        UserQueryRequest query = new UserQueryRequest();
        query.setUserId(userId);
        Page<User> userPage = new Page<>(current, size);
        return getUsersService().getQueryWrapper(query).page(userPage);
    }

    @Override
    public int getOrder() {
        return 2;
    }
}
