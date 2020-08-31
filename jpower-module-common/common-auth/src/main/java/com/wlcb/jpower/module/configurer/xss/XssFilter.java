package com.wlcb.jpower.module.configurer.xss;

import com.wlcb.jpower.module.common.utils.StringUtil;
import com.wlcb.jpower.module.properties.XssProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


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
        HttpServletResponse resp = (HttpServletResponse) response;
        if (handleExcludeURL(req, resp)) {
            filterChain.doFilter(request, response);
            return;
        }

        XssHttpServletRequestWrapper xssRequest = new XssHttpServletRequestWrapper((HttpServletRequest) request,xssProperties.getIsIncludeRichText());
        filterChain.doFilter(xssRequest, response);
    }

    private boolean handleExcludeURL(HttpServletRequest request, HttpServletResponse response) {

        if (xssProperties.getExcludes() == null || xssProperties.getExcludes().isEmpty()) {
            return false;
        }

        String url = request.getServletPath();
        for (String pattern : xssProperties.getExcludes()) {
            if (StringUtil.wildcardEquals(pattern,url)){
                return true;
            }
        }

        return false;
    }

}

