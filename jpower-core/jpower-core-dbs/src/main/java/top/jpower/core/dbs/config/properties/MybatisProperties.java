package top.jpower.core.dbs.config.properties;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * mybatis配置
 *
 * @author mr.g
 */
@Data
@ConfigurationProperties(prefix = "jpower.mybatis")
public class MybatisProperties {

    /**
     * where条件字段忽略规则
     */
    private FieldStrategy whereStrategy = FieldStrategy.IGNORE_EMPTY;
    /**
     * 是否开启动乐观锁
     */
    private boolean optimisticLocker = false;
    /**
     * 乐观锁字段
     */
    private String optimisticLockerColumn = "version";
    /**
     * 逻辑删除字段
     */
    private String logicDeleteColumn = "delete_time";

    /**
     * mp分页配置
     */
    private Page page = new Page();

    /**
     * Sql打印配置
     */
    private Sql sql = new Sql(true,0L);

    @Data
    public static class Page {
        /**
         * 单页分页条数默认条数
         */
        private Integer defaultLimit = 20;
        /**
         * 单页分页条数最高限制
         */
        private Integer maxLimit = 5000;
        /**
         * 是否自动优化count查询语句
         */
        private boolean optimizeCountQuery = true;
    }

    @Data
    @AllArgsConstructor
    public static class Sql {
        /** 是否开启打印 **/
        private boolean print;
        /** 超时（单位毫秒） **/
        private long printTimeout;
    }

    /**
     * 字段策略枚举类
     * <p>
     * 如果字段是基本数据类型则最终效果等同于 {@link #ALWAYS}
     *
     * @author mr.g
     * @since 2016-09-09
     */
    public enum FieldStrategy {
        /**
         * 自动忽略 null
         */
        IGNORE_NULL,
        /**
         * 忽略
         */
        IGNORE_NONE,
        /**
         * 自动忽略 null 和 空字符串
         */
        IGNORE_EMPTY,
        /**
         * 自动忽略 null 和 空白字符串
         */
        IGNORE_BLANK
    }
}
