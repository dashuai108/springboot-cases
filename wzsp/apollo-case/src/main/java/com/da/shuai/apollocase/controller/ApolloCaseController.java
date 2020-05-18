package com.da.shuai.apollocase.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: dashuai
 * @Date: 2020/5/13 10:24
 */

@RestController
@RequestMapping(value = "/apollo")
public class ApolloCaseController {

    private static Logger logger = LoggerFactory.getLogger(ApolloCaseController.class);

    @Value( "${wzs}" )
    String wzs;

    @RequestMapping(value = "/getvalue")
    public String getValue(String name){

        logger.debug("debug",name);
        logger.info("info",name);
        logger.warn("warn",name);

        return "hi :"+ name+" :"+wzs+ ": "+System.currentTimeMillis();
    }

}
