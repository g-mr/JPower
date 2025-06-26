package top.jpower.core.feign.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import top.jpower.core.util.constants.JpowerConstants;
import top.jpower.core.util.constants.TokenConstant;
import top.jpower.core.util.utils.WebUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * @author mr.g
 * @date 2022-04-22 09:31
 */

@Slf4j
@AutoConfiguration
public class RestTemplateConfig {

    @Bean
    @LoadBalanced
    @ConditionalOnMissingBean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {

        return builder.additionalInterceptors((request, body, execution) -> {

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
        }).build();
    }

}
