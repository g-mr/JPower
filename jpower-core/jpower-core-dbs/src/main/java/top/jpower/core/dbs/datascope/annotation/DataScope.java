package top.jpower.core.dbs.datascope.annotation;

import top.jpower.core.dbs.datascope.constants.DataScopeConstant;
import top.jpower.core.util.constants.StringPool;

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
    int type() default DataScopeConstant.ALL;

    /**
     * 自定义SQL
     **/
    String sql() default StringPool.EMPTY;
}
