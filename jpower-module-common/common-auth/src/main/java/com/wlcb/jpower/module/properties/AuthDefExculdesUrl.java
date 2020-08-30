package com.wlcb.jpower.module.properties;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName AuthDefExculdesUrl
 * @Description TODO 不拦截的URL
 * @Author 郭丁志
 * @Date 2020/8/30 0030 0:00
 * @Version 1.0
 */
public class AuthDefExculdesUrl {

    public static String TARGET = "/**";
    public static String REPLACEMENT = "";

    private static List<String> exculudesUrl = new ArrayList<>();

    static {
        exculudesUrl.add("/auth/**");
        exculudesUrl.add("/v2/api-docs-ext/**");
        exculudesUrl.add("/v2/api-docs/**");
        exculudesUrl.add("/swagger-resources/**");
        exculudesUrl.add("/doc.html");
        exculudesUrl.add("/webjars/**");
    }

    /**
     * 默认无需鉴权的API
     */
    public static List<String> getExculudesUrl() {
        return exculudesUrl;
    }


}
