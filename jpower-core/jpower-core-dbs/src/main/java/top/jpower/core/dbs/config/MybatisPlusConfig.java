package top.jpower.core.dbs.config;

import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.*;
import org.apache.ibatis.logging.nologging.NoLoggingImpl;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import top.jpower.core.dbs.config.interceptor.DemoInterceptor;
import top.jpower.core.dbs.config.interceptor.JpowerMybatisInterceptor;
import top.jpower.core.dbs.config.interceptor.MybatisSqlPrintInterceptor;
import top.jpower.core.dbs.config.interceptor.chain.MybatisInterceptor;
import top.jpower.core.dbs.config.properties.DemoProperties;
import top.jpower.core.dbs.config.properties.MybatisProperties;
import top.jpower.core.dbs.mp.CustomSqlInjector;
import top.jpower.core.dbs.tenant.JpowerTenantProperties;
import top.jpower.core.deploy.support.YamlAndPropertySourceFactory;
import top.jpower.core.util.user.UserConfig;
import top.jpower.core.util.utils.Fc;

import java.util.stream.Collectors;

/**
 * MybatisPlus 配置
 *
 * @author mr.g
 */
@AutoConfiguration(before = MybatisPlusAutoConfiguration.class)
@EnableTransactionManagement
@EnableConfigurationProperties({DemoProperties.class, MybatisProperties.class})
@PropertySource(value = "classpath:./jpower-db.yml",factory = YamlAndPropertySourceFactory.class)
public class MybatisPlusConfig {

    /**
     * 关闭MyBatis日志
     *
     * @author mr.g
     * @return com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer
     **/
   @Bean
   public ConfigurationCustomizer mybatisConfigurationCustomizer() {
       return configuration -> {
           configuration.setLogImpl(NoLoggingImpl.class);
       };
   }

    @Bean
    @ConditionalOnMissingBean
    public ISqlInjector sqlInjector(@Autowired(required = false) JpowerTenantProperties tenantProperties) {
        return new CustomSqlInjector(tenantProperties);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(UserConfig.class)
    public MetaObjectHandler metaObjectHandler(UserConfig userConfig){
        return new UpdateRelatedFieldsMetaHandler(userConfig);
    }

    /**
     * 全局配置
     **/
    @Bean
    @ConditionalOnMissingBean
    public GlobalConfig globalConfig(@Autowired(required = false) MetaObjectHandler metaHandler, ISqlInjector sqlInjector) {
        GlobalConfig globalConfig = new GlobalConfig();
        if (Fc.notNull(metaHandler)){
            globalConfig.setMetaObjectHandler(metaHandler);
        }
        globalConfig.setSqlInjector(sqlInjector);
        return globalConfig;
    }

    @Bean
    @ConditionalOnMissingBean({MybatisPlusInterceptor.class})
    public MybatisPlusInterceptor mybatisPlusInterceptor(@Autowired(required = false) DataPermissionInterceptor dataPermissionInterceptor,
                                                         @Autowired(required = false) TenantLineInnerInterceptor tenantLineInnerInterceptor,
                                                         @Autowired(required = false) DynamicTableNameInnerInterceptor dynamicTableNameInnerInterceptor,
                                                         ObjectProvider<InnerInterceptor> innerInterceptors,
                                                         DemoProperties demoProperties,
                                                         MybatisProperties mybatisProperties) {

        // TODO: 2021/11/22 0022 拦截器顺序最好不要改变

        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

        // 多租户插件
        if (tenantLineInnerInterceptor != null){
            interceptor.addInnerInterceptor(tenantLineInnerInterceptor);
        }

        //数据权限插件
        if (dataPermissionInterceptor != null){
            interceptor.addInnerInterceptor(dataPermissionInterceptor);
        }

        // 动态表名插件
        if (dynamicTableNameInnerInterceptor != null){
            interceptor.addInnerInterceptor(dynamicTableNameInnerInterceptor);
        }


        // 占位符替换插件（暂不加入）
//        interceptor.addInnerInterceptor(new ReplacePlaceholderInnerInterceptor());


        // 乐观锁插件
        if (mybatisProperties.isOptimisticLocker()){
            interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        }

        // 分页插件
        PaginationInnerInterceptor paginationInterceptor = new PaginationInnerInterceptor();
        paginationInterceptor.setOverflow(mybatisProperties.getPage().isOverflow());
        paginationInterceptor.setMaxLimit(mybatisProperties.getPage().getMaxLimit());
        paginationInterceptor.setOptimizeJoin(mybatisProperties.getPage().isOptimizeJoin());
        interceptor.addInnerInterceptor(paginationInterceptor);


        // 攻击SQL拦截,防止全表更新与删除
        if (mybatisProperties.isBlockAttack()){
            interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());
        }
        // 垃圾SQL拦截插件
        if (mybatisProperties.isIllegalSQL()){
            interceptor.addInnerInterceptor(new IllegalSQLInnerInterceptor());
        }
        //演示环境
        if (demoProperties.isEnable()){
            interceptor.addInnerInterceptor(new DemoInterceptor(demoProperties));
        }


        innerInterceptors.orderedStream().collect(Collectors.toList()).forEach(innerInterceptor -> {
            if (!interceptor.getInterceptors().contains(innerInterceptor)){
                interceptor.addInnerInterceptor(innerInterceptor);
            }
        });

        return interceptor;
    }

    @Bean
    @ConditionalOnMissingBean({JpowerMybatisInterceptor.class})
    public JpowerMybatisInterceptor jpowerMybatisInterceptor(ObjectProvider<MybatisInterceptor> mybatisInterceptors) {
        return new JpowerMybatisInterceptor(mybatisInterceptors.orderedStream().collect(Collectors.toList()));
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
