package com.wlcb.jpower.module.base;

import com.wlcb.jpower.module.base.utils.AppConstant;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.Properties;

/**
 * @ClassName JpowerApplication
 * @Description TODO 项目启动器
 * @Author 郭丁志
 * @Date 2020-08-02 17:04
 * @Version 1.0
 */
public class JpowerApplication {


    /**
     * @Author 郭丁志
     * @Description //TODO 项目启动
     * @Date 18:16 2020-08-02
     * @Param appName 项目模块名称，必须与client表的code值相同
     * @return org.springframework.context.ConfigurableApplicationContext
     **/
    public static ConfigurableApplicationContext run(String appName, Class source, String... args) {
        SpringApplicationBuilder builder = createSpringApplicationBuilder(appName, source, args);
        return builder.run(args);
    }

    private static SpringApplicationBuilder createSpringApplicationBuilder(String appName, Class source, String[] args) {
        Assert.hasText(appName, "[appName]服务名不能为空");

        SpringApplicationBuilder builder = new SpringApplicationBuilder(source);

        Properties props = System.getProperties();
        props.setProperty("spring.application.name", appName);
        props.setProperty("jpower.client-code", appName);
        props.setProperty("logging.config", "classpath:logback-spring.xml");
        props.setProperty("blade.is-local", String.valueOf(isLocalDev()));
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
