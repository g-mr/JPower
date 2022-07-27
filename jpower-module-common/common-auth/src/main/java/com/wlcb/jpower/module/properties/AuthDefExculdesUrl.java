package com.wlcb.jpower.module.properties;

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
        exculudesUrl.add("/v2/api-docs-ext/**");
        exculudesUrl.add("/v2/api-docs/**");
        exculudesUrl.add("/swagger-resources/**");
        exculudesUrl.add("/doc.html");
        exculudesUrl.add("/webjars/**");
        exculudesUrl.add("/druid/**");

        exculudesUrl.add("/core/dict/getDictListByType");
        exculudesUrl.add("/core/user/downloadTemplate");
        exculudesUrl.add("/core/tenant/selectors");
        exculudesUrl.add("/core/function/listMenuTree");
        exculudesUrl.add("/core/function/listBut");
    }

    /**
     * 默认无需鉴权的API
     */
    public static List<String> getExculudesUrl() {
        return exculudesUrl;
    }


}
