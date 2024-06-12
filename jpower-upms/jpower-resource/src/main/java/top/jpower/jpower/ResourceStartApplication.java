package top.jpower.jpower;

import top.jpower.common.constants.AppConstant;
import top.jpower.core.deploy.JpowerApplication;
import top.jpower.core.deploy.annotation.JpowerCloudApplication;
import top.jpower.core.feign.annotation.EnableJpowerFeignClients;

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
        JpowerApplication.run(AppConstant.JPOWER_RESOURCE, ResourceStartApplication.class,args);
    }

}
