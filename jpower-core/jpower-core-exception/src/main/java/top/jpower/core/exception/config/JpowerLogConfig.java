package top.jpower.core.exception.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.boot.autoconfigure.web.client.RestTemplateAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.client.RestTemplate;
import top.jpower.core.deploy.property.JpowerProperties;
import top.jpower.core.exception.aspectj.OperateLogAspect;
import top.jpower.core.exception.client.JdbcLogClient;
import top.jpower.core.exception.client.LogClient;
import top.jpower.core.exception.client.RestLogClient;
import top.jpower.core.exception.listener.ErrorLogListener;
import top.jpower.core.exception.listener.OperateLogListener;
import top.jpower.core.util.user.UserConfig;

/**
 * 日志工具配置
 *
 * @Author mr.g
 * @Date 2021/5/1 0001 0:16
 */
@AutoConfiguration(after = JdbcTemplateAutoConfiguration.class)
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

    @Configuration(proxyBeanMethods = false)
    @AutoConfigureAfter(RestTemplateAutoConfiguration.class)
    @ConditionalOnMissingBean(LogClient.class)
    @ConditionalOnBean(RestTemplate.class)
    @ConditionalOnProperty(prefix = "jpower", name = "server", havingValue = "CLOUD")
    static class RestLogClientConfiguration {

        @Bean
        LogClient logClient(RestTemplate restTemplate) {
            return new RestLogClient(restTemplate);
        }

    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnMissingBean(LogClient.class)
    @ConditionalOnBean(JdbcTemplate.class)
    @ConditionalOnProperty(prefix = "jpower", name = "server", havingValue = "BOOT", matchIfMissing = true)
    static class JdbcLogClientConfiguration {

        @Bean
        LogClient logClient(JdbcTemplate jdbcTemplate) {
            return new JdbcLogClient(jdbcTemplate);
        }

    }

}
