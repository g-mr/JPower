package top.jpower.core.boot.xss;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import lombok.Getter;
import org.apache.commons.codec.binary.StringUtils;
import top.jpower.core.util.constants.JpowerConstants;
import top.jpower.core.util.constants.StringPool;
import top.jpower.core.util.constants.TokenConstant;
import top.jpower.core.util.enums.Header;
import top.jpower.core.util.support.XssInjectionUtil;
import top.jpower.core.util.utils.Fc;
import top.jpower.core.util.utils.StringUtil;


/**
 * XSS具体过滤实现,连特殊字符和sql注入一起过滤
 *
 * @author mr.g
 **/
public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {

    private static final String HTML = "html";

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

        if(StringUtil.contains(name, HTML) && isIncludeRichText){
            return super.getParameter(name);
        }

        name = XssInjectionUtil.filter(name);
        String value = super.getParameter(name);
        if (Fc.isNotBlank(value)) {
            value = XssInjectionUtil.filter(value);
            if (StringUtil.equals(value, StringPool.NULL) || StringUtil.equals(value, StringPool.UNDEFINED)){
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
                arr[i] = XssInjectionUtil.filter(arr[i]);
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
        if (Fc.equalsValue(TokenConstant.PASS_HEADER_NAME,name) || Fc.equalsValue(TokenConstant.DATA_SCOPE_NAME,name) || Fc.equalsValue(JpowerConstants.AUTH_HEADER,name)){
            return super.getHeader(name);
        }

        name = XssInjectionUtil.filter(name);
        String value = super.getHeader(name);

        if (Fc.isNotBlank(value) && !Header.contains(name)) {
            value = XssInjectionUtil.filter(value);
            if (StringUtils.equals(value, StringPool.NULL) || StringUtils.equals(value, StringPool.UNDEFINED)){
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

