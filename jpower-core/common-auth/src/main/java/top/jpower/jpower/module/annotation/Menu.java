package top.jpower.jpower.module.annotation;

import lombok.Getter;
import top.jpower.core.utils.constants.ConstantsEnum;

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
     * 按钮或接口名称
     * e.g: 如果这个值为空，则取@Function注解的value值
     **/
    String name() default "";
    /**
     * 客户端编号
     **/
    String client();

    /**
     * 菜单编号
     **/
    String menuCode();

    /**
     * 按钮编号，如果为空则说明在菜单下面
     **/
    String btnCode() default "";

    /**
     * 功能编号
     **/
    String code();

    /**
     * 功能类型
     **/
    TYPE type();

    enum TYPE
    {
        /**
         * 按钮
         */
        BTN(ConstantsEnum.FUNCTION_TYPE.BTN.getValue()),

        /**
         * 接口
         */
        INTERFACE(ConstantsEnum.FUNCTION_TYPE.INTERFACE.getValue()),
        ;

        TYPE(int value) {
            this.value = value;
        }

        @Getter
        private final Integer value;
    }

}
