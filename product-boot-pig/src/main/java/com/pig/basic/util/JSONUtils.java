package com.pig.basic.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

import java.util.List;
import java.util.Map;

/**
*@discrption:JSON相关操作工具类
*@user:wengzhongjie
*@createTime:2019-11-13 19:12
*/
public class JSONUtils {

    /**
     * 对象序列化
     * @param obj 对象
     * @return
     */
    public static String toString(Object obj) {
        return JSONObject.toJSONString(obj);
    }

    /**
     * json字符串反序列化成指定对象
     * @param text  json字符串
     * @param clazz 指定对象
     * @param <T>
     * @return
     */
    public static <T>T toBean(String text,Class<T> clazz){
        return JSONObject.parseObject(text, clazz);
    }

    /**
     * json字符串反序列化成指定对象集合
     * @param text  json字符串
     * @param clazz 指定对象集合
     * @return
     */
    public static <E>List<E> toList(String text, Class<E> clazz) {
        return JSONObject.parseArray(text, clazz);
    }

    /**
     * json字符串转换成指定泛型的Map对象
     * @param text
     * @param k  键
     * @param v  值
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K,V> Map<K,V> toMap(String text, Class<K> k, Class<V> v) {
        return JSON.parseObject(text,new TypeReference<Map<K,V>>(k,v){});
    }

    /**
     * json字符串转换成指定复杂类型对象 要多复杂有多复杂<重要></>
     * @param text
     * @param type
     * @param <T>
     * @return
     */
    public static <T> T nativeRead(String text, TypeReference<T> type) {
        return JSONObject.parseObject(text, type);
    }


}
