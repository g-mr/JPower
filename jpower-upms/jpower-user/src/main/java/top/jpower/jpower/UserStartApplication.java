package top.jpower.jpower;

import org.springframework.transaction.annotation.EnableTransactionManagement;
import top.jpower.core.utils.constants.AppConstant;
import top.jpower.jpower.annotation.EnableJpowerFeignClients;
import top.jpower.jpower.module.common.deploy.JpowerApplication;
import top.jpower.jpower.module.common.deploy.service.annotation.JpowerCloudApplication;

/**
 * @ClassName SpringBootStartApplication
 * @Description TODO 用户管理入口
 * @Author 郭丁志
 * @Date 2020-02-24 18:41
 * @Version 1.0
 */
@EnableTransactionManagement
@JpowerCloudApplication
@EnableJpowerFeignClients
public class UserStartApplication {

    public static void main(String[] args) {
        JpowerApplication.run(AppConstant.getInstance().getJpowerUser(),UserStartApplication.class,args);
    }

}


