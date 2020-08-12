package com.wlcb.jpower.module.common.swagger;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @ClassName SwaggerConfiguration
 * @Description TODO Swagger配置
 * @Author 郭丁志
 * @Date 2020-08-12 11:23
 * @Version 1.0
 */
@Configuration
@EnableSwagger2
@EnableKnife4j
@Profile({"dev", "test"})
@EnableConfigurationProperties(SwaggerProperties.class)
@Import({BeanValidatorPluginsConfiguration.class})
public class SwaggerConfiguration {

    public SwaggerProperties getSwaggerProperties(){
        return new SwaggerProperties();
    }

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(SwaggerConfigUtil.basePackage(getSwaggerProperties().getBasePackage()))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(getSwaggerProperties().getTitle())
                .description(getSwaggerProperties().getDescription())
                .license(getSwaggerProperties().getLicense())
                .licenseUrl(getSwaggerProperties().getLicenseUrl())
                .termsOfServiceUrl(getSwaggerProperties().getTermsOfServiceUrl())
                .contact(getSwaggerProperties().getContact())
                .version(getSwaggerProperties().getVersion())
                .build();
    }

}
