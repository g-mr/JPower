package top.jpower.jpower.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.JdbcProperties;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import top.jpower.jpower.config.properties.DataSourceProperties;

import javax.sql.DataSource;

/**
 * 数据源配置<br/>
 * e.g:只有jpower.datasource.task配置之后才使用这个数据源,如意没有配置jpower.datasource.task则使用项目的数据源
 *
 * @author mr.g
 * @date 2023/7/4 9:37 PM
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(prefix = "jpower.datasource.task", name = {"driver-class-name","url","username","password"})
@EnableConfigurationProperties({DataSourceProperties.class, JdbcProperties.class})
@AutoConfigureAfter(JdbcTemplateAutoConfiguration.class)
public  class DataSourceConfig {

    @Bean(name="taskJdbcTemplate")
    public JdbcTemplate jdbcTemplate(DataSourceProperties dataSourceProperties, JdbcProperties properties) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSourceBuilder.create()
                .type(dataSourceProperties.getType())
                .driverClassName(dataSourceProperties.getDriverClassName())
                .url(dataSourceProperties.getUrl())
                .username(dataSourceProperties.getUsername())
                .password(dataSourceProperties.getPassword()).build());
        JdbcProperties.Template template = properties.getTemplate();
        jdbcTemplate.setFetchSize(template.getFetchSize());
        jdbcTemplate.setMaxRows(template.getMaxRows());
        if (template.getQueryTimeout() != null) {
            jdbcTemplate.setQueryTimeout((int) template.getQueryTimeout().getSeconds());
        }
        return jdbcTemplate;
    }

    @Bean("taskNamedParameterJdbcTemplate")
    public NamedParameterJdbcTemplate taskNamedParameterJdbcTemplate(@Qualifier("taskJdbcTemplate") JdbcTemplate taskJdbcTemplate) {
        return new NamedParameterJdbcTemplate(taskJdbcTemplate);
    }

    /**
     * <p>
     *     本来想使用springboot框架里的{@link JdbcTemplateAutoConfiguration}来注册主数据源,但是发现使用了 {@code @AutoConfigureAfter(JdbcTemplateAutoConfiguration.class)} 后不生效,回头研究研究肯定有办法
     * </p>
     **/
    @Bean("jdbcTemplate")
    @Primary
    @ConditionalOnMissingBean(name = "jdbcTemplate")
    public JdbcTemplate jdbcTemplate(DataSource dataSource, JdbcProperties properties) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        JdbcProperties.Template template = properties.getTemplate();
        jdbcTemplate.setFetchSize(template.getFetchSize());
        jdbcTemplate.setMaxRows(template.getMaxRows());
        if (template.getQueryTimeout() != null) {
            jdbcTemplate.setQueryTimeout((int) template.getQueryTimeout().getSeconds());
        }
        return jdbcTemplate;
    }

    @Bean("namedParameterJdbcTemplate")
    @Primary
    @ConditionalOnMissingBean(name = "namedParameterJdbcTemplate")
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate(@Qualifier("jdbcTemplate") JdbcTemplate jdbcTemplate) {
        return new NamedParameterJdbcTemplate(jdbcTemplate);
    }
}






