package com.wipe.userservice.manager.perm;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wipe.commonmodel.enums.EnumStatusCode;
import com.wipe.commonmodel.exception.ServiceException;
import com.wipe.userservice.pojo.domain.User;
import com.wipe.userservice.service.UsersService;
import lombok.Data;
import org.springframework.core.Ordered;

/**
 * @author wipe
 * @date 2025/6/18 下午3:20
 * @description 抽象权限检查处理器
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

    /**
     * 暴露对外的接口
     */
    public final Page<User> getUsers(String permissionCode, int current, int size) {
        if (matchPermission(permissionCode)) {
            return listUser(current, size);
        }
        if (next != null) {
            return next.getUsers(permissionCode, current, size);
        }
        throw new ServiceException(EnumStatusCode.ERROR_NOT_FOUND, "权限不存在");
    }

}
