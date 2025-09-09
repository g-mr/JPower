package top.jpower.core.feign.config;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import top.jpower.core.auth.utils.ShieldUtil;
import top.jpower.core.feign.config.client.FeignClientProxyFactory;
import top.jpower.core.feign.config.client.JpowerModeFeignConfiguration;
import top.jpower.core.feign.sentinel.handler.OriginParserHandler;
import top.jpower.core.feign.sentinel.handler.UrlBlockHandler;
import top.jpower.core.feign.sentinel.handler.UrlCleanerHandler;

/**
 * feign sentinel配置
 *
 * @author goo
 * @date 2021-03-10 11:39
 */
@AutoConfiguration
@EnableFeignClients(defaultConfiguration = JpowerModeFeignConfiguration.class)
public class JpowerFeignConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public FeignClientProxyFactory feignClientProxyFactory(ApplicationContext applicationContext) {
        return new FeignClientProxyFactory(applicationContext);
    }

    @Bean
    @ConditionalOnMissingBean
    public BlockExceptionHandler blockExceptionHandler() {
        return new UrlBlockHandler();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnClass(ShieldUtil.class)
    public OriginParserHandler originParserHandler() {
        return new OriginParserHandler();
    }

    @Bean
    @ConditionalOnMissingBean
    public UrlCleanerHandler urlCleanerHandler() {
        return new UrlCleanerHandler();
    }

}
