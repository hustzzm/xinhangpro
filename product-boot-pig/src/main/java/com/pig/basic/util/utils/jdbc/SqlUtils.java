package com.pig.basic.util.utils.jdbc;


import com.pig.basic.util.utils.ArrayUtils;
import com.pig.basic.util.utils.CastUtils;
import com.pig.basic.util.utils.ExceptionUtils;
import com.pig.basic.util.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class SqlUtils {

    private SqlUtils() {
        ExceptionUtils.requireNonInstance();
    }

    public static final String BACK_QUOTE = "`";


    public static final String BLANK = StringUtils.BLANK;
    public static final String LEFT_BRACKET = "(";
    public static final String RIGHT_BRACKET = ")";
    public static final String COMMA = ",";
    public static final String QUESTION = "?";
    public static final String EQUAL = "=";
    public static final String ASTERISK = "*";

    public static final String SELECT = "select";
    public static final String FROM = "from";
    public static final String WHERE = "where";
    public static final String DELETE = "delete";
    public static final String AND = "and";
    public static final String OR = "or";
    public static final String UPDATE = "update";
    public static final String SET = "set";
    public static final String INSERT = "insert";
    public static final String INTO = "into";
    public static final String VALUES = "values";

    private static String getFiled(String s) {
        if (s.contains(BLANK)) {
            return s;
        }
        return BACK_QUOTE + s + BACK_QUOTE;
    }

    private static String getWhereSql(Collection<String> reason) {

        return reason.stream().map(t -> getFiled(t) + EQUAL + QUESTION).collect(Collectors.joining(BLANK + AND + BLANK));
    }

    private static String getSetSql(Collection<String> set) {

        return set.stream().map(t -> getFiled(t) + EQUAL + QUESTION).collect(Collectors.joining(COMMA));
    }

    private static String getFieldsSql(String... field) {
        if (StringUtils.isNotEmpty(field)) {
            return Arrays.stream(field).map(t -> getFiled(t)).collect(Collectors.joining(COMMA));
        } else {
            return ASTERISK;
        }
    }

    private static String getValuesSql(Collection<String> set) {
        return set.stream().map(t -> QUESTION).collect(Collectors.joining(COMMA, LEFT_BRACKET, RIGHT_BRACKET));
    }

    private static String getSelectSql(String tableName, Collection<String> reason, String... field) {
        StringBuilder sb = new StringBuilder();
        sb.append(SELECT + BLANK);
        sb.append(getFieldsSql(field) + BLANK);
        sb.append(FROM + BLANK);
        sb.append(getFiled(tableName) + BLANK);
        sb.append(WHERE + BLANK);
        sb.append(getWhereSql(reason));
        return sb.toString();
    }

    private static String getDeleteSql(String tableName, Collection<String> reason) {
        StringBuilder sb = new StringBuilder();
        sb.append(DELETE + BLANK + FROM + BLANK);
        sb.append(getFiled(tableName) + BLANK);
        sb.append(WHERE + BLANK);
        sb.append(getWhereSql(reason));
        return sb.toString();
    }

    private static String getUpdateSql(String tableName, Collection<String> set, Collection<String> reason) {
        StringBuilder sb = new StringBuilder();
        sb.append(UPDATE + BLANK);
        sb.append(getFiled(tableName) + BLANK);
        sb.append(SET + BLANK);
        sb.append(getSetSql(set) + BLANK);
        sb.append(WHERE + BLANK);
        sb.append(getWhereSql(reason));
        return sb.toString();
    }

    public static String getInsertSql(String tableName, Collection<String> set) {
        StringBuilder sb = new StringBuilder();
        sb.append(INSERT + BLANK + INTO + BLANK);
        sb.append(getFiled(tableName) + BLANK);
        sb.append(LEFT_BRACKET + getFieldsSql(set.toArray(new String[]{})) + RIGHT_BRACKET + BLANK);
        sb.append(VALUES + BLANK);
        sb.append(getValuesSql(set));
        return sb.toString();
    }

    public static Two select(Map<String, Object> data, String tableName, String reason, String[] field) {
        return select(data, tableName, CastUtils.toList(reason), field);
    }

    public static Two select(Map<String, Object> data, String tableName, String[] reason, String[] field) {
        return select(data, tableName, CastUtils.toList(reason), field);
    }

    public static Two select(Map<String, Object> data, String tableName, Collection<String> reason, String[] field) {
        List<Object> list = new ArrayList<>();
        for (String k : reason) {
            list.add(data.get(k));
        }
        return new Two(getSelectSql(tableName, reason, field), list.toArray());
    }

    public static Two select(Map<String, Object> data, String tableName, String[] field) {
        return select(data, tableName, data.keySet(), field);
    }

    public static Two selectAll(Map<String, Object> data, String tableName) {
        return selectAll(data, tableName, data.keySet());
    }

    public static Two selectAll(Map<String, Object> data, String tableName, String... reason) {
        return selectAll(data, tableName, CastUtils.toList(reason));
    }

    public static Two selectAll(Map<String, Object> data, String tableName, Collection<String> reason) {
        List<Object> list = new ArrayList<>();
        for (String k : reason) {
            list.add(data.get(k));
        }
        return new Two(getSelectSql(tableName, reason), list.toArray());
    }

    public static Two delete(Map<String, Object> data, String tableName) {
        return delete(data, tableName, data.keySet());
    }

    public static Two delete(Map<String, Object> data, String tableName, String reason) {
        return delete(data, tableName, CastUtils.toList(reason));
    }

    public static Two delete(Map<String, Object> data, String tableName, String[] reason) {
        return delete(data, tableName, CastUtils.toList(reason));
    }

    public static Two delete(Map<String, Object> data, String tableName, Collection<String> reason) {
        List<Object> list = new ArrayList<>();
        for (String k : reason) {
            list.add(data.get(k));
        }
        return new Two(getDeleteSql(tableName, reason), list.toArray());
    }

    public static Two update(Map<String, Object> data, String tableName, String reason, String... exclude) {
        return update(data, tableName, CastUtils.toList(reason), exclude);
    }

    public static Two update(Map<String, Object> data, String tableName, String[] reason, String... exclude) {
        return update(data, tableName, CastUtils.toList(reason), exclude);
    }

    public static Two update(Map<String, Object> data, String tableName, Collection<String> reason, String... exclude) {
        Map<String, Object> map1 = returnNotNullFieldMap(data);
        Map<String, Object> map2 = returnMap(map1, exclude);
        Map<String, Object> map3 = returnMap(map2, reason.toArray(new String[]{}));
        List<Object> list = new ArrayList<>();
        Set<String> ks = map3.keySet();
        for (String k : ks) {
            list.add(map3.get(k));
        }
        for (String k : reason) {
            list.add(map2.get(k));
        }
        return new Two(getUpdateSql(tableName, ks, reason), list.toArray());
    }


    public static Two insert(Map<String, Object> data, String tableName, String... exclude) {
        Map<String, Object> map1 = returnNotNullFieldMap(data);
        Map<String, Object> map2 = returnMap(map1, exclude);
        List<Object> list = new ArrayList<>();
        Set<String> ks = map2.keySet();
        for (String k : ks) {
            list.add(map2.get(k));
        }
        return new Two(getInsertSql(tableName, ks), list.toArray());
    }


    public static String truncateSql(String tableName) {
        return "TRUNCATE TABLE " + getFiled(tableName);
    }

    public static String createSql(String oldTable, String newTable) {
        return "CREATE TABLE " + getFiled(newTable) + " SELECT * FROM " + getFiled(oldTable) + " WHERE 1=2";
    }

    public static String deleteTable(String tableName) {
        return "DROP TABLE " + getFiled(tableName);
    }


    public static Map<String, Object> returnNotNullFieldMap(Map<String, Object> data) {
        Map<String, Object> m = new HashMap<>();
        Set<String> keys = data.keySet();
        for (String k : keys) {
            Object v = data.get(k);
            if (v != null) {
                m.put(k, v);
            }
        }
        return m;
    }

    public static Map<String, Object> returnMap(Map<String, Object> data, String... exclude) {
        Map<String, Object> m = new HashMap<>();
        Set<String> keys = data.keySet();
        for (String k : keys) {
            if (!ArrayUtils.contain(exclude, k)) {
                m.put(k, data.get(k));
            }
        }
        return m;
    }


    public static class Two {

        private final String sql;
        private final Object[] params;

        public Two(String sql, Object[] params) {
            this.sql = sql;
            this.params = params.clone();
            log.debug(sql);
            log.debug(Arrays.toString(params));
        }

        public String getSql() {
            return sql;
        }

        public Object[] getParams() {
            return params.clone();
        }
    }
}
