package com.da.shuai.shardingspherecase.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.HashMap;

/**
 * @Author: dashuai
 * @Date: 2020/5/15 16:05
 */

@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {

    @Bean
    public RedisTemplate<String, Object> redistemplate(RedisConnectionFactory factory) {


        RedisTemplate template = new RedisTemplate<String, Object>();
        //配置连接工厂
        template.setConnectionFactory(factory);

        //使用jackson2jsonredisserializer来序列化和反序列化redis的value值（默认使用jdk的序列化方式）
        Jackson2JsonRedisSerializer jacksonseial = new Jackson2JsonRedisSerializer(Object.class);

        ObjectMapper om = new ObjectMapper();
        // 指定要序列化的域，field,get和set,以及修饰符范围，any是都有包括private和public
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // 指定序列化输入的类型，类必须是非final修饰的，final修饰的类，比如string,integer等会跑出异常
        om.activateDefaultTyping(LaissezFaireSubTypeValidator.instance ,
                ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        jacksonseial.setObjectMapper(om);

        // 值采用json序列化
        template.setValueSerializer(jacksonseial);
        //使用stringredisserializer来序列化和反序列化redis的key值
        template.setKeySerializer(new StringRedisSerializer());

        // 设置hash key 和value序列化模式
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(jacksonseial);
        template.afterPropertiesSet();

        return template;
    }

    /**
     * 对hash类型的数据操作
     *
     * @param redistemplate
     * @return
     */
    @Bean
    public HashOperations<String, String, Object> hashoperations(RedisTemplate<String, Object> redistemplate) {
        return redistemplate.opsForHash();
    }

    /**
     * 对redis字符串类型数据操作
     *
     * @param redistemplate
     * @return
     */
    @Bean
    public ValueOperations<String, Object> valueoperations(RedisTemplate<String, Object> redistemplate) {
        return redistemplate.opsForValue();
    }

    /**
     * 对链表类型的数据操作
     *
     * @param redistemplate
     * @return
     */
    @Bean
    public ListOperations<String, Object> listoperations(RedisTemplate<String, Object> redistemplate) {
        return redistemplate.opsForList();
    }

    /**
     * 对无序集合类型的数据操作
     *
     * @param redistemplate
     * @return
     */
    @Bean
    public SetOperations<String, Object> setoperations(RedisTemplate<String, Object> redistemplate) {
        return redistemplate.opsForSet();
    }

    /**
     * 对有序集合类型的数据操作
     *
     * @param redistemplate
     * @return
     */
    @Bean
    public ZSetOperations<String, Object> zsetoperations(RedisTemplate<String, Object> redistemplate) {
        return redistemplate.opsForZSet();
    }


}
