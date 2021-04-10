package com.wlcb.jpower.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName MonitorRestfulProperties
 * @Author mr.g
 */
@Data
@ConfigurationProperties(prefix = "jpower.monitor-restful")
public class MonitorRestfulProperties {

    /**
     * 是否启用接口监控
     */
    private Boolean enable = false;
    /**
     * 监控时间，cron表达式
     */
    private String cron = "0 0 1 * * ?";
    /**
     * 监控项目列表
     */
    private List<Routes> routes = new ArrayList<>();

    @Data
    public static class Routes{
        /**
         * 项目名称
         */
        private String name;
        /**
         * 获取接口信息地址
         */
        private String url;
        /**
         * 项目地址
         */
        private String location;
    }
}
