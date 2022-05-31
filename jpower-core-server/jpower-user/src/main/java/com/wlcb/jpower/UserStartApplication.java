package com.wlcb.jpower;

import com.wlcb.jpower.annotation.EnableJpowerFeignClients;
import com.wlcb.jpower.module.common.deploy.JpowerApplication;
import com.wlcb.jpower.module.common.deploy.service.annotation.JpowerCloudApplication;
import com.wlcb.jpower.module.common.utils.constants.AppConstant;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @ClassName SpringBootStartApplication
 * @Description TODO 用户管理入口
 * @Author 郭丁志
 * @Date 2020-02-24 18:41
 * @Version 1.0
 */
@EnableTransactionManagement
@JpowerCloudApplication
@EnableJpowerFeignClients
public class UserStartApplication {

    public static void main(String[] args) {
        JpowerApplication.run(AppConstant.JPOWER_USER,UserStartApplication.class,args);
    }

}


