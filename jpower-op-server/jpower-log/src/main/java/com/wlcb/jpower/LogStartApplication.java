package com.wlcb.jpower;

import com.wlcb.jpower.annotation.EnableJpowerFeignClients;
import com.wlcb.jpower.module.common.deploy.JpowerApplication;
import com.wlcb.jpower.module.common.deploy.service.annotation.JpowerCloudApplication;
import com.wlcb.jpower.module.common.utils.constants.AppConstant;

/**
 * @ClassName LogStartApplication
 * @Description TODO 日志服务启动入口
 * @Author mr.g
 */
@JpowerCloudApplication
@EnableJpowerFeignClients
public class LogStartApplication {

    public static void main(String[] args) {
        JpowerApplication.run(AppConstant.JPOWER_LOG,LogStartApplication.class,args);
    }

}
