package top.jpower.jpower.module.common.swagger;

import com.github.xiaoymin.knife4j.spring.extension.OpenApiExtensionResolver;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.ApiSelector;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static springfox.documentation.spring.web.plugins.Docket.DEFAULT_GROUP_NAME;

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
@Import({BeanValidatorPluginsConfiguration.class})
@RequiredArgsConstructor
public class SwaggerConfiguration {

    /**
     * 引入Knife4j扩展类
     */
    private final OpenApiExtensionResolver openApiExtensionResolver;


    @Bean
    @ConditionalOnMissingBean
    public Docket createRestApi(SwaggerProperties swaggerProperties) {
        // // todo 获取自定义注解 @ApiGroup 获取到类和分组名称进行分组实例化
        // AtomicInteger count = new AtomicInteger();
        // swaggerProperties.getGroup().forEach(group -> {
        //     SpringUtil.registerBean("docket"+(count.getAndIncrement()), createRestApi(group.getName(), swaggerProperties));
        // });
        return createRestApi(DEFAULT_GROUP_NAME, swaggerProperties);
    }



    public Docket createRestApi(String name, SwaggerProperties swaggerProperties) {

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
                .globalResponseMessage(RequestMethod.POST, new ArrayList<>())
                .globalResponseMessage(RequestMethod.GET, new ArrayList<>())
                .globalResponseMessage(RequestMethod.PUT, new ArrayList<>())
                .globalResponseMessage(RequestMethod.DELETE, new ArrayList<>())
                .groupName(name)
                .host(swaggerProperties.getHost())
                .apiInfo(apiInfo(swaggerProperties))
                .select()
                .apis(ApiSelector.DEFAULT.getRequestHandlerSelector().and(RequestHandlerSelectors.withClassAnnotation(Api.class)))
                .paths(Predicates.and(Predicates.not(Predicates.or(excludePath)), Predicates.or(basePath)))
                .build()
                .securitySchemes(securitySchemes(swaggerProperties))
                .securityContexts(newArrayList(securityContexts(swaggerProperties)))
                .extensions(openApiExtensionResolver.buildExtensions(name))
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
