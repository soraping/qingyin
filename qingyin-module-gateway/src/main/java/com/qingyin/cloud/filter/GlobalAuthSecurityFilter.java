package com.qingyin.cloud.filter;

import com.alibaba.fastjson2.JSON;
import com.qingyin.cloud.api.authority.UserProvider;
import com.qingyin.cloud.api.authority.vo.UserVo;
import com.qingyin.cloud.config.GatewayNoSecurityPathConfig;
import com.qingyin.cloud.constant.CommonConstant;
import com.qingyin.cloud.util.RedisUtils;
import com.qingyin.cloud.util.SecurityAuthUtils;
import com.qingyin.cloud.util.TokenParseUtils;
import com.qingyin.cloud.vo.User.LoginUserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.Objects;


/**
 * <h1>权限校验</h1>
 * 忽略 @NoSecurity 注解的接口
 */
@Slf4j
@Component
public class GlobalAuthSecurityFilter implements GlobalFilter, Ordered {

    @Resource
    private UserProvider userProvider;

    private final GatewayNoSecurityPathConfig gatewayNoSecurityPathConfig;

    public GlobalAuthSecurityFilter(GatewayNoSecurityPathConfig gatewayNoSecurityPathConfig) {
        this.gatewayNoSecurityPathConfig = gatewayNoSecurityPathConfig;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        log.info("===========GlobalAuthSecurityFilter============");

        // 移除 login-user 的请求头，避免伪造模拟
        SecurityAuthUtils.removeLoginUser(exchange);

        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        // 请求路径
        String requestUrl = request.getURI().getPath();
        log.info("请求路径 => [{}]", requestUrl);

        // 查看命中路由
        Route route = (Route) exchange.getAttributes().get(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
        if(Objects.isNull(route)) {
            log.error("[{}] 未命中路由", requestUrl);
            response.setStatusCode(HttpStatus.NOT_FOUND);
            return response.setComplete();
        }
        log.info("命中路由id => [{}]", route.getId());

        // 是否在忽略校验路由集合内
        Boolean isSkip = shouldSkipValidation(requestUrl);
        if(isSkip) {
            // 直接放行
            log.info("[{}] 无需校验，直接放行", requestUrl);
            return chain.filter(exchange);
        }

        // 检验 token
        HttpHeaders headers = request.getHeaders();
        String token = headers.getFirst(CommonConstant.JWT_USER_INFO_KEY);
        if(Objects.isNull(token)) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }

        LoginUserInfo loginUserInfo = null;
        try {
            loginUserInfo = TokenParseUtils.parseUserInfoFromToken(token);
        } catch (Exception ex) {
            log.error("parse user info from token error: [{}]", ex.getMessage(), ex);
        }
        // 获取不到登录用户信息, 返回 401
        if (null == loginUserInfo) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }

        // 检测用户状态
        UserVo userVo = getUserVo(loginUserInfo.getId());
        if(userVo.getIsDisable() == 1){
            log.error("user status is disable");
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }

        // 将 LoginUserInfo 设置在 header 中
        LoginUserInfo finalLoginUserInfo = loginUserInfo;
        ServerWebExchange newExchange = exchange.mutate()
                .request(builder -> SecurityAuthUtils.setLoginUserHeader(builder, finalLoginUserInfo)).build();

        // 解析通过, 则放行
        return chain.filter(newExchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }

    /**
     * 获取用户信息
     * @param userId
     * @return
     */
    private UserVo getUserVo(Long userId) {
        if(RedisUtils.exists(CommonConstant.LOGIN_USER_REDIS_KEY)) {
            String userInfos = (String) RedisUtils.get(CommonConstant.LOGIN_USER_REDIS_KEY);
            return JSON.parseObject(userInfos, UserVo.class);
        }
        UserVo userVo = userProvider.getUserDetailById(userId).getData();
        RedisUtils.set(CommonConstant.LOGIN_USER_REDIS_KEY, JSON.toJSONString(userVo), 5 * 60);
        return userVo;
    }

    /**
     * 是否忽略校验
     * @param path
     * @return
     */
    private Boolean shouldSkipValidation(String path) {
        return gatewayNoSecurityPathConfig.getPaths().contains(path);
    }
}
