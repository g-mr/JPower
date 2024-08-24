package top.jpower.core.boot.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import top.jpower.core.boot.xss.XssFilter;
import top.jpower.core.boot.xss.XssProperties;

/**
 * @author mr.g
 * @date 2024-8-24 21:38
 * @description
 */
@AutoConfiguration
@EnableConfigurationProperties({XssProperties.class})
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class XssConfig {

    @Bean
    @ConditionalOnMissingBean
    public XssFilter xssFilter(XssProperties xssProperties){
        return new XssFilter(xssProperties);
    }

}
