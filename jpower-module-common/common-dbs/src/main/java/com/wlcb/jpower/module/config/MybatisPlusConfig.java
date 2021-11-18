package com.wlcb.jpower.module.config;

import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.*;
import com.wlcb.jpower.module.common.deploy.property.YamlAndPropertySourceFactory;
import com.wlcb.jpower.module.config.interceptor.DemoInterceptor;
import com.wlcb.jpower.module.config.interceptor.JpowerMybatisInterceptor;
import com.wlcb.jpower.module.config.interceptor.MybatisSqlPrintInterceptor;
import com.wlcb.jpower.module.config.interceptor.chain.MybatisInterceptor;
import com.wlcb.jpower.module.config.properties.DemoProperties;
import com.wlcb.jpower.module.config.properties.MybatisProperties;
import com.wlcb.jpower.module.mp.CustomSqlInjector;
import lombok.AllArgsConstructor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.stream.Collectors;

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

    @Bean
    @ConditionalOnMissingBean
    public ISqlInjector sqlInjector() {
        return new CustomSqlInjector();
    }

    /**
     * 全局配置
     **/
    @Bean
    public GlobalConfig globalConfig(UpdateRelatedFieldsMetaHandler metaHandler,
                                     ISqlInjector sqlInjector) {
        GlobalConfig globalConfig = new GlobalConfig();
        globalConfig.setMetaObjectHandler(metaHandler);
        globalConfig.setSqlInjector(sqlInjector);
        return globalConfig;
    }

    @Bean
    @ConditionalOnMissingBean({MybatisPlusInterceptor.class})
    public MybatisPlusInterceptor mybatisPlusInterceptor(@Autowired(required = false) DataPermissionInterceptor dataScopeInterceptor,
                                                         @Autowired(required = false) TenantLineInnerInterceptor tenantLineInnerInterceptor,
//                                                         ObjectProvider<InnerInterceptor> innerInterceptors,
                                                         DemoProperties demoProperties,
                                                         MybatisProperties mybatisProperties) {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 乐观锁插件
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());

        // 多租户插件
        if (tenantLineInnerInterceptor != null){
            interceptor.addInnerInterceptor(tenantLineInnerInterceptor);
        }

        //数据权限插件
        if (dataScopeInterceptor != null){
            //数据权限要放在分页插件之前，否则count数量不准
            interceptor.addInnerInterceptor(dataScopeInterceptor);
        }


        //演示环境
        if (demoProperties.getEnable()){
            interceptor.addInnerInterceptor(new DemoInterceptor(demoProperties));
        }

        // 分页插件
        PaginationInnerInterceptor paginationInterceptor = new PaginationInnerInterceptor();
        paginationInterceptor.setOverflow(mybatisProperties.isOverflow());
        paginationInterceptor.setMaxLimit(mybatisProperties.getMaxLimit());
        paginationInterceptor.setOptimizeJoin(mybatisProperties.isOptimizeJoin());
        interceptor.addInnerInterceptor(paginationInterceptor);
        return interceptor;
    }

    @Bean
    @ConditionalOnMissingBean({JpowerMybatisInterceptor.class})
    public JpowerMybatisInterceptor jpowerMybatisInterceptor(ObjectProvider<MybatisInterceptor> mybatisInterceptors) {
        return new JpowerMybatisInterceptor(mybatisInterceptors.orderedStream().collect(Collectors.toList()));
    }

    /**
     * 该属性会在旧插件移除后一同移除
     *  避免缓存出现问题
     */
    @Bean
    public ConfigurationCustomizer configurationCustomizer() {
        return configuration -> configuration.setUseDeprecatedExecutor(false);
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
