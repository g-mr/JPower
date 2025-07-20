package top.jpower.core.boot.xss;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import top.jpower.core.util.utils.Fc;
import top.jpower.core.util.utils.StringUtil;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;


/**
 * xss过滤器
 *
 * @author mr.g
 */
@Slf4j
@RequiredArgsConstructor
public class XssFilter implements Filter, Ordered {

    private final XssProperties xssProperties;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException,ServletException {
        if (!xssProperties.getEnable()){
            filterChain.doFilter(request, response);
            return;
        }

        if(log.isDebugEnabled()){
            log.debug("xss filter is open");
        }

        HttpServletRequest req = (HttpServletRequest) request;
        if (handleExcludeURL(req) || handleDefaultExcludeURL(req)) {
            filterChain.doFilter(request, response);
            return;
        }

        filterChain.doFilter(new XssHttpServletRequestWrapper((HttpServletRequest) request,xssProperties.getIsIncludeRichText()), response);
    }

    private boolean handleExcludeURL(HttpServletRequest request) {
        List<String> list = xssProperties.getExcludes();
        return excludeURL(list,request);
    }

    private boolean handleDefaultExcludeURL(HttpServletRequest request) {
        List<String> list = XssProperties.getDefaultExcludes();
        return excludeURL(list,request);
    }

    private boolean excludeURL(List<String> list,HttpServletRequest request) {
        if (Fc.isEmpty(list)) {
            return false;
        }

        String url = request.getServletPath();
        for (String pattern : list) {
            if (Fc.isNotBlank(pattern) && Fc.isNotBlank(url) && !Fc.equalsValue(url,"/")){
                if (StringUtil.wildcardEquals(pattern,url)){
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE+6;
    }
}

