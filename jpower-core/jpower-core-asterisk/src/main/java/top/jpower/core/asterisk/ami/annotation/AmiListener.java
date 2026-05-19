package top.jpower.core.asterisk.ami.annotation;

import org.asteriskjava.manager.event.ManagerEvent;

import java.lang.annotation.*;

@Inherited
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface AmiListener {

    /**
     * 事件
     */
    Class<? extends ManagerEvent>[] value() default {};

}
