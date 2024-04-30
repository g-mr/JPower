package top.jpower.jpower.module.datascope.annotation;

import top.jpower.core.utils.constants.ConstantsEnum;
import top.jpower.core.utils.constants.StringPool;

import java.lang.annotation.*;

/**
 *
 * 数据权限
 *
 * @author mr.g
 **/

@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface DataScope {

    /**
     * 权限列
     **/
    String column();

    /**
     * 权限类型
     **/
    ConstantsEnum.DATA_SCOPE_TYPE type() default ConstantsEnum.DATA_SCOPE_TYPE.ALL;

    /**
     * 自定义SQL
     **/
    String sql() default StringPool.EMPTY;
}
