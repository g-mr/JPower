package top.jpower.jpower;

import org.springframework.transaction.annotation.EnableTransactionManagement;
import top.jpower.common.constants.AppConstant;
import top.jpower.core.deploy.JpowerApplication;
import top.jpower.core.deploy.annotation.JpowerCloudApplication;
import top.jpower.core.feign.annotation.EnableJpowerFeignClients;

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
        JpowerApplication.run(AppConstant.JPOWER_USER,UserStartApplication.class,args);
    }

}


