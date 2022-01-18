package com.wlcb.jpower.module.common.deploy;

import com.wlcb.jpower.module.common.deploy.service.DeployService;
import com.wlcb.jpower.module.common.utils.constants.AppConstant;
import com.wlcb.jpower.module.common.utils.constants.JpowerConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.*;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

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
     * @Param appName 项目模块名称，必须与client表的code值相同
     * @return org.springframework.context.ConfigurableApplicationContext
     **/
    public static ConfigurableApplicationContext run(String appName, Class source, String... args) {
        SpringApplicationBuilder builder = springApplicationBuilder(appName, source, args);
        return builder.run(args);
    }

    private static SpringApplicationBuilder springApplicationBuilder(String appName, Class source, String[] args) {
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
        List<String> presetProfiles = new ArrayList(Arrays.asList("dev", "test", "prod"));
        presetProfiles.retainAll(profiles);
        List<String> activeProfileList = new ArrayList<>(presetProfiles);
        String profile;
        if (activeProfileList.isEmpty()) {
            // 默认dev开发
            profile = AppConstant.DEV_CODE;
            activeProfileList.add(profile);
            builder.profiles(profile);

        } else if (activeProfileList.size() == 1) {
            profile = activeProfileList.get(0);
        } else {
            // 同时存在dev、test、prod环境抛出错误
            throw new RuntimeException("同时存在环境变量:[" + StringUtils.arrayToCommaDelimitedString(activeProfiles) + "]");
        }

        Properties props = System.getProperties();
        props.setProperty("jpower.name", appName);
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
        props.setProperty("spring.cloud.sentinel.transport.dashboard", "${jpower.".concat(profile).concat(".sentinel.dashboard:}"));
        //seata启用,默认关闭
        props.setProperty("seata.enabled", "${jpower.seata.enabled:false}");

        List<DeployService> deployServiceList = new ArrayList<>();
        ServiceLoader.load(DeployService.class).forEach(deployServiceList::add);
        deployServiceList.stream().sorted(Comparator.comparing(DeployService::getOrder)).collect(Collectors.toList())
                .forEach(deployService -> deployService.launcher(builder, appName, profile));

        log.info("{}项目已启动,运行环境：{}",appName,profile);
        return builder;
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 判断是否为本地开发环境
     * @Date 18:18 2020-08-02
     * @return boolean
     **/
    public static boolean isLocalDev() {
        String osName = System.getProperty("os.name");
        return StringUtils.hasText(osName) && !(AppConstant.OS_NAME_LINUX.equals(osName.toUpperCase()));
    }
}
