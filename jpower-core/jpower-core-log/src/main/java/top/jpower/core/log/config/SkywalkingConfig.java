package top.jpower.core.log.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import top.jpower.core.log.apm.SkywalkingApmProperties;
import top.jpower.core.log.apm.SkywalkingHttpInfoFilter;

import jakarta.servlet.http.HttpFilter;

/**
 * @author mr.g
 * @date 2024-8-24 18:50
 * @description
 */
@AutoConfiguration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@EnableConfigurationProperties({SkywalkingApmProperties.class})
public class SkywalkingConfig {

    /**
     * skywalking日志打印
     *
     * @author mr.g
     * @param skywalkingApmProperties 配置
     * @return jakarta.servlet.http.HttpFilter 过滤器
     **/
    @Bean
    @ConditionalOnMissingBean
    public HttpFilter httpFilter(SkywalkingApmProperties skywalkingApmProperties) {
        return new SkywalkingHttpInfoFilter(skywalkingApmProperties);
    }

}
