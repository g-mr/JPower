package top.jpower.jpower.module.common.deploy.service.annotation;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.core.annotation.AliasFor;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.lang.annotation.*;

/**
 * Cloud启动注解配置
 * @author mr.g
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@EnableDiscoveryClient
@EnableTransactionManagement
@SpringBootApplication
public @interface JpowerCloudApplication {

    @AliasFor(
            annotation = EnableAutoConfiguration.class
    )
    Class<?>[] exclude() default {};

}
