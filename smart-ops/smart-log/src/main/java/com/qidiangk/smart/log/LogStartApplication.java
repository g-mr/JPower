package com.qidiangk.smart.log;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import com.qidiangk.smart.common.constants.AppConstant;
import top.jpower.core.deploy.JpowerApplication;

/**
 * 日志服务启动入口
 *
 * @author mr.g
 */
@SpringBootApplication
@EnableFeignClients(basePackages = {"com.qidiangk.smart.*.api"})
public class LogStartApplication {

    public static void main(String[] args) {
        JpowerApplication.run(AppConstant.JPOWER_LOG, LogStartApplication.class,args);
    }

}
