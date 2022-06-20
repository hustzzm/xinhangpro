package com.pig.basic.util.utils.jdbc;


import com.pig.basic.util.utils.ArrayUtils;
import com.pig.basic.util.utils.CastUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;


public abstract class Dao {


    protected abstract Connection getConnection() throws SQLException;

    public List<Map<String, Object>> executeQuery(String sql, Object... objs) {
        try (Connection c = getConnection()) {
            return JdbcUtils.executeQuery(c, sql, objs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int executeUpdate(String sql, Object... objs) {
        try (Connection c = getConnection()) {
            return JdbcUtils.executeUpdate(c, sql, objs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void executeBatch(String sql, Object[][] objs, int count) {
        try (Connection c = getConnection()) {
            JdbcUtils.executeBatch(c, sql, objs, count);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String[] queryColumns(String tableName) {
        try (Connection c = getConnection()) {
            return JdbcUtils.queryColumns(c, tableName);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Map<String, Object>> queryColumns(String dbName, String tableName) {
        try (Connection c = getConnection()) {
            return JdbcUtils.queryColumns(c, dbName, tableName);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void listToDB(List<Map<String, Object>> list, String tableName, int count) {
        try (Connection c = getConnection()) {
            JdbcUtils.importDB(list, c, tableName, count);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void truncate(String tableName) {
        executeUpdate(SqlUtils.truncateSql(tableName));
    }

    public void create(String oldTable, String newTable) {
        executeUpdate(SqlUtils.createSql(oldTable, newTable));
    }

    public void deleteTable(String tableName) {
        executeUpdate(SqlUtils.deleteTable(tableName));
    }

    public List<Map<String, Object>> findAll(String tableName) {
        return executeQuery("select * from " + tableName);
    }


    public Map<String, Object> queryMap(String sql, Object... objs) {
        List<Map<String, Object>> l = executeQuery(sql, objs);
        return JdbcUtils.getMap(l);
    }

    public <T> T queryObjectOne(Class<T> cla, String sql, Object... objs) {
        Map<String, Object> m = queryMap(sql, objs);
        return JdbcUtils.getObject(m, cla);
    }

    public Map<String, Object> findById(String tableName, long id) {
        return queryMap("select * from " + tableName + " where id=?", id);
    }

    public long getCount(String sql, Object... objs) {
        return queryObjectOne(Long.class, sql, objs);
    }

    public long getCountTable(String tableName) {
        return getCount("select count(*) from " + tableName);
    }

    private Map<String, Object> excludeSurplus(Map<String, Object> data, String tableName) {
        String[] columns = queryColumns(tableName);
        Set<String> keys = data.keySet();
        Map<String, Object> map = new HashMap<>();
        for (String key : keys) {
            if (ArrayUtils.contain(columns, key)) {
                map.put(key, data.get(key));
            }
        }
        return map;
    }

    public int insert(Map<String, Object> data, String tableName, String... exclude) {
        Map<String, Object> map = excludeSurplus(data, tableName);
        SqlUtils.Two two = SqlUtils.insert(map, tableName, exclude);
        return executeUpdate(two.getSql(), two.getParams());
    }

    public int update(Map<String, Object> data, String tableName, Collection<String> reason, String... exclude) {
        Map<String, Object> map = excludeSurplus(data, tableName);
        SqlUtils.Two two = SqlUtils.update(map, tableName, reason, exclude);
        return executeUpdate(two.getSql(), two.getParams());
    }

    public int delete(Map<String, Object> data, String tableName, Collection<String> reason) {
        Map<String, Object> map = excludeSurplus(data, tableName);
        SqlUtils.Two two = SqlUtils.delete(map, tableName, reason);
        return executeUpdate(two.getSql(), two.getParams());
    }

    public int delete(Map<String, Object> data, String tableName) {
        Map<String, Object> map = excludeSurplus(data, tableName);
        SqlUtils.Two two = SqlUtils.delete(map, tableName);
        return executeUpdate(two.getSql(), two.getParams());
    }

    public List<Map<String, Object>> select(Map<String, Object> data, String tableName, Collection<String> reason, String[] field) {
        Map<String, Object> map = excludeSurplus(data, tableName);
        SqlUtils.Two two = SqlUtils.select(map, tableName, reason, field);
        return executeQuery(two.getSql(), two.getParams());
    }

    public List<Map<String, Object>> select(Map<String, Object> data, String tableName, String... field) {
        Map<String, Object> map = excludeSurplus(data, tableName);
        SqlUtils.Two two = SqlUtils.select(map, tableName, field);
        return executeQuery(two.getSql(), two.getParams());
    }

    public List<Map<String, Object>> selectAll(Map<String, Object> data, String tableName, Collection<String> reason) {
        Map<String, Object> map = excludeSurplus(data, tableName);
        SqlUtils.Two two = SqlUtils.selectAll(map, tableName, reason);
        return executeQuery(two.getSql(), two.getParams());
    }

    public List<Map<String, Object>> selectAll(Map<String, Object> data, String tableName) {
        Map<String, Object> map = excludeSurplus(data, tableName);
        SqlUtils.Two two = SqlUtils.selectAll(map, tableName);
        return executeQuery(two.getSql(), two.getParams());
    }


    public List<Map<String, Object>> select(Map<String, Object> data, String tableName, String reason, String[] field) {

        return select(data, tableName, CastUtils.toList(reason), field);
    }

    public List<Map<String, Object>> select(Map<String, Object> data, String tableName, String[] reason, String[] field) {

        return select(data, tableName, CastUtils.toList(reason), field);
    }

    public List<Map<String, Object>> selectAll(Map<String, Object> data, String tableName, String... reason) {

        return selectAll(data, tableName, CastUtils.toList(reason));
    }


    public int update(Map<String, Object> data, String tableName, String[] reason, String... exclude) {

        return update(data, tableName, CastUtils.toList(reason), exclude);
    }

    public int update(Map<String, Object> data, String tableName, String reason, String... exclude) {
        return update(data, tableName, CastUtils.toList(reason), exclude);
    }

    public int delete(Map<String, Object> data, String tableName, String reason) {

        return delete(data, tableName, CastUtils.toList(reason));
    }

    public int delete(Map<String, Object> data, String tableName, String[] reason) {

        return delete(data, tableName, CastUtils.toList(reason));
    }


}
