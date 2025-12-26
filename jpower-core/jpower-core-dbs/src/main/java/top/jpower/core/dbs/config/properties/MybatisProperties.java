package top.jpower.core.dbs.config.properties;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @ClassName DemoProperties
 * @Description TODO mybatis配置
 * @Author 郭丁志
 * @Date 2020-10-14 21:12
 * @Version 1.0
 */
@Data
@ConfigurationProperties(prefix = "jpower.mybatis")
public class MybatisProperties {

    /**
     * 是否开启动态表名
     */
//    private Boolean dynamicTableName = false;

    /**
     * 是否开启动乐观锁
     */
    private boolean optimisticLocker = false;
    /**
     * 是否开启动乐观锁
     */
    private String optimisticLockerColumn = "version";
    /**
     * 逻辑删除字段
     */
    private String logicDeleteColumn = "delete_time";

    /**
     * 是否开启垃圾SQL拦截
     */
//    private boolean illegalSQL = false;

    /**
     * 是否开启全表更新删除拦截（防止攻击）
     */
//    private boolean blockAttack = true;

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
    }

    @Data
    @AllArgsConstructor
    public static class Sql {
        /** 是否开启打印 **/
        private boolean print;
        /** 超时（单位毫秒） **/
        private long printTimeout;
    }

}
