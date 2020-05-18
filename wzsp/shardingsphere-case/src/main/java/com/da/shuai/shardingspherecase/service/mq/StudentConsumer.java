package com.da.shuai.shardingspherecase.service.mq;

import com.da.shuai.shardingspherecase.entity.Student;
import com.da.shuai.shardingspherecase.listener.MqListener;
import com.da.shuai.shardingspherecase.service.StudentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: dashuai
 * @Date: 2020/5/15 9:48
 */
@Component("studentConsumer")
@Slf4j
public class StudentConsumer implements MqListener {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private StudentService studentService;

    @Override
    public void handleMessage(String msgBody, String msgId) {

            if (StringUtils.isNotEmpty(msgBody)) {
//                log.info("数据收集-消费者【topicName = " + orderConf.getTopicName() + "，data = " + message + "】");

                // 业务处理...
                Student student = null;
                try {
                    student = objectMapper.readValue(msgBody, Student.class);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                try {
                    log.info("开始保存--save-student:"+student.toString());
                    studentService.insert(student);
                    log.info("保存成功--save-student:"+student.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                log.info("消息为空不做处理!");
            }


    }
}
