package com.qidiangk.smart.maxkb;

import com.qidiangk.smart.common.constants.AppConstant;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import top.jpower.core.deploy.JpowerApplication;

/**
 * 呼叫中心MAXKB管理入口
 *
 * @author mr.g
 */
@SpringBootApplication
@EnableFeignClients(basePackages = {"com.qidiangk.smart.*.api"})
public class MaxkbStartApplication {

    public static void main(String[] args) {
        JpowerApplication.run(AppConstant.JPOWER_SMART_MAXKB, MaxkbStartApplication.class,args);
    }

}
