package com.qingyin.cloud.filter;

import com.alibaba.fastjson2.JSON;
import com.qingyin.cloud.constant.CommonConstant;
import com.qingyin.cloud.vo.User.LoginUserInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;

/**
 * <h1>业务模块请求拦截器</h1>
 */
@SuppressWarnings("all")
@Slf4j
@Component
public class LoginUserInfoInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 部分请求不需要带有身份信息, 即白名单
        if (checkWhiteListUrl(request.getRequestURI())) {
            return true;
        }

        // 请求限制来源只能 gateway


        LoginUserInfo loginUserInfo = getUserInfoFromHeader(request);
        if(loginUserInfo != null) {
            log.info("set login user info: [{}]", request.getRequestURI());
            AccessContext.setLoginUserInfo(loginUserInfo);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        if (null != AccessContext.getLoginUserInfo()) {
            AccessContext.clearLoginUserInfo();
        }
    }

    private LoginUserInfo getUserInfoFromHeader(HttpServletRequest request) {
        String userInfos = request.getHeader(CommonConstant.LOGIN_USER_HEADER);
        if(userInfos != null) {
            return JSON.parseObject(URLDecoder.decode(userInfos), LoginUserInfo.class);
        }
        return null;
    }

    /**
     * <h2>校验是否是白名单接口</h2>
     * swagger2 接口
     * */
    private boolean checkWhiteListUrl(String url) {

        return StringUtils.containsAny(
                url,
                "springfox", "swagger", "v3",
                "webjars", "doc.html"
        );
    }
}
