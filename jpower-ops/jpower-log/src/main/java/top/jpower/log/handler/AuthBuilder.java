package top.jpower.log.handler;

import top.jpower.core.util.utils.Fc;
import top.jpower.log.interceptor.AuthInterceptor;
import top.jpower.log.properties.MonitorRestfulProperties;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author mr.g
 * @Date 2021/4/10 0010 17:34
 */
public class AuthBuilder {
    /**
     * AuthInterceptor缓存池
     */
    private static final Map<String, AuthInterceptor> INTERCEPTOR_POOL = new ConcurrentHashMap<>();

    /**
     * 获取Handler
     *
     * @param  route 监控服务信息
     * @return HttpInfoHandler
     */
    public static AuthInterceptor getInterceptor(MonitorRestfulProperties.Route route) {
        AuthInterceptor interceptor = INTERCEPTOR_POOL.get(route.getName());
        if (Fc.isNull(interceptor)){
            interceptor = new AuthInterceptor(route.getAuth());
            INTERCEPTOR_POOL.put(route.getName(),interceptor);

        }
        return interceptor;
    }

}
