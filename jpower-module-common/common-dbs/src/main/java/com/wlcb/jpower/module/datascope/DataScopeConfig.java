package com.wlcb.jpower.module.datascope;

import com.github.pagehelper.autoconfigure.PageHelperProperties;
import com.wlcb.jpower.module.datascope.interceptor.DataScopeQueryInterceptor;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName MybatisPlusConfig
 * @Description TODO mybatis配置
 * @Author 郭丁志
 * @Date 2020-07-03 11:47
 * @Version 2.0
 */
@Configuration(proxyBeanMethods = false)
//@AllArgsConstructor
public class DataScopeConfig {

//    private PageHelperProperties properties;


//    @Bean
//    @ConditionalOnProperty(value = {"jpower.datascope.enable"}, matchIfMissing = true)
//    @ConditionalOnMissingBean({DataScopeQueryInterceptor.class})
//    public DataScopeQueryInterceptor dataScopeQueryInterceptor() {
//        return new DataScopeQueryInterceptor();
//    }

    /**
     * 配置公用字段
     **/
    @Bean
    @ConditionalOnProperty(value = {"jpower.datascope.enable"}, matchIfMissing = true)
    @ConditionalOnMissingBean({DataScopeQueryInterceptor.class})
    public DataScopeQueryInterceptor dataScopeQueryInterceptor(PageHelperProperties properties) {

        properties.setDialect("asdasd");

        return new DataScopeQueryInterceptor();
    }

}
