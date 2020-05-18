package com.da.shuai.apollocase;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author dashuai
 */
@SpringBootApplication
@EnableApolloConfig
public class ApolloCaseApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApolloCaseApplication.class, args);
    }

}
