package com.wlcb.jpower.module.common.seata.deploy;

import com.wlcb.jpower.module.common.deploy.service.DeployService;
import com.wlcb.jpower.module.common.seata.constants.SeataConstants;
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
public class SeataDeployServiceImpl implements DeployService {

    @Override
    public void launcher(SpringApplicationBuilder builder, String appName, String profile) {
        Properties props = System.getProperties();
        //seata配置
        props.setProperty("seata.tx-service-group", appName.concat(SeataConstants.SUFFIX_SEATA_GROUP));
        props.setProperty("seata.service.grouplist.default", "${jpower.".concat(profile).concat(".seata.grouplist:}"));
        props.setProperty("seata.service.vgroup-mapping.".concat(appName.concat(SeataConstants.SUFFIX_SEATA_GROUP)), SeataConstants.DEFAULT);

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
