package com.wipe.userservice.manager.perm.responsibility;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wipe.commonmodel.enums.EnumStatusCode;
import com.wipe.commonmodel.exception.ServiceException;
import com.wipe.userservice.pojo.domain.User;
import com.wipe.userservice.pojo.dto.UserResetPasswordRequest;
import com.wipe.userservice.pojo.dto.UserUpdateRequest;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author wipe
 * @date 2025/6/18 下午4:54
 * 责任链对外暴露的管理器
 */
@Component
public class HandleByPermManager extends AbstractPermissionHandler
        implements ApplicationContextAware, ApplicationRunner {

    @Value("${perm-manager.ascend:1}")
    private Integer ascend;

    private ApplicationContext applicationContext;

    public HandleByPermManager() {
        super("", null);
    }

    @Override
    protected Page<User> listUser(int current, int size) {
        throw new ServiceException(EnumStatusCode.ERROR_OPERATION, "不该到达的方法");
    }

    @Override
    protected User userInfo(Long userId) {
        throw new ServiceException(EnumStatusCode.ERROR_OPERATION, "不该到达的方法");
    }

    @Override
    protected void modifyUserInfo(UserUpdateRequest userUpdateRequest) {
        throw new ServiceException(EnumStatusCode.ERROR_OPERATION, "不该到达的方法");
    }

    @Override
    protected void modifyPassword(UserResetPasswordRequest userResetPasswordRequest) {
        throw new ServiceException(EnumStatusCode.ERROR_OPERATION, "不该到达的方法");
    }

    @Override
    public int getOrder() {
        return -1;
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (!ascend.equals(1) && !ascend.equals(-1)) {
            throw new ServiceException(EnumStatusCode.ERROR_SYSTEM, "ascend must is 1 or -1");
        }
        // 获取所有 AbstractPermissionHandler，（除了本类）
        Map<String, AbstractPermissionHandler> beansOfType =
                applicationContext.getBeansOfType(AbstractPermissionHandler.class);
        // 排序
        List<AbstractPermissionHandler> list = beansOfType.values()
                .stream()
                .filter(handler -> handler != this)
                .sorted((a, b) -> ascend * (a.getOrder() - b.getOrder()))
                .collect(Collectors.toList());
        // 构造关系链
        AbstractPermissionHandler pointer = this;
        for (AbstractPermissionHandler node : list) {
            pointer.setNext(node);
            pointer = pointer.getNext();
        }
    }
}
