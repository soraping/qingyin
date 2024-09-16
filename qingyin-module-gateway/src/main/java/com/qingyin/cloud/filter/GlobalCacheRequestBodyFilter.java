package com.qingyin.cloud.filter;

import com.qingyin.cloud.util.TimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

/**
 * <h1>缓存请求 body 的全局过滤器</h1>
 * springcloud gateway request body 内容只能取一次
 * 这是因为 HTTP 请求体通常是以流（stream）的形式传输的，
 * 一旦读取并处理（例如，通过解析为 JSON 或 XML），流就会被消耗掉，无法再次读取。
 */
@Slf4j
@Component
@SuppressWarnings("all")
public class GlobalCacheRequestBodyFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String requestUrl = exchange.getRequest().getURI().toString();
        log.debug("{} 请求进入 {}", TimeUtils.timestampToDate(TimeUtils.timestamp()), requestUrl);
        ServerHttpRequest serverHttpRequest = exchange.getRequest();
        String methodName = serverHttpRequest.getMethodValue();
        String contentType = serverHttpRequest.getHeaders().getFirst("Content-Type");
        URI uri = serverHttpRequest.getURI();

        // post请求拦截判断重复调用
        if(HttpMethod.POST.name().equals(methodName) && !contentType.startsWith("multipart/form-data")){

            return DataBufferUtils.join(serverHttpRequest.getBody()).flatMap(dataBuffer -> {
                // 确保数据缓冲区不被释放, 必须要 DataBufferUtils.retain
                DataBufferUtils.retain(dataBuffer);
                // defer、just 都是去创建数据源, 得到当前数据的副本
                Flux<DataBuffer> cacheFlux = Flux.defer(() ->
                        Flux.just(dataBuffer.slice(0, dataBuffer.readableByteCount())));

                // 重新包装 ServerHttpRequest, 重写 getBody 方法, 能够返回请求数据
                ServerHttpRequest mutatedRequest =
                        new ServerHttpRequestDecorator(serverHttpRequest) {
                            @Override
                            public Flux<DataBuffer> getBody() {
                                return cacheFlux;
                            }
                        };
                // 将包装之后的 ServerHttpRequest 向下继续传递
                return chain.filter(exchange.mutate().request(mutatedRequest).build());
            });
        }

        //放行请求
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE + 1;
    }
}
