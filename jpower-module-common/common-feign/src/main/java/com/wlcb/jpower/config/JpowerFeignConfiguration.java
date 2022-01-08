package com.wlcb.jpower.config;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.wlcb.jpower.sentinel.handler.OriginParserHandler;
import com.wlcb.jpower.sentinel.handler.UrlBlockHandler;
import com.wlcb.jpower.sentinel.handler.UrlCleanerHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
    public OriginParserHandler originParserHandler() {
        return new OriginParserHandler();
    }

    @Bean
    @ConditionalOnMissingBean
    public UrlCleanerHandler urlCleanerHandler() {
        return new UrlCleanerHandler();
    }

}
