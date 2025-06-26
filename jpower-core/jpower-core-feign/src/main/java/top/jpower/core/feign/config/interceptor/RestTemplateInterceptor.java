package top.jpower.core.feign.config.interceptor;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import top.jpower.core.util.constants.JpowerConstants;
import top.jpower.core.util.constants.TokenConstant;
import top.jpower.core.util.utils.WebUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Enumeration;

/**
 * @author mr.g
 * @date 2025-6-26 23:18
 * @description
 */
public class RestTemplateInterceptor implements InitializingBean {

    @Autowired(required = false)
    private Collection<RestTemplate> restTemplates;

    @Override
    public void afterPropertiesSet() {
        restTemplates.forEach(restTemplate -> {
            restTemplate.getInterceptors().add((request, body, execution) -> {

                HttpServletRequest servletRequest = WebUtil.getRequest();
                if (servletRequest != null){
                    Enumeration<String> headerNames = servletRequest.getHeaderNames();
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

                                String values = servletRequest.getHeader(name);
                                request.getHeaders().add(name, values);

                            }
                        }
                    }
                }

                return execution.execute(request, body);
            });
        });
    }

}
