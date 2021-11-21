package com.wlcb.jpower.module.config.properties;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @ClassName DemoProperties
 * @Description TODO 演示环境配置
 * @Author 郭丁志
 * @Date 2020-10-14 21:12
 * @Version 1.0
 */
@Data
@ConfigurationProperties(prefix = "jpower.mybatis")
public class MybatisProperties {

    /**
     * 是否开启数据权限
     **/
    private boolean dataScope = true;

    /**
     * 是否开启动态表名
     */
    private boolean dynamicTableName = false;

    /**
     * 是否开启动乐观锁
     */
    private boolean optimisticLocker = true;

    /**
     * 是否开启垃圾SQL拦截
     */
    private boolean illegalSQL = false;

    /**
     * 是否开启全表更新删除拦截（防止攻击）
     */
    private boolean blockAttack = true;

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
         * 溢出总页数后是否进行处理
         */
        private boolean overflow = false;
        /**
         * 单页分页条数最高限制
         */
        private Long maxLimit = 5000L;
        /**
         * 生成 countSql 优化掉 join
         * 现在只支持 left join
         */
        private boolean optimizeJoin = true;
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
