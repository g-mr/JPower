package com.wlcb.jpower.module.common.swagger;

import com.wlcb.jpower.module.common.utils.constants.JpowerConstants;
import com.wlcb.jpower.module.common.utils.constants.TokenConstant;
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
@ConfigurationProperties(prefix = "jpower.swagger")
public class SwaggerProperties {

    private static final String BASIC_HEADER_KEY = "Authorization";
    private static final String HEADER = TokenConstant.HEADER;

    /** 分组名称 */
    private String groupName = "";

    /** 扫描路径 **/
    private List<String> basePackage = new ArrayList(Collections.singletonList("com.wlcb"));

    /**
     * swagger会解析的url规则
     **/
    private List<String> basePath = new ArrayList<>(Collections.singletonList("/**"));
    /**
     * 在basePath基础上需要排除的url规则
     **/
    private List<String> excludePath = Arrays.asList("/error", "/actuator/**");

    /**
     * host信息
     **/
    private String host = "";
    /**
     * 接口文档名称
     **/
    private String title = "Jpower 接口文档系统";
    /**
     * 接口文档描述
     **/
    private String description = "Jpower 接口文档系统";
    /**
     * 文档下方的license显示信息
     **/
    private String license = "Powered By Jpower";
    /**
     * license点击跳转链接
     **/
    private String licenseUrl = "https://gitee.com/gdzWork/JPower";
    /**
     * 服务地址
     **/
    private String termsOfServiceUrl = "https:localhost";
    /**
     * 服务版本
     **/
    private String version = JpowerConstants.JPOWER_VESION;
    /**
     * 联系人信息
     **/
    private Contact contact = new Contact("mr.g","localhost","");
    /**
     * 鉴权信息
     **/
    private List<Authorization> authorization = new ArrayList<>(Arrays.asList(new Authorization(BASIC_HEADER_KEY,"header"),new Authorization(HEADER,"header")));

    @Data
    @AllArgsConstructor
    static class Contact{
        /** 姓名 **/
        private String name;
        /** 地址 **/
        private String url;
        /** 邮箱 **/
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
