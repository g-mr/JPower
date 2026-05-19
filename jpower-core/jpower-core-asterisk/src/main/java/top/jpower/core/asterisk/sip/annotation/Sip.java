package top.jpower.core.asterisk.sip.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Inherited
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface Sip {

    /**
     * 用户名
     */
    String username();
    /**
     * 密码
     */
    String password();
    /**
     * 地址
     */
    String domain() default "127.0.0.1";
    /**
     * 端口
     */
    int port() default 5060;

    /**
     * 是否初始化时注册
     */
    boolean register() default true;
}
