package com.qingyin.cloud.util;

import com.alibaba.fastjson2.JSON;
import com.qingyin.cloud.constant.CommonConstant;
import com.qingyin.cloud.vo.User.LoginUserInfo;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
public class SecurityAuthUtils {



    /**
     * 将 user 并设置到 login-user 的请求头，使用 json 存储值
     *
     * @param builder 请求
     * @param user 用户
     */
    @SneakyThrows
    public static void setLoginUserHeader(ServerHttpRequest.Builder builder, LoginUserInfo user) {
        try {
            String userStr = JSON.toJSONString(user);
            userStr = URLEncoder.encode(userStr, StandardCharsets.UTF_8.name()); // 编码，避免中文乱码
            builder.header(CommonConstant.LOGIN_USER_HEADER, userStr);
        } catch (Exception ex) {
            log.error("[setLoginUserHeader][序列化 user({}) 发生异常]", user, ex);
            throw ex;
        }
    }

    /**
     * 移除请求头的用户
     *
     * @param exchange 请求
     * @return 请求
     */
    public static ServerWebExchange removeLoginUser(ServerWebExchange exchange) {
        // 如果不包含，直接返回
        if (!exchange.getRequest().getHeaders().containsKey(CommonConstant.LOGIN_USER_HEADER)) {
            return exchange;
        }
        // 如果包含，则移除。参考 RemoveRequestHeaderGatewayFilterFactory 实现
        ServerHttpRequest request = exchange.getRequest().mutate()
                .headers(httpHeaders -> httpHeaders.remove(CommonConstant.LOGIN_USER_HEADER)).build();
        return exchange.mutate().request(request).build();
    }
}
