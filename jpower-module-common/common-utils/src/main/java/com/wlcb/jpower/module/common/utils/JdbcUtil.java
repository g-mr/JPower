package com.wlcb.jpower.module.common.utils;

import cn.hutool.core.util.ArrayUtil;
import com.wlcb.jpower.module.common.utils.constants.StringPool;
import lombok.Cleanup;
import lombok.NonNull;
import lombok.SneakyThrows;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * JDBC工具
 *
 * @author mr.g
 **/
public class JdbcUtil {

    /**
     * 执行修改SQL
     *
     * @author mr.g
     * @param url 数据库连接
     * @param name 数据库名称
     * @param password 数据库密码
     * @param sqls 执行SQL
     * @return 是否执行完成
     **/
    public static boolean save(String url,String name,String password,@NonNull List<String> sqls){
        return save(null, url, name, password, ArrayUtil.toArray(sqls,String.class));
    }

    /**
     * 执行修改SQL
     *
     * @author mr.g
     * @param driverClass 驱动
     * @param url 数据库连接
     * @param name 数据库名称
     * @param password 数据库密码
     * @param sqls 执行SQL
     * @return 是否执行完成
     **/
    @SneakyThrows(SQLException.class)
    public static boolean save(String driverClass,String url,String name,String password,@NonNull String... sqls){
        @Cleanup Connection connection = getConnection(driverClass,url,name,password);
        @Cleanup Statement statement = connection.createStatement();
        for (String sql : sqls) {
            statement.addBatch(sql);
        }
        return statement.executeBatch().length > 0;
    }

    /**
     * 执行SQL返回LIST
     *
     * @author mr.g
     * @param url 数据库连接
     * @param name 数据库名称
     * @param password 数据库密码
     * @param sql 执行SQL
     * @param clasz 结果转换类型
     * @return 执行结果
     **/
    public static <T> List<T> selectList(String url,String name,String password,String sql,Class<T> clasz){
        return selectList(null, url, name, password, sql, clasz);
    }

    /**
     * 执行SQL返回LIST
     *
     * @author mr.g
     * @param driverClass 驱动
     * @param url 数据库连接
     * @param name 数据库名称
     * @param password 数据库密码
     * @param sql 执行SQL
     * @param clasz 结果转换类型
     * @return 执行结果
     **/
    @SneakyThrows(SQLException.class)
    public static <T> List<T> selectList(String driverClass,String url,String name,String password,String sql,Class<T> clasz){

        Connection connection = getConnection(driverClass,url,name,password);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);

        ResultSetMetaData metaData = resultSet.getMetaData();

        List<T> list = new ArrayList<>();

        while (resultSet.next()){
            if (ClassUtil.isSimpleValueType(clasz)){
                list.add(resultSet.getObject(1,clasz));
            } else {
                Map<String,Object> rowData = new HashMap<>(metaData.getColumnCount());
                for (int i = 1; i <= metaData.getColumnCount(); i++) {
                    rowData.put(StringUtil.underlineToHump(metaData.getColumnName(i)),resultSet.getObject(i));
                }
                list.add(BeanUtil.toBean(rowData,clasz));
            }
        }

        close(connection,resultSet,statement);
        return list;
    }

    /**
     * 执行SQL返回一条结果
     *
     * @author mr.g
     * @param url 数据库连接
     * @param name 数据库名称
     * @param password 数据库密码
     * @param sql 执行SQL
     * @param clasz 结果转换类型
     * @return 执行结果
     **/
    public static <T> T select(String url,String name,String password,String sql,Class<T> clasz){
        return select(null , url, name, password, sql, clasz);
    }

    /**
     * 执行SQL返回一条结果
     *
     * @author mr.g
     * @param driverClass 驱动
     * @param url 数据库连接
     * @param name 数据库名称
     * @param password 数据库密码
     * @param sql 执行SQL
     * @param clasz 结果转换类型
     * @return 执行结果
     **/
    @SneakyThrows(SQLException.class)
    public static <T> T select(String driverClass,String url,String name,String password,String sql,Class<T> clasz){

        Connection connection = getConnection(driverClass,url,name,password);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);

        resultSet.last();
        if (resultSet.getRow() > 1){
            throw new SQLException("执行结果不是一条");
        }
        resultSet.first();
        ResultSetMetaData metaData = resultSet.getMetaData();

        Map<String,Object> rowData = new HashMap<>(metaData.getColumnCount());
        if (resultSet.next()){
            if (ClassUtil.isSimpleValueType(clasz)){
                try {
                    return resultSet.getObject(1,clasz);
                } finally {
                    close(connection,resultSet,statement);
                }
            } else {
                for (int i = 1; i <= metaData.getColumnCount(); i++) {
                    rowData.put(StringUtil.underlineToHump(metaData.getColumnName(i)),resultSet.getObject(i));
                }
            }
        }

        close(connection,resultSet,statement);
        return BeanUtil.toBean(rowData,clasz);
    }

    /**
     * 获取JDBC连接
     *
     * @author mr.g
     * @param driverClass 驱动
     * @param url 连接URL
     * @param name 用户名
     * @param password 密码
     * @return JDBC连接
     **/
    @SneakyThrows({SQLException.class,ClassNotFoundException.class})
    public static Connection getConnection(String driverClass,String url,String name,String password){
        if (Fc.isNotBlank(driverClass)){
            Class.forName(driverClass);
        }
        return DriverManager.getConnection(url, name, password);
    }

    /**
     * 获取JDBC连接
     *
     * @author mr.g
     * @param url 连接URL
     * @param name 用户名
     * @param password 密码
     * @return JDBC连接
     **/
    public static Connection getConnection(String url,String name,String password){
        return getConnection(StringPool.EMPTY, url, name, password);

    }

    /**
     * 释放资源
     *
     * @author mr.g
     * @param connection 连接
     * @param resultSet 结果
     * @param statement 流
     **/
    public static void close(Connection connection,ResultSet resultSet,Statement statement){
        closeResultSet(resultSet);
        closeStatement(statement);
        closeConnection(connection);

    }

    /**
     * 释放资源
     *
     * @author mr.g
     * @param connection 连接
     * @param statement 流
     **/
    public static void close(Connection connection,Statement statement){
        closeStatement(statement);
        closeConnection(connection);

    }

    /**
     * 释放结果流
     *
     * @author mr.g
     * @param resultSet 结果
     * @return void
     **/
    public static void closeResultSet(ResultSet resultSet){
        try {
            if(resultSet !=null){
                resultSet.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 释放流
     *
     * @author mr.g
     * @param statement 流
     **/
    public static void closeStatement(Statement statement){
        try {
            if(statement !=null){
                statement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 释放连接
     *
     * @author mr.g
     * @param connection 连接
     **/
    public static void closeConnection(Connection connection){
        try {
            if(connection !=null){
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}