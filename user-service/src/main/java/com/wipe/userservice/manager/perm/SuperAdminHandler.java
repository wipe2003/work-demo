package com.wipe.userservice.manager.perm;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wipe.commonmodel.enums.EnumRole;
import com.wipe.userservice.pojo.domain.User;
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

    @Override
    public int getOrder() {
        return 0;
    }
}
