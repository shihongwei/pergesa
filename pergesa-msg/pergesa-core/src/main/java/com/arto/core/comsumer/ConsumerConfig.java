package com.arto.core.comsumer;

import com.arto.core.build.MqConfig;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by xiong.j on 2017/1/11.
 */
@Setter
@Getter
@ToString
public class ConsumerConfig extends MqConfig{

    /** 消息类型序列化类型 */
    private Class clz;

    /** 消息处理类 */
    private MqListener listener;

    /** 消费优先级
     * 1:重要消息(TODO 单条消息处理完成后消费标识同步提交，为了避免阻塞后续消息，消息处理出错 > 3次后该消息入库，等待调度任务重试处理)
     * 2:重要消息(单条消息处理完成后消费标识同步提交，消息处理出错 > 3次后等待1分种重新消费，后续消息等待)
     * 3:不重要消息(消费标识异步提交, 处理出错后将会丢失该条消息) */
    private int priority = 2;
}
