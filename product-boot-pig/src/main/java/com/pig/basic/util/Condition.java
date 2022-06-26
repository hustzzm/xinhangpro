package com.pig.basic.util;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.BeanUtils;

import java.util.Map;

/**
 * 查询条件包装
 */
public class Condition {

    /**
     * 获取mybatis plus中的QueryWrapper
     *
     * @param query   查询条件
     * @param clazz   实体类
     * @param <T>     类型
     * @return QueryWrapper
     */
    public static <T> QueryWrapper<T> getQueryWrapper(Map<String, Object> query,Class<T> clazz) {
        QueryWrapper<T> qw = new QueryWrapper<>();
        qw.setEntity(BeanUtils.instantiateClass(clazz));
        SqlKeyword.buildCondition(query, qw);
        return qw;
    }
}
