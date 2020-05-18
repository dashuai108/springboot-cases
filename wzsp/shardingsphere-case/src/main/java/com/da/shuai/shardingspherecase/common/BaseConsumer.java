package com.da.shuai.shardingspherecase.common;

import com.da.shuai.shardingspherecase.listener.MqListener;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * @Author: dashuai
 * @Date: 2020/5/15 9:28
 */

@Slf4j
@Configuration
@Data
public class BaseConsumer implements DisposableBean {


    private MqListener mqListener;

    /**
     * NameServer 地址
     */
    private String namesrvAddr;


    /**
     * 消费者的组名
     */
    private String consumerGroup;

    /**
     * 消费者的主题
     */
    private String consumerTopic;

    private String consumerTag;

    private int batchMaxSize;
    private String instanceName;

    private DefaultMQPushConsumer consumer = null;

    public BaseConsumer() {
    }

    ;

    public BaseConsumer(String namesrvAddr, String consumerGroup, int batchMaxSize, String consumerTopic, String instanceName, String consumerTag) {
        this.namesrvAddr = namesrvAddr;
        this.consumerGroup = consumerGroup;
        this.batchMaxSize = batchMaxSize;
        this.consumerTopic = consumerTopic;
        this.instanceName = instanceName;
        this.consumerTag = consumerTag;
    }


    public void registerListener() {
        try {
            log.info("rocketmq init【topicName = " + this.consumerTopic + "，groupName = " + this.consumerGroup + "】");
            if (consumer == null) {
                consumer = new DefaultMQPushConsumer(this.consumerGroup);
            }
            consumer.setNamesrvAddr(this.namesrvAddr);
            // consumer 消费策略：从某个时间点开始消费，和setConsumeTimestamp()配合使用，默认是半个小时以前
            consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_TIMESTAMP);
//            consumer.setConsumeTimestamp(Constants.CONSUMER_FROM_TIME);
            consumer.setConsumeMessageBatchMaxSize(this.batchMaxSize);
            if (StringUtils.isNotEmpty(consumerTag)) {
                consumer.subscribe(this.consumerTopic, this.consumerTag);
            } else {
                consumer.subscribe(this.consumerTopic, "*");
            }

            // 注册监听
            consumer.registerMessageListener(new MessageListenerOrderly() {
                String errmsg = "";

                @Override
                public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {
                    context.setAutoCommit(true);

                    for (int i = 0; i < msgs.size(); i++) {
                        MessageExt msgExt = msgs.get(i);
                        String msgId = msgExt.getMsgId();
                        String msgBody = new String(msgExt.getBody());
                        try {
                            mqListener.handleMessage(msgBody, msgId);
                        } catch (Exception e) {
                            String errmsg = String.format("consumer error: topic = %s, groupName = %s, data:%s, exception:%s", consumerTopic, consumerGroup, msgBody, e.getMessage());
                            log.error(errmsg, e);
                        }
                    }
                    return ConsumeOrderlyStatus.SUCCESS;
                }
            });
            consumer.start();
        } catch (Exception e) {
            String errmsg = "rocketmq registerMessageListener fail!【topicName = " + this.consumerTopic + "，groupName = " + this.consumerGroup + "】";
            log.error(errmsg, e);
        }
        log.info("rocketmq registerMessageListener success!【topicName = " + this.consumerTopic + "，groupName = " + this.consumerGroup + "】");
    }


    /**
     * Invoked by the containing {@code BeanFactory} on destruction of a bean.
     *
     * @throws Exception in case of shutdown errors. Exceptions will get logged
     *                   but not rethrown to allow other beans to release their resources as well.
     */
    @Override
    public void destroy() throws Exception {
        consumer.shutdown();
    }
}
