package com.wipe.userservice.controller;

import com.wipe.commonmodel.AxiosResult;
import com.wipe.userservice.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwsHeader;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author wipe
 * @date 2025/6/17 下午2:11
 */
@RestController("/index")
public class IndexController {

    @Resource
    private RocketMQTemplate rocketMQTemplate;

    @Resource
    private JwtUtil jwtUtil;

    @GetMapping("/send")
    public AxiosResult<Boolean> send() {
        rocketMQTemplate.convertAndSend("log-topic", "hello logging-service");
        return AxiosResult.success();
    }

    @GetMapping("/token")
    public AxiosResult<String> token() {
        String jwt = jwtUtil.createJwt(Map.of("username", "wipe"));
        return AxiosResult.success(jwt);
    }

    @GetMapping("/parse")
    public AxiosResult<String> parse(String token) {
        JwsHeader header = jwtUtil.parseHeader(token);
        Claims payload = jwtUtil.parsePayload(token);
        System.out.println(header);
        System.out.println(payload);
        return AxiosResult.success(header.toString() + payload.toString());
    }
}
