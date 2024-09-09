package com.qingyin.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * <h1>授权中心启动入口</h1>
 * */
@EnableDiscoveryClient
@SpringBootApplication(
        exclude = {
                RedisRepositoriesAutoConfiguration.class,
                DataSourceAutoConfiguration.class
        }
)
public class AuthorityCenterApplication {
    public static void main(String[] args){
        SpringApplication.run(AuthorityCenterApplication.class, args);
    }
}
