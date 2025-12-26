package top.jpower.core.dbs.datascope;

import com.mybatisflex.core.dialect.DbType;
import com.mybatisflex.core.dialect.DialectFactory;
import com.mybatisflex.core.dialect.IDialect;
import com.mybatisflex.spring.boot.MybatisFlexAutoConfiguration;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import top.jpower.core.dbs.datascope.handler.DataScopeHandler;
import top.jpower.core.util.user.UserConfig;

import java.util.Arrays;

/**
 * 数据权限配置
 *
 * @author mr.g
 */
@AutoConfiguration
@AutoConfigureAfter({UserConfig.class, MybatisFlexAutoConfiguration.class})
@ConditionalOnProperty(value = {"jpower.datascope.enable"}, matchIfMissing = true)
@ConditionalOnBean(UserConfig.class)
public class DataScopeConfig {


    /**
     * 创建数据权限处理器
     * 目前只实现MYSQL系列
     * @param userConfig 登录用户信息
     * @return 数据库方言
     */
    @Bean("dataScopeHandler")
    @ConditionalOnMissingBean
    @ConditionalOnBean(UserConfig.class)
    public IDialect dialect(UserConfig userConfig) {
        return new DataScopeHandler(userConfig);
    }

    /**
     * 配置数据权限拦截器
     **/
    @PostConstruct
    public void registerDataScope(IDialect dialect) {
        // 注册数据权限
        DbType[] supportedDbTypes = {
            DbType.MYSQL, DbType.H2, DbType.MARIADB, DbType.OSCAR, DbType.XUGU,
            DbType.OCEAN_BASE, DbType.CUBRID, DbType.GOLDILOCKS, DbType.CSIIDB,
            DbType.HIVE, DbType.DORIS, DbType.GOLDENDB, DbType.SUNDB, DbType.YASDB
        };
        Arrays.stream(supportedDbTypes)
            .forEach(dbType -> DialectFactory.registerDialect(dbType, dialect));
    }
}