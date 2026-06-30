package com.qidiangk.smart.aster;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import com.qidiangk.smart.common.constants.AppConstant;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import top.jpower.core.deploy.JpowerApplication;

/**
 * 呼叫中心管理入口
 *
 * @author mr.g
 */
@SpringBootApplication
@EnableFeignClients(basePackages = {"com.qidiangk.smart.*.api"})
@EnableAsync
@EnableScheduling
public class SmartAsterStartApplication {

    public static void main(String[] args) {
        JpowerApplication.run(AppConstant.JPOWER_SMART_ASTER, SmartAsterStartApplication.class,args);
    }

}
