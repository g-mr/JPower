package top.jpower.jpower.module.common.swagger;

import java.lang.annotation.*;

/**
 * API文档分组
 *
 * @author mr.g
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ApiGroup {

    /**
     * 分组名称
     **/
    String[] value();

}
