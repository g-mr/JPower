package com.wlcb.jpower.module.base.annotation;


import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 自定义操作日志记录注解
 * @author mr.gmac
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log{

    /** 模块 **/
    @AliasFor("title")
    String value() default "日志记录";

    /** 模块 **/
    @AliasFor("value")
    String title() default "日志记录";

    /** 功能类型 **/
    BusinessType businessType() default BusinessType.OTHER;

    /** 是否需要记录到数据库 (该配置暂时无效，功能待实现) **/
    boolean isSaveLog() default false;

    /** 是否获取Request信息 **/
    boolean isSaveRequestData() default true;

    enum BusinessType {
        /**
         * 其它
         */
        OTHER,

        /**
         * 新增
         */
        INSERT,

        /**
         * 修改
         */
        UPDATE,

        /**
         * 删除
         */
        DELETE,

        /**
         * 审核
         */
        REVIEW,

        /**
         * 授权
         */
        GRANT,

        /**
         * 导出
         */
        EXPORT,

        /**
         * 导入
         */
        IMPORT,

        /**
         * 强退
         */
        FORCE,

        /**
         * 生成代码
         */
        GENCODE,

        /**
         * 清空
         */
        CLEAN,
    }

    enum BusinessStatus
    {
        /**
         * 成功
         */
        SUCCESS,

        /**
         * 失败
         */
        FAIL,
    }
}
