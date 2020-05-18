package com.da.shuai.rocketmqprovider.controller;

import com.da.shuai.rocketmqprovider.entity.MQEntity;
import com.da.shuai.rocketmqprovider.service.IProducerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @Author: dashuai
 * @Date: 2020/5/14 13:45
 */

@RequestMapping(value = "/producer")
@RestController
@Slf4j
public class ProducerController {

    @Autowired
    private  IProducerService producerService;

    @Value("${spring.rocketmq.topic}")
    private String topic;

    @RequestMapping("/syncSend")
    public void syncSend(){
        MQEntity mqEntity = new MQEntity();
        mqEntity.addExt("createTime",new Date());
        mqEntity.addExt("msg","同步测试发送一条消息");
        producerService.send(topic,mqEntity);

    }

    @RequestMapping("/asyncSend")
    public void asyncSend(){
        MQEntity mqEntity = new MQEntity();
        mqEntity.addExt("createTime",new Date());
        mqEntity.addExt("msg","异步测试发送一条消息");
        producerService.send(topic, mqEntity, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                log.info("controller-asyncSend: "+sendResult.toString());
                log.info("controller-asyncSend-msgid: "+sendResult.getMsgId());
                log.info("controller-asyncSend-SendStatus: "+sendResult.getSendStatus());
            }

            @Override
            public void onException(Throwable throwable) {
                log.info("controller-asyncSend-exception: ",throwable.toString());
            }
        });
    }

    @RequestMapping("/oneway")
    public void sendOneway(){
        MQEntity mqEntity = new MQEntity();
        mqEntity.addExt("createTime",new Date());
        mqEntity.addExt("msg","单向发送测试发送一条消息");
        producerService.sendOneway(topic,mqEntity);
    }



}

