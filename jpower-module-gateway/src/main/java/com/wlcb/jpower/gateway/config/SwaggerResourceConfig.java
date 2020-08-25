package com.wlcb.jpower.gateway.config;

import com.wlcb.jpower.module.common.utils.constants.JpowerConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.support.NameUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName SwaggerResourceConfig
 * @Description TODO
 * @Author 郭丁志
 * @Date 2020-08-25 11:50
 * @Version 1.0
 */
@Component
@Primary
@Slf4j
public class SwaggerResourceConfig implements SwaggerResourcesProvider {

    @Autowired
    RouteLocator routeLocator;
//    @Autowired
//    RestTemplate restTemplate;
    @Autowired
    private GatewayProperties gatewayProperties;

    private final String url = "/swagger-resources";

    @Override
    public List<SwaggerResource> get() {
        List<SwaggerResource> resources = new ArrayList<>();
        List<String> routes = new ArrayList<>();
        //获取所有路由的ID
        routeLocator.getRoutes().subscribe(route -> routes.add(route.getId()));
        //过滤出配置文件中定义的路由->过滤出Path Route Predicate->根据路径拼接成api-docs路径->生成SwaggerResource
        gatewayProperties.getRoutes().stream().filter(routeDefinition -> routes.contains(routeDefinition.getId()))
                .forEach(routeDefinition -> {
                    routeDefinition.getPredicates().stream()
                            .filter(predicateDefinition -> "Path".equalsIgnoreCase(predicateDefinition.getName()))
                            .forEach(predicateDefinition -> {
                                  //在这里去获取swagger得中文名称  需要实现restTemplate
//                                restTemplate.getForObject("http://"+routeDefinition.getUri().getHost()+url, JSONArray.class);
                                resources.add(swaggerResource(routeDefinition.getId(),
                                        predicateDefinition.getArgs().get(NameUtils.GENERATED_NAME_PREFIX + "0")
                                                .replace("/**", "/v2/api-docs")));
                            });
                });
        return resources;
    }

    private SwaggerResource swaggerResource(String name, String location) {
        log.info("name:{},location:{}", name, location);
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(name);
        swaggerResource.setLocation(location);
        swaggerResource.setSwaggerVersion(JpowerConstants.JPOWER_VESION);
        return swaggerResource;
    }
}
