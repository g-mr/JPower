package top.jpower.jpower;

import top.jpower.common.constants.AppConstant;
import top.jpower.jpower.module.common.deploy.JpowerApplication;
import top.jpower.jpower.module.common.deploy.service.annotation.JpowerCloudApplication;

/**
 * 网关启动
 *
 * @author mr.g
 **/
@JpowerCloudApplication
public class GatewayApplication {
    public static void main(String[] args) {
        JpowerApplication.run(AppConstant.JPOWER_GATEWAY, GatewayApplication.class, args);
    }
}
