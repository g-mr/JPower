package com.wlcb.jpower.module.common.swagger;

import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.constants.JpowerConstants;
import lombok.AllArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
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
@EnableConfigurationProperties({DocsProperties.class})
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

    private SwaggerResource swaggerResource(SwaggerResource resource) {
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(resource.getName());
        if (Fc.isNotBlank(resource.getLocation())){
            swaggerResource.setLocation(resource.getLocation().concat("/v2/api-docs-ext"));
        }else {
            swaggerResource.setUrl(Fc.toStr(resource.getUrl()).concat("/v2/api-docs-ext"));
        }
        swaggerResource.setSwaggerVersion(Fc.isNotBlank(swaggerResource.getSwaggerVersion())?swaggerResource.getSwaggerVersion():JpowerConstants.JPOWER_VESION);
        return swaggerResource;
    }
}
