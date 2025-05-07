package top.jpower.core.dbs.config.properties;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import top.jpower.core.dbs.dbs.dao.mapper.base.JpowerBaseMapper;

import java.util.List;

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
     * Mapper 配置
     */
    private Mapper mapper = new Mapper();

    /**
     * 是否开启动态表名
     */
    private Boolean dynamicTableName = false;

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

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Mapper {
        /**
         * 扫描路径
         **/
        private List<String> scan;
        /**
         * 是否扫描Mapper注解
         **/
        private boolean scanMapperAnnotation = true;
        /**
         * 是否扫描父级接口
         **/
        private boolean scanBySuper = Boolean.TRUE;
        /**
         * 父级接口
         **/
        private Class<?> superClass = JpowerBaseMapper.class;
        /**
         * 是否懒加载
         **/
        private Boolean lazyInitialization;
        /**
         * 指定扫描的映射器的默认范围
         **/
        private String defaultScope;

    }
}
