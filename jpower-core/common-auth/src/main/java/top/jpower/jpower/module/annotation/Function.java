package top.jpower.jpower.module.annotation;

import java.lang.annotation.*;

/**
 * 功能接口权限注解
 *
 * @author mr.g
 * @date 2022-09-28 17:52
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Function {

    /**
     * 名称
     **/
    String value();

    /**
     * 别名
     **/
    String alias() default "";

    /**
     * 菜单
     **/
    Menu[] menus();

}
