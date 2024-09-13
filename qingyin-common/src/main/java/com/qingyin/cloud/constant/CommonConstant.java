package com.qingyin.cloud.constant;

/**
 * <h1>通用模块常量定义</h1>
 */
public final class CommonConstant {

    /**
     * 公钥
     */
    public static final String PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAow6Hi3+spX3TnGZdz8ecdkR2" +
            "mWISIH4P8pYm59sDRlmcCq37pMkRbsB9XXING+B2+6qHGdC91Zg6eSNOyCzaV52AICUd3TB+iDJSa4OuMjgSXPNxJ1e8U8Tefyen" +
            "DUtS2PWBWuVZa5wCDnhRjTs9mQMQEFtBzXagyfymQJaojpx4H8YJBLecxmjyYF4cpETHcadeDe3cR18uij9KBFkR7CNM4PhDSVlS" +
            "3XN+dbn2kEmAsodspEKfaSkfV+xZe2O9ngqt3AMeqcdLm/VL64wRiYJRwgeU0qbjq0YLLI6w+R7D3J0ATiszHUEe28YOdEX1VfNu" +
            "7l8voum18TW2aYZWCQIDAQAB";

    /**
     * jwt 中存储的用户信息 key
     */
    public static final String JWT_USER_INFO_KEY = "qingyin_user_token_key";

    /**
     * rpc 请求前缀
     */
    public static final String RPC_API_PREFIX = "/qingyin-rpc-api";

    /**
     * 表前缀
     */
    public static final String MYSQL_TABLE_PREFIX = "qy_";

    public static final String REDIS_KEY_PREFIX = "qingyin_";

    /**
     * 全局秘钥
     * echo "qingyin" | base64
     */
    public static final String GLOBAL_SECRET = "cWluZ3lpbgo=";
}
