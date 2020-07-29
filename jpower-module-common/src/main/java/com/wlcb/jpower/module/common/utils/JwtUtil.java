package com.wlcb.jpower.module.common.utils;

import com.wlcb.jpower.module.common.auth.TokenConstant;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @author mr.gmac
 */
public class JwtUtil {

    public static String BEARER = TokenConstant.JPOWER;
    public static Integer AUTH_LENGTH = TokenConstant.AUTH_LENGTH;

    /**
     * 获取token串
     *
     * @param request
     * @return String
     */
    public static String getToken(HttpServletRequest request) {
        String auth = request.getHeader(TokenConstant.HEADER);
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
                if (StringUtils.equals(cookie.getName(),"Authorization")){
                    return cookie.getValue();
                }
            }
        }

        String parameter = request.getParameter(TokenConstant.HEADER);
        if (StringUtil.isNotBlank(parameter)) {
            return parameter;
        }
        return null;
    }

}
