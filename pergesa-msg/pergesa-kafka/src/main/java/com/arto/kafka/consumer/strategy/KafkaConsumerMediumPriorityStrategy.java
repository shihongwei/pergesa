package com.arto.kafka.consumer.strategy;

import com.arto.core.common.MessageRecord;
import com.arto.event.util.ThreadUtil;
import com.arto.kafka.consumer.binding.KafkaConsumerConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import static com.arto.kafka.common.KUtil.buildMessageId;

/**
 * 普通消费模式，消息消费失败会重试三次，然后丢弃消息。适合容忍消息丢失的普通消息
 *
 * Created by xiong.j on 2017/1/20.
 */
@Slf4j
class KafkaConsumerMediumPriorityStrategy extends AbstractKafkaConsumerStrategy implements KafkaConsumerStrategy {

    @Override
    public void onMessage(final KafkaConsumerConfig config, final ConsumerRecord<String, String> record) {
        tryConsume(config, record);
    }

    @SuppressWarnings("unchecked")
    private void tryConsume(final KafkaConsumerConfig config, final ConsumerRecord<String, String> record) {
        MessageRecord message = null;
        try {
            // 反序列化消息
            message = deserializerMessage(config, record.value());
            // 生成消息ID
            message.setMessageId(buildMessageId(record.partition(), record.offset()));
        } catch (Throwable e) {
            log.warn("Deserializer record failed, Discard record:" + record, e);
        }

        // 如果消费出错，重试消费3次，超过三次后丢弃
        for (int i = 1; i <= 3; i++) {
            try {
                // 重复消费检测
                if (!checkRedeliver(config, message)) {
                    // 消费消息
                    onMessage(config, message);
                } else {
                    log.info("Discard redelivered message:" + message);
                }
                break;
            } catch (Throwable e) {
                log.warn("Receive message failed, waiting for retry. message=" + message, e);
                if (i == 3) {
                    // 持久化消息，以便重试
                    log.warn("Receive message failed 3 times, Discard message:" + message);
                } else {
                    // 消息处理错误，暂停处理一小会
                    ThreadUtil.sleep(5000, log);
                }
            }
        }
    }

}
