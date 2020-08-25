package com.wlcb.jpower;

import com.wlcb.jpower.module.common.deploy.JpowerApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @ClassName GetwayApplication
 * @Description TODO
 * @Author 郭丁志
 * @Date 2020/8/23 0023 17:49
 * @Version 1.0
 */
//@SpringCloudApplication
@SpringBootApplication
public class GetwayApplication {
    public static void main(String[] args) {
        JpowerApplication.run("jpower-getway",GetwayApplication.class, args);
//        SpringApplication.run(GetwayApplication.class, args);
    }
}
