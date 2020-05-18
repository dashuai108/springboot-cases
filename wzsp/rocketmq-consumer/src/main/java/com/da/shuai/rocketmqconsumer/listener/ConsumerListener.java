package com.da.shuai.rocketmqconsumer.listener;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.*;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @Author: dashuai
 * @Date: 2020/5/14 13:49
 */

@Component
@Slf4j
public class ConsumerListener {

    /**
     * NameServer 地址
     */
    @Value("${spring.rocketmq.name-server}")
    private String namesrvAddr;


    /**
     * 消费者的组名
     */
    @Value("${spring.rocketmq.consumer.group}")
    private String consumerGroup;

    /**
     * 消费者的主题
     */
    @Value("${spring.rocketmq.topic}")
    private String consumerTopic;

    @PostConstruct
    public void defaultMQPushConsumer() {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(consumerGroup);
        //设置服务端地址
        consumer.setNamesrvAddr(namesrvAddr);



        try {
            //订阅消息主题topic 以及Tag来过滤的消息
            consumer.subscribe(consumerTopic, "");

            //注册回调实现类来处理从broker拉取回来的消息
            consumer.registerMessageListener((MessageListenerOrderly) (list, consumeOrderlyContext) -> {
                consumeOrderlyContext.setAutoCommit(true);
                for (MessageExt msg : list) {

                    String msgContent = null;
                    try {
                        msgContent = new String(msg.getBody(), "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        log.error("转码失败", e);
                    }
                    log.info("######### MSG Content start ##########");
                    log.info(JSON.toJSONString(msgContent));
                    log.info("#########        END        ##########");

                }
                return ConsumeOrderlyStatus.SUCCESS;
            });

            consumer.start();
            log.info("Consumer started.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }






//    /**
//     * 创建MQ消费者
//     *
//     */
//    public void createConsumer() {
//        log.info("rocketMQ consumer initialize....");
////        String consumerId = prop.getProperty("ConsumerId");
////        String onsAddr = prop.getProperty("ONSAddr");
//        String consumerId = consumerGroup;
//        String onsAddr = namesrvAddr;
//        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(consumerId);
//        consumer.setNamesrvAddr(onsAddr);
////        if (StringUtils.hasText(prop.getProperty("consumeThreadMax"))) {
//        if (StringUtils.hasText("")) {
//            consumer.setConsumeThreadMax(Integer.parseInt("64"));
//        }
////        if (StringUtils.hasText(prop.getProperty("consumeThreadMin"))) {
//        if (StringUtils.hasText("")) {
//            consumer.setConsumeThreadMin(Integer.parseInt("32"));
//        }
//        //单位分钟
//        if (StringUtils.hasText("")) {
//            consumer.setConsumeTimeout(Long.parseLong("5000"));
//        }
//        // ----------重要，是否从最新位点消费！！！------------
//        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
//        Iterator<Map.Entry<Object, Object>> it = prop.entrySet().iterator();
//        while (it.hasNext()) {
//            Map.Entry<Object, Object> entry = it.next();
//            String key = (String) entry.getKey();
//            if (key.startsWith("*")) {
//                String topic = "consumerTopic";
//                String tag = "";
//                log.info("RocketMQ consumer initialize....subscribe:consumerId:{} topic:{} tags:{} starting.", consumerId, topic, tag);
////
////                mqConsumerRefMap.put(topic,(MqListener) prop.get(key));
//                try {
//                    consumer.subscribe(topic, tag);
//
//                } catch (MQClientException e) {
//                    log.info("RocketMQ consumer initialize....subscribe:consumerId:{} topic:{} tags:{} error.", consumerId, topic, tag, e);
//                }
//                log.info("RocketMQ consumer initialize....subscribe:consumerId:{} topic:{} tags:{} sucess.", consumerId, topic, tag);
//            }
//        }
//        try {
//            consumer.registerMessageListener(new MessageListenerConcurrently() {
//                @Override
//                public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
//                    log.info("rocketMQ accepting...{} Receive New Messages: {} {}", Thread.currentThread().getName(), msgs);
//
//                    boolean isSucess = false;
//                    String topic = context.getMessageQueue().getTopic();
//                    for (MessageExt message : msgs) {
//                        MqMessage msg = new MqMessage();
//                        msg.setMsgBody(message.getBody());
//                        msg.setKeys(message.getKeys());
//                        msg.setMsgId(message.getMsgId());
//                        msg.setTags(message.getTags());
//                        msg.setIp(message.getBornHostString());
//                        msg.setTopic(message.getTopic());
//
//                        MqListener consumer= mqConsumerRefMap.get(topic);
//                        if(consumer == null){
//                            //重试topic无法处理，从message获取topic 2019-12-31
//                            consumer= mqConsumerRefMap.get(message.getTopic());
//                        }
//                        isSucess = consumer.consume(msg);
//                        //消息失败
//                        if(!isSucess) {
//                            break;
//                        }
//                    }
//                    return isSucess == true ? ConsumeConcurrentlyStatus.CONSUME_SUCCESS : ConsumeConcurrentlyStatus.RECONSUME_LATER;
//                }
//            });
//
//            consumer.start();
//        } catch (MQClientException e) {
//            // TODO Auto-generated catch block
//            log.info("rocketMQ consumer initialize....starting. error", e);
//        }
//        log.info("rocketMQ consumer initialize....start success");
//    }


}
