package top.jpower.core.dbs.config;

import com.mybatisflex.annotation.InsertListener;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.UpdateListener;
import com.mybatisflex.core.FlexGlobalConfig;
import com.mybatisflex.core.keygen.KeyGenerators;
import com.mybatisflex.core.logicdelete.LogicDeleteProcessor;
import com.mybatisflex.core.logicdelete.impl.TimeStampLogicDeleteProcessor;
import com.mybatisflex.core.query.QueryColumnBehavior;
import com.mybatisflex.spring.boot.ConfigurationCustomizer;
import com.mybatisflex.spring.boot.MyBatisFlexCustomizer;
import com.mybatisflex.spring.boot.MybatisFlexAutoConfiguration;
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
import top.jpower.core.dbs.config.filling.InsertFieldsListener;
import top.jpower.core.dbs.config.filling.UpdateFieldsListener;
import top.jpower.core.dbs.config.interceptor.JpowerMybatisInterceptor;
import top.jpower.core.dbs.config.interceptor.MybatisSqlPrintInterceptor;
import top.jpower.core.dbs.config.interceptor.chain.MybatisInterceptor;
import top.jpower.core.dbs.config.properties.DemoProperties;
import top.jpower.core.dbs.config.properties.MybatisProperties;
import top.jpower.core.dbs.tenant.JpowerTenantProperties;
import top.jpower.core.deploy.support.YamlAndPropertySourceFactory;
import top.jpower.core.util.user.UserConfig;
import top.jpower.core.util.utils.Fc;

import java.util.stream.Collectors;

/**
 * MybatisFlex 配置
 *
 * @author mr.g
 */
@AutoConfiguration(before = MybatisFlexAutoConfiguration.class)
@EnableTransactionManagement
@EnableConfigurationProperties({DemoProperties.class, MybatisProperties.class})
@PropertySource(value = "classpath:./jpower-db.yml",factory = YamlAndPropertySourceFactory.class)
public class MybatisFlexConfig {


    /**
     * 一些mybatis配置
     *
     * @author mr.g
     * @return com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer
     **/
   @Bean
   @ConditionalOnMissingBean
   public ConfigurationCustomizer mybatisConfigurationCustomizer() {
       return configuration -> {
//           // 关闭缓存
//           configuration.setCacheEnabled(false);
//           // 开启驼峰命名转换
//           configuration.setMapUnderscoreToCamelCase(true);
//           // 设置空值时是否调用 setter
           configuration.setCallSettersOnNulls(true);
           // TODO 回头看看这里打印的SQL长啥样子
//           configuration.setLogImpl(StdOutImpl.class);
           configuration.setLogImpl(NoLoggingImpl.class);
       };
   }

   /**
    * 逻辑删除插件
    **/
    @Bean
    @ConditionalOnMissingBean
    public LogicDeleteProcessor logicDeleteProcessor(){
        return new TimeStampLogicDeleteProcessor();
    }

    /**
     * 插入字段填充
     **/
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(UserConfig.class)
    public InsertListener insertListener(UserConfig userConfig){
        return new InsertFieldsListener(userConfig);
    }

    /**
     * 更新字段填充
     **/
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(UserConfig.class)
    public UpdateListener updateListener(UserConfig userConfig){
        return new UpdateFieldsListener(userConfig);
    }

    /**
     * 默认的主键生成策略
     **/
    @Bean
    @ConditionalOnMissingBean
    public FlexGlobalConfig.KeyConfig keyConfig(){
        FlexGlobalConfig.KeyConfig keyConfig = new FlexGlobalConfig.KeyConfig();
        keyConfig.setKeyType(KeyType.Generator);
        keyConfig.setValue(KeyGenerators.flexId);
        keyConfig.setBefore(true);
        return keyConfig;
    }

    /**
     * 全局配置
     **/
    @Bean
    @ConditionalOnMissingBean
    public MyBatisFlexCustomizer myBatisFlexCustomizer(@Autowired(required = false) InsertListener insertListener,
                                                       @Autowired(required = false) UpdateListener updateListener,
                                                       FlexGlobalConfig.KeyConfig keyConfig,
                                                       MybatisProperties mybatisProperties,
                                                       JpowerTenantProperties tenantProperties) {
        if (mybatisProperties.getWhereStrategy() != null){
            switch (mybatisProperties.getWhereStrategy()) {
                case IGNORE_NONE -> QueryColumnBehavior.setIgnoreFunction(QueryColumnBehavior.IGNORE_NONE);
                case IGNORE_BLANK -> QueryColumnBehavior.setIgnoreFunction(QueryColumnBehavior.IGNORE_BLANK);
                case IGNORE_NULL -> QueryColumnBehavior.setIgnoreFunction(QueryColumnBehavior.IGNORE_NULL);
                default -> QueryColumnBehavior.setIgnoreFunction(QueryColumnBehavior.IGNORE_EMPTY);
            }
        }
        return config -> {
            if (Fc.notNull(insertListener)){
                config.registerInsertListener(insertListener);
            }
            if (Fc.notNull(insertListener)){
                config.registerUpdateListener(updateListener);
            }
            config.setKeyConfig(keyConfig);
            config.setDefaultMaxPageSize(mybatisProperties.getPage().getMaxLimit());
            config.setDefaultPageSize(mybatisProperties.getPage().getDefaultLimit());
            // 实现不同的租户使用不同的数据源可以使用这个方式
//             config.setDataSourceMissingHandler(new DataSourceMissingHandler() {
//                 @Override
//                 public Map<String, DataSource> handle(String s, Map<String, DataSource> map) {
//                     return null;
//                 }
//             });
            // TODO 需要试试全局配置了然后在字段上再加注解，会产生什么？其他配置都一样
            config.setVersionColumn(mybatisProperties.getOptimisticLockerColumn());
            config.setLogicDeleteColumn(mybatisProperties.getLogicDeleteColumn());
            config.setTenantColumn(tenantProperties.getColumn());
        };
    }

    @Bean
    @ConditionalOnMissingBean({JpowerMybatisInterceptor.class})
    public JpowerMybatisInterceptor jpowerMybatisInterceptor(ObjectProvider<MybatisInterceptor> mybatisInterceptors) {
        return new JpowerMybatisInterceptor(mybatisInterceptors.orderedStream().collect(Collectors.toList()));
    }

    /**
     * sql打印
     **/
    @Order(Ordered.HIGHEST_PRECEDENCE+10)
    @Bean
    @ConditionalOnProperty(value = {"jpower.mybatis.sql.print"}, matchIfMissing = true)
    public MybatisSqlPrintInterceptor mybatisSqlPrintIntercepter(MybatisProperties mybatisProperties) {
        return new MybatisSqlPrintInterceptor(mybatisProperties.getSql());
    }

}
