package top.jpower.jpower.config;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.jpower.jpower.module.common.utils.ShieldUtil;
import top.jpower.jpower.sentinel.handler.OriginParserHandler;
import top.jpower.jpower.sentinel.handler.UrlBlockHandler;
import top.jpower.jpower.sentinel.handler.UrlCleanerHandler;

/**
 * @author goo
 * @description sentinel 配置
 * @date 2021-03-10 11:39
 */
@Configuration(proxyBeanMethods = false)
public class JpowerFeignConfiguration {

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
