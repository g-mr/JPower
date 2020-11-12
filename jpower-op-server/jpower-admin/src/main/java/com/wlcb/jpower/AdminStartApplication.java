package com.wlcb.jpower;

import com.wlcb.jpower.module.common.deploy.JpowerApplication;
import com.wlcb.jpower.module.common.utils.constants.AppConstant;
import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @ClassName AdminStartApplication
 * @Description TODO spring boot admin启动入口
 * @Author 郭丁志
 * @Date 2020-02-24 18:41
 * @Version 1.0
 */
@SpringBootApplication
@EnableAdminServer
public class AdminStartApplication {

    public static void main(String[] args) {
        JpowerApplication.run(AppConstant.JPOWER_ADMIN,AdminStartApplication.class,args);
    }

}
