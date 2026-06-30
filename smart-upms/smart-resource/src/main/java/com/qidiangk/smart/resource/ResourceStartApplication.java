package com.qidiangk.smart.resource;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import com.qidiangk.smart.common.constants.AppConstant;
import top.jpower.core.deploy.JpowerApplication;

/**
 * 资源服务启动类
 * <p>
 * 文件管理微服务的启动入口
 * </p>
 *
 * @author mr.g
 */
@SpringBootApplication
@EnableFeignClients(basePackages = {"com.qidiangk.smart.*.api"})
public class ResourceStartApplication {

    public static void main(String[] args) {
        JpowerApplication.run(AppConstant.JPOWER_RESOURCE, ResourceStartApplication.class,args);
    }

}
