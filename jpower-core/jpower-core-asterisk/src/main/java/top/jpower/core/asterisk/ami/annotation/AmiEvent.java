package top.jpower.core.asterisk.ami.annotation;

import org.asteriskjava.manager.event.ManagerEvent;

import java.lang.annotation.*;

@Inherited
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AmiEvent {

    /**
     * 事件
     */
    Class<? extends ManagerEvent> value();

}
