package com.wlcb.jpower.module.common.utils;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * @author mr.gmac
 */
public class JwtUtil {

    public static String BEARER = "jpower";
    public static Integer AUTH_LENGTH = 7;
    public static String HEADER = "jpower-auth";

    /**
     * 获取token串
     *
     * @param request
     * @return String
     */
    public static String getToken(HttpServletRequest request) {
        String auth = request.getHeader(HEADER);
        if (StringUtil.isNotBlank(auth) && auth.length() > AUTH_LENGTH) {
            String headStr = auth.substring(0, 6).toLowerCase();
            if (headStr.compareTo(BEARER) == 0) {
                auth = auth.substring(AUTH_LENGTH);
                return auth;
            }
        }

        Cookie[] cookies = request.getCookies();
        if (cookies!=null && cookies.length>0){
            for (Cookie cookie : cookies) {
                if (StringUtils.equals(cookie.getName(),HEADER)){
                    return cookie.getValue();
                }
            }
        }

        String parameter = request.getParameter(HEADER);
        if (StringUtil.isNotBlank(parameter)) {
            return parameter;
        }
        return null;
    }

}
