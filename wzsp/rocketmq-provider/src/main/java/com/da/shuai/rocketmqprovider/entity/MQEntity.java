package com.da.shuai.rocketmqprovider.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 消息实体类
 * @Author: dashuai
 * @Date: 2020/5/14 11:22
 */
@Data
public class MQEntity implements Serializable {
    private static final long serialVersionUID = -6214204999192241722L;

    private Map<String, Object> extObj = new LinkedHashMap<>();

    private String mqId ;

    private String mqKey;

    /**
     * 添加附加字段
     * @param key
     * @param value
     */
    public void addExt(String key , Object value){
        extObj.put(key, value);
    }

    /**
     * 获取附加字段
     * @param key
     */
    public void getExt(String key ){
        extObj.get(key);
    }

}
