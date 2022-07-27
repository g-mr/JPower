package com.wlcb.jpower.module.configurer.xss;

import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.SqlInjectionUtil;
import com.wlcb.jpower.module.common.utils.StringUtil;
import com.wlcb.jpower.module.common.utils.constants.TokenConstant;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import static com.sun.org.apache.xml.internal.serialize.Method.HTML;
import static com.wlcb.jpower.module.common.utils.constants.StringPool.NULL;
import static com.wlcb.jpower.module.common.utils.constants.StringPool.UNDEFINED;

/**
 * XSS具体过滤实现,连特殊字符和sql注入一起过滤
 *
 * @author mr.g
 **/
public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {

    @Getter
    private HttpServletRequest originalRequest;
    private boolean isIncludeRichText;

    XssHttpServletRequestWrapper(HttpServletRequest request, boolean isIncludeRichText) {
        super(request);
        this.originalRequest = request;
        this.isIncludeRichText = isIncludeRichText;
    }


    /**
     * 覆盖getParameter方法，将参数名和参数值都做xss过滤。<br/>
     * 如果需要获得原始的值，则通过super.getParameterValues(name)来获取<br/>
     * getParameterNames,getParameterValues和getParameterMap也可能需要覆盖
     */
    @Override
    public String getParameter(String name) {

        if(StringUtil.contains(name,HTML) && isIncludeRichText){
            return super.getParameter(name);
        }

        name = SqlInjectionUtil.filter(name);
        String value = super.getParameter(name);
        if (StringUtils.isNotBlank(value)) {
            value = SqlInjectionUtil.filter(value);
            if (StringUtil.equals(value,NULL) || StringUtil.equals(value,UNDEFINED)){
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
                arr[i] = SqlInjectionUtil.filter(arr[i]);
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
        if (Fc.equalsValue(TokenConstant.PASS_HEADER_NAME,name) || Fc.equalsValue(TokenConstant.DATA_SCOPE_NAME,name) || Fc.equalsValue(TokenConstant.HEADER,name)){
            return super.getHeader(name);
        }

        name = SqlInjectionUtil.filter(name);
        String value = super.getHeader(name);
        if (StringUtils.isNotBlank(value) && !StringUtils.equals(name,"Accept") ) {
            value = SqlInjectionUtil.filter(value);
            if (StringUtils.equals(value,NULL) || StringUtils.equals(value,UNDEFINED)){
                value = null;
            }
        }
        return value;
    }

    /**
     * 获取最原始的request的静态方法
     *
     * @return
     */
    public static HttpServletRequest getOriginalRequest(HttpServletRequest req) {
        if (req instanceof XssHttpServletRequestWrapper) {
            return ((XssHttpServletRequestWrapper) req).getOriginalRequest();
        }
        return req;
    }

}

