package com.wlcb.jpower.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author mr.g
 * @date 2021-05-08 17:28
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(prefix = "knife4j", name = "enable", havingValue = "true", matchIfMissing = false)
public class Swagger2Configuration {

    @Bean
    @ConditionalOnClass(SwaggerWebConfig.class)
    public SwaggerWebConfig getSwaggerWebMvcConfigurer() {
        return new SwaggerWebConfig();
    }

}