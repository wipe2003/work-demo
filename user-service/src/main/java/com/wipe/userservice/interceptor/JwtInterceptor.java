package com.wipe.userservice.interceptor;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.Header;
import com.wipe.commonmodel.enums.EnumStatusCode;
import com.wipe.commonmodel.util.ThrowUtil;
import com.wipe.userservice.pojo.vo.UserVo;
import com.wipe.userservice.util.JwtUtil;
import com.wipe.userservice.util.UserContextHolder;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author wipe
 * @date 2025/6/18 下午2:56
 * 还可以通过 AOP 实现登陆拦截和权限校验
 */
@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Resource
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取 token
        String token = request.getHeader(Header.AUTHORIZATION.getValue());
        ThrowUtil.throwIf(StrUtil.isBlank(token), EnumStatusCode.ERROR_NOT_LOGIN);
        // 解析 token
        Claims claims = jwtUtil.parsePayload(token);
        // 写入 thread local
        UserVo userVo = new UserVo();
        userVo.setUserId(claims.get("userId", Long.class));
        userVo.setUsername(claims.get("username", String.class));
        UserContextHolder.set(userVo);
        return true;

    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        UserContextHolder.remove();
    }
}
