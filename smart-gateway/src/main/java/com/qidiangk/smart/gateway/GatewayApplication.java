package com.qidiangk.smart.gateway;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.qidiangk.smart.common.constants.AppConstant;
import top.jpower.core.deploy.JpowerApplication;

/**
 * 网关启动
 *
 * @author mr.g
 **/
@SpringBootApplication
public class GatewayApplication {

    public static void main(String[] args) {
        JpowerApplication.run(AppConstant.JPOWER_GATEWAY, GatewayApplication.class, args);
    }

}
