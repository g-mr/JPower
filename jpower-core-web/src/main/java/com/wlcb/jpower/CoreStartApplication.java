package com.wlcb.jpower;

import com.wlcb.jpower.module.base.JpowerApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @ClassName SpringBootStartApplication
 * @Description TODO 用户管理入口
 * @Author 郭丁志
 * @Date 2020-02-24 18:41
 * @Version 1.0
 */
@SpringBootApplication
@EnableTransactionManagement
//@EnableDiscoveryClient
public class CoreStartApplication {

    public static void main(String[] args) {
        JpowerApplication.run("jpower-system",CoreStartApplication.class,args);
    }

}
