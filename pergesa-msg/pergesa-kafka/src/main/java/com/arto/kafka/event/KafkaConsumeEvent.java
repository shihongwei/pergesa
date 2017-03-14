package com.arto.kafka.event;

import com.arto.core.event.MqEvent;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by xiongjie on 2016/12/21.
 */
@Setter
@Getter
@ToString
public class KafkaConsumeEvent extends MqEvent{

    private String messageId;

    public KafkaConsumeEvent(){
        this.setGroup(KafkaConsumeEvent.class);
    }
}
