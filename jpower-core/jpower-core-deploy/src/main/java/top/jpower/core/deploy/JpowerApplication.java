package top.jpower.core.deploy;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.*;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import top.jpower.core.deploy.service.DeployService;
import top.jpower.core.util.constants.JpowerConstants;
import top.jpower.core.util.utils.ClassUtil;
import top.jpower.core.util.utils.Fc;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

/**
 * 项目启动器
 *
 * @author mr.g
 */
@Slf4j
public class JpowerApplication {

    /**
     * 项目启动
     *
     * @author mr.g
     * @param appName 项目模块名称
     * @return org.springframework.context.ConfigurableApplicationContext
     **/
    public static ConfigurableApplicationContext run(String appName, Class<?> source, String... args) {
		SpringApplication application = springApplicationBuilder(appName, source, args);
        ConfigurableApplicationContext context = application.run(args);

        log.info("启动成功：appName={}，profiles={}，port={}",appName,context.getEnvironment().getActiveProfiles(),context.getEnvironment().getProperty("server.port"));
        return context;
    }

    @SneakyThrows(UnknownHostException.class)
    private static SpringApplication springApplicationBuilder(String appName, Class<?> source, String[] args) {
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
            builder.profiles(profile);
        } else {
            // 同时存在dev、test、prod环境抛出错误
            throw new RuntimeException("同时存在环境变量:[" + StringUtils.arrayToCommaDelimitedString(activeProfiles) + "]");
        }

        // 冗余配置，主要是为了日志框架来获取这些变量，回头想想办法看能不能优化
        InetAddress address = InetAddress.getLocalHost();
        Properties props = System.getProperties();
        props.setProperty("jpower.applicationName", appName);
        props.setProperty("jpower.hostName", Fc.toStr(address.getHostName(), "127.0.0.1"));
        props.setProperty("jpower.ip", Fc.toStr(address.getHostAddress(), "127.0.0.1"));
        props.setProperty("jpower.env", profile);
        props.setProperty("jpower.version", JpowerConstants.JPOWER_VESION);
        props.setProperty("jpower.mainPackages", ClassUtil.getPackage(source));

		SpringApplication application = builder.build();
		application.addListeners((ApplicationListener<ApplicationEnvironmentPreparedEvent>) event -> {
			List<DeployService> deployServices = new ArrayList<>();
			ServiceLoader.load(DeployService.class).forEach(deployServices::add);
			deployServices.stream()
					.sorted(Comparator.comparing(DeployService::getOrder))
					.forEach(service -> service.deploy(application, event.getEnvironment(), appName, profile));
		});

        return application;
    }

}
