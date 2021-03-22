package com.wlcb.jpower.module.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.core.parser.ISqlParser;
import com.baomidou.mybatisplus.core.parser.ISqlParserFilter;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.pagination.optimize.JsqlParserCountOptimize;
import com.wlcb.jpower.module.common.utils.ObjectUtil;
import com.wlcb.jpower.module.datascope.interceptor.DataScopeInterceptor;
import com.wlcb.jpower.module.mp.CustomSqlInjector;
import lombok.AllArgsConstructor;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Arrays;

/**
 * @ClassName MybatisPlusConfig
 * @Description TODO mybatis配置
 * @Author 郭丁志
 * @Date 2020-07-03 11:47
 * @Version 1.0
 */
@EnableTransactionManagement
@AllArgsConstructor
@Configuration
public class MybatisPlusConfig {

    private final TenantLineHandler tenantLineHandler;

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

    /**
     * 分页插件
     */
//    @Bean
//    public PaginationInterceptor paginationInterceptor(ObjectProvider<ISqlParser[]> sqlParsers, ObjectProvider<ISqlParserFilter> sqlParserFilter) {
//        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
//
//        ISqlParser[] sqlParsersArray = sqlParsers.getIfAvailable();
//        if (ObjectUtil.isNotEmpty(sqlParsersArray)) {
//            paginationInterceptor.setSqlParserList(Arrays.asList(sqlParsersArray));
//        }
//
//        paginationInterceptor.setSqlParserFilter(sqlParserFilter.getIfAvailable());
//        paginationInterceptor.setCountSqlParser(new JsqlParserCountOptimize(true));
//        return paginationInterceptor;
//    }

    @Bean
    public MybatisPlusInterceptor paginationInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 乐观锁插件
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        // 分页插件 DbType：数据库类型(根据类型获取应使用的分页方言)
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        // 多租户
        interceptor.addInnerInterceptor(new TenantLineInnerInterceptor(tenantLineHandler));
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
