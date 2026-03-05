package top.jpower.log;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import top.jpower.common.constants.AppConstant;
import top.jpower.core.deploy.JpowerApplication;

/**
 * 日志服务启动入口
 *
 * @author mr.g
 */
@EnableTransactionManagement
@SpringBootApplication(scanBasePackages = "top.jpower")
@EnableFeignClients(basePackages = {"top.jpower.*.api"})
public class LogStartApplication {

    public static void main(String[] args) {
        JpowerApplication.run(AppConstant.JPOWER_LOG, LogStartApplication.class,args);
    }

}
