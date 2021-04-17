package com.wlcb.jpower.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.validation.annotation.Validated;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName MonitorRestfulProperties
 * @Author mr.g
 */
@Data
@ConfigurationProperties(prefix = "jpower.monitor-restful")
@Validated
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
    private List<Route> routes = new ArrayList<>();

    /**
     * 所有服务得鉴权信息
     */
    @NestedConfigurationProperty
    private AuthInfoConfiguration auth = null;

    @Data
    public static class Route{
        /**
         * 项目名称
         */
        private String name;
        /**
         * 获取接口信息地址
         */
        private String url = "/v2/api-docs";
        /**
         * 项目地址
         */
        @NotBlank
        private String location;

        /**
         * 鉴权信息
         */
        @NestedConfigurationProperty
        private AuthInfoConfiguration auth = null;
    }

    @PostConstruct
    private void validatedRoutesName(){
        long size = routes.stream().map(Route::getName).distinct().count();
        if (size != routes.size()){
            throw new IllegalStateException("监控项目名称重复，请检查jpower.monitor-restful.routes.name配置");
        }
    }
}
