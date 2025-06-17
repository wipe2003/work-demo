package com.wipe.loggingservice.controller;

import com.wipe.loggingservice.pojo.domain.OperationLogs;
import com.wipe.loggingservice.service.OperationLogsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author wipe
 * @date 2025/6/16 下午4:44
 */
@RestController
@RequestMapping("/log")
public class LoggingController {

    @Resource
    private OperationLogsService operationLogsService;

    @GetMapping("/test")
    public String test() {
        return "hello world";
    }

    @GetMapping("/log")
    public String log() {
        OperationLogs logs = new OperationLogs();
        logs.setUserId(1L);
        logs.setAction("rizhi");
        logs.setIp("123");
        logs.setDetail("no");
        operationLogsService.save(logs);
        return "success";
    }
}
