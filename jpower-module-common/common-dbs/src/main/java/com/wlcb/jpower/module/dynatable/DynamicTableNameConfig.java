package com.wlcb.jpower.module.dynatable;

import com.baomidou.mybatisplus.extension.plugins.handler.TableNameHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.DynamicTableNameInnerInterceptor;
import com.wlcb.jpower.module.config.MybatisPlusConfig;
import com.wlcb.jpower.module.dynatable.handler.DynamicTableNameHandler;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 动态表名配置
 * @author mr.g
 * @date 2021-11-21 19:22
 */
@Configuration(proxyBeanMethods = false)
@AutoConfigureBefore({MybatisPlusConfig.class})
public class DynamicTableNameConfig {

    @Bean
    @ConditionalOnMissingBean({TableNameHandler.class})
    public TableNameHandler dataScopeHandler() {
        return new DynamicTableNameHandler();
    }

    /**
     * 配置数据权限拦截器
     **/
    @Bean
    @ConditionalOnProperty(value = {"jpower.mybatis.dynamicTableName"}, matchIfMissing = false)
    @ConditionalOnBean(TableNameHandler.class)
    @ConditionalOnMissingBean({DynamicTableNameInnerInterceptor.class})
    public DynamicTableNameInnerInterceptor dataScopeQueryInterceptor(TableNameHandler tableNameHandler) {
        return new DynamicTableNameInnerInterceptor(tableNameHandler);
    }
}
