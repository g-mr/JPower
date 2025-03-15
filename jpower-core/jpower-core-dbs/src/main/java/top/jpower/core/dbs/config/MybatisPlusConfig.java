package top.jpower.core.dbs.config;

import cn.hutool.core.collection.ListUtil;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusProperties;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.*;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.StringUtils;
import top.jpower.core.dbs.config.interceptor.chain.MybatisInterceptor;
import top.jpower.core.dbs.tenant.JpowerTenantProperties;
import top.jpower.core.deploy.support.YamlAndPropertySourceFactory;
import top.jpower.core.util.user.UserConfig;
import top.jpower.core.dbs.config.interceptor.DemoInterceptor;
import top.jpower.core.dbs.config.interceptor.JpowerMybatisInterceptor;
import top.jpower.core.dbs.config.interceptor.MybatisSqlPrintInterceptor;
import top.jpower.core.dbs.config.properties.DemoProperties;
import top.jpower.core.dbs.config.properties.MybatisProperties;
import top.jpower.core.dbs.mp.CustomSqlInjector;
import top.jpower.core.util.utils.ClassUtil;
import top.jpower.core.util.utils.Fc;
import top.jpower.core.util.utils.SpringUtil;

import java.beans.PropertyDescriptor;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * MybatisPlus 配置
 *
 * @author mr.g
 */
@AutoConfiguration(before = MybatisPlusAutoConfiguration.class)
@EnableTransactionManagement
@AllArgsConstructor
@EnableConfigurationProperties({DemoProperties.class, MybatisProperties.class})
@MapperScan(basePackages = "${jpower.mybatis.mapper:${jpower.mainPackages}}", annotationClass = Mapper.class, lazyInitialization = "${mybatis.lazy-initialization:false}")
@PropertySource(value = "classpath:./jpower-db.yml",factory = YamlAndPropertySourceFactory.class)
public class MybatisPlusConfig {

//    @Bean
//    @ConditionalOnMissingBean
//    public MapperScannerConfigurer mapperScannerConfigurer(BeanFactory beanFactory, MybatisProperties mybatisProperties,
//                                                           MybatisPlusProperties mybatisPlusProperties){
//        List<String> packages;
//        if (Fc.isNull(mybatisProperties.getMapper())){
//            if (!AutoConfigurationPackages.has(beanFactory)) {
//                packages = ListUtil.toList(ClassUtil.getPackage(SpringUtil.getMainClass()));
//            } else {
//                packages = AutoConfigurationPackages.get(beanFactory);
//            }
//        } else {
//            packages = ListUtil.of(mybatisProperties.getMapper());
//        }
//
//        MapperScannerConfigurer configurer = new MapperScannerConfigurer();
//        configurer.setProcessPropertyPlaceHolders(Boolean.FALSE);
//        configurer.setAnnotationClass(Mapper.class);
//        configurer.setBasePackage(StringUtils.collectionToCommaDelimitedString(packages));
//
////        configurer.setLazyInitialization("${mybatis-plus.lazy-initialization:${mybatis.lazy-initialization:false}}");
////        configurer.setDefaultScope("${mybatis-plus.mapper-default-scope:}");
//        return configurer;
//    }

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
