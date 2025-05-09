package top.jpower.core.dbs.config;

import cn.hutool.core.collection.ListUtil;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.*;
import lombok.AllArgsConstructor;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.StringUtils;
import top.jpower.core.dbs.config.interceptor.DemoInterceptor;
import top.jpower.core.dbs.config.interceptor.JpowerMybatisInterceptor;
import top.jpower.core.dbs.config.interceptor.MybatisSqlPrintInterceptor;
import top.jpower.core.dbs.config.interceptor.chain.MybatisInterceptor;
import top.jpower.core.dbs.config.properties.DemoProperties;
import top.jpower.core.dbs.config.properties.MybatisProperties;
import top.jpower.core.dbs.mp.CustomSqlInjector;
import top.jpower.core.dbs.tenant.JpowerTenantProperties;
import top.jpower.core.deploy.property.JpowerProperties;
import top.jpower.core.deploy.support.YamlAndPropertySourceFactory;
import top.jpower.core.util.constants.StringPool;
import top.jpower.core.util.user.UserConfig;
import top.jpower.core.util.utils.ClassUtil;
import top.jpower.core.util.utils.Fc;
import top.jpower.core.util.utils.SpringUtil;

import java.util.List;
import java.util.stream.Collectors;

/**
 * MybatisPlus 配置
 *
 * @author mr.g
 */
@AutoConfiguration(before = MybatisPlusAutoConfiguration.class)
@EnableTransactionManagement
@AllArgsConstructor
@EnableConfigurationProperties({DemoProperties.class, MybatisProperties.class})
@PropertySource(value = "classpath:./jpower-db.yml",factory = YamlAndPropertySourceFactory.class)
public class MybatisPlusConfig {

    /**
     * Mapper扫描
     *
     * @author mr.g
     * @param beanFactory BeanFactory
     * @param mybatisProperties Mybatis配置
     * @param jpowerProperties Jpower配置
     * @return org.mybatis.spring.mapper.MapperScannerConfigurer
     **/
    @Bean
    @ConditionalOnMissingBean
    public MapperScannerConfigurer mapperScannerConfigurer(BeanFactory beanFactory, MybatisProperties mybatisProperties, JpowerProperties jpowerProperties){
        // todo 这么写会导致MybatisProperties和JpowerProperties无法映射到配置值，MapperScannerConfigurer实例化早于映射，需要想办法解决
        MybatisProperties.Mapper mapper = mybatisProperties.getMapper();
        List<String> packages;
        if (Fc.isEmpty(mapper.getScan())){
            if (!AutoConfigurationPackages.has(beanFactory)) {

                if (Fc.isNotBlank(jpowerProperties.getMainPackages())){
                    packages = ListUtil.of(jpowerProperties.getMainPackages());
                } else {
                    packages = ListUtil.toList(ClassUtil.getPackage(SpringUtil.getMainClass()));
                }

            } else {
                packages = AutoConfigurationPackages.get(beanFactory);
            }

            if (!mapper.isScanBySuper()){
                packages = packages.stream().peek(pk -> pk = pk + ".**.mapper").collect(Collectors.toList());
            }

        } else {
            packages = mapper.getScan();
        }


        MapperScannerConfigurer configurer = new MapperScannerConfigurer();
        configurer.setProcessPropertyPlaceHolders(Boolean.FALSE);
        if (mapper.isScanBySuper()){
            configurer.setMarkerInterface(mapper.getSuperClass());
        }
        configurer.setBasePackage(StringUtils.collectionToCommaDelimitedString(packages));
        if (mapper.isScanMapperAnnotation()){
            configurer.setAnnotationClass(Mapper.class);
        }
        configurer.setLazyInitialization(Fc.toStr(mapper.getLazyInitialization(), SpringUtil.getProperty("mybatis-plus.lazy-initialization", SpringUtil.getProperty("mybatis.lazy-initialization", "false"))));
        configurer.setDefaultScope(Fc.toStr(mapper.getDefaultScope(), SpringUtil.getProperty("mybatis-plus.mapper-default-scope", SpringUtil.getProperty("mybatis.mapper-default-scope", StringPool.EMPTY))));
        return configurer;
    }

    @Bean
    @ConditionalOnMissingBean
    public ISqlInjector sqlInjector(@Autowired(required = false) JpowerTenantProperties tenantProperties) {
        return new CustomSqlInjector(tenantProperties);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(UserConfig.class)
    public UpdateRelatedFieldsMetaHandler updateRelatedFieldsMetaHandler(UserConfig userConfig){
        return new UpdateRelatedFieldsMetaHandler(userConfig);
    }

    /**
     * 全局配置
     **/
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(UpdateRelatedFieldsMetaHandler.class)
    public GlobalConfig globalConfig(UpdateRelatedFieldsMetaHandler metaHandler,
                                     ISqlInjector sqlInjector) {
        GlobalConfig globalConfig = new GlobalConfig();
        globalConfig.setMetaObjectHandler(metaHandler);
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
