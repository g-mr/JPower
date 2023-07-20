package com.wlcb.jpower.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.sql.DataSource;

/**
 * @author mr.g
 * @date 2023/7/5 11:18 PM
 */
@Data
@ConfigurationProperties(prefix = "jpower.datasource.task")
public class DataSourceProperties {

    /**
     * 连接池
     **/
    private Class<? extends DataSource> type;
    /**
     * 驱动
     **/
    private String driverClassName;
    /**
     * 地址
     **/
    private String url;
    /**
     * 用户名
     **/
    private String username;
    /**
     * 密码
     **/
    private String password;

}
