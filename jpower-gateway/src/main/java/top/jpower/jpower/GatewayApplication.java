package top.jpower.jpower;

import top.jpower.jpower.module.common.deploy.JpowerApplication;
import top.jpower.jpower.module.common.deploy.service.annotation.JpowerCloudApplication;
import top.jpower.jpower.module.common.utils.constants.AppConstant;

/**
 * 网关启动
 *
 * @author mr.g
 **/
@JpowerCloudApplication
//@EnableJpowerFeignClients
public class GatewayApplication {
    public static void main(String[] args) {
        JpowerApplication.run(AppConstant.getInstance().getJpowerGateway(), GatewayApplication.class, args);
    }
}
