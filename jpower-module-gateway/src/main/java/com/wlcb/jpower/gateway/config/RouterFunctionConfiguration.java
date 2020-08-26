package com.wlcb.jpower.gateway.config;

import com.wlcb.jpower.gateway.handler.HystrixFallbackHandler;
import com.wlcb.jpower.gateway.handler.SwaggerResourceHandler;
import com.wlcb.jpower.gateway.handler.SwaggerSecurityHandler;
import com.wlcb.jpower.gateway.handler.SwaggerUiHandler;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;

/**
 * @ClassName RouterFunctionConfiguration
 * @Description TODO  路由配置信息
 * @Author 郭丁志
 * @Date 2020/8/25 0025 19:36
 * @Version 1.0
 */
@Slf4j
@Configuration
@AllArgsConstructor
public class RouterFunctionConfiguration {

    private final SwaggerResourceHandler swaggerResourceHandler;
    private final SwaggerSecurityHandler swaggerSecurityHandler;
    private final SwaggerUiHandler swaggerUiHandler;
    private final HystrixFallbackHandler hystrixFallbackHandler;

    @Bean
    public RouterFunction routerFunction() {
        return RouterFunctions.route(RequestPredicates.GET("/swagger-resources")
                .and(RequestPredicates.accept(MediaType.ALL)), swaggerResourceHandler)
                .andRoute(RequestPredicates.GET("/swagger-resources/configuration/ui")
                        .and(RequestPredicates.accept(MediaType.ALL)), swaggerUiHandler)
                .andRoute(RequestPredicates.GET("/swagger-resources/configuration/security")
                        .and(RequestPredicates.accept(MediaType.ALL)), swaggerSecurityHandler)
                .andRoute(RequestPredicates.GET("/defaultfallback")
                        .and(RequestPredicates.accept(MediaType.TEXT_PLAIN)), hystrixFallbackHandler);
    }

}