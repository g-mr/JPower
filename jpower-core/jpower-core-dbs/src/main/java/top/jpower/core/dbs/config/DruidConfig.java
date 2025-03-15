package top.jpower.core.dbs.config;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import top.jpower.core.dbs.config.properties.DruidProperties;
import top.jpower.core.util.utils.Fc;

/**
 * 连接池配置
 *
 * @author mr.g
 */
@AutoConfiguration
@EnableConfigurationProperties(DruidProperties.class)
public class DruidConfig {

    /**
     * 主要实现web监控的配置处理
     *
     * @param druidProperties 配置文件
     * @return
     */
    @Bean
    public ServletRegistrationBean<StatViewServlet> druidServlet(DruidProperties druidProperties) {
        ServletRegistrationBean<StatViewServlet> servletRegistrationBean = new ServletRegistrationBean<>(
                //表示进行druid监控的配置处理操作
                new StatViewServlet(), "/druid/*");
        //白名单
        if (Fc.isNotBlank(druidProperties.getAllow())){
            servletRegistrationBean.addInitParameter("allow", druidProperties.getAllow());
        }
        //黑名单
        if (Fc.isNotBlank(druidProperties.getDeny())){
            servletRegistrationBean.addInitParameter("deny", druidProperties.getDeny());
        }
        //用户名
        if (Fc.isNotBlank(druidProperties.getLoginUsername())){
            servletRegistrationBean.addInitParameter("loginUsername", druidProperties.getLoginUsername());
        }
        //密码
        if (Fc.isNotBlank(druidProperties.getLoginPassword())){
            servletRegistrationBean.addInitParameter("loginPassword", druidProperties.getLoginPassword());
        }
        //是否可以重置数据源
        if (Fc.notNull(druidProperties.getResetEnable())){
            servletRegistrationBean.addInitParameter("resetEnable", Fc.toStr(Fc.toBool(druidProperties.getResetEnable())));
        }
        return servletRegistrationBean;

    }

    /**
     * 监控
     *
     * @author mr.g
     */
    @Bean
    public FilterRegistrationBean<WebStatFilter> filterRegistrationBean() {
        FilterRegistrationBean<WebStatFilter> filterRegistrationBean = new FilterRegistrationBean<>(new WebStatFilter());
        //所有请求进行监控处理
        filterRegistrationBean.addUrlPatterns("/*");
        //排除
        filterRegistrationBean.addInitParameter("exclusions", "/static/*,*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        return filterRegistrationBean;
    }

}
