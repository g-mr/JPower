package com.wlcb.jpower.module.common.swagger;

import com.github.xiaoymin.knife4j.spring.extension.OpenApiExtensionResolver;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.ApiSelectorBuilder;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * @ClassName SwaggerConfiguration
 * @Description TODO Swagger配置
 * @Author 郭丁志
 * @Date 2020-08-12 11:23
 * @Version 2.0
 */
@Configuration(proxyBeanMethods = false)
@EnableSwagger
@EnableConfigurationProperties({SwaggerProperties.class})
@Import({BeanValidatorPluginsConfiguration.class})
public class SwaggerConfiguration {

    private static final String DEFAULT_BASE_PATH = "/**";
    private static final List<String> DEFAULT_EXCLUDE_PATH = Arrays.asList("/error", "/actuator/**");

//    @Value("${spring.application.name}")
//    private String appName;

    /**
     * 引入Knife4j扩展类
     */
    @Autowired
    private OpenApiExtensionResolver openApiExtensionResolver;

//    @Bean
//    @ConditionalOnMissingBean
//    public SwaggerProperties getSwaggerProperties(){
//        return new SwaggerProperties();
//    }

    @Bean
    @ConditionalOnMissingBean
    public Docket createRestApi(SwaggerProperties swaggerProperties) {

//        getSwaggerProperties()

        // base-path处理
        if (swaggerProperties.getBasePath().size() == 0) {
            swaggerProperties.getBasePath().add(DEFAULT_BASE_PATH);
        }

        // exclude-path处理
        if (swaggerProperties.getExcludePath().size() == 0) {
            swaggerProperties.getExcludePath().addAll(DEFAULT_EXCLUDE_PATH);
        }

        ApiSelectorBuilder apis = new Docket(DocumentationType.SWAGGER_2)
                .globalResponseMessage(RequestMethod.POST,new ArrayList<>())
                .globalResponseMessage(RequestMethod.GET,new ArrayList<>())
                .globalResponseMessage(RequestMethod.PUT,new ArrayList<>())
                .globalResponseMessage(RequestMethod.DELETE,new ArrayList<>())
                .host(swaggerProperties.getHost())
                .apiInfo(apiInfo(swaggerProperties))
                .select()
                .apis(SwaggerConfigUtil.basePackage(swaggerProperties.getBasePackage()))
                .paths(PathSelectors.any());

        swaggerProperties.getBasePath().forEach(p -> apis.paths(PathSelectors.ant(p)));
        swaggerProperties.getExcludePath().forEach(p -> apis.paths(PathSelectors.ant(p).negate()));

        return apis.build()
                .securitySchemes(securitySchemes(swaggerProperties))
                .securityContexts(Lists.newArrayList(securityContexts(swaggerProperties)))
                .extensions(openApiExtensionResolver.buildExtensions("s"))
                .pathMapping("/");
    }

    private List<? extends SecurityScheme> securitySchemes(SwaggerProperties swaggerProperties) {
        List<ApiKey> list = new ArrayList<>();
        swaggerProperties.getAuthorization().forEach(authorization -> {
            list.add(new ApiKey(authorization.getName(),authorization.getName(),authorization.getType()));
        });

        return list;
    }

    private List<SecurityContext> securityContexts(SwaggerProperties swaggerProperties) {
        return newArrayList(
                SecurityContext.builder()
                        .securityReferences(defaultAuth(swaggerProperties))
                        .forPaths(PathSelectors.regex("^(?!auth).*$"))
                        .build()
        );
    }

    private List<SecurityReference> defaultAuth(SwaggerProperties swaggerProperties) {
        List<SecurityReference> securityReferences = new ArrayList<>();
        swaggerProperties.getAuthorization().forEach(authorization -> {
            securityReferences.add(new SecurityReference(authorization.getName(), authorization.getAuthorizationScopes().toArray(new AuthorizationScope[authorization.getAuthorizationScopes().size()])));
        });
        return securityReferences;
    }

    private ApiInfo apiInfo(SwaggerProperties properties) {
        return new ApiInfoBuilder()
                .title(properties.getTitle())
                .description(properties.getDescription())
                .license(properties.getLicense())
                .licenseUrl(properties.getLicenseUrl())
                .termsOfServiceUrl(properties.getTermsOfServiceUrl())
                .contact(new Contact(properties.getContact().getName(),properties.getContact().getUrl(),properties.getContact().getEmail()))
                .version(properties.getVersion())
                .build();
    }

}
