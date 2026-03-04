package top.jpower.jpower;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import top.jpower.common.constants.AppConstant;
import top.jpower.core.deploy.JpowerApplication;

/**
 * 登录服务入口
 *
 * @author mr.g
 */
@EnableTransactionManagement
@SpringBootApplication(scanBasePackages = "top.jpower")
@EnableFeignClients(basePackages = {"top.jpower.*.api"})
public class AuthStartApplication {
    public static void main(String[] args) {
        JpowerApplication.run(AppConstant.JPOWER_AUTH,AuthStartApplication.class,args);
    }
}
