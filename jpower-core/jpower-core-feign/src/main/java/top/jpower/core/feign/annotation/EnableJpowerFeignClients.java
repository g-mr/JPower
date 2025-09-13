package top.jpower.core.feign.annotation;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import java.lang.annotation.*;

/**
 * @author mr.g
 * @date 0:06 2021/3/10 0010
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
//@EnableFeignClients
@EnableAutoConfiguration
public @interface EnableJpowerFeignClients {

    String[] value() default {};

    String[] basePackages() default {};

    Class<?>[] basePackageClasses() default {};

    Class<?>[] defaultConfiguration() default {};

    Class<?>[] clients() default {};

}
