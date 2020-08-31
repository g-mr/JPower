package com.wlcb.jpower.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.*;

/**
 * @ClassName MutableHttpServletRequest
 * @Description TODO
 * @Author 郭丁志
 * @Date 2020/8/31 0031 21:43
 * @Version 1.0
 */
public final class MutableHttpServletRequest extends HttpServletRequestWrapper {
    private final Map<String, String> customHeaders;

    public MutableHttpServletRequest(HttpServletRequest request){
        super(request);
        this.customHeaders = new HashMap<>();
    }

    public void putHeader(String name, String value){
        this.customHeaders.put(name, value);
    }

    @Override
    public String getHeader(String name) {
        String headerValue = customHeaders.get(name);

        if (headerValue != null){
            return headerValue;
        }
        return ((HttpServletRequest) getRequest()).getHeader(name);
    }

    @Override
    public Enumeration<String> getHeaderNames() {
        if (customHeaders.isEmpty()) {
            return super.getHeaderNames();
        }

        Set<String> set = new HashSet<String>(customHeaders.keySet());
        // 添加自定义header
        Enumeration<String> e = ((HttpServletRequest) getRequest()).getHeaderNames();
        while (e.hasMoreElements()) {
            String n = e.nextElement();
            set.add(n);
        }

        return Collections.enumeration(set);
    }
}
