package com.pig.basic.util.utils.jdbc;


import com.pig.basic.util.utils.CastUtils;
import com.pig.basic.util.utils.ExceptionUtils;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;


public class JdbcUtils {

    private JdbcUtils() {
        ExceptionUtils.requireNonInstance();
    }


    public static Connection getConnection(DataSource dataSource) throws SQLException {
        return dataSource.getConnection();
    }

    public static Connection getConnection(String url, String username, String password, String driverClass) throws SQLException {
        try {
            Class.forName(driverClass);
            return DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Map<String, Object>> executeQuery(Connection connection, String sql, Object... objs) {
        List<Map<String, Object>> list = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            for (int i = 0; i < objs.length; i++) {
                statement.setObject(i + 1, objs[i]);
            }
            try (ResultSet result = statement.executeQuery()) {
                ResultSetMetaData metaData = result.getMetaData();
                while (result.next()) {
                    Map<String, Object> map = new HashMap<>();
                    for (int i = 1; i <= metaData.getColumnCount(); i++) {
                        map.put(metaData.getColumnName(i), result.getObject(i));
                    }
                    list.add(map);
                }
                return list;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static int executeUpdate(Connection connection, String sql, Object... objs) {
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            for (int i = 0; i < objs.length; i++) {
                statement.setObject(i + 1, objs[i]);
            }
            return statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void executeBatch(Connection connection, String sql, Object[][] objs, int count) {
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            //关闭自动提交
            connection.setAutoCommit(false);
            for (int i = 0; i < objs.length; i++) {
                for (int j = 0; j < objs[i].length; j++) {
                    statement.setObject(j + 1, objs[i][j]);
                }
                statement.addBatch();
                if ((i + 1) % count == 0) {
                    statement.executeBatch();
                    connection.commit();
                    statement.clearBatch();
                }
            }
            //不足count的部分
            statement.executeBatch();
            connection.commit();
            statement.clearBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            //重新打开
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }


    public static String[] queryColumns(Connection connection, String tableName) {
        String sql = "select * from " + tableName + " where 1=2 ";
        try (Statement statement = connection.createStatement()) {
            try (ResultSet result = statement.executeQuery(sql)) {
                ResultSetMetaData metaData = result.getMetaData();
                int count = metaData.getColumnCount();
                String[] names = new String[count];
//                JDBCType[] types = new JDBCType[count];
                for (int i = 1; i <= count; i++) {
                    names[i - 1] = metaData.getColumnName(i);
                }
                return names;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Map<String, Object>> queryColumns(Connection connection, String dbName, String tableName) {
        String sql = "SELECT * FROM INFORMATION_SCHEMA.columns WHERE TABLE_SCHEMA= ? AND TABLE_NAME = ? ";
        return executeQuery(connection, sql, dbName, tableName);
    }

    public static void importDB(List<Map<String, Object>> list, Connection connection, String tableName, int size) {
        if (list.isEmpty()) {
            return;
        }
        Map<String, Object> map = list.get(0);
        List<String> keys = CastUtils.toList(map.keySet());
        String[] names = queryColumns(connection, tableName);
        keys.retainAll(CastUtils.toList(names));
        String sql = SqlUtils.getInsertSql(tableName, keys);
        int row = list.size();
        int column = keys.size();
        Object[][] objects = new Object[row][column];
        for (int i = 0; i < row; i++) {
            Map<String, Object> m = list.get(i);
            for (int j = 0; j < keys.size(); j++) {
                objects[i][j] = m.get(keys.get(j));
            }
        }
        executeBatch(connection, sql, objects, size);
    }


    public static Map<String, Object> getMap(List<Map<String, Object>> l) {
        if (l.size() == 1) {
            return l.get(0);
        }
        ExceptionUtils.mustUnique("数据");
        return null;
    }

    public static <T> T getObject(Map<String, Object> map, Class<T> cla) {
        Set<String> set = map.keySet();
        if (set.size() == 1) {
            for (String k : set) {
                return cla.cast(map.get(k));
            }
        }
        ExceptionUtils.mustUnique("字段");
        return null;
    }

}
