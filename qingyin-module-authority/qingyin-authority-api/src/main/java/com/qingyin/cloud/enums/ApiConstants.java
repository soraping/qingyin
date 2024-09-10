package com.qingyin.cloud.enums;

import com.qingyin.cloud.constant.CommonConstant;

public class ApiConstants {

    /**
     * 服务名
     * 注意，需要保证和 spring.application.name 保持一致
     */
    public static final String NAME = "qingyin-authority-provider";


    public static final String PREFIX = CommonConstant.RPC_API_PREFIX + "/authority";
}
