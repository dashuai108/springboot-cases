package com.da.shuai.rocketmqprovider.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.da.shuai.rocketmqprovider.entity.MQEntity;
import com.da.shuai.rocketmqprovider.service.IProducerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.UUID;

/**
 * @Author: dashuai
 * @Date: 2020/5/14 13:36
 */
@Service
@Slf4j
public class ProducerServiceIpl implements IProducerService {


    /**
     * 服务器地址
     */
    @Value("${spring.rocketmq.name-server}")
    private String namesrvAddr;

    /**
     * 消息组
     */
    @Value("${spring.rocketmq.producer.group}")
    private String producerGroup;

    //生产者
    private DefaultMQProducer producer;


    /**
     * spring 容器初始化所有属性后调用此方法
     * Producer对象在使用之前必须要调用start初始化，初始化一次即可
     * 注意：切记不可以在每次发送消息时，都调用start方法
     */
    @PostConstruct
    public void defaultMQProducer() throws Exception {
        producer = new DefaultMQProducer();
        producer.setProducerGroup(this.producerGroup);
        producer.setNamesrvAddr(this.namesrvAddr);
        producer.setSendMsgTimeout(10000);
        producer.setVipChannelEnabled(false);

        producer.start();

        log.info("[{}:{}] start successd!", producerGroup, namesrvAddr);
//        logger.info( "[{}:{}] start successd!",producerGroup,namesrvAddr );
    }


    /**
     * @param topic
     * @param entity
     * @return
     */
    public Message message(String topic, MQEntity entity) {
        String keys = UUID.randomUUID().toString();
        entity.setMqKey(keys);
        String tags = entity.getClass().getName();
        //  logger.info("&#x4E1A;&#x52A1;:{},tags:{},keys:{},entity:{}",topic, tags, keys, entity);

        String smsContent = MessageFormat.format("业务:{0},tags:{1},keys:{2},entity:{3}", topic, tags, keys, JSONObject.toJSONString(entity));
        log.info(smsContent);

        Message msg = null;
        try {
            msg = new Message(topic, tags, keys,
                    JSON.toJSONString(entity).getBytes(RemotingHelper.DEFAULT_CHARSET));
        } catch (UnsupportedEncodingException e) {
            log.error("消息转码失败", e);
        }
        return msg;
    }


    /**
     * 同步发送消息
     *
     * @param topic
     * @param entity
     */
    @Override
    public void send(String topic, MQEntity entity) {
        //组装消息
        Message msg = message(topic, entity);
        try {
            SendResult send = producer.send(msg, 10000);

            log.info("SendResult: "+send.toString());
            log.info("SendResult-msgID: "+send.getMsgId());
            log.info("SendResult-SendStatus: "+send.getSendStatus());
        } catch (Exception e) {
            log.error(entity.getMqKey().concat(":发送消息失败"), e);
            throw new RuntimeException("发送消息失败", e);
        }


    }

    /**
     * 发送MQ， 提供回调函数  超时时间默认3秒
     *
     * @param topic
     * @param entity
     * @param sendCallback
     */
    @Override
    public void send(String topic, MQEntity entity, SendCallback sendCallback) {
        Message msg = message(topic, entity);
        try {
            producer.send(msg, sendCallback);
        } catch (Exception e) {
            log.error(entity.getMqKey().concat(":发送消息失败"), e);
            throw new RuntimeException("发送消息失败", e);
        }

    }

    /**
     * 单向发送MQ，不等待服务器回应且没有回调函数触发，适用于某些耗时非常短，但对可靠性要求并不高的场景，例如日志收集。
     *
     * @param topic
     * @param entity
     */
    @Override
    public void sendOneway(String topic, MQEntity entity) {
        Message msg = message(topic, entity);
        try {
            producer.sendOneway(msg);
        } catch (Exception e) {
            log.error(entity.getMqKey().concat(":发送消息失败"), e);
            throw new RuntimeException("发送消息失败", e);
        }

    }
}
