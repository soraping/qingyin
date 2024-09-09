package com.qingyin.cloud.advice;

import com.qingyin.cloud.enums.ErrorEnum;
import com.qingyin.cloud.exception.LoginException;
import com.qingyin.cloud.vo.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * <h2>全局异常捕获处理</h2>
 * */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionAdvice {

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(Exception.class)
    public CommonResponse<Object> handleException(Exception ex){
        log.error("service has error: [{}]", ex.getMessage(), ex);
        return CommonResponse.failed(ErrorEnum.SYSTEM_ERROR.getCode(), ex.getMessage());
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(LoginException.class)
    public CommonResponse<Object> handleLoginException(LoginException ex){
        return CommonResponse.failed(ex.getCode(), ex.getMessage());
    }


}
