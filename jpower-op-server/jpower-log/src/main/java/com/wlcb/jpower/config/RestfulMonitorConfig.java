package com.wlcb.jpower.config;

import com.wlcb.jpower.interceptor.AuthInterceptor;
import com.wlcb.jpower.interceptor.LogInterceptor;
import com.wlcb.jpower.interceptor.RollbackInterceptor;
import com.wlcb.jpower.properties.MonitorRestfulProperties;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
    public AuthInterceptor authInterceptor(@NotNull MonitorRestfulProperties properties){
        return new AuthInterceptor(properties.getAuth());
    }

    @Bean
    public LogInterceptor logInterceptor(){
        return new LogInterceptor();
    }

}
