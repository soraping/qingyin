package com.qingyin.cloud.enums;

import com.qingyin.cloud.constant.CommonConstant;

public class ApiConstants {

    /**
     * 服务名
     * 注意，需要保证和 spring.application.name 保持一致
     */
    public static final String FEIGN_VALUE = "qingyin-authority-provider";

    public static final String AUTH_NAME = "qingyin-authority-provider";

    public static final String USER_NAME = "qingyin-user-provider";


    public static final String AUTH_PREFIX = CommonConstant.RPC_API_PREFIX + "/authority";

    public static final String USER_PREFIX = CommonConstant.RPC_API_PREFIX + "/user";
}
