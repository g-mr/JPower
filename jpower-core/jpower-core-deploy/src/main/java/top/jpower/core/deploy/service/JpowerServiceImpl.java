package top.jpower.core.deploy.service;

import com.google.auto.service.AutoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.util.StringUtils;
import top.jpower.core.deploy.property.JpowerProperties;
import top.jpower.core.util.constants.JpowerConstants;
import top.jpower.core.util.utils.BeanUtil;
import top.jpower.core.util.utils.ChainMap;
import top.jpower.core.util.utils.ClassUtil;
import top.jpower.core.util.utils.Fc;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

import static top.jpower.core.util.constants.StringPool.LINUX;

/**
 * 初始化配置
 *
 * @author mr.g
 **/
@Slf4j
@AutoService(DeployService.class)
public class JpowerServiceImpl implements DeployService {

    /**
     * 启动时 处理 SpringApplicationBuilder
     * @param builder SpringApplication 启动器
     * @param properties 配置
     * @param appName 项目名称
     * @param profile 环境变量
     */
    @Override
    public void deploy(SpringApplication application, ConfigurableEnvironment environment, String appName, String profile) {
        // 获取本机地址

        JpowerProperties properties = new JpowerProperties();
        properties.setApplicationName(appName);
        try {
            InetAddress address = InetAddress.getLocalHost();
            properties.setHostName(Fc.toStr(address.getHostName(), "127.0.0.1"));
            properties.setIp(Fc.toStr(address.getHostAddress(), "127.0.0.1"));
        } catch (UnknownHostException e) {
            log.warn("无法获取本机信息", e);
        }
        properties.setEnv(profile);
        properties.setVersion(JpowerConstants.JPOWER_VESION);
        properties.setIsLocal(isLocalDev());
        properties.setMainPackages(ClassUtil.getPackage(application.getMainApplicationClass()));
        properties.setPort(environment.getProperty("server.port", Integer.TYPE));

        Map<String, Object> map = BeanUtil.beanToMap(properties, ChainMap.<String, Object>create().map(), true, key -> "jpower." + key);
        map.put("spring.application.name", appName);
        map.put("spring.profiles.active", profile);


        environment.getPropertySources().addFirst(new MapPropertySource("jpowerService", map));
    }

    /**
     * 判断是否为本地开发环境
     *
     * @author mr.g
     **/
    public static boolean isLocalDev() {
        String osName = System.getProperty("os.name");
        return StringUtils.hasText(osName) && !(LINUX.equalsIgnoreCase(osName));
    }

    /**
     * 获取排列顺序
     * @return order
     */
    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

}
