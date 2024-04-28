package top.jpower.jpower.module.common.seata.deploy;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.core.Ordered;
import top.jpower.jpower.module.base.annotation.LoaderService;
import top.jpower.jpower.module.common.deploy.service.DeployService;
import top.jpower.jpower.module.common.seata.constants.SeataConstants;

import java.util.Properties;

/**
 * @ClassName DeployService
 * @Description TODO 启动参数配置
 * @Author 郭丁志
 * @Date 2020-08-19 16:22
 * @Version 1.0
 */
@LoaderService(DeployService.class)
public class SeataDeployServiceImpl implements DeployService {

    @Override
    public void deploy(SpringApplicationBuilder builder, Properties properties, String appName, String profile) {
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
