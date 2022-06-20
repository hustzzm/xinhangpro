package com.pig.basic.util.utils.jdbc;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;


@Component
public class BaseDao extends Dao {

    private final DataSource dataSource;


    @Autowired
    public BaseDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    @Override
    protected Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    @Override
    public int executeUpdate(String sql, Object... objs) {
        Connection c = DataSourceUtils.getConnection(dataSource);
        int i = JdbcUtils.executeUpdate(c, sql, objs);
        DataSourceUtils.releaseConnection(c, dataSource);
        return i;
    }
}
