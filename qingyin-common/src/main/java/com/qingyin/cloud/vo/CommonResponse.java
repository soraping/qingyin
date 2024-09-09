package com.qingyin.cloud.vo;

import com.qingyin.cloud.enums.ErrorEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * <h1>通用响应对象定义</h1>
 * {
 *     "code": 0,
 *     "message": "",
 *     "data": {}
 * }
 * */
@Data
public class CommonResponse<T> extends BaseVo {

    /**
     * 错误码
     */
    private Integer code;

    /**
     * 错误信息
     */
    private String message;

    /**
     * 泛型响应数据
     */
    private T data;

    /** 无参构造 **/
    protected CommonResponse() {}

    /**
     * 带参构造
     * @param code
     * @param message
     */
    public CommonResponse(Integer code, String message){
        this.code = code;
        this.message = message;
    }
    public CommonResponse(Integer code, String message, T data){
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 成功返回结果
     * @return
     */
    public static CommonResponse<Object> success() {
        return new CommonResponse<>(ErrorEnum.SUCCESS.getCode(), ErrorEnum.SUCCESS.getMsg(), new ArrayList<>());
    }

    public static CommonResponse<Object> success(Integer code) {
        return new CommonResponse<>(code, ErrorEnum.SUCCESS.getMsg(), new ArrayList<>());
    }

    public static CommonResponse<Object> success(String msg) {
        return new CommonResponse<>(ErrorEnum.SUCCESS.getCode(), msg, new ArrayList<>());
    }

    public static CommonResponse<Object> success(Integer code, String msg) {
        return new CommonResponse<>(code, msg, new ArrayList<>());
    }

    public static <T> CommonResponse<T> success(String msg, T data) {
        return new CommonResponse<>(ErrorEnum.SUCCESS.getCode(), msg, data);
    }

    public static <T> CommonResponse<T> success(Integer code, String msg, T data) {
        return new CommonResponse<>(code, msg, data);
    }

    /**
     * 响应失败结果
     * @param code
     * @return
     * @param <T>
     */
    public static <T> CommonResponse <T> failed(Integer code, String msg) {
        return new CommonResponse<T>(code, msg);
    }

}
