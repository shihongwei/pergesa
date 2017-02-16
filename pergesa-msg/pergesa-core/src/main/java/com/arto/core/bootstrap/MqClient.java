package com.arto.core.bootstrap;

import com.arto.core.consumer.ConsumerConfig;
import com.arto.core.consumer.MqConsumer;
import com.arto.core.exception.MqClientException;
import com.arto.core.producer.MqProducer;
import com.arto.core.producer.ProducerConfig;

import java.util.Iterator;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 消息中间件客户端入口，可灵活配备不同的消息中间件，默认实现了Kafka和ActiveMq。
 *
 * Created by xiong.j on 2017/1/11.
 */
public class MqClient {

    private static final ConcurrentMap<String, MqFactory> factoryMap
            = new ConcurrentHashMap<String, MqFactory>(3);

    static {
        // 初始化
        init();
    }

    /**
     *  使用SPI加载默认的消息中件间客户端实现
     */
    private static void init() {
        ServiceLoader<MqFactory> serviceLoader = ServiceLoader.load(MqFactory.class);
        Iterator<MqFactory> mqFactories = serviceLoader.iterator();
        MqFactory mqFactory;
        while(mqFactories.hasNext()){
            mqFactory = mqFactories.next();
            factoryMap.put(mqFactory.getMqType(), mqFactory);
        }
    }

    /**
     * 根据生产者配置文件生成一个新的生产者
     *
     * @param config
     * @return
     */
    public static MqProducer buildProducer(ProducerConfig config){
        if (factoryMap.containsKey(config.getType().getMemo())) {
            return factoryMap.get(config.getType().getMemo()).buildProducer(config);
        } else {
            throw new MqClientException("Not support this MQ type:" + config.getType().getMemo());
        }
    }

    /**
     * 根据消费者配置文件生成一个新的消费者
     *
     * @param config
     * @return
     */
    public static MqConsumer buildConsumer(ConsumerConfig config){
        if (factoryMap.containsKey(config.getType().getMemo())) {
            return factoryMap.get(config.getType().getMemo()).buildConsumer(config);
        } else {
            throw new MqClientException("Not support this MQ type:" + config.getType().getMemo());
        }
    }

    /**
     * 添加一个新的消息中件客户端生成器
     *
     * @param key
     * @param factory
     * @return
     */
    public synchronized static void addMqFactory(String key, MqFactory factory){
        if (factoryMap.containsKey(key)) {
            throw new MqClientException("Can't override exist MqFactory, key:" + key);
        } else {
            factoryMap.put(key, factory);
        }
    }

    /**
     * 销毁所有的生产者和消息者
     */
    public static void destroy(){
        for(Map.Entry<String, MqFactory> entry : factoryMap.entrySet()){
            entry.getValue().destroy();
        }
        factoryMap.clear();
    }
}