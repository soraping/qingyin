package com.qingyin.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(
        exclude = {
                RedisRepositoriesAutoConfiguration.class,
                DataSourceAutoConfiguration.class
        }
)
@EnableDiscoveryClient
public class QingyinAccountApplication {
    public static void main(String[] args) {
        SpringApplication.run(QingyinAccountApplication.class, args);
    }
}