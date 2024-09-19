package com.qingyin.cloud.config;

import com.qingyin.cloud.core.OkHttpInterceptor;
import feign.Feign;
import lombok.Getter;
import lombok.Setter;
import okhttp3.OkHttpClient;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.commons.httpclient.OkHttpClientConnectionPoolFactory;
import org.springframework.cloud.commons.httpclient.OkHttpClientFactory;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PreDestroy;
import java.util.concurrent.TimeUnit;

/**
 * 默认的情况下，openFeign使用的上是HttpURLConnection发起请求，具体代码可查看feign.Client.Default类实现，
 * 也就是说，openFeign每次需要创建一个新的请求，而不是使用的链接池，所以我们的需要替换掉这个默认的实现，改用一个有链接池的实现。
 */
@Configuration
@ConditionalOnClass(Feign.class)
@AutoConfigureBefore(FeignAutoConfiguration.class)
public class FeignConfig {

    @Getter
    @Setter
    @Configuration
    @ConfigurationProperties(prefix = "feign.okhttp")
    @ConditionalOnProperty(name = "feign.okhttp.enabled", havingValue = "true")
    protected static class OkHttpProperties {
        boolean followRedirects = true;
        // 链接超时时间，单位毫秒
        int connectTimeout = 5000;
        boolean disableSslValidation = false;
        // 读超时，单位毫秒
        int readTimeout = 5000;
        // 写超时，单位毫秒
        int writeTimeout = 5000;
        // 是否自动重连
        boolean retryOnConnectionFailure = true;
        // 最大空闲链接
        int maxIdleConnections = 10;
        // 默认保持5分钟
        long keepAliveDuration = 1000 * 60 * 5L;
    }

    /**
     * 配置okhttp以及对应的链接池
     */
    @Configuration(
            proxyBeanMethods = false
    )
    @ConditionalOnClass({OkHttpClient.class})
    @ConditionalOnMissingBean({okhttp3.OkHttpClient.class})
    @ConditionalOnProperty({"feign.okhttp.enabled"})
    protected static class OkHttpFeignConfiguration {
        private okhttp3.OkHttpClient okHttpClient;

        private final OkHttpInterceptor okHttpInterceptor;

        protected OkHttpFeignConfiguration(OkHttpInterceptor okHttpInterceptor) {
            this.okHttpInterceptor = okHttpInterceptor;
        }

        @Bean
        public okhttp3.OkHttpClient client(OkHttpClientFactory httpClientFactory, OkHttpProperties properties,
                                           OkHttpClientConnectionPoolFactory connectionPoolFactory) {
            this.okHttpClient = httpClientFactory.createBuilder(properties.isDisableSslValidation())
                    // 链接超时时间
                    .connectTimeout(properties.getConnectTimeout(), TimeUnit.MILLISECONDS)
                    // 是否禁用重定向
                    .followRedirects(properties.isFollowRedirects())
                    //设置读超时
                    .readTimeout(properties.getReadTimeout(), TimeUnit.MILLISECONDS)
                    //设置写超时
                    .writeTimeout(properties.getWriteTimeout(), TimeUnit.MILLISECONDS)
                    // 链接失败是否重试
                    .retryOnConnectionFailure(properties.isRetryOnConnectionFailure())
                    //链接池
                    .connectionPool(connectionPoolFactory.create(properties.getMaxIdleConnections(),
                            properties.getKeepAliveDuration(), TimeUnit.MILLISECONDS))
                    .addInterceptor(okHttpInterceptor)
                    .build();
            return this.okHttpClient;
        }

        @PreDestroy
        public void destroy() {
            if (this.okHttpClient != null) {
                this.okHttpClient.dispatcher().executorService().shutdown();
                this.okHttpClient.connectionPool().evictAll();
            }
        }
    }


}
