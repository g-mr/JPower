package top.jpower.jpower.module.tenant;

import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.jpower.jpower.module.config.MybatisPlusConfig;

/**
 * 多租户配置
 *
 * @author 郭丁志
 * @date 2020-10-14 20:54
 */
@Configuration(proxyBeanMethods = false)
@AutoConfigureBefore({MybatisPlusConfig.class})
@EnableConfigurationProperties({JpowerTenantProperties.class})
public class TenantConfiguration {

    @Bean
    @ConditionalOnMissingBean({TenantLineHandler.class})
    public TenantLineHandler tenantHandler(JpowerTenantProperties properties) {
        return new JpowerTenantHandler(properties);
    }

    @Bean
    @ConditionalOnProperty(value = {"jpower.tenant.enable"}, matchIfMissing = true)
    @ConditionalOnMissingBean({InsertBatchSomeColumnTenant.class})
    public InsertBatchSomeColumnTenant insertBatchSomeColumnTenant(TenantLineHandler tenantHandler) {
        return new InsertBatchSomeColumnTenant(tenantHandler);
    }

    @Bean
    @ConditionalOnProperty(value = {"jpower.tenant.enable"}, matchIfMissing = true)
    @ConditionalOnMissingBean({TenantLineInnerInterceptor.class})
    public TenantLineInnerInterceptor tenantSqlParser(TenantLineHandler tenantHandler) {
        TenantLineInnerInterceptor innerInterceptor = new TenantLineInnerInterceptor();
        innerInterceptor.setTenantLineHandler(tenantHandler);
        return innerInterceptor;
    }
}
