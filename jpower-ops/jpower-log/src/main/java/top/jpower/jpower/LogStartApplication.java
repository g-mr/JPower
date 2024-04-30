package top.jpower.jpower;

import top.jpower.common.constants.AppConstant;
import top.jpower.jpower.annotation.EnableJpowerFeignClients;
import top.jpower.jpower.module.common.deploy.JpowerApplication;
import top.jpower.jpower.module.common.deploy.service.annotation.JpowerCloudApplication;

/**
 * @ClassName LogStartApplication
 * @Description TODO 日志服务启动入口
 * @Author mr.g
 */
@JpowerCloudApplication
@EnableJpowerFeignClients
public class LogStartApplication {

    public static void main(String[] args) {
        JpowerApplication.run(AppConstant.JPOWER_LOG, LogStartApplication.class,args);
    }

}
