package com.wlcb.jpower.module.common.utils;


import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.ArrayUtil;
import com.alibaba.fastjson.JSON;
import com.wlcb.jpower.module.common.utils.constants.CharsetKit;
import com.wlcb.jpower.module.common.utils.constants.StringPool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;


/**
 * Web 工具类
 * @author mr.g
 */
@Slf4j
public class WebUtil extends org.springframework.web.util.WebUtils {

    /**
     * 读取cookie
     *
     * @param name cookie name
     * @return cookie value
     */
    @Nullable
    public static String getCookieVal(String name) {
        HttpServletRequest request = getRequest();
        Assert.notNull(request, "request from RequestContextHolder is null");
        return getCookieVal(request, name);
    }

    /**
     * 读取cookie
     *
     * @param request HttpServletRequest
     * @param name    cookie name
     * @return cookie value
     */
    @Nullable
    public static String getCookieVal(HttpServletRequest request, String name) {
        Cookie cookie = getCookie(request, name);
        return Fc.notNull(cookie) ? cookie.getValue() : null;
    }

    /**
     * 清除 某个指定的cookie
     *
     * @param response HttpServletResponse
     * @param name     cookie name
     */
    public static void removeCookie(HttpServletResponse response, String name) {
        addCookie(response, name, null, 0);
    }

    /**
     * 设定返回给客户端的Cookie
     *
     * @param response 响应对象{@link HttpServletResponse}
     * @param cookie Servlet Cookie对象
     */
    public static void addCookie(HttpServletResponse response, Cookie cookie) {
        response.addCookie(cookie);
    }

    /**
     * 设定返回给客户端的Cookie
     *
     * @param response 响应对象{@link HttpServletResponse}
     * @param name Cookie名
     * @param value Cookie值
     */
    public static void addCookie(HttpServletResponse response, String name, String value) {
        response.addCookie(new Cookie(name, value));
    }

    /**
     * 设定返回给客户端的Cookie<br>
     * Path: "/"<br>
     * No Domain
     *
     * @param response 响应对象{@link HttpServletResponse}
     * @param name cookie名
     * @param value cookie值
     * @param maxAgeInSeconds -1: 关闭浏览器清除Cookie. 0: 立即清除Cookie. &gt;0 : Cookie存在的秒数.
     */
    public static void addCookie(HttpServletResponse response, String name, String value, int maxAgeInSeconds) {
        addCookie(response, name, value, maxAgeInSeconds, "/", null);
    }

    /**
     * 设置cookie
     *
     * @param response 响应对象{@link HttpServletResponse}
     * @param name cookie名
     * @param value cookie值
     * @param maxAgeInSeconds -1: 关闭浏览器清除Cookie. 0: 立即清除Cookie. &gt;0 : Cookie存在的秒数.
     * @param path Cookie的有效路径
     * @param domain the domain name within which this cookie is visible; form is according to RFC 2109
     */
    public static void addCookie(HttpServletResponse response, String name, String value, int maxAgeInSeconds, String path, String domain) {
        Cookie cookie = new Cookie(name, value);
        if (domain != null) {
            cookie.setDomain(domain);
        }
        cookie.setMaxAge(maxAgeInSeconds);
        cookie.setPath(path);
        addCookie(response, cookie);
    }

    /**
     * 获取 HttpServletRequest
     *
     * @return {HttpServletRequest}
     */
    public static HttpServletRequest getRequest() {
        ServletRequestAttributes requestAttributes = getRequestAttributes();
        return Fc.isNull(requestAttributes) ? null : requestAttributes.getRequest();
    }

    /**
     * 获取response
     */
    public static HttpServletResponse getResponse(){
        return getRequestAttributes().getResponse();
    }

    /**
     * 获取session
     */
    public static HttpSession getSession() {
        return getRequest().getSession();
    }

    public static ServletRequestAttributes getRequestAttributes() {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        return Fc.isNull(attributes) ? null : (ServletRequestAttributes) attributes;
    }

    /**
     * 返回json
     *
     * @param response HttpServletResponse
     * @param result   结果对象
     */
    public static void renderJson(HttpServletResponse response, Object result) {
        renderJson(response, result, APPLICATION_JSON_UTF8_VALUE);
    }

