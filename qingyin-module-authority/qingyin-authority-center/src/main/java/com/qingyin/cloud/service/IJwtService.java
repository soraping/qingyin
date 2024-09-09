package com.qingyin.cloud.service;

/**
 * <h1>JWT 相关服务接口定义</h1>
 */
public interface IJwtService {
    /**
     * <h2>生成 JWT Token, 使用默认的超时时间</h2>
     * */
    String generateToken(String username, String password) throws Exception;

    /**
     * <h2>生成指定超时时间的 Token, 单位是天</h2>
     * */
    String generateToken(String username, String password, int expire) throws Exception;
}
