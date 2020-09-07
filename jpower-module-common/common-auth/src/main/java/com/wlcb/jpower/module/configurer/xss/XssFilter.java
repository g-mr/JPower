package com.wlcb.jpower.module.configurer.xss;

import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.StringUtil;
import com.wlcb.jpower.module.properties.XssProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;


/**
 * @ClassName XssFilter
 * @Description TODO xss过滤器
 * @Author 郭丁志
 * @Date 2020-05-01 23:19
 * @Version 1.0
 */
@Component
@Slf4j
@Order(1)
@EnableConfigurationProperties({XssProperties.class})
public class XssFilter implements Filter {

    @Resource
    private XssProperties xssProperties;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException,ServletException {
        if(log.isDebugEnabled()){
            log.debug("xss filter is open");
        }

        HttpServletRequest req = (HttpServletRequest) request;
        if (handleExcludeURL(req) && handleDefaultExcludeURL(req)) {
            filterChain.doFilter(request, response);
            return;
        }

        XssHttpServletRequestWrapper xssRequest = new XssHttpServletRequestWrapper((HttpServletRequest) request,xssProperties.getIsIncludeRichText());
        filterChain.doFilter(xssRequest, response);
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
            if (Fc.isNotBlank(pattern) && Fc.isNotBlank(url) && !Fc.equals(url,"/")){
                if (StringUtil.wildcardEquals(pattern,url)){
                    return true;
                }
            }
        }

        return false;
    }
}

