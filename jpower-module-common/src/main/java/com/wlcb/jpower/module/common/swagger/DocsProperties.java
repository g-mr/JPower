package com.wlcb.jpower.module.common.swagger;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import springfox.documentation.swagger.web.SwaggerResource;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName DocsProperties
 * @Description TODO 文档聚合
 * @Author 郭丁志
 * @Date 2020/8/13 0013 0:51
 * @Version 1.0
 */
@Data
@ConfigurationProperties(prefix = "jpower.docs")
public class DocsProperties {

    private final List<SwaggerResource> resource = new ArrayList<>();

}
