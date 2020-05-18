package com.da.shuai.rocketmqprovider.service;

import com.da.shuai.rocketmqprovider.entity.MQEntity;
import org.apache.rocketmq.client.producer.SendCallback;

/**
 * 消息生产者接口
 * @Author: dashuai
 * @Date: 2020/5/14 11:17
 */
public interface IProducerService {

    /**
     *  同步发送消息
     * @param topic
     * @param entity
     */
    void send(String topic, MQEntity entity);

    /**
     * 发送MQ， 提供回调函数  超时时间默认3秒
     * @param topic
     * @param entity
     * @param sendCallback
     */
    void send(String topic, MQEntity entity, SendCallback sendCallback);

    /**
     * 单向发送MQ，不等待服务器回应且没有回调函数触发，适用于某些耗时非常短，但对可靠性要求并不高的场景，例如日志收集。
     * @param topic
     * @param entity
     */
    void sendOneway(String topic, MQEntity entity);
}
