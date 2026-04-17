package top.jpower.admin;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.autoconfigure.AutoConfigurationExcludeFilter;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.TypeExcludeFilter;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import top.jpower.common.constants.AppConstant;
import top.jpower.core.boot.config.JpowerJacksonConfig;
import top.jpower.core.deploy.JpowerApplication;

/**
 * spring boot admin启动入口
 *
 * @author mr.g
 */
@SpringBootApplication
@ComponentScan(
        excludeFilters = {@ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = {JpowerJacksonConfig.class}
        ),@ComponentScan.Filter(
                type = FilterType.CUSTOM,
                classes = {TypeExcludeFilter.class}
        ), @ComponentScan.Filter(
                type = FilterType.CUSTOM,
                classes = {AutoConfigurationExcludeFilter.class}
        )}
)
@EnableAdminServer
public class AdminStartApplication {

    public static void main(String[] args) {
        JpowerApplication.run(AppConstant.JPOWER_ADMIN,AdminStartApplication.class,args);
    }

}
