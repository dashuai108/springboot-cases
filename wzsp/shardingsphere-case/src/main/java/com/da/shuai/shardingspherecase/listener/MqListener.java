package com.da.shuai.shardingspherecase.listener;

/**
 * 一个钩子 操作业务逻辑
 * @Author: dashuai
 * @Date: 2020/5/15 9:40
 */
@FunctionalInterface
public interface MqListener {

    void handleMessage(String msgBody, String msgId);
}
