package com.wlcb.jpower;

import com.wlcb.jpower.module.common.deploy.JpowerApplication;
import com.wlcb.jpower.module.common.utils.constants.AppConstant;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @ClassName GetwayApplication
 * @Description TODO
 * @Author 郭丁志
 * @Date 2020/8/23 0023 17:49
 * @Version 1.0
 */
//@SpringCloudApplication
@SpringBootApplication
@EnableFeignClients
public class GatewayApplication {
    public static void main(String[] args) {
        JpowerApplication.run(AppConstant.JPOWER_GATEWAY,GatewayApplication.class, args);
//        SpringApplication.run(GetwayApplication.class, args);
    }
}
