package com.wipe.userservice.rpc.perm;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wipe.commonmodel.AxiosResult;
import com.wipe.commonmodel.model.domain.permission.UserRoles;
import com.wipe.commonmodel.model.dto.permission.UserRolePageRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author wipe
 * @date 2025/6/17 下午8:02
 * 可以配置降级策略，但捕获异常后要抛出，否则分布式事务会失效
 */
@FeignClient("permission-service")
public interface PermissionClient {

    /**
     * 绑定默认角色
     *
     * @param userId 用户id
     * @return 绑定结果
     */
    @RequestMapping(value = "/perm/bind_default", method = RequestMethod.POST)
    AxiosResult<Boolean> bindDefaultRole(@RequestParam("userId") Long userId);

    /**
     * 获取角色码
     *
     * @return 角色码
     */
    @RequestMapping(value = "/perm/role_code",method = RequestMethod.GET)
    AxiosResult<String> roleCode(@RequestParam("userId") Long userId);

    /**
     * 获取用户角色列表
     *
     * @param userRolePageRequest 请求参数
     * @return 用户角色列表
     */
    @RequestMapping(value = "/perm/users")
    AxiosResult<Page<UserRoles>> listUserRole(@RequestBody UserRolePageRequest userRolePageRequest);


}
