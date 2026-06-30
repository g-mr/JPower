package com.qidiangk.smart.system;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import com.qidiangk.smart.common.constants.AppConstant;
import top.jpower.core.deploy.JpowerApplication;

/**
 * 系统管理入口
 *
 * @author mr.g
 */
@SpringBootApplication
@EnableFeignClients(basePackages = {"com.qidiangk.smart.*.api"})
public class SystemStartApplication {

    public static void main(String[] args) {
        JpowerApplication.run(AppConstant.JPOWER_SYSTEM,SystemStartApplication.class,args);
    }

}
