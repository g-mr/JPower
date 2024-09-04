package top.jpower.jpower.module.common.swagger;

import com.github.xiaoymin.knife4j.spring.extension.OpenApiExtensionResolver;
import com.google.common.base.Predicates;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.RequestHandler;
import springfox.documentation.annotations.ApiIgnore;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.ApiSelector;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import top.jpower.core.util.utils.AnnotationUtil;
import top.jpower.core.util.utils.Fc;
import top.jpower.core.util.utils.SpringUtil;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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


    public static Predicate<RequestHandler> withGroupName(final String groupName) {
        return input -> Optional.ofNullable(input.declaringClass())
                .map(clz->AnnotationUtil.getAnnotation(clz, ApiGroup.class))
                .map(apiGroup -> Fc.contains(apiGroup.value(), groupName)).orElse(false);
    }

    @Bean
    @ConditionalOnMissingBean
    public Docket createRestApi(SwaggerProperties swaggerProperties) {

        Map<String, Object> map = SpringUtil.getApplicationContext().getBeansWithAnnotation(ApiGroup.class);
        List<String> list = map.values().stream()
                .map(Object::getClass)
                .filter(clz -> AnnotatedElementUtils.hasAnnotation(clz, Api.class) && !AnnotatedElementUtils.hasAnnotation(clz, ApiIgnore.class))
                .map(clz -> AnnotationUtil.<String[]>getAnnotationValue(clz, ApiGroup.class))
                .flatMap(Arrays::stream).distinct().collect(Collectors.toList());

        AtomicInteger count = new AtomicInteger();
        list.forEach(group -> {
            SpringUtil.registerBean("docket"+(count.getAndIncrement()), createRestApi(group, withGroupName(group), swaggerProperties));
        });

        return createRestApi(DEFAULT_GROUP_NAME, ApiSelector.DEFAULT.getRequestHandlerSelector().and(RequestHandlerSelectors.withClassAnnotation(Api.class)), swaggerProperties);
    }

    public Docket createRestApi(String name, Predicate<RequestHandler> predicate, SwaggerProperties swaggerProperties) {

        // base-path处理
        List<com.google.common.base.Predicate<String>> basePath = new ArrayList<>();
        for (String path : swaggerProperties.getBasePath()) {
            basePath.add((input -> new AntPathMatcher().match(path, input)));
        }

        // exclude-path处理
        List<com.google.common.base.Predicate<String>> excludePath = new ArrayList<>();
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
                .apis(predicate)
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
