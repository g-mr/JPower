package com.qidiangk.smart.auth;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import com.qidiangk.smart.common.constants.AppConstant;
import top.jpower.core.deploy.JpowerApplication;

/**
 * 登录服务入口
 *
 * @author mr.g
 */
@SpringBootApplication
@EnableFeignClients(basePackages = {"com.qidiangk.smart.*.api"})
public class AuthStartApplication {
    public static void main(String[] args) {
        JpowerApplication.run(AppConstant.JPOWER_AUTH,AuthStartApplication.class,args);
    }
}
