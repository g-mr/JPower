package top.jpower.jpower.module.common.utils;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import lombok.SneakyThrows;
import top.jpower.core.utils.utils.Fc;
import top.jpower.core.utils.utils.SpringUtil;

import java.util.List;
import java.util.Properties;

/**
 * Nacos工具类
 *
 * @author mr.g
 * @date 2022/10/5 15:07
 */
public class NacosUtil {
    

    @SneakyThrows(NacosException.class)
    public static List<String> getAllServers() {
        NacosDiscoveryProperties discoveryProperties = SpringUtil.getBean(NacosDiscoveryProperties.class);
        Properties properties = new Properties();
        properties.setProperty("serverAddr",discoveryProperties.getServerAddr());
        if (Fc.isNotBlank(discoveryProperties.getNamespace())){
            properties.setProperty("namespace",discoveryProperties.getNamespace());
        }
        NamingService naming = NamingFactory.createNamingService(properties);
        return naming.getServicesOfServer(1,10000).getData();
    }

}
