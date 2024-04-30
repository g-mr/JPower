package top.jpower.jpower;

import top.jpower.common.constants.AppConstant;
import top.jpower.jpower.annotation.EnableJpowerFeignClients;
import top.jpower.jpower.module.common.deploy.JpowerApplication;
import top.jpower.jpower.module.common.deploy.service.annotation.JpowerCloudApplication;

/**
 * @ClassName SpringBootStartApplication
 * @Description TODO 系统管理入口
 * @Author Ding
 * @Date 2020-02-24 18:41
 * @Version 1.0
 */
@JpowerCloudApplication
@EnableJpowerFeignClients
public class SystemStartApplication {

    public static void main(String[] args) {
        JpowerApplication.run(AppConstant.JPOWER_SYSTEM,SystemStartApplication.class,args);
    }

}
