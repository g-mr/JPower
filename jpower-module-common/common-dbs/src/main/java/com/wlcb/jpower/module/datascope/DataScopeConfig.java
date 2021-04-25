package com.wlcb.jpower.module.datascope;

import com.github.pagehelper.autoconfigure.PageHelperProperties;
import com.wlcb.jpower.module.datascope.handler.DataScopeHandler;
import com.wlcb.jpower.module.datascope.interceptor.DataScopeInterceptor;
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
public class DataScopeConfig {


    @Bean("dataScopeHandler")
    @ConditionalOnMissingBean({DataScopeHandler.class})
    public DataScopeHandler dataScopeHandler() {
        return new DataScopeHandler();
    }

    /**
     * 配置数据权限拦截器
     **/
    @Bean
    @ConditionalOnProperty(value = {"jpower.datascope.enable"}, matchIfMissing = true)
    @ConditionalOnBean(DataScopeHandler.class)
    @ConditionalOnMissingBean({DataScopeInterceptor.class})
    public DataScopeInterceptor dataScopeQueryInterceptor(PageHelperProperties properties,DataScopeHandler dataScopeHandler) {
        properties.setDialect(JpowerPageHelper.class.getName());
        properties.getProperties().setProperty("dataScopeHandler","dataScopeHandler");
        return new DataScopeInterceptor(dataScopeHandler);
    }
}
