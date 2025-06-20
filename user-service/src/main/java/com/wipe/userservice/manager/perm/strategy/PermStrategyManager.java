package com.wipe.userservice.manager.perm.strategy;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wipe.commonmodel.enums.EnumStatusCode;
import com.wipe.commonmodel.exception.ServiceException;
import com.wipe.userservice.pojo.domain.User;
import com.wipe.userservice.pojo.dto.UserResetPasswordRequest;
import com.wipe.userservice.pojo.dto.UserUpdateRequest;
import com.wipe.userservice.service.UsersService;
import org.springframework.beans.BeansException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author wipe
 * @date 2025/6/20 下午5:03
 * 策略管理类
 */
@Component
public class PermStrategyManager implements BasePermStrategy, ApplicationContextAware, CommandLineRunner {

    private Map<String, BasePermStrategy> strategyMap;

    private ApplicationContext applicationContext;

    private final UsersService usersService;

    public PermStrategyManager(UsersService usersService) {
        this.usersService = usersService;
    }

    @Override
    public String getRoleCode() {
        return "";
    }

    @Override
    public Page<User> getUsers(int current, int size) {
        return getStrategy().getUsers(current, size);
    }

    @Override
    public User getUserInfo(Long userId) {
        return getStrategy().getUserInfo(userId);
    }

    @Override
    public void updateUserInfo(UserUpdateRequest userUpdateRequest) {
        getStrategy().updateUserInfo(userUpdateRequest);
    }

    @Override
    public void resetPassword(UserResetPasswordRequest userResetPasswordRequest) {
        getStrategy().resetPassword(userResetPasswordRequest);
    }

    private BasePermStrategy getStrategy() {
        String roleCode = usersService.getCurrentRoleCode();
        BasePermStrategy basePermStrategy = strategyMap.get(roleCode);
        if (basePermStrategy == null) {
            throw new ServiceException(EnumStatusCode.ERROR_PARAMS, "角色不存在");
        }
        return basePermStrategy;
    }


    @Override
    public void run(String... args) throws Exception {
        Map<String, BasePermStrategy> beansOfType =
                applicationContext.getBeansOfType(BasePermStrategy.class);
        strategyMap = beansOfType.values().stream()
                .filter(strategy -> strategy != this)
                .collect(Collectors.toMap(BasePermStrategy::getRoleCode, strategy -> strategy));
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
