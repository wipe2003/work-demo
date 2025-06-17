package com.wipe.loggingservice.consumer;

import com.wipe.commonmodel.enums.EnumStatusCode;
import com.wipe.commonmodel.exception.ServiceException;
import com.wipe.loggingservice.pojo.domain.OperationLogs;
import com.wipe.loggingservice.service.OperationLogsService;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.support.RocketMQConsumerLifecycleListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author wipe
 * @date 2025/6/17 下午2:15
 */
@Component
@RocketMQMessageListener(consumerGroup = "test", topic = "log-topic")
public class RocketConsumer implements RocketMQListener<String>, RocketMQConsumerLifecycleListener<DefaultMQPushConsumer> {

    @Resource
    private OperationLogsService operationLogsService;

    @Override
    public void onMessage(String s) {
        if (true) {
            throw new ServiceException(EnumStatusCode.ERROR_OPERATION, "消费失败");
        }
        OperationLogs logs = new OperationLogs();
        logs.setUserId(123L);
        logs.setDetail(s);
        operationLogsService.save(logs);
    }


    @Override
    public void prepareStart(DefaultMQPushConsumer consumer) {
        consumer.setMaxReconsumeTimes(3);
    }
}
