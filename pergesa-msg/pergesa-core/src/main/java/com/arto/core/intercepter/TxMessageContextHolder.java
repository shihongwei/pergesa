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
package com.arto.core.intercepter;

import com.arto.core.event.MqEvent;
import com.arto.event.util.ThreadContextHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * 在线程上下文中缓存未提交的事务消息.
 *
 * Created by xiong.j on 2017/3/3.
 */
public class TxMessageContextHolder extends ThreadContextHolder {

    @SuppressWarnings("unchecked")
    public static List<MqEvent> getTxMessages(){
        return (List)getContext();
    }

    @SuppressWarnings("unchecked")
    public static void setTxMessage(MqEvent object){
        if (getContext() == null) {
            init();
        }
        ((List)getContext()).add(object);
    }

    private static synchronized void init(){
        if (getContext() == null) {
            List<MqEvent> list = new ArrayList<MqEvent>();
            setContext(list);
        }
    }
}
