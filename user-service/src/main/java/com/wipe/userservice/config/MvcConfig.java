package com.wipe.userservice.config;

import com.wipe.userservice.interceptor.JwtInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * @author wipe
 * @date 2025/6/18 下午3:04
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Resource
    private JwtInterceptor jwtInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/user/login",
                        "/user/register",
                        "/doc.html/**",
                        "/v2/api-docs/**",
                        "/webjars/**",
                        "/.well-known/**",
                        "/error/**",
                        "/swagger-resources/**",
                        "/favicon.ico")
                .order(-1);
    }
}
