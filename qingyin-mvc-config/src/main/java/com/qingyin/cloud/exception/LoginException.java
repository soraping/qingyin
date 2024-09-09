package com.qingyin.cloud.exception;

/**
 * 登录异常
 */
public class LoginException extends BaseException{
    public LoginException(Integer code, String msg) {
        super(code, msg);
    }
}
