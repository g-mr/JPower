package top.jpower.core.dbs.tenant;

import com.mybatisflex.core.tenant.TenantFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import top.jpower.core.dbs.config.MybatisFlexConfig;
import top.jpower.core.util.user.UserConfig;

/**
 * 多租户配置
 *
 * @author 郭丁志
 * @date 2020-10-14 20:54
 */
@AutoConfiguration
@AutoConfigureBefore({MybatisFlexConfig.class})
@AutoConfigureAfter(UserConfig.class)
@EnableConfigurationProperties({JpowerTenantProperties.class})
@ConditionalOnProperty(value = {"jpower.tenant.enable"}, matchIfMissing = true)
public class TenantConfiguration {

    @Bean
    @ConditionalOnMissingBean({TenantFactory.class})
    @ConditionalOnBean(UserConfig.class)
    public TenantFactory tenantHandler(JpowerTenantProperties properties, UserConfig userConfig) {
        return new JpowerTenantHandler(properties, userConfig);
    }

}
