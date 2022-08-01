package com.wlcb.jpower;

import com.wlcb.jpower.module.common.deploy.JpowerApplication;
import com.wlcb.jpower.module.common.deploy.service.annotation.JpowerCloudApplication;
import com.wlcb.jpower.module.common.utils.constants.AppConstant;

/**
 * 网关启动
 *
 * @author mr.g
 **/
@JpowerCloudApplication
//@EnableJpowerFeignClients
public class GatewayApplication {
    public static void main(String[] args) {
        JpowerApplication.run(AppConstant.JPOWER_GATEWAY,GatewayApplication.class, args);
    }
}
