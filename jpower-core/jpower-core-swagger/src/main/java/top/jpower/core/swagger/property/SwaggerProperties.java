package top.jpower.core.swagger.property;

import cn.hutool.core.collection.ListUtil;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpHeaders;
import top.jpower.core.util.constants.JpowerConstants;

import java.util.List;
import java.util.Map;

import static top.jpower.core.util.constants.JpowerConstants.JPOWER;

/**
 * swagger配置参数
 *
 * @author 郭丁志
 */
@Data
@ConfigurationProperties(prefix = "jpower.swagger")
public class SwaggerProperties {

    public static final String CLIENT = HttpHeaders.AUTHORIZATION;
    public static final String JPOWER_AUTH = JpowerConstants.AUTH_HEADER;
    public static final String MENU_CODE = JpowerConstants.HEADER_MENU;

    /**
     * swagger会解析的url规则
     **/
//    private List<String> bath = ListUtil.of("/**");
    /**
     * 在basePath基础上需要排除的url规则
     **/
//    private List<String> excludePath = ListUtil.of("/error", "/actuator/**", "/getAllFunction");

    /**
     * host信息
     **/
//    private String host = "";
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
     * 服务条款
     **/
    private String termsOfServiceUrl = "http:localhost";
    /**
     * 服务版本
     **/
    private String version = JpowerConstants.JPOWER_VESION;
    /**
     * 摘要
     **/
    private String summary;
    /**
     * 联系人信息
     **/
    private Contact contact = new Contact().name("mr.g").url("localhost").email("");
    /**
     * 鉴权信息
     **/
    private List<SecurityScheme> authorization = ListUtil.of
            (new SecurityScheme()
                            .name(JPOWER_AUTH)
                            .type(SecurityScheme.Type.HTTP)
                            .scheme(JPOWER)
                            .bearerFormat("JWT")
                            .description("请输入TOKEN"),
                    new SecurityScheme()
                            .name(CLIENT)
                            .type(SecurityScheme.Type.HTTP)
                            .scheme("basic")
                            .description("请输入客户端"),
                    new SecurityScheme()
                            .name(MENU_CODE)
                            .type(SecurityScheme.Type.APIKEY)
                            .in(SecurityScheme.In.HEADER)
                            .description("请输入上级菜单用于数据权限"));

    /**
     * 接口分组
     **/
    private Map<String, GroupInfo> groups;

    @Data
    public static class GroupInfo {
        private List<String> pathsToMatch;
        private List<String> packagesToScan;
        private List<String> pathsToExclude;
        private List<String> packagesToExclude;
    }
}
