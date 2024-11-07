package top.jpower.core.redis.deploy;

import org.springframework.boot.builder.SpringApplicationBuilder;
import top.jpower.core.deploy.service.DeployService;
import top.jpower.core.util.annotation.LoaderService;
import top.jpower.core.util.constants.JpowerConstants;
import top.jpower.core.util.utils.Fc;

import java.util.Properties;

/**
 * 启动初始化
 *
 * @author mr.g
 * @date 2024-11-7 22:50
 */
@LoaderService(DeployService.class)
public class RedisDeploy implements DeployService {

    @Override
    public void deploy(SpringApplicationBuilder builder, Properties properties, String appName, String profile) {
        if (Fc.equalsValue(profile, JpowerConstants.PROD_CODE)){
            properties.setProperty("jpower.redis.log", "false");
        }
    }

}
