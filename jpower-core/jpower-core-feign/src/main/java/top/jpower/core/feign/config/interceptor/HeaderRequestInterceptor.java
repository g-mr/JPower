package top.jpower.core.feign.config.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import top.jpower.core.util.constants.JpowerConstants;
import top.jpower.core.util.constants.TokenConstant;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * feign调用时传递header
 *
 * @author mr.g
 **/
@AutoConfiguration
@Slf4j
public class HeaderRequestInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null){
            HttpServletRequest request = attributes.getRequest();
            Enumeration<String> headerNames = request.getHeaderNames();
            if (headerNames != null) {
                while (headerNames.hasMoreElements()) {
                    String name = headerNames.nextElement();
                    if (name.equalsIgnoreCase("Authorization")
                            || name.equalsIgnoreCase("User-Type")
                            || name.equalsIgnoreCase(JpowerConstants.HEADER_MENU)
                            || name.equalsIgnoreCase(JpowerConstants.AUTH_HEADER)
                            || name.equalsIgnoreCase(JpowerConstants.HEADER_TENANT)
                            || name.equalsIgnoreCase(TokenConstant.DATA_SCOPE_NAME)
                            || name.equalsIgnoreCase(TokenConstant.PASS_HEADER_NAME)){
                        String values = request.getHeader(name);
                        requestTemplate.header(name, values);
                    }
                }
            }
            if (!requestTemplate.queries().containsKey(TokenConstant.TENANT_CODE)){
                requestTemplate.query(TokenConstant.TENANT_CODE,request.getParameter(TokenConstant.TENANT_CODE));
            }
        }
    }

}
