package com.qingyin.cloud.filter;

import com.qingyin.cloud.annotation.NoSecurity;
import com.qingyin.cloud.api.authority.UserProvider;
import com.qingyin.cloud.api.authority.vo.UserVo;
import com.qingyin.cloud.constant.CommonConstant;
import com.qingyin.cloud.util.TokenParseUtils;
import com.qingyin.cloud.vo.User.LoginUserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.reactive.HandlerMapping;
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

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        log.info("===========GlobalAuthSecurityFilter============");

        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        // 获取是否有忽略注解
        HandlerMethod handlerMethod =
                exchange.getAttribute(HandlerMapping.BEST_MATCHING_HANDLER_ATTRIBUTE);
        NoSecurity noSecurityAnnotation = AnnotatedElementUtils.findMergedAnnotation(handlerMethod.getMethod(), NoSecurity.class);
        // 直接放行
        if(noSecurityAnnotation != null) {
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
        UserVo userVo = userProvider.getUserDetailById(loginUserInfo.getId()).getData();
        if(userVo.getIsDisable() == 1){
            log.error("user status is disable");
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }

        // 解析通过, 则放行
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
