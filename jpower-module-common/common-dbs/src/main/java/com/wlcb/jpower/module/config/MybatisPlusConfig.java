package com.wlcb.jpower.module.config;

import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import com.wlcb.jpower.module.datascope.interceptor.DataScopeInterceptor;
import com.wlcb.jpower.module.mp.CustomSqlInjector;
import com.wlcb.jpower.module.tenant.JpowerTenantProperties;
import lombok.AllArgsConstructor;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
@Configuration(proxyBeanMethods = false)
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

    /**
     * mapper扫描
     */
    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer() {
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setBasePackage("com.wlcb.**.dbs.dao");
        return mapperScannerConfigurer;
    }

    @Bean
    @ConditionalOnMissingBean({MybatisPlusInterceptor.class})
    public MybatisPlusInterceptor mybatisPlusInterceptor(TenantLineInnerInterceptor tenantLineInnerInterceptor, JpowerTenantProperties tenantProperties) {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 乐观锁插件
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        // 分页插件
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        // 多租户插件
        if (tenantProperties.getEnable()){
            interceptor.addInnerInterceptor(tenantLineInnerInterceptor);
        }
        return interceptor;
    }

    @Bean
    @ConditionalOnMissingBean
    public ISqlInjector sqlInjector() {
        return new CustomSqlInjector();
    }

    /**
     * 数据权限插件
     **/
    @Order(10)
    @Bean
    @ConditionalOnProperty(value = {"jpower.datascope.enable"}, matchIfMissing = true)
    public DataScopeInterceptor dataScopeInterceptor() {
        return new DataScopeInterceptor();
    }

    /**
     * 演示环境
     **/
    @Order(11)
    @Bean
    @ConditionalOnProperty(value = {"jpower.demo.enable"}, matchIfMissing = false)
    public DemoInterceptor demoInterceptor() {
        return new DemoInterceptor();
    }

}
