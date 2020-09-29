下载skywalking8.1.0；

解压apache-skywalking-apm-8.1.0.tar.gz

1、用application.yml替换config/application.yml文件
    2）修改storage.mysql.properties.jdbcUrl: ${SW_JDBC_URL:"【jdbc:mysql://mysql地址:端口/数据库】"}
    3）修改storage.mysql.properties.dataSource.user: ${SW_DATA_SOURCE_USER:【数据库账号】}
    4）修改storage.mysql.properties.dataSource.password: ${SW_DATA_SOURCE_USER:【数据库密码】}
   ##动态配置
    5）修改configuration.nacos.serverAddr: ${SW_CONFIG_NACOS_SERVER_ADDR:【nacos地址】}
    6）修改configuration.nacos.port: ${SW_CONFIG_NACOS_SERVER_PORT:【nacos端口】}
2、用self.yaml替换config/fe../self.yml文件
3、将apm-trace-ignore-plugin.config文件添加到/agent/config/下
4、将mysql驱动JAR拷贝到oap-libs/下
5、拷贝/agent/optional-plugins/apm-trace-ignore-plugin-8.1.0.jar文件到/agent/plugins/
·
进入/bin目录执行：sh startup.sh 启动