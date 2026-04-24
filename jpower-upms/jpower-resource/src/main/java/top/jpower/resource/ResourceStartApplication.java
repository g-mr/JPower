package top.jpower.resource;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import top.jpower.common.constants.AppConstant;
import top.jpower.core.deploy.JpowerApplication;

/**
 * 资源服务启动类
 * <p>
 * 文件管理微服务的启动入口
 * </p>
 *
 * @author mr.g
 */
@SpringBootApplication
@EnableFeignClients(basePackages = {"top.jpower.*.api"})
public class ResourceStartApplication {

    public static void main(String[] args) {
        JpowerApplication.run(AppConstant.JPOWER_RESOURCE, ResourceStartApplication.class,args);
    }

}
