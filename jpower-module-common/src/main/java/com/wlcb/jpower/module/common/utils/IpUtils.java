package com.wlcb.jpower.module.common.utils;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * @ClassName IpUtils
 * @Description TODO IP工具类
 * @Author 郭丁志
 * @Date 2020-07-02 15:52
 * @Version 1.0
 */
public class IpUtils {

    /**
     * @Author 郭丁志
     * @Description //TODO 获取客户端真实IP
     * @Date 15:53 2020-07-02
     * @return java.lang.String
     **/
    public static String getRemortIP(HttpServletRequest request) {
        if (request.getHeader("x-forwarded-for") == null) {
            return request.getRemoteAddr();
        }
        String ips = request.getHeader("x-forwarded-for");

        if (ips.contains(",")){
            return getIP(ips.split(","));
        }

        return ips;
    }

    private static String getIP(String[] ips){
        for (String ip : ips) {
            if (StringUtils.equals(ip,"unknown")){
                return getIP(SplitList.removeStart(ips));
            }else {
                return ip;
            }
        }

        return null;
    }

}
