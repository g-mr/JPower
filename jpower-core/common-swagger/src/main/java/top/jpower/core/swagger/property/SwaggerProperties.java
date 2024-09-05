package top.jpower.core.swagger.property;

import cn.hutool.core.collection.ListUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import top.jpower.core.util.constants.JpowerConstants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * swagger配置参数
 *
 * @author 郭丁志
 */
@Data
@ConfigurationProperties(prefix = "jpower.swagger")
public class SwaggerProperties {

    private static final String BASIC_HEADER_KEY = "Authorization";
    private static final String HEADER = JpowerConstants.AUTH_HEADER;
    private static final String MENU_CODE = JpowerConstants.HEADER_MENU;

    /**
     * swagger会解析的url规则
     **/
    private List<String> basePath = new ArrayList<>(Collections.singletonList("/**"));
    /**
     * 在basePath基础上需要排除的url规则
     **/
    private List<String> excludePath = ListUtil.toList("/error", "/actuator/**", "/getAllFunction");

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
    private String termsOfServiceUrl = "http:localhost";
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
    private List<Authorization> authorization = ListUtil.of(new Authorization(BASIC_HEADER_KEY,"/**", "/auth/**"), new Authorization(HEADER, "/**", "/auth/**"), new Authorization(MENU_CODE));

    @Data
    @AllArgsConstructor
    public static class Contact {
        private String name;
        private String url;
        private String email;
    }

    @Data
    @AllArgsConstructor
    public static class Authorization {

        /**
         * 授权header头名称
         **/
        private String name;

        /**
         * 需要在哪些url规则上展示授权
         **/
        private String path = "/**";

        /**
         * 需要在哪些url规则上不展示授权
         **/
        private String excludePath = "/**";

        public Authorization(String name){
            this.name = name;
        }

    }

}
