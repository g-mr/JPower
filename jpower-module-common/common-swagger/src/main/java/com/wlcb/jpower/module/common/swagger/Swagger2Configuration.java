package com.wlcb.jpower.module.common.swagger;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

/**
 * @author mr.g
 * @date 2021-05-08 17:28
 */
@EnableSwagger2WebMvc
@ConditionalOnProperty(prefix = "knife4j", name = "enable", havingValue = "true", matchIfMissing = true)
@Import(BeanValidatorPluginsConfiguration.class)
public class Swagger2Configuration {

    @Bean
    @ConditionalOnClass(SwaggerWebConfig.class)
    public SwaggerWebConfig getSwaggerWebMvcConfigurer() {
        return new SwaggerWebConfig();
    }

}