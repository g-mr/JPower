package com.wlcb.jpower.module.config.properties;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @ClassName DemoProperties
 * @Description TODO 演示环境配置
 * @Author 郭丁志
 * @Date 2020-10-14 21:12
 * @Version 1.0
 */
@Data
@ConfigurationProperties(prefix = "jpower.mybatis")
public class MybatisProperties {

    private Sql sql = new Sql(true,0L);

    @Data
    @AllArgsConstructor
    public static class Sql {
        /** 是否开启打印 **/
        private boolean print;
        /** 超时（单位毫秒） **/
        private long printTimeout;
    }
}
