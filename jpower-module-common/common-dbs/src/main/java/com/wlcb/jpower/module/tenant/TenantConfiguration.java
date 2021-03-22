package com.wlcb.jpower.module.tenant;

import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.baomidou.mybatisplus.extension.plugins.tenant.TenantHandler;
import com.baomidou.mybatisplus.extension.plugins.tenant.TenantSqlParser;
import com.wlcb.jpower.module.config.MybatisPlusConfig;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName TenantConfiguration
 * @Description TODO 多租户配置
 * @Author 郭丁志
 * @Date 2020-10-14 20:54
 * @Version 1.0
 */
@Configuration(proxyBeanMethods = false)
@AllArgsConstructor
@AutoConfigureBefore({MybatisPlusConfig.class})
@EnableConfigurationProperties({JpowerTenantProperties.class})
public class TenantConfiguration {

    /**
     * 多租户配置类
     */
    private final JpowerTenantProperties properties;

//    @Bean
//    @ConditionalOnMissingBean({TenantHandler.class})
//    public TenantHandler tenantHandler(JpowerTenantProperties properties) {
//        return new JpowerTenantHandler(properties);
//    }
//
//    @Bean
//    @ConditionalOnProperty(value = {"jpower.tenant.enable"},matchIfMissing = true)
//    public TenantSqlParser tenantSqlParser(TenantHandler tenantHandler) {
//        TenantSqlParser tenantSqlParser = new TenantSqlParser();
//        tenantSqlParser.setTenantHandler(tenantHandler);
//        return tenantSqlParser;
//    }

    /**
     * 自定义租户处理器
     *
     * @return TenantHandler
     */
    @Bean
    @ConditionalOnMissingBean(TenantLineHandler.class)
    @ConditionalOnProperty(value = {"jpower.tenant.enable"},matchIfMissing = true)
    public TenantLineHandler bladeTenantHandler() {
        return new JpowerTenantHandler(properties);
    }

    /**
     * 自定义租户id生成器
     *
     * @return TenantId
     */
    @Bean
    @ConditionalOnMissingBean(TenantConstant.class)
    public TenantConstant tenantId() {
        return new TenantId();
    }

}
