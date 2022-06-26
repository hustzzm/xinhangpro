package com.pig.basic.constant;

public interface SystemConstant {
    /**
     * redis-OK
     */
    String OK = "OK";

    /**
     * redis-key-前缀-shiro:cache:
     */
    String PREFIX_SHIRO_CACHE = "shiro:cache:";

    /**
     * redis-key-前缀-shiro:access_token:
     */
    String PREFIX_SHIRO_ACCESS_TOKEN = "shiro:access_token:";

    /**
     * redis-key-前缀-shiro:refresh_token:
     */
    String PREFIX_SHIRO_REFRESH_TOKEN = "shiro:refresh_token:";

    /**
     * JWT-currentTimeMillis:
     */
    String CURRENT_TIME_MILLIS = "currentTimeMillis";

    /**
     * JWT-account:
     */
    String ACCOUNT = "userCode";

    /**
     * Header:
     */
    String USER_HEADER = "Berry-Auth";

    /**
     * 默认密码
     */
    String DEFAULT_PASSWORD = "111111";

    /**
     * 数据库enable字段 是
     */
    int ENABLE_TRUE = 1;

    /**
     * 数据库enable字段 否
     */
    int ENABLE_FALSE = 0;

    /**
     * 写入添加换行
     */
    String Line_break_windows = "\r\n";

    String Line_break_linux = "\n";
    String split_path_windows = "\\";
    String split_path_linux = "/";
}
