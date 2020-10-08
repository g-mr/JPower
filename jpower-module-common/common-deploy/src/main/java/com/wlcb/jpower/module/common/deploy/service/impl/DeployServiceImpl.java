package com.wlcb.jpower.module.common.deploy.service.impl;

import com.wlcb.jpower.module.common.deploy.service.DeployService;
import com.wlcb.jpower.module.common.nacos.NacosConstants;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.core.Ordered;

import java.util.Properties;

/**
 * @ClassName DeployService
 * @Description TODO 启动参数配置
 * @Author 郭丁志
 * @Date 2020-08-19 16:22
 * @Version 1.0
 */
public class DeployServiceImpl implements DeployService {

    @Override
    public void launcher(SpringApplicationBuilder builder, String appName, String profile) {
        Properties props = System.getProperties();
        props.setProperty("spring.cloud.nacos.config.file-extension", NacosConstants.FILE_EXTENSION);
        props.setProperty("spring.cloud.nacos.config.shared-configs[0].data-id", NacosConstants.DATA_ID);
        props.setProperty("spring.cloud.nacos.config.shared-configs[0].refresh", NacosConstants.CONFIG_REFRESH);
        props.setProperty("spring.cloud.nacos.config.shared-configs[0].group", NacosConstants.CONFIG_GROUP);

        props.setProperty("spring.cloud.nacos.config.shared-configs[1].data-id", NacosConstants.nacosPublicDataId(profile));
        props.setProperty("spring.cloud.nacos.config.shared-configs[1].refresh", NacosConstants.CONFIG_REFRESH);
        props.setProperty("spring.cloud.nacos.config.shared-configs[1].group", NacosConstants.CONFIG_GROUP);

        //seata注册nacos模式配置
//        props.setProperty("seata.registry.type", "nacos");
//        props.setProperty("seata.registry.nacos.server-addr", "${jpower.".concat(profile).concat(".nacos.server-addr:}"));
//        props.setProperty("seata.registry.nacos.namespace", "${jpower.".concat(profile).concat(".nacos.namespace:}"));
//        props.setProperty("seata.registry.nacos.group", NacosConstants.CONFIG_GROUP);
//        props.setProperty("seata.config.type", "nacos");
//        props.setProperty("seata.config.nacos.server-addr", "${jpower.".concat(profile).concat(".nacos.server-addr:}"));
//        props.setProperty("seata.config.nacos.namespace", "${jpower.".concat(profile).concat(".nacos.namespace:}"));
//        props.setProperty("seata.config.nacos.group", NacosConstants.CONFIG_GROUP);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

}
