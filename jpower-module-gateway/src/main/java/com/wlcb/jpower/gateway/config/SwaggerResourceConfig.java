package com.wlcb.jpower.gateway.config;

import com.alibaba.cloud.nacos.NacosConfigProperties;
import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.config.ConfigService;
import com.wlcb.jpower.gateway.route.DynamicRouteDefinition;
import com.wlcb.jpower.gateway.utils.NacosUtils;
import com.wlcb.jpower.module.common.utils.ExceptionsUtil;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.constants.JpowerConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

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
    private NacosConfigProperties nacosConfigProperties;
    @Autowired
    RouteLocator routeLocator;

    @Override
    public List<SwaggerResource> get() {
        List<SwaggerResource> resources = new ArrayList<>();
        try {
            List<String> routes = new ArrayList<>();
            //获取所有路由的ID
            routeLocator.getRoutes().subscribe(route -> routes.add(route.getId()));

            Properties properties = new Properties();
            properties.setProperty(PropertyKeyConst.SERVER_ADDR,nacosConfigProperties.getServerAddr());
            properties.setProperty(PropertyKeyConst.NAMESPACE,nacosConfigProperties.getNamespace());
            ConfigService configService= NacosFactory.createConfigService(properties);
            String configInfo = configService.getConfig(NacosUtils.getRouteDataId(),nacosConfigProperties.getGroup(),5000L);
            if (Fc.isNotBlank(configInfo)){
                List<DynamicRouteDefinition> list = JSON.parseArray(configInfo,DynamicRouteDefinition.class);

                list.stream().filter(routeDefinition -> routes.contains(routeDefinition.getId()) && routeDefinition.getUri().toString().startsWith("lb://"))
                        .forEach(routeDefinition -> resources.add(swaggerResource(Fc.isNotBlank(routeDefinition.getName())?routeDefinition.getName():routeDefinition.getId(),
                                routeDefinition.getUri().getHost().concat("/v2/api-docs"))));
            }
        }catch (Exception e){
            log.error("文档生成失败={}", ExceptionsUtil.getStackTraceAsString(e));
        }
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
