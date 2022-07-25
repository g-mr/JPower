package com.wlcb.jpower.module.common.utils;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.text.csv.CsvData;
import cn.hutool.core.text.csv.CsvReadConfig;
import cn.hutool.core.text.csv.CsvRow;
import cn.hutool.core.text.csv.CsvUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ArrayUtil;
import com.wlcb.jpower.module.common.support.ChainMap;
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
import java.util.concurrent.atomic.AtomicInteger;

/**
 * JDBC工具
 *
 * @author mr.g
 **/
public class JdbcUtil {

    public static void main(String[] args) {

        List<ChainMap> list = selectList("jdbc:mysql://82.156.227.156:3306/hand-phone?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&allowMultiQueries=true&autoReconnect=true&useTimezone=true&serverTimezone=GMT%2B8&tinyInt1isBit=false",
                "root","P@ssw0rdMysql","select * from tb_core_city c where c.rankd = 4 and (select count(1) from tb_core_city d where d.pcode = c.code) = 0", ChainMap.class);


        AtomicInteger connSum = new AtomicInteger();

        list.forEach(m -> {

            while (connSum.get()>100){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println("线程等待报错=>"+e.getMessage());
                }
            }

            ThreadUtil.execAsync(()->{

                connSum.getAndIncrement();


                try {
                    ChainMap map = select("jdbc:mysql://82.156.227.156:3306/hand-phone?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&allowMultiQueries=true&autoReconnect=true&useTimezone=true&serverTimezone=GMT%2B8&tinyInt1isBit=false",
                            "root","P@ssw0rdMysql","select * from tb_core_city1 where rankd = 4 and code like '"+StringUtil.subPre(m.getStr("code"),6)+"%' and fullname like '"+m.getStr("name")+"%'", ChainMap.class);


                    if (Fc.isNotEmpty(map)){
                        FileUtil.appendUtf8Lines(ListUtil.toList("查到地区："+map.getStr("fullname")+",原CODE=>"+map.getStr("code")+",现code=>"+m.getStr("code")),"/Users/mr.gmac/Desktop/shuju.txt");


//                        List<ChainMap> listChild = selectList("jdbc:mysql://82.156.227.156:3306/hand-phone?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&allowMultiQueries=true&autoReconnect=true&useTimezone=true&serverTimezone=GMT%2B8&tinyInt1isBit=false",
//                                "root","P@ssw0rdMysql","select * from tb_core_city1 where pcode = '"+map.getStr("code")+"'", ChainMap.class);
//
//                        List<String> sqls = new ArrayList<>();
//
//                        listChild.forEach(chain->{
//
//                            chain.put("note","模糊匹配name");
//                            chain.put("pcode",m.getStr("code"));
//                            String code = StringUtil.subPre(m.getStr("code"),9)+StringUtil.subSufByLength(chain.getStr("code"),3);
//                            chain.put("code",code);
//
//                            String sql = "insert into tb_core_city ("
//                                    +
//                                    CollectionUtil.join(chain.keySet(),",", StringUtil::humpToUnderline)
//                                    +
//                                    ") values ("
//                                    +
//                                    CollectionUtil.join(chain.values(),",", v-> Fc.notNull(v)? "'"+v+"'" : null)
//                                    +
//                                    ")";
//
//                            sqls.add(sql);
//                        });
//
//
//                        if (Fc.isNotEmpty(sqls)) {
//
//                            boolean save = save("jdbc:mysql://82.156.227.156:3306/hand-phone?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&allowMultiQueries=true&autoReconnect=true&useTimezone=true&serverTimezone=GMT%2B8",
//                                    "root","P@ssw0rdMysql", sqls);
//
//                            if (!save){
//                                FileUtil.appendUtf8String("保存失败=>"+save+",数量=>"+sqls.size()+",所属街道=>"+m.getStr("fullname")+",所属CODE=>"+m.getStr("code")+",原来CODE=>"+map.getStr("code"),new File("/Users/mr.gmac/Desktop/errorSql.txt"));
//                            }
//
//                            System.out.println("保存成功=>"+save+",数量=>"+sqls.size()+",所属街道=>"+m.getStr("fullname")+",所属CODE=>"+m.getStr("code")+",原来CODE=>"+map.getStr("code"));
//                        }

                    }
                } catch (Exception e){
                    FileUtil.appendUtf8Lines(ListUtil.toList(m.getStr("name")+":"+m.getStr("code")+"=====>"+ExceptionUtil.getStackTraceAsString(e)),"/Users/mr.gmac/Desktop/error.txt");
                }


                connSum.getAndDecrement();
            },false);

        });

    }


    private static void addPcodeChild(){

        List<ChainMap> list = selectList("jdbc:mysql://82.156.227.156:3306/hand-phone?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&allowMultiQueries=true&autoReconnect=true&useTimezone=true&serverTimezone=GMT%2B8&tinyInt1isBit=false",
                "root","P@ssw0rdMysql","select c.* from tb_core_city c where c.rankd = 4 and (select count(1) from tb_core_city d where d.pcode = c.code) = 0", ChainMap.class);


        AtomicInteger connSum = new AtomicInteger();

        list.forEach(m -> {

            while (connSum.get()>100){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println("线程等待报错=>"+e.getMessage());
                }
            }

            ThreadUtil.execAsync(()->{

                connSum.getAndIncrement();

                ChainMap map = select("jdbc:mysql://82.156.227.156:3306/hand-phone?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&allowMultiQueries=true&autoReconnect=true&useTimezone=true&serverTimezone=GMT%2B8&tinyInt1isBit=false",
                        "root","P@ssw0rdMysql","select * from tb_core_city1 where pcode = '"+m.getStr("pcode")+"' and fullname = '"+m.getStr("fullname")+"'", ChainMap.class);

                if (Fc.isNotEmpty(map)){

                    List<ChainMap> listChild = selectList("jdbc:mysql://82.156.227.156:3306/hand-phone?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&allowMultiQueries=true&autoReconnect=true&useTimezone=true&serverTimezone=GMT%2B8&tinyInt1isBit=false",
                            "root","P@ssw0rdMysql","select * from tb_core_city1 where pcode = '"+map.getStr("code")+"'", ChainMap.class);


                    List<String> sqls = new ArrayList<>();

                    listChild.forEach(chain->{

                        chain.put("note","addPcodeChild");
                        chain.put("pcode",m.getStr("code"));
                        String code = StringUtil.subPre(m.getStr("code"),9)+StringUtil.subSufByLength(chain.getStr("code"),3);
                        chain.put("code",code);

                        String sql = "insert into tb_core_city ("
                                +
                                CollectionUtil.join(chain.keySet(),",", StringUtil::humpToUnderline)
                                +
                                ") values ("
                                +
                                CollectionUtil.join(chain.values(),",", v-> Fc.notNull(v)? "'"+v+"'" : null)
                                +
                                ")";

                        sqls.add(sql);
                    });



                    if (Fc.isNotEmpty(sqls)) {

                        boolean save = save("jdbc:mysql://82.156.227.156:3306/hand-phone?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&allowMultiQueries=true&autoReconnect=true&useTimezone=true&serverTimezone=GMT%2B8",
                                "root","P@ssw0rdMysql", sqls);

                        if (!save){
                            FileUtil.appendUtf8String("保存失败=>"+save+",数量=>"+sqls.size()+",所属街道=>"+m.getStr("fullname")+",所属CODE=>"+m.getStr("code")+",原来CODE=>"+map.getStr("code"),new File("/Users/mr.gmac/Desktop/errorSql.txt"));
                        }

                        System.out.println("保存成功=>"+save+",数量=>"+sqls.size()+",所属街道=>"+m.getStr("fullname")+",所属CODE=>"+m.getStr("code")+",原来CODE=>"+map.getStr("code"));
                    }

                }

                connSum.getAndDecrement();
            },false);

        });


        System.out.println("所有线程已经执行.....");

    }


    private static void saveLeven5(){
        List<ChainMap> list = selectList("jdbc:mysql://82.156.227.156:3306/hand-phone?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&allowMultiQueries=true&autoReconnect=true&useTimezone=true&serverTimezone=GMT%2B8",
                "root","P@ssw0rdMysql","select c.* from tb_core_city c where c.rankd = 4", ChainMap.class);

        AtomicInteger connSum = new AtomicInteger();

        list.forEach(m -> {

            while (connSum.get()>=100){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println("线程等待报错=>"+e.getMessage());
                }
            }

            ThreadUtil.execAsync(()->{

                connSum.getAndIncrement();

                Integer count = select("jdbc:mysql://82.156.227.156:3306/hand-phone?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&allowMultiQueries=true&autoReconnect=true&useTimezone=true&serverTimezone=GMT%2B8",
                        "root","P@ssw0rdMysql","select count(1) from tb_core_city where pcode = '"+m.getStr("code")+"'", Integer.class);

                if (count <= 0){
                    List<ChainMap> listChild = selectList("jdbc:mysql://82.156.227.156:3306/hand-phone?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&allowMultiQueries=true&autoReconnect=true&useTimezone=true&serverTimezone=GMT%2B8&tinyInt1isBit=false",
                            "root","P@ssw0rdMysql","select c.* from tb_core_city1 c left join tb_core_city1 p on p.code = c.pcode where p.code = '"+m.getStr("code")+"' and p.name = '"+m.getStr("fullname")+"'", ChainMap.class);

                    List<String> sqls = new ArrayList<>();

                    listChild.forEach(map->{

                        String sql = "insert into tb_core_city ("
                                +
                                CollectionUtil.join(map.keySet(),",", StringUtil::humpToUnderline)
                                +
                                ") values ("
                                +
                                CollectionUtil.join(map.values(),",", v-> Fc.notNull(v)? "'"+v+"'" : null)
                                +
                                ")";

                        sqls.add(sql);
                    });


                    if (Fc.isNotEmpty(sqls)) {

                        boolean save = save("jdbc:mysql://82.156.227.156:3306/hand-phone?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&allowMultiQueries=true&autoReconnect=true&useTimezone=true&serverTimezone=GMT%2B8",
                                "root","P@ssw0rdMysql", sqls);

                        if (!save){
                            FileUtil.appendUtf8String("保存成功=>"+save+",数量=>"+sqls.size()+",所属街道=>"+m.getStr("fullname")+",所属CODE=>"+m.getStr("code"),new File("/Users/mr.gmac/Desktop/errorSql.txt"));
                        }

                        System.out.println("保存成功=>"+save+",数量=>"+sqls.size()+",所属街道=>"+m.getStr("fullname")+",所属CODE=>"+m.getStr("code"));
                    }
                }


                connSum.getAndDecrement();
            },false);

        });


        System.out.println("所有线程已经执行.....");
    }


    private void csvtomysql(){

        CsvReadConfig config = new CsvReadConfig();
        config.setContainsHeader(true);
        config.setTrimField(true);

        CsvData data =  CsvUtil.getReader(config).read(new File("/Users/mr.gmac/Desktop/ok_data_level4.csv"));

        List<String> sqls = new ArrayList<>();
        for (CsvRow csvRow : data) {
            String code = csvRow.getByName("ext_id");
            String pcode = Fc.equalsValue(csvRow.getByName("pid"),"0")?"-1":csvRow.getByName("pid");
            String name = csvRow.getByName("name");
            String fullname = csvRow.getByName("ext_name");
            int sort = (int) csvRow.getByName("pinyin_prefix").charAt(0);
            Integer rankd = Fc.toInt(csvRow.getByName("deep"))+1;

            if (!Fc.equalsValue(pcode,"-1")){
                pcode = pcode + String.format("%0" + (12 - pcode.length()) + "d", 0);
            }

            String sql = "insert into tb_core_city_copy (id, code, pcode, name, fullname, rankd, lng, lat, country_code, city_type, note, sort_num, create_user, create_time, update_user, update_time, status, is_deleted, create_org) values " +
                    "('"+Fc.randomUUID()+"', "+code+", "+pcode+", '"+name+"', '"+fullname+"', "+rankd+", null, null, 'CHN', "+rankd+", null, "+sort+", 'root', now(), 'root', now(), 1, 0, null)";

            sqls.add(sql);
        }

        ListUtil.split(sqls,100).forEach(list -> {

            boolean save = save("jdbc:mysql://82.156.227.156:3306/jpower?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&allowMultiQueries=true&autoReconnect=true&useTimezone=true&serverTimezone=GMT%2B8",
                    "root","P@ssw0rdMysql", list);

            System.out.println(save);

        });


    }

    private void test1(){
        //查询
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            //1.获取连接对象
            connection = JdbcUtil.getConnection("jdbc:mysql://82.156.227.156:3306/jpower?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&allowMultiQueries=true&autoReconnect=true&useTimezone=true&serverTimezone=GMT%2B8",
                    "root","P@ssw0rdMysql");
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

        if (resultSet.getRow() > 1){
            throw new SQLException("执行结果不是一条");
        }
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