package com.wipe.permissionservice.aop;

import cn.hutool.http.Header;
import com.wipe.commonmodel.enums.EnumStatusCode;
import com.wipe.commonmodel.exception.ServiceException;
import com.wipe.commonmodel.util.JwtUtil;
import com.wipe.permissionservice.aop.annotation.PermissionCheck;
import com.wipe.permissionservice.service.PermissionService;
import io.jsonwebtoken.Claims;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author wipe
 * @date 2025/6/19 上午12:49
 */
@Aspect
@Component
public class PermissionAspect {

    @Resource
    private PermissionService permissionService;

    @Resource
    private JwtUtil jwtUtil;

    @Before("@annotation(permissionCheck)")
    public void checkPermission(JoinPoint joinPoint, PermissionCheck permissionCheck) {
        // 从请求对象中获取用户 id
        ServletRequestAttributes servletRequestAttributes =
                (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        String header = request.getHeader(Header.AUTHORIZATION.getValue());
        Claims claims = jwtUtil.parsePayload(header);
        // 鉴权
        Long id = claims.get("userId", Long.class);
        String userRoleCode = permissionService.getUserRoleCode(id);
        if (!permissionCheck.value().getRoleCode().equals(userRoleCode)) {
            throw new ServiceException(EnumStatusCode.ERROR_NO_AUTH);
        }
    }
}
