package com.qingyin.cloud.exception;

import com.qingyin.cloud.enums.ErrorEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class BaseException extends RuntimeException{
    private Integer code;
    private String msg;

    public BaseException() {
        this.code = ErrorEnum.FAILED.getCode();
        this.msg = ErrorEnum.FAILED.getMsg();
    }

    public BaseException(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
