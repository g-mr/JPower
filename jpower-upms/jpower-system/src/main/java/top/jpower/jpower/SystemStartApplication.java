package top.jpower.jpower;

import top.jpower.common.constants.AppConstant;
import top.jpower.core.deploy.JpowerApplication;
import top.jpower.core.deploy.annotation.JpowerCloudApplication;
import top.jpower.core.feign.annotation.EnableJpowerFeignClients;

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
