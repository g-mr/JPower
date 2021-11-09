package com.wlcb.jpower.module.common.swagger;

import com.github.xiaoymin.knife4j.spring.extension.OpenApiExtensionResolver;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Lists;
import com.wlcb.jpower.module.common.deploy.props.JpowerProperties;
import com.wlcb.jpower.module.common.utils.ClassUtil;
import com.wlcb.jpower.module.common.utils.Fc;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.google.common.collect.Lists.newArrayList;

/**
 * @ClassName SwaggerConfiguration
 * @Description TODO Swagger配置
 * @Author mr.g
 * @Date 2020-08-12 11:23
 * @Version 2.0
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({SwaggerProperties.class})
@ConditionalOnProperty(prefix = "knife4j", name = "enable", havingValue = "true", matchIfMissing = false)
@Import({Swagger2Configuration.class})
@AllArgsConstructor
public class SwaggerConfiguration {

    /**
     * JPower配置
     **/
    private final JpowerProperties properties;

    /**
     * 引入Knife4j扩展类
     */
    private final OpenApiExtensionResolver openApiExtensionResolver;

    private Predicate<RequestHandler> basePackage(final List<String> basePackage) {
        return input -> declaringClass(input).map(handlerPackage(basePackage)).orElse(true);
    }

    private Function<Class<?>, Boolean> handlerPackage(final List<String> basePackage) {
        return input -> basePackage.stream().anyMatch(ClassUtil.getPackage(input)::startsWith);
    }

    private Optional<? extends Class<?>> declaringClass(RequestHandler input) {
        return Optional.ofNullable(input.declaringClass());
    }

    @Bean
    @ConditionalOnMissingBean
    public Docket createRestApi(SwaggerProperties swaggerProperties) {

        // base-path处理
        List<Predicate<String>> basePath = new ArrayList<>();
        for (String path : swaggerProperties.getBasePath()) {
            basePath.add((input -> new AntPathMatcher().match(path, input)));
        }

        // exclude-path处理
        List<Predicate<String>> excludePath = new ArrayList<>();
        for (String path : swaggerProperties.getExcludePath()) {
            excludePath.add((input -> new AntPathMatcher().match(path, input)));
        }

        return new Docket(DocumentationType.SWAGGER_2)
                .globalResponseMessage(RequestMethod.POST,new ArrayList<>())
                .globalResponseMessage(RequestMethod.GET,new ArrayList<>())
                .globalResponseMessage(RequestMethod.PUT,new ArrayList<>())
                .globalResponseMessage(RequestMethod.DELETE,new ArrayList<>())
                .host(swaggerProperties.getHost())
                .apiInfo(apiInfo(swaggerProperties))
                .select()
                .apis(basePackage(swaggerProperties.getBasePackage()))
                .paths(Predicates.and(Predicates.not(Predicates.or(excludePath)), Predicates.or(basePath)))
                .paths(PathSelectors.any())
                .build()
                .securitySchemes(securitySchemes(swaggerProperties))
                .securityContexts(Lists.newArrayList(securityContexts(swaggerProperties)))
                .extensions(openApiExtensionResolver.buildExtensions(Fc.isBlank(swaggerProperties.getGroupName())?properties.getName():swaggerProperties.getGroupName()))
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
        swaggerProperties.getAuthorization().forEach(authorization -> securityReferences.add(new SecurityReference(authorization.getName(), authorization.getAuthorizationScopes().toArray(new AuthorizationScope[authorization.getAuthorizationScopes().size()]))));
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
