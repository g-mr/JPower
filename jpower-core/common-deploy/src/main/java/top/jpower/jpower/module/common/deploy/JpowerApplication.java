package top.jpower.jpower.module.common.deploy;

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
import top.jpower.core.utils.constants.JpowerConstants;
import top.jpower.core.utils.utils.Fc;
import top.jpower.core.utils.utils.FileUtil;
import top.jpower.jpower.module.common.deploy.service.DeployService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static top.jpower.core.utils.constants.StringPool.LINUX;

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
    @SneakyThrows
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
        List<String> presetProfiles = new ArrayList(Arrays.asList(JpowerConstants.DEV_CODE, JpowerConstants.TEST_CODE, JpowerConstants.PROD_CODE));
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

        Properties properties = getYmlProperties();

        Properties props = System.getProperties();
        props.setProperty("jpower.applicationName", appName);
        props.setProperty("jpower.env", profile);
        props.setProperty("jpower.version", JpowerConstants.JPOWER_VESION);
        props.setProperty("jpower.is-local", String.valueOf(isLocalDev()));
        props.setProperty("spring.application.name", appName);
        props.setProperty("spring.profiles.active", profile);
        props.setProperty("spring.main.allow-bean-definition-overriding", "true");
        //nacos配置
        props.setProperty("spring.cloud.nacos.discovery.server-addr", "${jpower.".concat(profile).concat(".nacos.server-addr:}"));
        props.setProperty("spring.cloud.nacos.config.server-addr", "${jpower.".concat(profile).concat(".nacos.server-addr:}"));
        props.setProperty("spring.cloud.nacos.discovery.namespace", "${jpower.".concat(profile).concat(".nacos.namespace:}"));
        props.setProperty("spring.cloud.nacos.config.namespace", "${jpower.".concat(profile).concat(".nacos.namespace:}"));
        //sentinel配置
        String sentinelServer = properties.getProperty("jpower."+profile+".sentinel.dashboard");
        if (Fc.isNotBlank(sentinelServer)) {
            props.setProperty("csp.sentinel.dashboard.server", sentinelServer);
            props.setProperty("csp.sentinel.app.name",appName);
        }
        props.setProperty("spring.cloud.sentinel.transport.dashboard", "${jpower.".concat(profile).concat(".sentinel.dashboard:}"));
        //seata启用,默认关闭
        props.setProperty("seata.enabled", "${jpower.seata.enabled:false}");

        List<DeployService> deployServiceList = new ArrayList<>();
        ServiceLoader.load(DeployService.class).forEach(deployServiceList::add);
        deployServiceList.stream().sorted(Comparator.comparing(DeployService::getOrder)).collect(Collectors.toList())
                .forEach(deployService -> deployService.deploy(builder, properties, appName, profile));

        log.info("{}项目已启动,运行环境：{}",appName,profile);
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
            log.warn("读取配置文件报错==={}", e.getMessage());
        }

        Properties properties = new Properties();
        YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
        yaml.setResources(resources);
        properties.putAll(yaml.getObject());

        try {
            yaml = new YamlPropertiesFactoryBean();
            yaml.setResources(new FileUrlResource(FileUtil.getSysRootPath() + File.separator + "application.yml"));
            if (Fc.notNull(yaml.getObject())){
                properties.putAll(yaml.getObject());
            }
        } catch (IllegalStateException e){
            log.warn("读取配置文件报错==={}", e.getMessage());
        }

        try {
            yaml = new YamlPropertiesFactoryBean();
            yaml.setResources(new FileUrlResource(FileUtil.getSysRootPath() + File.separator + "bootstrap.yml"));
            if (Fc.notNull(yaml.getObject())){
                properties.putAll(yaml.getObject());
            }
        } catch (IllegalStateException e){
            log.warn("读取配置文件报错==={}", e.getMessage());
        }

        try {
            yaml = new YamlPropertiesFactoryBean();
            yaml.setResources(new FileUrlResource(FileUtil.getSysRootResourcePath() + File.separator + "application.yml"));
            if (Fc.notNull(yaml.getObject())){
                properties.putAll(yaml.getObject());
            }
        } catch (IllegalStateException e){
            log.warn("读取配置文件报错==={}", e.getMessage());
        }

        try {
            yaml = new YamlPropertiesFactoryBean();
            yaml.setResources(new FileUrlResource(FileUtil.getSysRootResourcePath() + File.separator + "bootstrap.yml"));
            if (Fc.notNull(yaml.getObject())){
                properties.putAll(yaml.getObject());
            }
        } catch (IllegalStateException e){
            log.warn("读取配置文件报错==={}", e.getMessage());
        }

        try {
            Properties systemProperties = System.getProperties();
            if(Fc.isNotEmpty(systemProperties)){
                properties.putAll(systemProperties);
            }
        } catch (IllegalStateException e){
            log.warn("读取系统配置报错==={}", e.getMessage());
        }

        return properties;
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
