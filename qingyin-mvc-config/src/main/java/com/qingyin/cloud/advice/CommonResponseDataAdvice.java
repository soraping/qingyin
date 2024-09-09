package com.qingyin.cloud.advice;

import com.qingyin.cloud.annotation.IgnoreResponseAdvice;
import com.qingyin.cloud.vo.CommonResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;


/**
 * <h1>实现统一响应</h1>
 * */
@RestControllerAdvice(value = "com.qingyin.cloud")
public class CommonResponseDataAdvice implements ResponseBodyAdvice<Object> {

    /**
     * <h2>判断是否需要对响应进行处理</h2>
     * */
    @Override
    @SuppressWarnings("all")
    public boolean supports(MethodParameter returnType,
                            Class<? extends HttpMessageConverter<?>> converterType) {

        /**
         * 判断是否在类上注解
         */
        if(returnType.getDeclaringClass().isAnnotationPresent(IgnoreResponseAdvice.class)){
            return false;
        }

        /**
         * 判断是否在方法上注解
         */
        if(returnType.getMethod().isAnnotationPresent(IgnoreResponseAdvice.class)){
            return false;
        }

        return true;
    }

    @Override
    @SuppressWarnings("all")
    public Object beforeBodyWrite(Object body,
                                  MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request,
                                  ServerHttpResponse response) {

        CommonResponse<Object> commonResponse = new CommonResponse<>(0, "");
        if(null == body){
            return commonResponse;
        }else if(body instanceof CommonResponse){
            commonResponse = (CommonResponse<Object>) body;
        }else {
            commonResponse.setData(body);
        }

        return commonResponse;
    }
}
