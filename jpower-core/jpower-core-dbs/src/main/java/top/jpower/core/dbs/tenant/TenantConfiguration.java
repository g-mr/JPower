package top.jpower.core.dbs.tenant;

import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import top.jpower.core.dbs.config.MybatisPlusConfig;
import top.jpower.core.util.user.UserConfig;

/**
 * 多租户配置
 *
 * @author 郭丁志
 * @date 2020-10-14 20:54
 */
@AutoConfiguration
@AutoConfigureBefore({MybatisPlusConfig.class})
@AutoConfigureAfter(UserConfig.class)
@EnableConfigurationProperties({JpowerTenantProperties.class})
public class TenantConfiguration {

    @Bean
    @ConditionalOnMissingBean({TenantLineHandler.class})
    @ConditionalOnBean(UserConfig.class)
    public TenantLineHandler tenantHandler(JpowerTenantProperties properties, UserConfig userConfig) {
        return new JpowerTenantHandler(properties, userConfig);
    }

    @Bean
    @ConditionalOnBean(TenantLineHandler.class)
    @ConditionalOnMissingBean({InsertBatchSomeColumnTenant.class})
    public InsertBatchSomeColumnTenant insertBatchSomeColumnTenant(TenantLineHandler tenantHandler) {
        return new InsertBatchSomeColumnTenant(tenantHandler);
    }

    @Bean
    @ConditionalOnBean(TenantLineHandler.class)
    @ConditionalOnMissingBean({TenantLineInnerInterceptor.class})
    @ConditionalOnProperty(value = {"jpower.tenant.enable"}, matchIfMissing = true)
    public TenantLineInnerInterceptor tenantSqlParser(TenantLineHandler tenantHandler) {
        TenantLineInnerInterceptor innerInterceptor = new TenantLineInnerInterceptor();
        innerInterceptor.setTenantLineHandler(tenantHandler);
        return innerInterceptor;
    }
}
