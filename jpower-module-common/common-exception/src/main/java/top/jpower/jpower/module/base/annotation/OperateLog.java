package top.jpower.jpower.module.base.annotation;


import java.lang.annotation.*;

/**
 * 自定义操作日志记录注解
 * @author mr.g
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperateLog{

    /** 模块 **/
    String title() default "日志记录";

    /** 功能类型 **/
    BusinessType businessType() default BusinessType.OTHER;

    /**
     *  功能类型为其他时的信息<br/>
     *  必须{@link #businessType}={@link BusinessType#OTHER}时生效
     **/
    String businessOther() default "";

    /** 是否需要记录到数据库 **/
    boolean isSaveLog() default true;

    /** 方法执行失败是否保存日志 **/
    boolean isErrorSaveLog() default true;

    /** 是否获取Request信息 **/
    boolean isSaveRequestData() default true;

    /**
     * 操作内容<br/>
     * <per>
     * e.g: 支持EL表达式
     * </per>
     **/
    String content() default "";

    /**
     * 记录ID<br/>
     * <per>
     *     e.g: 支持EL表达式
     * </per>
     **/
    String recordId() default "";

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

        /**
         * 审核
         */
        REVIEW,
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
