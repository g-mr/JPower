package com.wlcb.jpower.module.config;

import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import com.wlcb.jpower.module.common.deploy.property.YamlAndPropertySourceFactory;
import com.wlcb.jpower.module.config.interceptor.DemoInterceptor;
import com.wlcb.jpower.module.config.interceptor.MybatisSqlPrintInterceptor;
import com.wlcb.jpower.module.config.properties.DemoProperties;
import com.wlcb.jpower.module.config.properties.MybatisProperties;
import com.wlcb.jpower.module.datascope.interceptor.DataScopeInterceptor;
import com.wlcb.jpower.module.mp.CustomSqlInjector;
import com.wlcb.jpower.module.tenant.JpowerTenantProperties;
import lombok.AllArgsConstructor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @ClassName MybatisPlusConfig
 * @Description TODO mybatis配置
 * @Author 郭丁志
 * @Date 2020-07-03 11:47
 * @Version 2.0
 */
@EnableTransactionManagement
@AllArgsConstructor
@MapperScan("com.wlcb.**.dbs.dao.**")
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({DemoProperties.class, MybatisProperties.class})
@PropertySource(value = "classpath:./jpower-db.yml",factory = YamlAndPropertySourceFactory.class)
public class MybatisPlusConfig {

    /**
     * 配置公用字段
     **/
    @Bean
    public GlobalConfig globalConfig() {
        GlobalConfig globalConfig = new GlobalConfig();
        globalConfig.setMetaObjectHandler(new UpdateRelatedFieldsMetaHandler());
        return globalConfig;
    }

    @Bean
    @ConditionalOnMissingBean({MybatisPlusInterceptor.class})
    public MybatisPlusInterceptor mybatisPlusInterceptor(DataScopeInterceptor dataScopeInterceptor,
                                                         TenantLineInnerInterceptor tenantLineInnerInterceptor,
                                                         JpowerTenantProperties tenantProperties,
                                                         DemoProperties demoProperties,
                                                         MybatisProperties mybatisProperties) {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 乐观锁插件
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        // 分页插件
        JpowerPaginationInterceptor paginationInterceptor = new JpowerPaginationInterceptor();
        paginationInterceptor.setQueryInterceptor(dataScopeInterceptor);
        paginationInterceptor.setOverflow(mybatisProperties.isOverflow());
        paginationInterceptor.setMaxLimit(mybatisProperties.getMaxLimit());
        paginationInterceptor.setOptimizeJoin(mybatisProperties.isOptimizeJoin());
        interceptor.addInnerInterceptor(paginationInterceptor);
        // 多租户插件
        if (tenantProperties.getEnable()){
            interceptor.addInnerInterceptor(tenantLineInnerInterceptor);
        }
        //演示环境
        if (demoProperties.getEnable()){
            interceptor.addInnerInterceptor(new DemoInterceptor(demoProperties));
        }
        return interceptor;
    }

    /**
     * 该属性会在旧插件移除后一同移除
     */
    @Bean
    public ConfigurationCustomizer configurationCustomizer() {
        return configuration -> configuration.setUseDeprecatedExecutor(false);
    }

    @Bean
    @ConditionalOnMissingBean
    public ISqlInjector sqlInjector() {
        return new CustomSqlInjector();
    }

    /**
     * sql打印
     **/
    @Order(Ordered.HIGHEST_PRECEDENCE)
    @Bean
    @ConditionalOnProperty(value = {"jpower.mybatis.sql.print"}, matchIfMissing = true)
    public MybatisSqlPrintInterceptor mybatisSqlPrintIntercepter(MybatisProperties mybatisProperties) {
        return new MybatisSqlPrintInterceptor(mybatisProperties.getSql());
    }

}
