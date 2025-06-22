package com.wipe.loggingservice.consumer;

import cn.hutool.core.lang.TypeReference;
import com.wipe.commonmodel.constant.TopicConstant;
import com.wipe.commonmodel.util.JsonConverterUtil;
import com.wipe.loggingservice.pojo.domain.OperationLogs;
import com.wipe.loggingservice.service.OperationLogsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQPushConsumerLifecycleListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author wipe
 * @date 2025/6/17 下午2:15
 */
@Slf4j
@Component
@RocketMQMessageListener(consumerGroup = "logging-group", topic = TopicConstant.USER_REGISTER_TOPIC)
public class LoggingConsumer implements RocketMQListener<MessageExt>, RocketMQPushConsumerLifecycleListener {

    private static final TypeReference<Map<String, Object>> MAP_TYPE_REFERENCE = new TypeReference<>() {
    };

    @Value("${rocketmq.consumer.max-reconsume-times}")
    private int maxReconsumeTimes;

    @Resource
    private OperationLogsService operationLogsService;


    @Override
    public void onMessage(MessageExt messageExt) {
        // 可以通过 redis、mysql、msgId 来保证 消息不重复消费
        // 但对于日志服务而言，幂不幂等对业务影响不大
        try {
            byte[] body = messageExt.getBody();
            Map<String, Object> payload = JsonConverterUtil.convert(body, MAP_TYPE_REFERENCE);
            OperationLogs logs = new OperationLogs();
            logs.setUserId((Long) payload.get("user_id"));
            logs.setAction((String) payload.get("action"));
            logs.setDetail((String) payload.get("detail"));
            operationLogsService.save(logs);
        } catch (Exception e) {
            if (messageExt.getReconsumeTimes() == maxReconsumeTimes) {
                log.error("MsgId：{}，达到最大消费失败次数", messageExt.getMsgId());
            }
            throw new RuntimeException(e);
        }
    }

    @Override
    public void prepareStart(DefaultMQPushConsumer consumer) {
        consumer.setMaxReconsumeTimes(maxReconsumeTimes);
    }
}
