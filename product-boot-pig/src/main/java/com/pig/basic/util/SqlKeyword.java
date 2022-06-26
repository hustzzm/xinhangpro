package com.pig.basic.util;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.util.ObjectUtils;

import java.util.Map;

/**
 * sql组装器
 */
public class SqlKeyword {

    private static final String EQUAL = "_equal";
    private static final String NOT_EQUAL = "_notequal";
    private static final String LIKE = "_like";
    private static final String NOT_LIKE = "_notlike";
    private static final String GT = "_gt";
    private static final String LT = "_lt";
    private static final String GE = "_ge";
    private static final String LE = "_le";
    private static final String DATE_GT = "_dategt";
    private static final String DATE_EQUAL = "_dateequal";
    private static final String DATE_LT = "_datelt";
    private static final String IS_NULL = "_null";
    private static final String NOT_NULL = "_notnull";
    private static final String IGNORE = "_ignore";
    private static final String IN = "_in";

    /**
     * 条件构造器
     *
     * @param query 查询字段
     * @param qw    查询包装类
     */
    public static void buildCondition(Map<String, Object> query, QueryWrapper<?> qw) {
        if (query.isEmpty()) {
            return;
        }
        query.forEach((k, v) -> {
            if (hasEmpty(k, v) || k.endsWith(IGNORE)) {
                return;
            }
            if (k.endsWith(EQUAL)) {
                qw.eq(getColumn(k, EQUAL), v);
            } else if (k.endsWith(NOT_EQUAL)) {
                qw.ne(getColumn(k, NOT_EQUAL), v);
            } else if (k.endsWith(NOT_LIKE)) {
                qw.notLike(getColumn(k, NOT_LIKE), v);
            } else if (k.endsWith(GT)) {
                qw.gt(getColumn(k, GT), v);
            } else if (k.endsWith(LT)) {
                qw.lt(getColumn(k, LT), v);
            } else if (k.endsWith(DATE_GT)) {
                qw.gt(getColumn(k, DATE_GT), v);
            } else if (k.endsWith(DATE_EQUAL)) {
                qw.eq(getColumn(k, DATE_EQUAL), v);
            } else if (k.endsWith(DATE_LT)) {
                qw.lt(getColumn(k, DATE_LT), v);
            } else if (k.endsWith(IS_NULL)) {
                qw.isNull(getColumn(k, IS_NULL));
            } else if (k.endsWith(NOT_NULL)) {
                qw.isNotNull(getColumn(k, NOT_NULL));
            }else if (k.endsWith(IN)) {
                qw.in(getColumn(k, IN), v);
            }else if (k.endsWith(GE)) {
                qw.ge(getColumn(k, GE), v);
            }else if (k.endsWith(LE)) {
                qw.le(getColumn(k, LE), v);
            } else {
                qw.like(getColumn(k, LIKE), v);
            }
        });
    }

    /**
     * 获取数据库字段
     *
     * @param column  字段名
     * @param keyword 关键字
     * @return String
     */
    private static String getColumn(String column, String keyword) {
        return StringUtil.humpToUnderline(StrUtil.removeSuffix(column, keyword));
    }

    public static boolean hasEmpty(Object... os) {
        Object[] var1 = os;
        int var2 = os.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            Object o = var1[var3];
            if (ObjectUtils.isEmpty(o)) {
                return true;
            }
        }

        return false;
    }

}
