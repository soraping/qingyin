package com.qingyin.cloud.config;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;

import java.util.stream.Collectors;

/**
 * gateway 中使用 feign 必须要手动注入此 bean
 * Spring Cloud Gateway是基于WebFlux的，是ReactiveWeb，
 * 所以HttpMessageConverters不会自动注入。在gateway服务中配置以下Bean，即可解决。
 *
 */
@Configuration
public class GatewayFeignConfig {
    @Bean
    @ConditionalOnMissingBean
    public HttpMessageConverters messageConverters(ObjectProvider<HttpMessageConverter<?>> converters) {
        return new HttpMessageConverters(converters.orderedStream().collect(Collectors.toList()));
    }
}
