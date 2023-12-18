package top.jpower.jpower.module.common.apm;

import com.google.common.collect.ImmutableSet;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.skywalking.apm.toolkit.trace.ActiveSpan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import top.jpower.jpower.module.common.utils.ExceptionUtil;
import top.jpower.jpower.module.common.utils.Fc;
import top.jpower.jpower.module.common.utils.StringUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * skywalking APM 打印请求和响应
 *
 * @author mr.g
 * @date 2022-05-26 14:52
 */
@Slf4j
@Component
@AllArgsConstructor
@EnableConfigurationProperties({SkywalkingApmProperties.class})
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class ApmHttpInfoFilter extends HttpFilter {

    private SkywalkingApmProperties apmProperties;

    private static final ImmutableSet<String> IGNORED_HEADERS;
    private static final long serialVersionUID = 3019775050229344922L;

    static {
        Set<String> ignoredHeaders = ImmutableSet.of(
                "Content-Type",
                "User-Agent",
                "Accept",
                "Cache-Control",
                "Postman-Token",
                "Host",
                "Accept-Encoding",
                "Connection",
                "Content-Length")
                .stream()
                .map(String::toUpperCase)
                .collect(Collectors.toSet());
        IGNORED_HEADERS = ImmutableSet.copyOf(ignoredHeaders);
    }

    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        if (!apmProperties.isEnable()){
            filterChain.doFilter(request, response);
            return;
        }

        if (request.isAsyncStarted()){
            filterChain.doFilter(request, response);
            return;
        }

        if (request.isAsyncStarted()){
            filterChain.doFilter(request, response);
            return;
        }

        if (excludeUrl(apmProperties.getExcludes(), request.getServletPath())){
            filterChain.doFilter(request, response);
            return;
        }

        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        try {
            filterChain.doFilter(requestWrapper, responseWrapper);
        } finally {
            try {
                //构造请求的HEADER
                StringBuilder sbHeader = new StringBuilder("");

                //构造header
                Enumeration<String> headerNames = request.getHeaderNames();
                while (headerNames.hasMoreElements()) {
                    String headerName = headerNames.nextElement();
                    if (!IGNORED_HEADERS.contains(headerName.toUpperCase())) {
                        sbHeader.append(" -H ").append(headerName).append(": ").append(request.getHeader(headerName)).append(" | ");
                    }
                }

                ActiveSpan.tag("请求头", sbHeader.toString());

                //构造请求的参数
                StringBuilder sbParam = new StringBuilder("");

                request.getParameterMap().forEach((k,v)-> sbParam.append(" -P ").append(k).append(": ").append(Arrays.toString(v)).append(" | "));

                //获取body
                String body = new String(requestWrapper.getContentAsByteArray(), StandardCharsets.UTF_8);
                if (Fc.isNotBlank(body)) {
                    sbParam.append(" -B ").append(body);
                }
                //输出到input
                ActiveSpan.tag("请求参数", sbParam.toString());

                //获取返回值body
                String responseBody = new String(responseWrapper.getContentAsByteArray(), StandardCharsets.UTF_8);
                //输出到output
                ActiveSpan.tag("响应数据", responseBody);
            } catch (Exception e) {
                log.error("fail to build http log:{}", ExceptionUtil.getStackTraceAsString(e));
            } finally {
                //这一行必须添加，否则就一直不返回
                responseWrapper.copyBodyToResponse();
            }
        }
    }

    private boolean excludeUrl(List<String> list, String url) {
        if (Fc.isEmpty(list)) {
            return false;
        }

        for (String pattern : list) {
            if (Fc.isNotBlank(pattern) && Fc.isNotBlank(url) && !Fc.equalsValue(url,"/")){
                if (StringUtil.wildcardEquals(pattern,url)){
                    return true;
                }
            }
        }

        return false;
    }
}