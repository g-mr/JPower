package top.jpower.core.seata.deploy;

import com.google.auto.service.AutoService;
import org.springframework.boot.SpringApplication;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import top.jpower.core.deploy.service.DeployService;
import top.jpower.core.seata.constants.SeataConstants;
import top.jpower.core.util.utils.ChainMap;
import top.jpower.core.util.utils.Fc;

import java.util.Map;

/**
 * Seata启动参数配置
 *
 * @author mr.g
 */
@AutoService(DeployService.class)
public class SeataDeployServiceImpl implements DeployService {

    @Override
    public void deploy(SpringApplication builder, ConfigurableEnvironment properties, String appName, String profile) {

		//seata配置
		Map<String, Object> map = ChainMap.<String, Object>create()
				.put("seata.enabled", Fc.isNotBlank(properties.getProperty("seata.service.grouplist.default")))
				.put("seata.tx-service-group", appName.concat(SeataConstants.SUFFIX_SEATA_GROUP))
				.put("seata.service.vgroup-mapping.".concat(appName.concat(SeataConstants.SUFFIX_SEATA_GROUP)), SeataConstants.DEFAULT)
				.map();


        //seata注册nacos模式配置
//        map.put("seata.registry.type", "nacos");
//        map.put("seata.registry.nacos.server-addr", "${spring.cloud.nacos.server-addr:}");
//        map.put("seata.registry.nacos.namespace", "${spring.cloud.nacos.namespace:}");
//        map.put("seata.config.type", "nacos");
//        map.put("seata.config.nacos.server-addr", "${spring.cloud.nacos.server-addr:}");
//        map.put("seata.config.nacos.namespace", "${spring.cloud.nacos.namespace:}");

		properties.getPropertySources().addLast(new MapPropertySource("seataDeployService", map));
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

}
