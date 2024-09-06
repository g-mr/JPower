package top.jpower.core.swagger.config;

import com.github.xiaoymin.knife4j.spring.extension.OpenApiExtensionResolver;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.RequestHandler;
import springfox.documentation.annotations.ApiIgnore;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.ApiSelector;
import springfox.documentation.spring.web.plugins.Docket;
import top.jpower.core.swagger.annotation.ApiGroup;
import top.jpower.core.swagger.property.SwaggerProperties;
import top.jpower.core.swagger.utils.PathMatch;
import top.jpower.core.util.utils.AnnotationUtil;
import top.jpower.core.util.utils.BeanUtil;
import top.jpower.core.util.utils.SpringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static springfox.documentation.spring.web.plugins.Docket.DEFAULT_GROUP_NAME;

/**
 * Swagger配置
 *
 * @author mr.g
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({SwaggerProperties.class})
@ConditionalOnProperty(prefix = "knife4j", name = "enable", havingValue = "true", matchIfMissing = false)
@Import({BeanValidatorPluginsConfiguration.class})
@RequiredArgsConstructor
public class SwaggerConfig {

    /**
     * 引入Knife4j扩展类
     */
    private final OpenApiExtensionResolver openApiExtensionResolver;

    @Bean
    public WebMvcConfigurer swaggerWebMvcConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addResourceHandlers(ResourceHandlerRegistry registry) {
                registry.addResourceHandler("/js/**").addResourceLocations("classpath:/js/");
                registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");
                registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
            }
        };
    }

    private Docket createRestApi(String name, Predicate<RequestHandler> controllerSelects, SwaggerProperties swaggerProperties, ApiInfo apiInfo, SecurityApi securityApi) {
        return new Docket(DocumentationType.SWAGGER_2)
                .globalResponseMessage(RequestMethod.POST, new ArrayList<>())
                .globalResponseMessage(RequestMethod.GET, new ArrayList<>())
                .globalResponseMessage(RequestMethod.PUT, new ArrayList<>())
                .globalResponseMessage(RequestMethod.DELETE, new ArrayList<>())
                .groupName(name)
                .host(swaggerProperties.getHost())
                .apiInfo(apiInfo)
                .select()
                .apis(controllerSelects)
                .paths(PathMatch.ant(swaggerProperties.getBath()).and(PathMatch.ant(swaggerProperties.getExcludePath()).negate()))
                .build()
                .securitySchemes(securityApi.getAllSecurity())
                .securityContexts(securityApi.getSecurityForApi())
                .extensions(openApiExtensionResolver.buildExtensions(name))
                .pathMapping("/");
    }

    @Bean
    @ConditionalOnMissingBean
    public Docket docket(SwaggerProperties swaggerProperties, ApiInfo apiInfo, SecurityApi securityApi) {

        Map<String, Object> map = SpringUtil.getApplicationContext().getBeansWithAnnotation(ApiGroup.class);
        List<String> list = map.values().stream()
                .map(Object::getClass)
                .filter(clz -> AnnotatedElementUtils.hasAnnotation(clz, Api.class) && !AnnotatedElementUtils.hasAnnotation(clz, ApiIgnore.class))
                .map(clz -> AnnotationUtil.<String[]>getAnnotationValue(clz, ApiGroup.class))
                .flatMap(Arrays::stream)
                .distinct()
                .collect(Collectors.toList());

        AtomicInteger count = new AtomicInteger(0);
        list.forEach(group -> SpringUtil.registerBean("docket"+(count.getAndIncrement()), createRestApi(group, PathMatch.withGroupName(group), swaggerProperties, apiInfo, securityApi)));

        return createRestApi(DEFAULT_GROUP_NAME, ApiSelector.DEFAULT.getRequestHandlerSelector().and(RequestHandlerSelectors.withClassAnnotation(Api.class)), swaggerProperties, apiInfo, securityApi);
    }


    @Bean
    @ConditionalOnMissingBean
    public SecurityApi securityApi(SwaggerProperties properties){
        return SecurityApi.build(properties.getAuthorization());
    }

    @Bean
    @ConditionalOnMissingBean
    public ApiInfo apiInfo(SwaggerProperties properties) {
        return new ApiInfoBuilder()
                .title(properties.getTitle())
                .description(properties.getDescription())
                .license(properties.getLicense())
                .licenseUrl(properties.getLicenseUrl())
                .termsOfServiceUrl(properties.getTermsOfServiceUrl())
                .contact(BeanUtil.copyProperties(properties.getContact(), Contact.class))
                .version(properties.getVersion())
                .build();
    }

}
