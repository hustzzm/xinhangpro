package com.pig.modules.wes.util;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 以map为基础的简单缓存
 */
public class CacheUtil {
    /**
     * 缓存map
     */
    private static ConcurrentHashMap<String,Object> cacheMap=new ConcurrentHashMap<>();

    /**
     * 缓存获取
     * @param key
     * @return
     */
    public static Object get(String key) {
        if (!cacheMap.containsKey(key)) {
            return null;
        }
        return cacheMap.get(key);
    }

    /**
     * 缓存设置
     * @param key
     * @param value
     */
    public static void set(String key, Object value) {
        cacheMap.put(key, value);
    }

    /**
     * 判断缓存在不在
     * @param key
     * @return
     */
    public static boolean isExist(String key) {
        return cacheMap.containsKey(key);
    }


}
