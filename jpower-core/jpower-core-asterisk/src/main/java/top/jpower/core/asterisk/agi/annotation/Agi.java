package top.jpower.core.asterisk.agi.annotation;

import top.jpower.core.asterisk.agi.fastagi.support.AgiSupport;
import top.jpower.core.asterisk.agi.fastagi.support.DefaultAgiSupport;

import java.lang.annotation.*;

@Inherited
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Agi {

    /**
     * Agi服务名称
     */
    String value();

    /**
     * Agi服务名称
     */
    Class<? extends AgiSupport> support() default DefaultAgiSupport.class;

}
