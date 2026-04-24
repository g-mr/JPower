package top.jpower.user;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import top.jpower.common.constants.AppConstant;
import top.jpower.core.deploy.JpowerApplication;

/**
 * 用户管理入口
 *
 * @author mr.g
 */
@SpringBootApplication
@EnableFeignClients(basePackages = {"top.jpower.*.api"})
public class UserStartApplication {

    public static void main(String[] args) {
        JpowerApplication.run(AppConstant.JPOWER_USER,UserStartApplication.class,args);
    }

}


