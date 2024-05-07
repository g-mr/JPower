package top.jpower.jpower.module.common.nacos.deploy;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.core.Ordered;
import top.jpower.core.deploy.service.DeployService;
import top.jpower.core.util.annotation.LoaderService;
import top.jpower.jpower.module.common.nacos.NacosConstants;

import java.util.Properties;

/**
 * @ClassName DeployService
 * @Description TODO 扩展启动参数配置
 * @Author 郭丁志
 * @Date 2020-08-19 16:22
 * @Version 1.0
 */
@LoaderService(DeployService.class)
public class NacosServiceImpl implements DeployService {

    @Override
    public void deploy(SpringApplicationBuilder builder, Properties properties, String appName, String profile) {

        NacosConstants nacosConstants = NacosConstants.getInstance();

        Properties props = System.getProperties();
        props.setProperty("spring.cloud.nacos.config.file-extension", NacosConstants.FILE_EXTENSION);

        props.setProperty("spring.cloud.nacos.config.shared-configs[0].data-id", nacosConstants.nacosDataId());
        props.setProperty("spring.cloud.nacos.config.shared-configs[0].refresh", NacosConstants.CONFIG_REFRESH);
        props.setProperty("spring.cloud.nacos.config.shared-configs[0].group", NacosConstants.CONFIG_GROUP);

        props.setProperty("spring.cloud.nacos.config.shared-configs[1].data-id", nacosConstants.nacosProfileDataId(profile));
        props.setProperty("spring.cloud.nacos.config.shared-configs[1].refresh", NacosConstants.CONFIG_REFRESH);
        props.setProperty("spring.cloud.nacos.config.shared-configs[1].group", NacosConstants.CONFIG_GROUP);

    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

}
