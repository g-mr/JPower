package top.jpower.core.swagger.config;

import cn.hutool.core.util.ArrayUtil;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.customizers.OpenApiBuilderCustomizer;
import org.springdoc.core.customizers.ServerBaseUrlCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springdoc.core.providers.JavadocProvider;
import org.springdoc.core.service.OpenAPIService;
import org.springdoc.core.service.SecurityService;
import org.springdoc.core.utils.PropertyResolverUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.jpower.core.swagger.property.SwaggerProperties;
import top.jpower.core.util.utils.Fc;
import top.jpower.core.util.utils.SpringUtil;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 * Swagger配置
 *
 * @author mr.g
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({SwaggerProperties.class})
@ConditionalOnProperty(prefix = "knife4j", name = "enable", havingValue = "true", matchIfMissing = false)
@RequiredArgsConstructor
public class SwaggerConfig {

    /**
     * 接口文档信息
     *
     * @param properties 配置
     * @return 接口文档信息
     */
    @Bean
    @ConditionalOnMissingBean
    public Info info(SwaggerProperties properties) {
        return new Info()
                .title(properties.getTitle())
                .description(properties.getDescription())
                .license(new License().name(properties.getLicense()).url(properties.getLicenseUrl()))
                .termsOfService(properties.getTermsOfServiceUrl())
                .contact(properties.getContact())
                .version(properties.getVersion())
                .summary(properties.getSummary());
    }

    /**
     * 接口文档安全配置
     *
     * @param properties 配置
     * @return 接口文档安全配置
     */
    @Bean
    @ConditionalOnMissingBean
    public SecurityApi securityApi(SwaggerProperties properties){
        return SecurityApi.build(properties.getAuthorization());
    }

    /**
     * 创建接口文档
     *
     * @param servers 服务信息
     * @param info 接口文档信息
     * @param securityApi 接口文档安全配置
     * @return 接口文档
     */
    @Bean
    @ConditionalOnMissingBean
    public OpenAPI createApi(List<Server> servers, Info info, SecurityApi securityApi) {
        return new OpenAPI()
                .servers(servers)
                .info(info)
                .components(new Components()
                        .securitySchemes(securityApi.getAllSecurity())
//                        .addParameters() // 通用参数
//                        .addResponses() // 通用响应
                )
                .security(securityApi.getSecurityForApi())
//                .externalDocs(new ExternalDocumentation()
//                        .description("了解更多关于本API的设计")
//                        .url("https://docs.example.com")
//                ) 额外文档
                ;

    }

    /**
     * 创建接口文档服务
     *
     * @param openAPI 创建的接口文档
     * @param securityParser Springdoc提供的安全解析器
     * @param springDocConfigProperties Springdoc配置属性
     * @param propertyResolverUtils Springdoc属性解析器
     * @param openApiBuilderCustomizers 自定义接口文档构建器
     * @param serverBaseUrlCustomizers 自定义接口文档服务地址
     * @param javadocProvider 自定义接口提供者
     * @return 接口文档服务
     */
    @Bean
    @ConditionalOnMissingBean
    public OpenAPIService openApiBuilder(Optional<OpenAPI> openAPI,
                                         SecurityService securityParser,
                                         SpringDocConfigProperties springDocConfigProperties,
                                         PropertyResolverUtils propertyResolverUtils,
                                         Optional<List<OpenApiBuilderCustomizer>> openApiBuilderCustomizers,
                                         Optional<List<ServerBaseUrlCustomizer>> serverBaseUrlCustomizers,
                                         Optional<JavadocProvider> javadocProvider) {
        return new OpenAPIService(openAPI, securityParser, springDocConfigProperties,
                propertyResolverUtils, openApiBuilderCustomizers, serverBaseUrlCustomizers, javadocProvider);
    }

    /**
     * 创建接口分组
     *
     * @param properties 配置
     * @return 接口分组
     */
    @Bean
    @ConditionalOnMissingBean
    public static GroupedOpenApi groupedOpenApi(SwaggerProperties properties) {

        if (Fc.isNotEmpty(properties.getGroups())){
            AtomicInteger count = new AtomicInteger(0);
            properties.getGroups().forEach((name, group) -> {
                if (Fc.isNotBlank(name) && Fc.notNull(group)) {
                    GroupedOpenApi.Builder builder = GroupedOpenApi.builder()
                            .group(name);

                    setIfNotEmpty(builder::pathsToMatch, group.getPathsToMatch());
                    setIfNotEmpty(builder::packagesToScan, group.getPackagesToScan());
                    setIfNotEmpty(builder::pathsToExclude, group.getPathsToExclude());
                    setIfNotEmpty(builder::packagesToExclude, group.getPackagesToExclude());

                    SpringUtil.registerBean("groupedOpenApi"+(count.getAndIncrement()), builder.build());
                }
            });
        }

        return GroupedOpenApi.builder()
                .group("all")
                .pathsToMatch("/**")
                .pathsToExclude("/actuator/**")
                .build();
    }

    /**
     * 设置非空参数
     *
     * @param setter 参数设置器
     * @param list 参数列表
     */
    private static void setIfNotEmpty(Consumer<String[]> setter, List<String> list) {
        if (Fc.isNotEmpty(list)) {
            setter.accept(ArrayUtil.toArray(list, String.class));
        }
    }

}
