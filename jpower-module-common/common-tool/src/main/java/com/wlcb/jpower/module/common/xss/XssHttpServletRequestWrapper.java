package com.wlcb.jpower.module.common.xss;

import com.wlcb.jpower.module.common.utils.SqlInjectionUtil;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * @ClassName XssHttpServletRequestWrapper
 * @Description TODO XSS具体过滤实现,连特殊字符和sql注入一起过滤
 * @Author 郭丁志
 * @Date 2020-05-01 22:49
 * @Version 1.0
 */
public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {
    HttpServletRequest orgRequest = null;
    private boolean isIncludeRichText = false;

    public XssHttpServletRequestWrapper(HttpServletRequest request, boolean isIncludeRichText) {
        super(request);
        orgRequest = request;
        this.isIncludeRichText = isIncludeRichText;
    }


    /**
     * 覆盖getParameter方法，将参数名和参数值都做xss过滤。<br/>
     * 如果需要获得原始的值，则通过super.getParameterValues(name)来获取<br/>
     * getParameterNames,getParameterValues和getParameterMap也可能需要覆盖
     */
    @Override
    public String getParameter(String name) {
        if(("content".equals(name) || name.endsWith("WithHtml")) && !isIncludeRichText){
            return super.getParameter(name);
        }
        name = SqlInjectionUtil.filter(name);//;JsoupUtils.clean(name);
        String value = super.getParameter(name);
        if (StringUtils.isNotBlank(value)) {
            value = SqlInjectionUtil.filter(value);//JsoupUtils.clean(value);
            if (StringUtils.equals(value,"null") || StringUtils.equals(value,"undefined")){
                value = null;
            }
        }
        return value;
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] arr = super.getParameterValues(name);
        if(arr != null){
            for (int i=0;i<arr.length;i++) {
                arr[i] = SqlInjectionUtil.filter(arr[i]);//JsoupUtils.clean(arr[i]);
            }
        }
        return arr;
    }


    /**
     * 覆盖getHeader方法，将参数名和参数值都做xss过滤。<br/>
     * 如果需要获得原始的值，则通过super.getHeaders(name)来获取<br/>
     * getHeaderNames 也可能需要覆盖
     */
    @Override
    public String getHeader(String name) {
        name = SqlInjectionUtil.filter(name);//JsoupUtils.clean(name);
        String value = super.getHeader(name);
        if (StringUtils.isNotBlank(value) && !StringUtils.equals(name,"Accept") ) {
            value = SqlInjectionUtil.filter(value);//JsoupUtils.clean(value);
            if (StringUtils.equals(value,"null") || StringUtils.equals(value,"undefined")){
                value = null;
            }
        }
        return value;
    }

    /**
     * 获取最原始的request
     *
     * @return
     */
    public HttpServletRequest getOrgRequest() {
        return orgRequest;
    }

    /**
     * 获取最原始的request的静态方法
     *
     * @return
     */
    public static HttpServletRequest getOrgRequest(HttpServletRequest req) {
        if (req instanceof XssHttpServletRequestWrapper) {
            return ((XssHttpServletRequestWrapper) req).getOrgRequest();
        }
        return req;
    }

}

