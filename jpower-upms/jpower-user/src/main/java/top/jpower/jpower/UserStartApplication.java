package top.jpower.jpower;

import org.springframework.transaction.annotation.EnableTransactionManagement;
import top.jpower.common.constants.AppConstant;
import top.jpower.core.deploy.JpowerApplication;
import top.jpower.core.deploy.annotation.JpowerCloudApplication;
import top.jpower.core.feign.annotation.EnableJpowerFeignClients;
import top.jpower.core.redis.topic.RedisTopicScan;

/**
 * 用户管理入口
 *
 * @author mr.g
 */
@EnableTransactionManagement
@JpowerCloudApplication
@EnableJpowerFeignClients
public class UserStartApplication {

    public static void main(String[] args) {
        JpowerApplication.run(AppConstant.JPOWER_USER,UserStartApplication.class,args);
    }

}


