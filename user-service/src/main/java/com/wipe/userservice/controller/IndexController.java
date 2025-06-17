package com.wipe.userservice.controller;

import com.wipe.commonmodel.AxiosResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author wipe
 * @date 2025/6/17 下午2:11
 */
@RestController("/index")
public class IndexController {

    @Resource
    private RocketMQTemplate rocketMQTemplate;

    @GetMapping("/send")
    public AxiosResult<Boolean> send(){
        rocketMQTemplate.convertAndSend("log-topic", "hello logging-service");
        return AxiosResult.success();
    }
}
