package top.jpower.admin;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import top.jpower.common.constants.AppConstant;
import top.jpower.core.boot.config.JpowerJacksonConfig;
import top.jpower.core.deploy.JpowerApplication;
import top.jpower.core.log.config.SkywalkingConfig;

/**
 * spring boot admin启动入口
 *
 * @author mr.g
 */
@SpringBootApplication(
        exclude = {
			SkywalkingConfig.class,
			JpowerJacksonConfig.class,
        }
)
@EnableAdminServer
public class AdminStartApplication {

    public static void main(String[] args) {
        JpowerApplication.run(AppConstant.JPOWER_ADMIN, AdminStartApplication.class, args);
    }

}
