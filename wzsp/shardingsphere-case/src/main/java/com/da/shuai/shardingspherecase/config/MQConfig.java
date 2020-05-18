package com.da.shuai.shardingspherecase.config;

import com.da.shuai.shardingspherecase.common.BaseConsumer;
import com.da.shuai.shardingspherecase.service.mq.StudentConsumer;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.concurrent.*;

/**
 * @Author: dashuai
 * @Date: 2020/5/15 10:30
 */
@Data
@Configuration
public class MQConfig {


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

    @Value("${spring.rocketmq.tag}")
    private String consumerTag;

    private BaseConsumer consumer;

    @Bean
    public BaseConsumer startOrderConsumer(@Qualifier("studentConsumer") StudentConsumer studentConsumer) throws InterruptedException {
        consumer = new BaseConsumer(namesrvAddr, consumerGroup, 10, consumerTopic, null, "tag-0514");

//        consumer.setMqMessageListener(orderConsumer);

        consumer.setMqListener(studentConsumer);


        // 有界队列
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(10);
        // 放弃拒绝的任务并抛出异常
        RejectedExecutionHandler abortPolicyHandler = new ThreadPoolExecutor.AbortPolicy();
        RejectedExecutionHandler discardPolicyHandler = new ThreadPoolExecutor.DiscardPolicy();

        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(10, 100, 10, TimeUnit.SECONDS, workQueue, discardPolicyHandler);

        threadPool.execute(new StartConsumer(consumer));
        threadPool.shutdown();
        if (threadPool.awaitTermination(6, TimeUnit.SECONDS)) {
            threadPool.shutdownNow();
        }

//        new Thread(consumer::registerListener).start();
        return consumer;
    }


}

class StartConsumer implements Runnable {

    private BaseConsumer consumer;


    public StartConsumer(BaseConsumer consumer) {
        this.consumer = consumer;
    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        consumer.registerListener();
    }
}
