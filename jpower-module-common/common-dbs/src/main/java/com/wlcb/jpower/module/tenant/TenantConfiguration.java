package com.wlcb.jpower.module.tenant;

import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import com.wlcb.jpower.module.config.MybatisPlusConfig;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName TenantConfiguration
 * @Description TODO 多租户配置
 * @Author 郭丁志
 * @Date 2020-10-14 20:54
 * @Version 2.0
 */
@Configuration(proxyBeanMethods = false)
@AllArgsConstructor
@AutoConfigureBefore({MybatisPlusConfig.class})
@EnableConfigurationProperties({JpowerTenantProperties.class})
public class TenantConfiguration {

    @Bean
    @ConditionalOnMissingBean({TenantLineHandler.class})
    public TenantLineHandler tenantHandler(JpowerTenantProperties properties) {
        return new JpowerTenantHandler(properties);
    }

    @Bean
    @ConditionalOnMissingBean({TenantLineInnerInterceptor.class})
    public TenantLineInnerInterceptor tenantSqlParser(TenantLineHandler tenantHandler) {
        TenantLineInnerInterceptor innerInterceptor = new TenantLineInnerInterceptor();
        innerInterceptor.setTenantLineHandler(tenantHandler);
        return innerInterceptor;
    }
}
