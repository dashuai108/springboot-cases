package com.da.shuai.shardingspherecase;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class ShardingsphereCaseApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShardingsphereCaseApplication.class, args);
    }

}
