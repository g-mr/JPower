package top.jpower.core.auth.properties;

import java.util.ArrayList;
import java.util.List;

/**
 * 不拦截的URL
 *
 * @author mr.g
 **/
public class AuthDefExculdesUrl {

    private static List<String> exculudesUrl = new ArrayList<>();

    static {
        exculudesUrl.add("/auth/**");
        exculudesUrl.add("/core/system/configure");
        exculudesUrl.add("/v3/api-docs-ext/**");
        exculudesUrl.add("/v3/api-docs/**");
        exculudesUrl.add("/swagger-resources/**");
        exculudesUrl.add("/doc.html");
        exculudesUrl.add("/webjars/**");
        exculudesUrl.add("/druid/**");
    }

    /**
     * 默认无需鉴权的API
     */
    public static List<String> getExculudesUrl() {
        return exculudesUrl;
    }


}
