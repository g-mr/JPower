package com.wlcb.jpower;

import com.wlcb.jpower.annotation.EnableJpowerFeignClients;
import com.wlcb.jpower.module.common.deploy.JpowerApplication;
import com.wlcb.jpower.module.common.deploy.service.annotation.JpowerCloudApplication;
import com.wlcb.jpower.module.common.utils.constants.AppConstant;

/**
 * @ClassName GetwayApplication
 * @Description TODO
 * @Author 郭丁志
 * @Date 2020/8/23 0023 17:49
 * @Version 1.0
 */
//@SpringCloudApplication
@JpowerCloudApplication
@EnableJpowerFeignClients
public class GatewayApplication {
    public static void main(String[] args) {
        JpowerApplication.run(AppConstant.JPOWER_GATEWAY,GatewayApplication.class, args);
    }
}
