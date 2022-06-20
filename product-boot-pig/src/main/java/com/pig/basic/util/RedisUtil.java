package com.pig.basic.util;

import cn.hutool.core.util.ObjectUtil;
import com.pig.basic.constant.SystemConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;
import java.util.Set;

/**
 * redis工具类
 */
@Component
public class RedisUtil {

    private static JedisPool jedisPool;

    @Autowired
    public void setJedisPool(JedisPool jedisPool) {
        RedisUtil.jedisPool = jedisPool;
    }

    /**
     * 获取Jedis实例
     */
    public static synchronized Jedis getJedis() {
        try {
            if (jedisPool != null) {
                return jedisPool.getResource();
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new RuntimeException("获取Jedis资源异常:" + e.getMessage());
        }
    }

    /**
     * 释放Jedis资源
     */
    public static void closePool() {
        try {
            jedisPool.close();
        } catch (Exception e) {
            throw new RuntimeException("释放Jedis资源异常:" + e.getMessage());
        }
    }

    /**
     * 获取redis键值-object
     */
    public static Object getObject(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            byte[] bytes = jedis.get(key.getBytes());
            if (bytes != null && bytes.length != 0) {
                return ObjectUtil.unserialize(bytes);
            }
        } catch (Exception e) {
            throw new RuntimeException("获取Redis键值getObject方法异常:key=" + key + " cause=" + e.getMessage());
        }
        return null;
    }

    /**
     * 设置redis键值-object
     */
    public static String setObject(String key, Object value) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.set(key.getBytes(), ObjectUtil.serialize(value));
        } catch (Exception e) {
            throw new RuntimeException("设置Redis键值setObject方法异常:key=" + key + " value=" + value + " cause=" + e.getMessage());
        }
    }

    /**
     * 设置redis键值-object-expiretime
     */
    public static String setObject(String key, Object value, int expiretime) {
        String result;
        try (Jedis jedis = jedisPool.getResource()) {
            result = jedis.set(key.getBytes(), ObjectUtil.serialize(value));
            if (SystemConstant.OK.equals(result)) {
                jedis.expire(key.getBytes(), expiretime);
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException("设置Redis键值setObject方法异常:key=" + key + " value=" + value + " cause=" + e.getMessage());
        }
    }

    /**
     * 获取redis键值-Json
     */
    public static String getJson(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.get(key);
        } catch (Exception e) {
            throw new RuntimeException("获取Redis键值getJson方法异常:key=" + key + " cause=" + e.getMessage());
        }
    }

    /**
     * 设置redis键值-Json
     */
    public static String setJson(String key, String value) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.set(key, value);
        } catch (Exception e) {
            throw new RuntimeException("设置Redis键值setJson方法异常:key=" + key + " value=" + value + " cause=" + e.getMessage());
        }
    }

    /**
     * 设置redis键值-Json
     */
    public static Long rightPush(String key, Object value) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.rpush(key.getBytes(), ObjectUtil.serialize(value));
        } catch (Exception e) {
            throw new RuntimeException("设置Redis键值rightPush方法异常:key=" + key + " value=" + value + " cause=" + e.getMessage());
        }
    }

    /**
     * 设置redis键值-Json
     */
    public static List range(String key, int i, int j) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.lrange(key,i,j);
        } catch (Exception e) {
            throw new RuntimeException("设置Redis键值range方法异常:key=" + key + " cause=" + e.getMessage());
        }
    }

    /**
     * 设置redis键值-Json
     */
    public static String leftPop(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.lpop(key);
        } catch (Exception e) {
            throw new RuntimeException("设置Redis键值leftPop方法异常:key=" + key + " cause=" + e.getMessage());
        }
    }

    /**
     * 设置redis键值-Json
     */
    public static void expire(String key, int expiretime) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.expire(key, expiretime);
        } catch (Exception e) {
            throw new RuntimeException("设置Redis键值超时时间方法异常:key=" + key + " cause=" + e.getMessage());
        }
    }

    /**
     * 设置redis键值-Json-expiretime
     */
    public static String setJson(String key, String value, int expiretime) {
        String result;
        try (Jedis jedis = jedisPool.getResource()) {
            result = jedis.set(key, value);
            if (SystemConstant.OK.equals(result)) {
                jedis.expire(key, expiretime);
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException("设置Redis键值setJson方法异常:key=" + key + " value=" + value + " cause=" + e.getMessage());
        }
    }

    /**
     * 删除key
     */
    public static Long delKey(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.del(key.getBytes());
        } catch (Exception e) {
            throw new RuntimeException("删除Redis的键delKey方法异常:key=" + key + " cause=" + e.getMessage());
        }
    }

    /**
     * key是否存在
     */
    public static Boolean exists(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.exists(key.getBytes());
        } catch (Exception e) {
            throw new RuntimeException("查询Redis的键是否存在exists方法异常:key=" + key + " cause=" + e.getMessage());
        }
    }

    /**
     * 模糊查询获取key集合(keys的速度非常快，但在一个大的数据库中使用它仍然可能造成性能问题，生产不推荐使用)
     */
    public static Set<String> keysS(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.keys(key);
        } catch (Exception e) {
            throw new RuntimeException("模糊查询Redis的键集合keysS方法异常:key=" + key + " cause=" + e.getMessage());
        }
    }

    /**
     * 模糊查询获取key集合(keys的速度非常快，但在一个大的数据库中使用它仍然可能造成性能问题，生产不推荐使用)
     */
    public static Set<byte[]> keysB(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.keys(key.getBytes());
        } catch (Exception e) {
            throw new RuntimeException("模糊查询Redis的键集合keysB方法异常:key=" + key + " cause=" + e.getMessage());
        }
    }

    /**
     * 获取过期剩余时间
     */
    public static Long ttl(String key) {
        Long result = -2L;
        try (Jedis jedis = jedisPool.getResource()) {
            result = jedis.ttl(key);
            return result;
        } catch (Exception e) {
            throw new RuntimeException("获取Redis键过期剩余时间ttl方法异常:key=" + key + " cause=" + e.getMessage());
        }
    }


}
