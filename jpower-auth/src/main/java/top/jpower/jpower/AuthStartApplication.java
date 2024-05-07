package top.jpower.jpower;

import org.springframework.transaction.annotation.EnableTransactionManagement;
import top.jpower.common.constants.AppConstant;
import top.jpower.core.deploy.JpowerApplication;
import top.jpower.core.deploy.annotation.JpowerCloudApplication;
import top.jpower.jpower.annotation.EnableJpowerFeignClients;

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
        JpowerApplication.run(AppConstant.JPOWER_AUTH,AuthStartApplication.class,args);
    }
}
