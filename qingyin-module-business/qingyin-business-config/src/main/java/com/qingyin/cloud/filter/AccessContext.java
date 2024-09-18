package com.qingyin.cloud.filter;

import com.qingyin.cloud.vo.User.LoginUserInfo;

/**
 * <h1>使用 ThreadLocal 去单独存储每一个线程携带的 LoginUserInfo 信息</h1>
 * 要及时的清理我们保存到 ThreadLocal 中的用户信息:
 *  1. 保证没有资源泄露
 *  2. 保证线程在重用时, 不会出现数据混乱
 * */
public class AccessContext {
    private static final ThreadLocal<LoginUserInfo> loginUserInfoThreadLocal = new ThreadLocal<>();

    public static LoginUserInfo getLoginUserInfo() {
        return loginUserInfoThreadLocal.get();
    }

    public static void setLoginUserInfo(LoginUserInfo loginUserInfo) {
        loginUserInfoThreadLocal.set(loginUserInfo);
    }

    public static void clearLoginUserInfo() {
        loginUserInfoThreadLocal.remove();
    }

}
