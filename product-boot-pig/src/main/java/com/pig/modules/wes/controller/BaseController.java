package com.pig.modules.wes.controller;

import com.pig.basic.shiro.UserVo;
import com.pig.basic.util.CommonResult;
import com.pig.basic.util.CommonUtil;
import com.pig.basic.util.utils.ArrayUtils;
import com.pig.basic.util.utils.jdbc.BaseDao;
import com.pig.basic.util.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


@Component
public class BaseController {

    @Resource
    private JdbcTemplate jdbcTemplate;
    @Autowired
    protected BaseDao baseDao;


    private static List<Map<String, Object>> rd_dict;
    private static List<Map<String, Object>> rd_param;

    @PostConstruct
    private void init() {
        System.out.println("加载缓存。。。");
        rd_dict = baseDao.executeQuery("select * from rd_dict where  is_deleted=0 ");
        rd_param = baseDao.executeQuery("select * from rd_param where is_deleted=0 ");
        System.out.println("加载缓存成功");
    }


    //字典
    public Map<Object, String> getDict(String code) {
        List<Map<String, Object>> list = rd_dict.stream().filter(t -> Objects.equals(t.get("code"), code)).collect(Collectors.toList());
        Map<Object, String> map = new HashMap<>();
        for (Map<String, Object> m : list) {
            map.put(m.get("dict_key"), (String) m.get("dict_value"));
        }
        return map;
    }

    public Map<String, String> getDict2(String code) {
        List<Map<String, Object>> list = rd_dict.stream().filter(t -> Objects.equals(t.get("code"), code)).collect(Collectors.toList());
        Map<String, String> map = new HashMap<>();
        for (Map<String, Object> m : list) {
            map.put((String) m.get("remark"), (String) m.get("dict_value"));
        }
        return map;
    }

    //获取配置信息
    public String getConfig(String key) {
        Map<String, Object> map = rd_param.stream().filter(t -> Objects.equals(t.get("param_key"), key)).collect(Collectors.toList()).get(0);
        return (String) map.get("param_value");
    }


    public Map<String, Object> template(HttpServletRequest request, String sql, String defaultOrder, Object... param) {
        String pageNum = request.getParameter("pageNum");
        String pageSize = request.getParameter("pageSize");
        String orderBy = request.getParameter("orderBy");
        String filters = request.getParameter("filters");
        if (StringUtils.isNotBlank(filters)) {
            sql += " and " + filters;
        }
        String countSql = getCountSql(sql);
        if (StringUtils.isNotBlank(orderBy)) {
            sql += " " + orderBy;
        } else {
            sql += " " + defaultOrder;
        }
        String pageSql = getPageSql(sql, pageNum, pageSize);
        List<Map<String, Object>> list = jdbcTemplate.queryForList(pageSql, param);
        Map<String, Object> m = jdbcTemplate.queryForMap(countSql, param);
        Map<String, Object> map = new HashMap<>();
        map.put("list", list);
        map.put("total", m.get("count"));
        return map;
    }

    public static String getCountSql(String sql) {
        return "select count(*) as count from (" + sql + ") a ";
    }

    public static String getPageSql(String sql, int pageNum, int pageSize) {
        return sql + " limit " + ((pageNum - 1) * pageSize) + "," + pageSize;
    }

    public static String getPageSql(String sql, String pageNum, String pageSize) {
        return getPageSql(sql, Integer.parseInt(pageNum), Integer.parseInt(pageSize));
    }

    public static final String DATE_TIME = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE = "yyyy-MM-dd";
    public static final String DATE_TIME_MILLI = "yyyy-MM-dd HH:mm:ss:SSS";
    public static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern(DATE_TIME);
    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern(DATE);
    public static final DateTimeFormatter DATE_TIME_MILLI_FORMAT = DateTimeFormatter.ofPattern(DATE_TIME_MILLI);


    public static String toString(LocalDate date) {
        return DATE_FORMAT.format(date);
    }

    public static String toString(LocalDateTime time) {
        return DATE_TIME_FORMAT.format(time);
    }

    public String getCurrentDate() {
        return toString(LocalDate.now());
    }

    public String getCurrentDateAdd4Random() {
        String time = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS").format(LocalDateTime.now());
        int i = (int) (Math.random() * 9000) + 1000;
        return time + "_" + i;
    }

    public String getCurrentDateTime() {
        return toString(LocalDateTime.now());
    }

    public static String joinSql(List<?> list, List<Object> data, String filed, String join, String symbol, String question) {
        StringBuilder sb = new StringBuilder();
        if (StringUtils.isNotEmpty(list)) {
            sb.append(" and ( ");
            sb.append(list.stream().map(t -> {
                data.add(t);
                return " " + filed + " " + symbol + " " + question + " ";
            }).collect(Collectors.joining(join)));
            sb.append(" )");
        }
        return sb.toString();
    }

    public static String joinSqlLike(List<?> list, List<Object> data, String filed, String join) {
        List<String> l = list.stream().map(t -> "%" + t + "%").collect(Collectors.toList());
        return joinSql(l, data, filed, join, "like", "?");
    }


    public static String mysqlLike(String s) {
        return "concat('%'," + s + ",'%')";
    }


    public String joinSqlOr(List<?> list, List<Object> data, String filed) {
        return joinSql(list, data, filed, "or", "=", "?");
    }

    public String joinMySqlOrLike(List<?> list, List<Object> data, String filed) {
        return joinSql(list, data, filed, "or", "like", mysqlLike("?"));
    }

    public String joinMySqlOrLike(String v, List<Object> data, String filed) {
        List<String> l = new ArrayList<>();
        l.add(v);
        return joinSql(l, data, filed, "or", "like", mysqlLike("?"));
    }

    public String joinSqlOrLike(List<?> list, List<Object> data, String filed) {
        return joinSqlLike(list, data, filed, "or");
    }

    public String joinSqlOrLike(String v, List<Object> data, String filed) {
        List<String> l = new ArrayList<>();
        l.add(v);
        return joinSqlLike(l, data, filed, "or");
    }


    public static void fillMapCreate(Map<String, Object> map) {
        map.put("create_time", LocalDateTime.now());
        UserVo userVo = CommonUtil.getUserVoFormToken();
        map.put("create_user_id", userVo.getUserName());
        map.put("create_user_name", userVo.getUserName());
        fillMapUpdate(map);
    }

    public static void fillMapCreate2(Map<String, Object> map) {
        map.put("create_time", LocalDateTime.now());
        UserVo userVo = CommonUtil.getUserVoFormToken();
        map.put("create_user_id", userVo.getUserName());
        map.put("create_user_name", userVo.getUserName());
    }

    public static void fillMapUpdate(Map<String, Object> map) {
        map.put("update_time", LocalDateTime.now());
        UserVo userVo = CommonUtil.getUserVoFormToken();
        map.put("update_user_id", userVo.getUserName());
        map.put("update_user_name", userVo.getUserName());
        fillMapEnable(map);
    }

    public static void fillMapEnable(Map<String, Object> map) {
        if (map.get("enable") == null) {
            map.put("enable", -1);
        }
    }

    public static Map<String, Object> returnMap(Map<String, Object> map, String... strs) {
        Map<String, Object> m = new HashMap<>();
        Set<String> ks = map.keySet();
        for (String k : ks) {
            if (ArrayUtils.contain(strs, k)) {
                m.put(k, map.get(k));
            }
        }
        return m;
    }


}
