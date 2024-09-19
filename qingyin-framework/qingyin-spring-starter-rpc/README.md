### `openfeign` 相关问题

#### 开启 `okhttp` 连接池

- 新增依赖

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-openfeign</artifactId>
</dependency>
<dependency>
<groupId>io.github.openfeign</groupId>
    <artifactId>feign-okhttp</artifactId>
</dependency>
```

- 新增配置

```yaml
feign:
  # 不使用httpclient，改用okhttp
  httpclient:
    enabled: false
  okhttp:
    enabled: true
    # 是否禁用重定向
    follow-redirects: true
    connect-timeout: 5000
    # 链接失败是否重试
    retry-on-connection-failure: true
    read-timeout: 5000
    write-timeout: 5000
    # 最大空闲数量
    max-idle-connections: 5
    # 生存时间
    keep-alive-duration: 15000

  # 开启压缩功能
  compression:
    request:
      enabled: true
      mime-types: text/xml,application/xml,application/json
      min-request-size: 2048
    response:
      enabled: true

  client:
    config:
      # 设置超时，囊括了okhttp的超时，okhttp属于真正执行的超时，openFeign属于服务间的超时
      # 设置全局超时时间
      default:
        connectTimeout: 2000
        readTimeout: 5000
      # 针对特定contextId设置超时时间
#      walletApi:
#        connectTimeout: 1000
#        readTimeout: 2000

```

#### 添加 `LoadBalancerCacheManager`

```xml
<!-- 解决项目启动警告：LoadBalancerCacheManager not available, returning delegate without caching.-->
<!-- 如果注册中心有自己的缓存，那么就可以禁用loadbalancer的缓存-->
<dependency>
    <groupId>com.github.ben-manes.caffeine</groupId>
    <artifactId>caffeine</artifactId>
    <version>3.1.1</version>
</dependency>
<!-- 解决项目启动警告：Spring Cloud LoadBalancer is currently working with the default cache. 
You can switch to using Caffeine cache, 
by adding it and org.springframework.cache.caffeine.CaffeineCacheManager to the classpath.-->
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-context-support</artifactId>
</dependency>
```

```yaml
spring:
  cloud:
   loadbalancer:
   cache:
   # 开启缓存，如果注册中心有自己的缓存，那么就可以禁用loadbalancer的缓存
   enabled: true
   # 过期时间10s
   ttl: 10
   # 容量256M
   capacity: 256
   caffeine:
#   initialCapacity=[integer]: sets Caffeine.initialCapacity.
#   maximumSize=[long]: sets Caffeine.maximumSize.
#   maximumWeight=[long]: sets Caffeine.maximumWeight.
#   expireAfterAccess=[duration]: sets Caffeine.expireAfterAccess(long, java.util.concurrent.TimeUnit).
#   expireAfterWrite=[duration]: sets Caffeine.expireAfterWrite(long, java.util.concurrent.TimeUnit).
#   refreshAfterWrite=[duration]: sets Caffeine.refreshAfterWrite(long, java.util.concurrent.TimeUnit).
#   weakKeys: sets Caffeine.weakKeys().
#   weakValues: sets Caffeine.weakValues().
#   softValues: sets Caffeine.softValues().
#   recordStats: sets Caffeine.recordStats().
#   initialCapacity初始化键值对的数量
   spec: initialCapacity=500,expireAfterWrite=5s

```

#### 什么情况下引入依赖

**`okhttp` 是一个请求库，使用 `@EnableFeignClients` 注解的服务需要配置上述的配置。**