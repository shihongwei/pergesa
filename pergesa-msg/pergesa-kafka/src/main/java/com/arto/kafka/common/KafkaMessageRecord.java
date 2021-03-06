/**
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements. See the NOTICE
 * file distributed with this work for additional information regarding copyright ownership. The ASF licenses this file
 * to You under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package com.arto.kafka.common;

import com.arto.core.common.MessageRecord;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by xiong.j on 2017/1/13.
 */
@Setter
@Getter
@ToString(callSuper = true)
public class KafkaMessageRecord<T> extends MessageRecord<T>{

    /** 主键 */
    transient private String key;

    /** 分区 */
    transient private int partition = -1;

    public KafkaMessageRecord(T message) {
        super(message);
    }

    public KafkaMessageRecord(String businessId, String businessType, T message){
        super(businessId, businessType, message);
    }

}
