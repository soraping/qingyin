### spring-cloud-gateway

#### 使用 nacos 配置管理实现动态路由

#### 在 gateway 中调用 openfeign 接口

- 手动注入 `HttpMessageConverters`

`Spring Cloud Gateway` 是基于 `WebFlux` 的，是 `ReactiveWeb`，所以 `HttpMessageConverters` 不会自动注入。
在 `gateway` 服务中配置以下 `Bean`，即可解决。

```java
@Configuration
public class GatewayFeignConfig {
    @Bean
    @ConditionalOnMissingBean
    public HttpMessageConverters messageConverters(ObjectProvider<HttpMessageConverter<?>> converters) {
        return new HttpMessageConverters(converters.orderedStream().collect(Collectors.toList()));
    }
}
```