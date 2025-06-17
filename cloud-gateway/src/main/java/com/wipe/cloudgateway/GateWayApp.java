package com.wipe.cloudgateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author wipe
 * @date 2025/6/16 下午9:17
 */
@Slf4j
@SpringBootApplication
public class GateWayApp {

    public static void main(String[] args) {
        ConfigurableApplicationContext context =
                SpringApplication.run(GateWayApp.class, args);
        String port = context.getEnvironment().getProperty("server.port");
        log.info("gateways aggregate documents: http://127.0.0.1:{}/doc.html", port);
    }
}
