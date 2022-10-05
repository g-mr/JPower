package com.wlcb.jpower.module.annotation;

import java.lang.annotation.*;

/**
 * @author mr.g
 * @date 2022-09-30 16:40
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Menu {

    /**
     * 客户端编号
     **/
    String client();

    /**
     * 菜单编号
     **/
    String menuCode();

    /**
     * 功能编号
     **/
    String code();

}
