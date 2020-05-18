package com.da.shuai.dubboconsumer.controller;


import com.da.shuai.dubboprovider.service.HelloService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: dashuai
 * @Date: 2020/5/13 15:30
 */

@RestController
public class DubboConsumerController {

    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Reference(version = "${hello.service.version}")
    private HelloService serviceName;

    //    @HystrixCommand(fallbackMethod = "helloFallback", commandProperties= {@HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds", value="1200")})
    @HystrixCommand(fallbackMethod = "helloFallback")
    @RequestMapping("/say")
    public String say(String name) {
        logger.info("name:" + name);
        return serviceName.say(name);
    }

    public String helloFallback(String name) {
        return "熔断效果 " + name;
    }
}
