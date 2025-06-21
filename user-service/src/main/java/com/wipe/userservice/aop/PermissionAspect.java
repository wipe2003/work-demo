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
        // 从请求对象中获取用户 id
//        ServletRequestAttributes servletRequestAttributes =
//                (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
//        HttpServletRequest request = servletRequestAttributes.getRequest();
//        String header = request.getHeader(Header.AUTHORIZATION.getValue());
//        Claims claims = jwtUtil.parsePayload(header);
        // 鉴权
//        Long id = claims.get("userId", Long.class);
//        String userRoleCode = permissionService.getUserRoleCode(id);
        String roleCode = usersService.getCurrentRoleCode();
        if (!permissionCheck.value().getRoleCode().equals(roleCode)) {
            throw new ServiceException(EnumStatusCode.ERROR_NO_AUTH);
        }
    }
}
