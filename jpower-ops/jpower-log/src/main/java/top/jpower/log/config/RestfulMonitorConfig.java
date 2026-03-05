package top.jpower.log.config;

import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.jpower.log.interceptor.AuthInterceptor;
import top.jpower.log.interceptor.LogInterceptor;
import top.jpower.log.interceptor.RollbackInterceptor;
import top.jpower.log.properties.MonitorRestfulProperties;

/**
 * @Description TODO 接口监控拦截器配置
 * @Author mr.g
 * @Date 2021/4/1 0001 22:26
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(MonitorRestfulProperties.class)
@AllArgsConstructor
public class RestfulMonitorConfig {

    @Bean
    @ConditionalOnMissingBean
    public RollbackInterceptor rollbackInterceptor(){
        return new RollbackInterceptor();
    }

    @Bean
    public AuthInterceptor authInterceptor(MonitorRestfulProperties properties){
        return new AuthInterceptor(properties.getAuth());
    }

    @Bean
    public LogInterceptor logInterceptor(){
        return new LogInterceptor();
    }

}
