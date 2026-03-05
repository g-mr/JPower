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
 * @ClassName AdminStartApplication
 * @Description TODO spring boot admin启动入口
 * @Author 郭丁志
 * @Date 2020-02-24 18:41
 * @Version 1.0
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
