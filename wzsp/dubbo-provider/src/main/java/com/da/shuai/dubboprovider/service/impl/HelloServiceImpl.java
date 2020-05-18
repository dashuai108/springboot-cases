package com.da.shuai.dubboprovider.service.impl;

import com.da.shuai.dubboprovider.service.HelloService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.apache.dubbo.config.annotation.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;

/**
 * @Author: dashuai
 * @Date: 2020/5/13 15:19
 */
@Service(version = "${hello.service.version}")
public class HelloServiceImpl implements HelloService {



    private final Logger logger = LoggerFactory.getLogger(getClass());
    /**
     *
     */
    @Value(value = "${dubbo.application.name}")
    private String serviceName;


    @HystrixCommand(commandProperties = {@HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "20"), @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "20000")})
    @Override
    public String say(String name) {
        logger.info("服务端：" + name);
        return serviceName +"服务发来问候： Hello "+name +" ， 今天是 " + new Date().toString();
//        throw new RuntimeException("运行时异常");
    }







}
