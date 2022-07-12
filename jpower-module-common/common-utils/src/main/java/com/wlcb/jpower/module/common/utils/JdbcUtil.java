package com.wlcb.jpower.module.common.utils;

import cn.hutool.core.text.csv.CsvData;
import cn.hutool.core.text.csv.CsvReadConfig;
import cn.hutool.core.text.csv.CsvRow;
import cn.hutool.core.text.csv.CsvUtil;

import java.io.File;
import java.sql.*;

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
            connection = JdbcUtil.getConn();
            //2.根据连接对象获取statement
            statement = connection.createStatement();


            CsvReadConfig config = new CsvReadConfig();
            config.setContainsHeader(true);
            config.setTrimField(true);

            CsvData data =  CsvUtil.getReader(config).read(new File("C:\\Users\\mr.g\\Desktop\\ok_data_level4.csv"));

            for (CsvRow csvRow : data) {
                String code = csvRow.getByName("ext_id");
                String name = csvRow.getByName("ext_name");
                if (!Fc.equalsValue(code,"0")){

                    //3.执行sql语句,得到resultSet
                    String sql = "select * from tb_core_city where code = '"+code+"'";

                    resultSet = statement.executeQuery(sql);
                    //4.遍历结果集
                    if (resultSet.next()){

                        if (!Fc.equalsValue(resultSet.getString("name"),name)){
                            System.out.println("查到不对应的数据=>"+code+","+name+","+resultSet.getString("name"));
                            FileUtil.appendUtf8Lines(CollectionUtil.toList(code+","+name+","+resultSet.getString("name")),"C:\\Users\\mr.g\\Desktop\\不对应.txt");
                        }

                    }else {
                        System.out.println("没有查到CODE=>"+code+","+name);
                        FileUtil.appendUtf8Lines(CollectionUtil.toList(code+","+name),"C:\\Users\\mr.g\\Desktop\\没有查到的.txt");

                    }

                }
            }

        } catch (Exception e) {
            // TODO: handle exception
        }finally {
            JdbcUtil.close(connection, resultSet, statement);
        }
    }

    static String driverClass = "com.mysql.cj.jdbc.Driver";
    static String url = "jdbc:mysql://82.156.227.156:3306/hand-phone?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&allowMultiQueries=true&autoReconnect=true&useTimezone=true&serverTimezone=GMT%2B8";
    static String name = "hand-phone";
    static String password = "123456";

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
     * 注册驱动 建立参数
     * <p>Title: close</p>
     * <p>Description: </p>
     * @param connection
     * @param resultSet
     * @param statement
     */

    public static Connection getConn(){

        Connection connection = null;
        //2. 建立连接 参数一： 协议 + 访问的数据库 ， 参数二： 用户名 ， 参数三： 密码。
        try {
            //Class.forName(driverClass);可写可不写
//            Class.forName(driverClass);
            connection = DriverManager.getConnection(url, name, password);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return connection;

    }

    /**
     * 释放资源
     * <p>Title: close</p>
     * <p>Description: </p>
     * @param connection
     * @param resultSet
     * @param statement
     */
    public static void close(Connection connection,ResultSet resultSet,Statement statement){
        closeRS(resultSet);
        closeSt(statement);
        closeConn(connection);

    }

    private static void closeRS(ResultSet resultSet){
        try {
            if(resultSet !=null){
                resultSet.close();
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally {
            resultSet = null;
        }
    }

    private static void closeSt(Statement statement){
        try {
            if(statement !=null){
                statement.close();
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally {
            statement = null;
        }
    }

    private static void closeConn(Connection connection){
        try {
            if(connection !=null){
                connection.close();
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally {
            connection = null;
        }
    }



}