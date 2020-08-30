package com.wlcb.jpower.gateway.utils;

import com.wlcb.jpower.module.common.utils.StringUtil;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

/**
 * @ClassName IpUtil
 * @Description TODO
 * @Author 郭丁志
 * @Date 2020/8/30 0030 0:14
 * @Version 1.0
 */
public class IpUtil {

    public static final String UN_KNOWN = "unknown";

    @Nullable
    public static String getIP(ServerHttpRequest request) {
        Assert.notNull(request, "HttpServletRequest is null");
        String ip = request.getHeaders().getFirst("X-Requested-For");
        if (StringUtil.isBlank(ip) || UN_KNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeaders().getFirst("X-Forwarded-For");
        }
        if (StringUtil.isBlank(ip) || UN_KNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeaders().getFirst("Proxy-Client-IP");
        }
        if (StringUtil.isBlank(ip) || UN_KNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeaders().getFirst("WL-Proxy-Client-IP");
        }
        if (StringUtil.isBlank(ip) || UN_KNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeaders().getFirst("HTTP_CLIENT_IP");
        }
        if (StringUtil.isBlank(ip) || UN_KNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeaders().getFirst("HTTP_X_FORWARDED_FOR");
        }
        if (StringUtil.isBlank(ip) || UN_KNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddress().getHostName();
        }
        return StringUtil.isBlank(ip) ? null : ip.split(",")[0];
    }

}
