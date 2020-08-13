package com.wlcb.jpower.module.common.swagger;

import com.wlcb.jpower.module.common.auth.SecureConstant;
import com.wlcb.jpower.module.common.auth.TokenConstant;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;

import java.util.ArrayList;
import java.util.Arrays;
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

    private String groupName = "基础框架";
    private String title = "Jpower 接口文档系统";
    private String description = "Jpower 接口文档系统";
    private String license = "Powered By Jpower";
    private String licenseUrl = "http:localhost";
    private String termsOfServiceUrl = "https:localhost";
    private String version = "1.0.1";
    private Contact contact = new Contact("GDZ","localhost","");

    private List<Authorization> authorization = new ArrayList<>(Arrays.asList(new Authorization(SecureConstant.BASIC_HEADER_KEY,"header"),new Authorization(TokenConstant.HEADER,"header")));

    @Data
    static class Authorization{
        private String name;
        private String type;

        private List<AuthorizationScope> authorizationScopes = new ArrayList(Collections.singletonList(new AuthorizationScope("global","accessEverything")));

        public Authorization(String name, String header) {
            this.name = name;
            this.type = header;
        }
    }
}
