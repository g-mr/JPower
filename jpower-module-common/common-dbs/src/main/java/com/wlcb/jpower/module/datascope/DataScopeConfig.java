package com.wlcb.jpower.module.datascope;

import com.baomidou.mybatisplus.extension.plugins.handler.DataPermissionHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.DataPermissionInterceptor;
import com.github.pagehelper.autoconfigure.PageHelperProperties;
import com.wlcb.jpower.module.config.MybatisPlusConfig;
import com.wlcb.jpower.module.datascope.handler.DataScopeHandler;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName 数据权限配置
 * @Description TODO mybatis配置
 * @Author 郭丁志
 * @Date 2020-07-03 11:47
 * @Version 2.0
 */
@Configuration(proxyBeanMethods = false)
@AutoConfigureBefore({MybatisPlusConfig.class})
public class DataScopeConfig {


    @Bean("dataScopeHandler")
    @ConditionalOnMissingBean({DataPermissionHandler.class})
    public DataPermissionHandler dataScopeHandler() {
        return new DataScopeHandler();
    }

    /**
     * 配置数据权限拦截器
     **/
    @Bean
    @ConditionalOnProperty(value = {"jpower.datascope.enable"}, matchIfMissing = true)
    @ConditionalOnBean(DataPermissionHandler.class)
    @ConditionalOnMissingBean({DataPermissionInterceptor.class})
    public DataPermissionInterceptor dataScopeQueryInterceptor(PageHelperProperties properties, DataPermissionHandler dataPermissionHandler) {
        properties.setDialect(JpowerPageHelper.class.getName());
        properties.put("dataScopeHandler",dataPermissionHandler);
        return new DataPermissionInterceptor(dataPermissionHandler);
    }
}
