package com.pig.basic.shiro.cache;

import cn.hutool.core.util.ObjectUtil;
import com.pig.basic.constant.SystemConstant;

import com.pig.basic.listener.PropertiesListenerConfig;
import com.pig.basic.shiro.token.JWTUtil;
import com.pig.basic.util.RedisUtil;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;

import java.util.*;


/**
 * 重写Shiro的Cache保存读取
 * @param <K>
 * @param <V>
 */
public class CustomCache<K,V> implements Cache<K,V> {

    /**
     * 缓存的key名称获取为shiro:cache:account
     * @param key
     * @return java.lang.String
     * @author dolyw.com
     * @date 2018/9/4 18:33
     */
    private String getKey(Object key) {
        return SystemConstant.PREFIX_SHIRO_CACHE + JWTUtil.getClaim(key.toString(), SystemConstant.ACCOUNT);
    }

    /**
     * 获取缓存
     */
    @Override
    public Object get(Object key) throws CacheException {
        if(!RedisUtil.exists(this.getKey(key))){
            return null;
        }
        return RedisUtil.getObject(this.getKey(key));
    }

    /**
     * 保存缓存
     */
    @Override
    public Object put(Object key, Object value) throws CacheException {
        // 读取配置文件，获取Redis的Shiro缓存过期时间
        String shiroCacheExpireTime = PropertiesListenerConfig.getProperty("shiroCacheExpireTime");
        // 设置Redis的Shiro缓存
        return RedisUtil.setObject(this.getKey(key), value, Integer.parseInt(shiroCacheExpireTime));
    }

    /**
     * 移除缓存
     */
    @Override
    public Object remove(Object key) throws CacheException {
        if(!RedisUtil.exists(this.getKey(key))){
            return null;
        }
        RedisUtil.delKey(this.getKey(key));
        return null;
    }

    /**
     * 清空所有缓存
     */
    @Override
    public void clear() throws CacheException {
        Objects.requireNonNull(RedisUtil.getJedis()).flushDB();
    }

    /**
     * 缓存的个数
     */
    @Override
    public int size() {
        Long size = Objects.requireNonNull(RedisUtil.getJedis()).dbSize();
        return size.intValue();
    }

    /**
     * 获取所有的key
     */
    @Override
    public Set keys() {
        Set<byte[]> keys = Objects.requireNonNull(RedisUtil.getJedis()).keys("*".getBytes());
        Set<Object> set = new HashSet<Object>();
        for (byte[] bs : keys) {
            set.add(ObjectUtil.unserialize(bs));
        }
        return set;
    }

    /**
     * 获取所有的value
     */
    @Override
    public Collection values() {
        Set keys = this.keys();
        List<Object> values = new ArrayList<Object>();
        for (Object key : keys) {
            values.add(RedisUtil.getObject(this.getKey(key)));
        }
        return values;
    }
}
