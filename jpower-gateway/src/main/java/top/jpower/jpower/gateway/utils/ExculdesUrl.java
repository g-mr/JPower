package top.jpower.jpower.gateway.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName ExculdesUrl
 * @Description TODO
 * @Author 郭丁志
 * @Date 2020/8/30 0030 23:45
 * @Version 1.0
 */
public class ExculdesUrl {

    private static List<String> exculudesUrl = new ArrayList<>();

    static {
        exculudesUrl.add("/auth/**");
        exculudesUrl.add("/v2/api-docs-ext/**");
        exculudesUrl.add("/v2/api-docs/**");
        exculudesUrl.add("/all/restful");
        exculudesUrl.add("/core/system/configure");
    }

    /**
     * 默认无需鉴权的API
     */
    public static List<String> getExculudesUrl() {
        return exculudesUrl;
    }


}
