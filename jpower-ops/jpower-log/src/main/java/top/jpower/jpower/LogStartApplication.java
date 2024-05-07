package top.jpower.jpower;

import top.jpower.common.constants.AppConstant;
import top.jpower.core.deploy.JpowerApplication;
import top.jpower.core.deploy.annotation.JpowerCloudApplication;
import top.jpower.jpower.annotation.EnableJpowerFeignClients;

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
