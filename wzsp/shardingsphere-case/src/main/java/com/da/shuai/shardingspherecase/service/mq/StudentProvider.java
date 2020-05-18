package com.da.shuai.shardingspherecase.service.mq;

import com.alibaba.fastjson.JSON;
import com.da.shuai.shardingspherecase.entity.Student;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @Author: dashuai
 * @Date: 2020/5/15 9:54
 */

@Component
@Slf4j
public class StudentProvider {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * NameServer 地址
     */
    @Value("${spring.rocketmq.name-server}")
    private String namesrvAddr;


    /**
     * 组名
     */
    @Value("${spring.rocketmq.producer.group}")
    private String producerGroup;

    /**
     * 主题
     */
    @Value("${spring.rocketmq.topic}")
    private String consumerTopic;

    @Value("${spring.rocketmq.tag}")
    private String consumerTag;

    private static String instanceName = "student_queue_instance";

    private static String getMessageContent(int i) throws Exception {
        Student student = new Student();
        student.setId(new Random().nextInt(200)+100);
        student.setAge((int)(Math.random()*100));
        student.setName("名字：--"+i);
        student.setSex((i%2==0)?"男":"女");
        student.setStuclass(i+"班");

        String jsonStr = objectMapper.writeValueAsString(student);
//        return JSON.toJSONString(student);
        return jsonStr;
    }



    public  void save() {
        DefaultMQProducer producer = new DefaultMQProducer(producerGroup);
        producer.setNamesrvAddr(namesrvAddr);
//        producer.setInstanceName(instanceName);

        try {
            producer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < 10; i++) {
            try {
                Message msg = new Message(consumerTopic,
//                        "tag" + String.valueOf(i),
                        "tag-0514",
                        "key" + String.valueOf(i),
                        getMessageContent(i).getBytes("UTF-8"));

                SendResult sendResult = producer.send(msg);

                log.info(sendResult.toString());


                TimeUnit.MILLISECONDS.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        producer.shutdown();
    }

}
