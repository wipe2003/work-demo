package com.wipe.userservice.manager.perm;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wipe.userservice.pojo.domain.User;
import org.springframework.beans.BeansException;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.util.Comparator;
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

    private ApplicationContext applicationContext;


    public HandleByPermManager() {
        super("", null);
    }

    @Override
    protected Page<User> listUser(int current, int size) {
        return null;
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
        // 获取所有 AbstractPermissionHandler，（除了本类）
        Map<String, AbstractPermissionHandler> beansOfType =
                applicationContext.getBeansOfType(AbstractPermissionHandler.class);
        // 排序
        List<AbstractPermissionHandler> list = beansOfType.values()
                .stream()
                .filter(handler -> handler != this)
                .sorted(Comparator.comparingInt(Ordered::getOrder))
                .collect(Collectors.toList());
        // 构造关系链
        AbstractPermissionHandler pointer = this;
        for (AbstractPermissionHandler node : list) {
            pointer.setNext(node);
            pointer = pointer.getNext();
        }
    }
}
