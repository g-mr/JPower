package com.wlcb.jpower.module.common.swagger;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import springfox.documentation.service.Contact;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @ClassName SwaggerProperties
 * @Description TODO swagger配置参数
 * @Author 郭丁志
 * @Date 2020-08-12 16:16
 * @Version 1.0
 */
@Data
@ConfigurationProperties(prefix = "swagger")
public class SwaggerProperties {

    private List<String> basePackage = new ArrayList(Collections.singletonList("com.wlcb"));

    private String title = "Jpower 接口文档系统";
    private String description = "Jpower 接口文档系统";
    private String license = "Powered By Jpower";
    private String licenseUrl = "";
    private String termsOfServiceUrl = "";
    private String version = "1.0.1";
    private Contact contact;

}
