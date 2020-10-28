package com.wlcb.jpower.module.tenant;

import com.baomidou.mybatisplus.extension.plugins.tenant.TenantHandler;
import com.baomidou.mybatisplus.extension.plugins.tenant.TenantSqlParser;
import com.wlcb.jpower.module.config.MybatisPlusConfig;
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
@Configuration
@AutoConfigureBefore({MybatisPlusConfig.class})
@EnableConfigurationProperties({JpowerTenantProperties.class})
public class TenantConfiguration {


    @Bean
    @ConditionalOnMissingBean({TenantHandler.class})
    public TenantHandler tenantHandler(JpowerTenantProperties properties) {
        return new JpowerTenantHandler(properties);
    }

    @Bean
    @ConditionalOnProperty(value = {"jpower.tenant.enable"},matchIfMissing = true)
    public TenantSqlParser tenantSqlParser(TenantHandler tenantHandler) {
        TenantSqlParser tenantSqlParser = new TenantSqlParser();
        tenantSqlParser.setTenantHandler(tenantHandler);
        return tenantSqlParser;
    }

}
