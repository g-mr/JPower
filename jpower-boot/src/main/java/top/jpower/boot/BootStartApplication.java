package top.jpower.boot;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import top.jpower.common.constants.AppConstant;
import top.jpower.core.deploy.JpowerApplication;

/**
 * 单体版本启动器，运行此模块即可启动整个系统
 *
 * @author mr.g
 **/
@SpringBootApplication(scanBasePackages = "top.jpower")
@EnableFeignClients(basePackages = "top.jpower.*.api")
public class BootStartApplication {
    public static void main(String[] args) {
        JpowerApplication.run(AppConstant.JPOWER_BOOT, BootStartApplication.class, args);
    }
}