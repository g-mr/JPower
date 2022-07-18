package com.wlcb.jpower.module.common.utils;

import cn.hutool.core.text.csv.CsvData;
import cn.hutool.core.text.csv.CsvReadConfig;
import cn.hutool.core.text.csv.CsvRow;
import cn.hutool.core.text.csv.CsvUtil;
import com.wlcb.jpower.module.common.utils.constants.StringPool;
import lombok.Cleanup;
import lombok.NonNull;
import lombok.SneakyThrows;

import java.io.File;
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

    public static void main(String[] args) {

        //查询
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            //1.获取连接对象
            connection = JdbcUtil.getConnection("","","");
            //2.根据连接对象获取statement
            statement = connection.createStatement();


            CsvReadConfig config = new CsvReadConfig();
            config.setContainsHeader(true);
            config.setTrimField(true);

            CsvData data =  CsvUtil.getReader(config).read(new File("/Users/mr.gmac/Desktop/ok_data_level4.csv"));

            File 不对应 = new File("/Users/mr.gmac/Desktop/不对应.txt");
            FileUtil.touch(不对应);
            File 没有查到的 = new File("/Users/mr.gmac/Desktop/没有查到的.txt");
            FileUtil.touch(没有查到的);

            for (CsvRow csvRow : data) {
                String code = csvRow.getByName("ext_id");
                String name = csvRow.getByName("ext_name");
                Integer rand = Fc.toInt(csvRow.getByName("deep"))+1;
                if (!Fc.equalsValue(code,"0")){

                    //3.执行sql语句,得到resultSet
                    String sql = "select * from tb_core_city where code = '"+code+"'";

                    resultSet = statement.executeQuery(sql);
                    //4.遍历结果集
                    if (resultSet.next()){

                        if (!Fc.equalsValue(resultSet.getString("name"),name)){
                            if (rand <= 3){
                                System.out.println("查到不对应的数据=>"+code+","+name+","+resultSet.getString("name"));
                            }
                            FileUtil.appendUtf8Lines(CollectionUtil.toList(code+","+name+","+rand+","+resultSet.getString("name")+","+resultSet.getInt("rankd")),不对应);
                        }

                    }else {
                        if (rand <= 3) {
                            System.out.println("没有查到CODE=>" + code + "," + name);
                        }
                        FileUtil.appendUtf8Lines(CollectionUtil.toList(code+","+name+","+rand),没有查到的);

                    }

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            JdbcUtil.close(connection, resultSet, statement);
        }
    }

    static String driverClass = "com.mysql.cj.jdbc.Driver";
    static String url = "jdbc:mysql://82.156.227.156:3306/hand-phone?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&allowMultiQueries=true&autoReconnect=true&useTimezone=true&serverTimezone=GMT%2B8";
    static String name = "hand-phone";
    static String password = "hand-phone";

    //读取jdbc.properties
    static{
//        try {
//            //1.创建一个属性配置对象
//            Properties properties = new Properties();
//
//            //1.对应文件位于工程根目录
//            //InputStream is = new FileInputStream("jdbc.properties");
//
//            //2.使用类加载器,读取drc下的资源文件  对应文件位于src目录底下  建议使用
//            InputStream is = JdbcUtil.class.getClassLoader().getResourceAsStream("jdbc.properties");
//            //2.导入输入流,抓取异常
//            properties.load(is);
//            //3.读取属性
//            driverClass = properties.getProperty("driverClass");
//            url = properties.getProperty("url");
//            name = properties.getProperty("name");
//            password = properties.getProperty("password");
//        } catch (Exception e) {
//            // TODO: handle exception
//            e.printStackTrace();
//        }

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
    @SneakyThrows(SQLException.class)
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
            Map<String,Object> rowData = new HashMap<>(metaData.getColumnCount());
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                rowData.put(StringUtil.underlineToHump(metaData.getColumnName(i)),resultSet.getObject(i));
            }
            list.add(BeanUtil.toBean(rowData,clasz));
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
    @SneakyThrows(SQLException.class)
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

        if (resultSet.getRow() > 1){
            throw new SQLException("执行结果不是一条");
        }
        ResultSetMetaData metaData = resultSet.getMetaData();
        Map<String,Object> rowData = new HashMap<>(metaData.getColumnCount());
        if (resultSet.next()){
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                rowData.put(StringUtil.underlineToHump(metaData.getColumnName(i)),resultSet.getObject(i));
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