package top.jpower.core.boot.xss;

import lombok.Data;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * XSS配置
 *
 * @author mr.g
 */
@Data
@ConfigurationProperties("jpower.xss")
public class XssProperties {

    /** 是否开XSS过滤 **/
    private Boolean enable = true;

    /** 是否开启富文本过滤 **/
    private Boolean isIncludeRichText = false;
    /** 不过滤得URL **/
    private List<String> excludes = new ArrayList<>();

    @Getter
    private static List<String> defaultExcludes = new ArrayList<>();

    static {
        defaultExcludes.add("/v3/api-docs-ext/**");
        defaultExcludes.add("/v3/api-docs/**");
        defaultExcludes.add("/swagger-resources/**");
        defaultExcludes.add("/doc.html");
        defaultExcludes.add("/webjars/**");
        defaultExcludes.add("/druid/**");
        // 回头加个注解式的过滤
        defaultExcludes.add("/core/dataScope/update");
        defaultExcludes.add("/core/dataScope/add");
    }
}
