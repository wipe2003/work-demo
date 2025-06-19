package com.wipe.userservice.rpc.perm;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wipe.commonmodel.AxiosResult;
import com.wipe.commonmodel.model.domain.permission.UserRoles;
import com.wipe.commonmodel.model.dto.permission.UserRolePageRequest;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * @author wipe
 * @date 2025/6/19 下午1:11
 * 降级逻辑
 */
public class PermissionFallBackFactory implements FallbackFactory<PermissionClient> {
    @Override
    public PermissionClient create(Throwable cause) {
        return new PermissionClient() {
            @Override
            public AxiosResult<Boolean> bindDefaultRole(Long userId) {
                return null;
            }

            @Override
            public AxiosResult<String> roleCode(Long userId) {
                return null;
            }

            @Override
            public AxiosResult<Page<UserRoles>> listUserRole(UserRolePageRequest userRolePageRequest) {
                return null;
            }
        };
    }
}
