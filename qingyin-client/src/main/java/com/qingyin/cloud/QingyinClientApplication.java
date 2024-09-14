package com.qingyin.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = {"com.qingyin.cloud.api.authority"})
@EnableDiscoveryClient
public class QingyinClientApplication {
    public static void main(String[] args) {
        SpringApplication.run(QingyinClientApplication.class, args);
    }
}