package top.jpower.core.deploy;

import cn.hutool.core.util.ArrayUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.*;
import org.springframework.core.io.FileUrlResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import top.jpower.core.deploy.service.DeployService;
import top.jpower.core.util.constants.JpowerConstants;
import top.jpower.core.util.utils.ClassUtil;
import top.jpower.core.util.utils.Fc;
import top.jpower.core.util.utils.FileUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.util.*;
import java.util.stream.Collectors;

import static top.jpower.core.util.constants.StringPool.LINUX;

/**
 * @ClassName JpowerApplication
 * @Description TODO 项目启动器
 * @Author Ding
 * @Date 2020-08-02 17:04
 * @Version 1.0
 */
@Slf4j
public class JpowerApplication {

    /**
     * @Author 郭丁志
     * @Description //TODO 项目启动
     * @Date 18:16 2020-08-02
     * @Param appName 项目模块名称
     * @return org.springframework.context.ConfigurableApplicationContext
     **/
    @SneakyThrows(IOException.class)
    public static ConfigurableApplicationContext run(String appName, Class source, String... args) {
        SpringApplicationBuilder builder = springApplicationBuilder(appName, source, args);
        ConfigurableApplicationContext context = builder.run(args);

        log.info("启动成功：appName={}，profiles={}，port={}",appName,context.getEnvironment().getActiveProfiles(),context.getEnvironment().getProperty("server.port"));
        return context;
    }

    private static SpringApplicationBuilder springApplicationBuilder(String appName, Class source, String[] args) throws IOException {
        Assert.hasText(appName, "服务名(appName)不能为空");
        SpringApplicationBuilder builder = new SpringApplicationBuilder(source);

        //读取环境变量配置
        ConfigurableEnvironment environment = new StandardEnvironment();
        MutablePropertySources propertySources = environment.getPropertySources();
        propertySources.addFirst(new SimpleCommandLinePropertySource(args));
        propertySources.addLast(new MapPropertySource("systemProperties", environment.getSystemProperties()));
        propertySources.addLast(new SystemEnvironmentPropertySource("systemEnvironment", environment.getSystemEnvironment()));

        // 获取配置的环境变量
        String[] activeProfiles = environment.getActiveProfiles();

        // 判断环境:dev、test、prod
        List<String> profiles = Arrays.asList(activeProfiles);
        List<String> presetProfiles = new ArrayList<>(Arrays.asList(JpowerConstants.DEV_CODE, JpowerConstants.TEST_CODE, JpowerConstants.PROD_CODE));
        presetProfiles.retainAll(profiles);
        List<String> activeProfileList = new ArrayList<>(presetProfiles);
        String profile;
        if (activeProfileList.isEmpty()) {
            // 默认dev开发
            profile = JpowerConstants.DEV_CODE;
            activeProfileList.add(profile);
            builder.profiles(profile);

        } else if (activeProfileList.size() == 1) {
            profile = activeProfileList.get(0);
        } else {
            // 同时存在dev、test、prod环境抛出错误
            throw new RuntimeException("同时存在环境变量:[" + StringUtils.arrayToCommaDelimitedString(activeProfiles) + "]");
        }

        // 获取本机地址
        InetAddress address = InetAddress.getLocalHost();

        Properties props = getYmlProperties();
        props.setProperty("jpower.applicationName", appName);
        props.setProperty("jpower.port", Fc.toStr(props.get("server.port")));
        props.setProperty("jpower.hostName", Fc.toStr(address.getHostName(), "127.0.0.1"));
        props.setProperty("jpower.ip", Fc.toStr(address.getHostAddress(), "127.0.0.1"));
        props.setProperty("jpower.env", profile);
        props.setProperty("jpower.version", JpowerConstants.JPOWER_VESION);
        props.setProperty("jpower.is-local", String.valueOf(isLocalDev()));
        props.setProperty("jpower.mainPackages", ClassUtil.getPackage(source));
        props.setProperty("spring.application.name", appName);
        props.setProperty("spring.profiles.active", profile);
        props.setProperty("spring.main.allow-bean-definition-overriding", "true");
        if ((Boolean) props.getOrDefault("spring.cloud.nacos.config.enabled", true)){
            //nacos配置
            props.setProperty("spring.cloud.nacos.discovery.server-addr", "${jpower.".concat(profile).concat(".nacos.server-addr}"));
            props.setProperty("spring.cloud.nacos.config.server-addr", "${jpower.".concat(profile).concat(".nacos.server-addr}"));
            props.setProperty("spring.cloud.nacos.discovery.namespace", "${jpower.".concat(profile).concat(".nacos.namespace:}"));
            props.setProperty("spring.cloud.nacos.config.namespace", "${jpower.".concat(profile).concat(".nacos.namespace:}"));
        }
        if ((Boolean) props.getOrDefault("spring.cloud.sentinel.enabled", true)){
            //sentinel配置
            String sentinelServer = props.getProperty("jpower."+profile+".sentinel.dashboard");
            if (Fc.isNotBlank(sentinelServer)) {
                props.setProperty("csp.sentinel.dashboard.server", sentinelServer);
                props.setProperty("csp.sentinel.app.name",appName);
            }
            props.setProperty("spring.cloud.sentinel.transport.dashboard", "${jpower.".concat(profile).concat(".sentinel.dashboard:}"));
        }
        //seata启用,默认关闭
        props.setProperty("seata.enabled", "${jpower.seata.enabled:false}");

        List<DeployService> deployServiceList = new ArrayList<>();
        ServiceLoader.load(DeployService.class).forEach(deployServiceList::add);
        deployServiceList.stream().sorted(Comparator.comparing(DeployService::getOrder)).collect(Collectors.toList())
                .forEach(deployService -> deployService.deploy(builder, props, appName, profile));

        log.info("{}项目启动,运行环境：{}",appName,profile);
        return builder;
    }

    /**
     * 获取项目配置
     *
     * @author mr.g
     * @return java.util.Properties 配置
     **/
    private static Properties getYmlProperties() throws IOException {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources("classpath:/*.yml");
        try {
            Resource[] resourcesConfig = resolver.getResources("classpath:/config/*.yml");
            resources = ArrayUtil.append(resources, resourcesConfig);
        } catch (FileNotFoundException e){
            log.warn("配置文件不存在，直接忽略==={}", e.getMessage());
        }

        Properties properties = System.getProperties();
        YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
        yaml.setResources(resources);
        if (Fc.isNotEmpty(yaml.getObject())){
            yaml.getObject().forEach(properties::putIfAbsent);
        }

        buildProperties(FileUtil.getSysRootPath() + File.separator + "application.yml", properties);
        buildProperties(FileUtil.getSysRootPath() + File.separator + "bootstrap.yml", properties);
        buildProperties(FileUtil.getSysRootResourcePath() + File.separator + "application.yml", properties);
        buildProperties(FileUtil.getSysRootResourcePath() + File.separator + "bootstrap.yml", properties);

        return properties;
    }

    private static void buildProperties(String file, Properties properties) throws IOException{
        try {
            YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
            yaml.setResources(new FileUrlResource(file));
            if (Fc.notNull(yaml.getObject())){
                yaml.getObject().forEach(properties::putIfAbsent);
            }
        } catch (IllegalStateException e){
            log.warn("读取配置文件异常,忽略配置文件==={}", e.getMessage());
        }
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 判断是否为本地开发环境
     * @Date 18:18 2020-08-02
     * @return boolean
     **/
    public static boolean isLocalDev() {
        String osName = System.getProperty("os.name");
        return StringUtils.hasText(osName) && !(LINUX.equals(osName.toUpperCase()));
    }
}
