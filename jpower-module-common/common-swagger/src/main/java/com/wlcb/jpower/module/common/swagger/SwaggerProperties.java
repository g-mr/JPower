package com.wlcb.jpower.module.common.swagger;

import com.wlcb.jpower.module.common.utils.constants.JpowerConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import springfox.documentation.service.AuthorizationScope;

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

    private static final String BASIC_HEADER_KEY = "Authorization";
    private static final String HEADER = "jpower-auth";

    private List<String> basePackage = new ArrayList(Collections.singletonList("com.wlcb"));

    /**
     * swagger会解析的url规则
     **/
    private List<String> basePath = new ArrayList<>();
    /**
     * 在basePath基础上需要排除的url规则
     **/
    private List<String> excludePath = new ArrayList<>();

    /**
     * host信息
     **/
    private String host = "";

    private String title = "Jpower 接口文档系统";
    private String description = "Jpower 接口文档系统";
    private String license = "Powered By Jpower";
    private String licenseUrl = "http:localhost";
    private String termsOfServiceUrl = "https:localhost";
    private String version = JpowerConstants.JPOWER_VESION;
    private Contact contact = new Contact("GDZ","localhost","");

    private List<Authorization> authorization = new ArrayList<>(Arrays.asList(new Authorization(BASIC_HEADER_KEY,"header"),new Authorization(HEADER,"header")));

    @Data
    @AllArgsConstructor
    static class Contact{
        private String name;
        private String url;
        private String email;
    }

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
