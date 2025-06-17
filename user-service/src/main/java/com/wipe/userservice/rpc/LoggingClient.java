package com.wipe.userservice.rpc;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author wipe
 * @date 2025/6/16 下午8:47
 */
@FeignClient("logging-service")
public interface LoggingClient {

    @RequestMapping(value = "/log/test", method = RequestMethod.GET)
    String test();

    @RequestMapping(value = "/log/log", method = RequestMethod.GET)
    String log();
}
