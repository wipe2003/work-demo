package com.wipe.userservice.aop;

import com.wipe.commonmodel.enums.EnumStatusCode;
import com.wipe.commonmodel.exception.ServiceException;
import com.wipe.userservice.aop.annotation.PermissionCheck;
import com.wipe.userservice.service.UsersService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author wipe
 * @date 2025/6/19 上午12:49
 */
@Aspect
@Component
public class PermissionAspect {

    @Resource
    private UsersService usersService;

    @Before("@annotation(permissionCheck)")
    public void checkPermission(JoinPoint joinPoint, PermissionCheck permissionCheck) {
        String roleCode = usersService.getCurrentRoleCode();
        if (!permissionCheck.value().getRoleCode().equals(roleCode)) {
            throw new ServiceException(EnumStatusCode.ERROR_NO_AUTH);
        }
    }
}
