package com.wipe.userservice;

import com.wipe.userservice.manager.perm.AbstractPermissionHandler;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Map;

/**
 * Hello world!
 *
 * @author 29770
 */
@EnableDiscoveryClient
@SpringBootApplication
@EnableTransactionManagement
@MapperScan("com.wipe.userservice.mapper")
@EnableFeignClients(basePackages = "com.wipe.userservice.rpc")
public class UserApp {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(UserApp.class, args);
        Map<String, AbstractPermissionHandler> beansOfType =
                context.getBeansOfType(AbstractPermissionHandler.class);
        System.out.println(beansOfType);
    }
}
