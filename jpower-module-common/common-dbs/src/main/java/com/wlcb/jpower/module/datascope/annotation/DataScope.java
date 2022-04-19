package com.wlcb.jpower.module.datascope.annotation;

import com.wlcb.jpower.module.common.utils.constants.ConstantsEnum.DATA_SCOPE_TYPE;
import com.wlcb.jpower.module.common.utils.constants.StringPool;

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
    DATA_SCOPE_TYPE type() default DATA_SCOPE_TYPE.ALL;

    /**
     * 自定义SQL
     **/
    String sql() default StringPool.EMPTY;
}
