package top.jpower.jpower;

import org.springframework.transaction.annotation.EnableTransactionManagement;
import top.jpower.jpower.annotation.EnableJpowerFeignClients;
import top.jpower.jpower.module.common.deploy.JpowerApplication;
import top.jpower.jpower.module.common.deploy.service.annotation.JpowerCloudApplication;
import top.jpower.jpower.module.common.utils.constants.AppConstant;

/**
 * @ClassName LoginStartApplication
 * @Description TODO 登录服务入口
 * @Author 郭丁志
 * @Date 2020-02-24 18:41
 * @Version 1.0
 */
@EnableTransactionManagement
@JpowerCloudApplication
@EnableJpowerFeignClients
public class AuthStartApplication {
    public static void main(String[] args) {
        JpowerApplication.run(AppConstant.getInstance().getJpowerAuth(),AuthStartApplication.class,args);
    }
}
