package top.jpower.core.dbs.dictbind.annotation;


import java.lang.annotation.*;


/**
 * 字典翻译注解
 *
 * @author mr.g
 **/
@Target({ ElementType.FIELD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Dict {

    /**
     * 字典名称
     */
    String name();

    /**
     * 赋值的属性名称
     */
    String attributes() default "";


}
