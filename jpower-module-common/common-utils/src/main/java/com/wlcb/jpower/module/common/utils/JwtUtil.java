package com.wlcb.jpower.module.common.utils;

import com.wlcb.jpower.module.common.utils.constants.CharsetKit;
import com.wlcb.jpower.module.common.utils.constants.TokenConstant;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;

//import io.netty.handler.codec.http.FullHttpRequest;

/**
 * @author mr.gmac
 */
public class JwtUtil {

    public static String JPOWER = TokenConstant.JPOWER;
    public static Integer AUTH_LENGTH = TokenConstant.AUTH_LENGTH;
    public static String HEADER = JPOWER + "-auth";
    public static String SIGN_KEY = TokenConstant.SIGN_KEY;

    private static String BASE64_SECURITY = Base64.getEncoder().encodeToString(SIGN_KEY.getBytes(CharsetKit.CHARSET_UTF_8));

    /**
     * 解析jsonWebToken
     *
     * @param jsonWebToken jsonWebToken
     * @return Claims
     */
    public static Claims parseJWT(String jsonWebToken) {
        if (Fc.isBlank(jsonWebToken)) {
            return null;
        }

        try {
            return Jwts.parser()
                    .setSigningKey(Base64.getDecoder().decode(BASE64_SECURITY))
                    .parseClaimsJws(jsonWebToken).getBody();
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * 解析获取最后的token
     * @Author ding
     * @Date 17:35 2021-02-08
     * @param auth
     * @return java.lang.String
     **/
    public static String parsingToken(String auth) {
        if (StringUtil.isNotBlank(auth) && auth.length() > AUTH_LENGTH) {
            String headStr = auth.substring(0, 6).toLowerCase();
            if (headStr.compareTo(JPOWER) == 0) {
                auth = auth.substring(AUTH_LENGTH);
                return auth;
            }
        }
        return null;
    }

    /**
     * 获取token串
     *
     * @param request
     * @return String
     */
    public static String getToken(HttpServletRequest request) {
        if (Fc.isNull(request)) {
            return null;
        }

        String auth = request.getHeader(HEADER);
        if (StringUtil.isNotBlank(auth)) {
            return parsingToken(auth);
        }

        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (StringUtils.equals(cookie.getName(), HEADER)) {
                    if (Fc.isNotBlank(cookie.getValue())) {
                        auth = parsingToken(Fc.decode(cookie.getValue()));
                        return auth;
                    }
                }
            }
        }

        String parameter = request.getParameter(HEADER);
        if (StringUtil.isNotBlank(parameter)) {
            auth = parameter;
            return auth;
        }
        return null;
    }

//    /**
//     * uri 与 headers取token
//     *
//     * @param fullHttpRequest
//     * @return String
//     */
//    public static String getToken(FullHttpRequest fullHttpRequest) {
//        if (Fc.isNull(fullHttpRequest)) {
//            return null;
//        }
//
//        String auth = fullHttpRequest.headers().get(HEADER);
//        if (StringUtil.isNotBlank(auth)) {
//            return parsingToken(auth);
//        }
//        String parameter = "";
//        String uri = UrlUtil.decodeURL(fullHttpRequest.uri(), Charset.defaultCharset());
//        String params = uri.substring(uri.indexOf("?") + 1, uri.length());
//        parameter = Splitter.on("&").withKeyValueSeparator("=").split(params).get(HEADER);
//        if (StringUtil.isNotBlank(parameter)) {
//            auth = parameter;
//            return auth;
//        }
//        return null;
//    }
}
