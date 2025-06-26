package top.jpower.core.exception.config;

import feign.Feign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import top.jpower.core.deploy.property.JpowerProperties;
import top.jpower.core.exception.aspectj.OperateLogAspect;
import top.jpower.core.exception.client.FeignLogClient;
import top.jpower.core.exception.client.JdbcLogClient;
import top.jpower.core.exception.client.LogClient;
import top.jpower.core.exception.feign.LogTraceClient;
import top.jpower.core.exception.listener.ErrorLogListener;
import top.jpower.core.exception.listener.OperateLogListener;
import top.jpower.core.feign.config.DynamicFeignConfig;
import top.jpower.core.util.user.UserConfig;

/**
 * 日志工具配置
 *
 * @Author mr.g
 * @Date 2021/5/1 0001 0:16
 */
@AutoConfiguration
@ConditionalOnWebApplication
public class JpowerLogConfig {

    @Bean
    public OperateLogAspect apiLogAspect(@Autowired(required = false) UserConfig userConfig) {
        return new OperateLogAspect(userConfig);
    }

    @Bean
    public OperateLogListener operateLogListener(JpowerProperties jpowerProperties, @Autowired(required = false) LogClient logClient) {
        return new OperateLogListener(jpowerProperties, logClient);
    }

    @Bean
    public ErrorLogListener errorLogListener(JpowerProperties jpowerProperties, @Autowired(required = false) LogClient logClient) {
        return new ErrorLogListener(jpowerProperties, logClient);
    }

    @AutoConfiguration
    @ConditionalOnClass({DynamicFeignConfig.class, Feign.class})
    @ConditionalOnMissingBean(LogClient.class)
    @EnableFeignClients(clients = LogTraceClient.class)
    @ConditionalOnProperty(prefix = "jpower", name = "server", havingValue = "CLOUD")
    public static class FeignLogClientConfiguration {

        @Bean
        public LogClient logClient(LogTraceClient logTraceClient) {
            return new FeignLogClient(logTraceClient);
        }

    }

    @AutoConfiguration
    @ConditionalOnMissingBean(LogClient.class)
    @ConditionalOnClass(JdbcTemplate.class)
    @ConditionalOnBean(JdbcTemplate.class)
    @ConditionalOnProperty(prefix = "jpower", name = "server", havingValue = "BOOT", matchIfMissing = true)
    public static class JdbcLogClientConfiguration {

        @Bean
        public LogClient logClient(JdbcTemplate jdbcTemplate) {
            return new JdbcLogClient(jdbcTemplate);
        }

    }

}
