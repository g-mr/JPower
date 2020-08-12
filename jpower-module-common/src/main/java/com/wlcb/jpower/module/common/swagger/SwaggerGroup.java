package com.wlcb.jpower.module.common.swagger;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName SwaggerGroup
 * @Description TODO 聚合文档
 * @Author 郭丁志
 * @Date 2020/8/13 0013 0:49
 * @Version 1.0
 */
@Primary
@Component
@AllArgsConstructor
public class SwaggerGroup implements SwaggerResourcesProvider {

    private DocsProperties docsProperties;

    @Override
    public List<SwaggerResource> get() {
        List<SwaggerResource> list = new ArrayList<>();
        docsProperties.getResource().forEach(docProperties -> {
            list.add(swaggerResource(docProperties));
        });
        return list;
    }

    private SwaggerResource swaggerResource(DocsProperties.DocProperties docProperties) {
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(docProperties.getName());
        swaggerResource.setLocation(docProperties.getLocation().concat("/v2/api-docs-ext"));
        swaggerResource.setUrl(docProperties.getUrl());
        swaggerResource.setSwaggerVersion(docProperties.getVersion());
        return swaggerResource;
    }
}