    /**
     * 返回json
     *
     * @param response    HttpServletResponse
     * @param result      结果对象
     * @param contentType contentType
     */
    public static void renderJson(HttpServletResponse response, Object result, String contentType) {
        response.setCharacterEncoding(StringPool.UTF_8);
        response.setContentType(contentType);
        try (PrintWriter out = response.getWriter()) {
            out.append(JSON.toJSONString(result));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 获取ip
     *
     * @return {String}
     */
    public static String getIP() {
        return getIP(WebUtil.getRequest());
    }

    /**
     * 获取ip
     *
     * @param request HttpServletRequest
     * @return {String}
     */
    @Nullable
    public static String getIP(HttpServletRequest request, String... otherHeaderNames) {
        Assert.notNull(request, "HttpServletRequest is null");
        String[] headers = { "X-Forwarded-For", "X-Real-IP", "Proxy-Client-IP", "WL-Proxy-Client-IP", "HTTP_CLIENT_IP", "HTTP_X_FORWARDED_FOR" };
        if (ArrayUtil.isNotEmpty(otherHeaderNames)) {
            headers = ArrayUtil.addAll(headers, otherHeaderNames);
        }
        return getClientIPByHeader(request, headers);
    }

    /**
     * 获取客户端IP
     *
     * <p>
     * headerNames参数用于自定义检测的Header<br>
     * 需要注意的是，使用此方法获取的客户IP地址必须在Http服务器（例如Nginx）中配置头信息，否则容易造成IP伪造。
     * </p>
     *
     * @param request 请求对象{@link HttpServletRequest}
     * @param headerNames 自定义头，通常在Http服务器（例如Nginx）中配置
     * @return IP地址
     */
    public static String getClientIPByHeader(HttpServletRequest request, String... headerNames) {
        String ip;
        for (String header : headerNames) {
            ip = request.getHeader(header);
            if (false == NetUtil.isUnknown(ip)) {
                return NetUtil.getMultistageReverseProxyIp(ip);
            }
        }

        ip = request.getRemoteAddr();
        return NetUtil.getMultistageReverseProxyIp(ip);
    }

    /***
     * 获取 request 中 json 字符串的内容
     *
     * @param request request
     * @return 字符串内容
     */
    public static String getRequestParamString(HttpServletRequest request) {
        try {
            return getRequestStr(request);
        } catch (Exception ex) {
            return StringPool.EMPTY;
        }
    }

    /**
     * 获取 request 请求内容
     *
     * @param request request
     * @return String
     * @throws IOException IOException
     */
    public static String getRequestStr(HttpServletRequest request) throws IOException {
        String queryString = request.getQueryString();
        if (StringUtil.isNotBlank(queryString)) {
            return new String(queryString.getBytes(CharsetKit.CHARSET_ISO_8859_1), CharsetKit.CHARSET_UTF_8 ).replaceAll("&amp;", "&").replaceAll("%22", "\"");
        }
        return getRequestStr(request, getRequestBytes(request));
    }

    /**
     * 获取 request 请求的 byte[] 数组
     *
     * @param request request
     * @return byte[]
     * @throws IOException IOException
     */
    public static byte[] getRequestBytes(HttpServletRequest request) {
        try {
            return IoUtil.readBytes(request.getInputStream());
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    /**
     * 获取 request 请求内容
     *
     * @param request request
     * @param buffer buffer
     * @return String
     * @throws IOException IOException
     */
    public static String getRequestStr(HttpServletRequest request, byte[] buffer) throws IOException {
        String charEncoding = request.getCharacterEncoding();
        if (charEncoding == null) {
            charEncoding = StringPool.UTF_8;
        }
        String str = new String(buffer, charEncoding).trim();
        if (StringUtil.isBlank(str)) {
            StringBuilder sb = new StringBuilder();
            Enumeration<String> parameterNames = request.getParameterNames();
            while (parameterNames.hasMoreElements()) {
                String key = parameterNames.nextElement();
                String value = request.getParameter(key);
                StringUtil.appendBuilder(sb, key, "=", value, "&");
            }
            str = StringUtil.removeSuffix(sb.toString(), "&");
        }
        return str.replaceAll("&amp;", "&");
    }


}

