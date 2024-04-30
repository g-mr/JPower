package top.jpower.jpower;

import top.jpower.core.utils.constants.AppConstant;
import top.jpower.jpower.annotation.EnableJpowerFeignClients;
import top.jpower.jpower.module.common.deploy.JpowerApplication;
import top.jpower.jpower.module.common.deploy.service.annotation.JpowerCloudApplication;

/**
 * @ClassName FileStartApplication
 * @Description TODO 文件管理入口
 * @Author 郭丁志
 * @Date 2020-02-24 18:41
 * @Version 1.0
 */
@JpowerCloudApplication
@EnableJpowerFeignClients
public class ResourceStartApplication {

    public static void main(String[] args) {
        JpowerApplication.run(AppConstant.getInstance().getJpowerResource(),ResourceStartApplication.class,args);
    }

}
