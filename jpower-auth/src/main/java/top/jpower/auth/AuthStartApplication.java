package top.jpower.auth;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import top.jpower.common.constants.AppConstant;
import top.jpower.core.deploy.JpowerApplication;

/**
 * 登录服务入口
 *
 * @author mr.g
 */
@SpringBootApplication
@EnableFeignClients(basePackages = {"top.jpower.*.api"})
public class AuthStartApplication {
    public static void main(String[] args) {
        JpowerApplication.run(AppConstant.JPOWER_AUTH,AuthStartApplication.class,args);
    }
}
