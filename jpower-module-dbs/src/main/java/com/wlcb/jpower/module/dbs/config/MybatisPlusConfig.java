package com.wlcb.jpower.module.dbs.config;

import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.pagination.optimize.JsqlParserCountOptimize;
import com.wlcb.jpower.module.mp.CustomSqlInjector;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @ClassName MybatisPlusConfig
 * @Description TODO mybatis配置
 * @Author 郭丁志
 * @Date 2020-07-03 11:47
 * @Version 1.0
 */
@EnableTransactionManagement
@Configuration
//@MapperScan(basePackages = "com.wlcb.*.*.dbs.dao")
public class MybatisPlusConfig {

    /**
     * @Author 郭丁志
     * @Description //TODO 配置公用字段
     * @Date 17:48 2020-07-09
     * @Param []
     * @return com.baomidou.mybatisplus.core.config.GlobalConfig
     **/
    @Bean
    public GlobalConfig globalConfig() {
        GlobalConfig globalConfig = new GlobalConfig();
        globalConfig.setMetaObjectHandler(new UpdateRelatedFieldsMetaHandler());
        return globalConfig;
    }

    @Bean
    public OptimisticLockerInterceptor optimisticLockerInterceptor(){
        OptimisticLockerInterceptor interceptor = new OptimisticLockerInterceptor();
        return interceptor;
    }

    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer() {
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setBasePackage("com.wlcb.**.dbs.dao");
        return mapperScannerConfigurer;
    }

    /**
     * 分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        // todo 开启 count 的 join 优化,只针对部分 left join;
                // mp在join的情况下如果存在放大记录数的情况，mp的分页会出现问题，所以在join的情况下分页最好自己写sql，使用PageHelper进行分页
        paginationInterceptor.setCountSqlParser(new JsqlParserCountOptimize(true));
        return paginationInterceptor;
    }

    @Bean
    public ISqlInjector sqlInjector() {
        return new CustomSqlInjector();
    }

}
