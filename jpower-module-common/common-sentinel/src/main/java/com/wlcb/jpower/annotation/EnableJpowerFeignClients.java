package com.wlcb.jpower.annotation;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;

import java.lang.annotation.*;

/**
 * @author goo
 * @date 0:06 2021/3/10 0010
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@EnableFeignClients
@EnableAutoConfiguration(excludeName = "org.springframework.cloud.netflix.ribbon.RibbonAutoConfiguration")
public @interface EnableJpowerFeignClients {

    String[] value() default {};

    String[] basePackages() default { "com.wlcb.jpower" };

    Class<?>[] basePackageClasses() default {};

    Class<?>[] defaultConfiguration() default {};

    Class<?>[] clients() default {};

}
