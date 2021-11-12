package com.wlcb.jpower.module.config.interceptor;


import com.wlcb.jpower.module.common.utils.DateUtil;
import com.wlcb.jpower.module.common.utils.ExceptionsUtil;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.constants.StringPool;
import com.wlcb.jpower.module.config.properties.MybatisProperties;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.TypeHandlerRegistry;

import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;

import static com.wlcb.jpower.module.common.utils.constants.StringPool.NEWLINE;
import static com.wlcb.jpower.module.common.utils.constants.StringPool.TAB;

/**
 * @author mr.g
 * @date 2021-05-20 17:00
 */
@Slf4j
@Intercepts(
    {
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
    }
)
@RequiredArgsConstructor
public class MybatisSqlPrintInterceptor implements Interceptor {


    private final MybatisProperties.Sql sqlProperties;

    @Override
    @SneakyThrows
    public Object intercept(Invocation invocation) {
        long startTime = System.currentTimeMillis();
        Object rest = invocation.proceed();
        try {
            long time = System.currentTimeMillis() - startTime;
            // 超过超时时长则打印
            if(time >= sqlProperties.getPrintTimeout()) {
                Object[] args = invocation.getArgs();
                MappedStatement ms = (MappedStatement) args[0];
                boolean isUpdate = args.length == 2;
                BoundSql boundSql;
                if (isUpdate){
                    boundSql = ms.getBoundSql(args[1]);
                }else {
                    if (args.length == 4) {
                        boundSql = ms.getBoundSql(args[1]);
                    } else {
                        // 使用Executor的代理对象调用query(args[6])
                        boundSql = (BoundSql) args[5];
                    }
                }

                printSql(boundSql,ms.getConfiguration(),ms.getId(),time,rest);
            }
        } catch (Exception e) {
            log.error("==> 打印sql 日志异常 {}", NEWLINE+ExceptionsUtil.getStackTraceAsString(e));
        }
        return rest;
    }

    public void printSql(BoundSql boundSql,Configuration configuration,String sqlId,long time,Object rest) {
        // 替换参数格式化Sql语句，去除换行符
        String sql = formatSql(boundSql, configuration).concat(";");

        String[] mappers = getMapper(sqlId);

        StringBuilder sb = new StringBuilder(StringPool.NEWLINE)
                .append(TAB).append("==> Mapper name：").append(mappers[0]).append(StringPool.NEWLINE)
                .append(TAB).append("==> Mapper method：").append(mappers[1]).append(StringPool.NEWLINE)
                .append(TAB).append("==> Execute SQL：").append(sql).append(StringPool.NEWLINE)
                .append(TAB).append("<== Time：").append(time).append(" ms ").append(StringPool.NEWLINE);

        if (rest instanceof List){
            sb.append(TAB).append("<== Total: ").append(((List) rest).size()).append(StringPool.NEWLINE);
        }else {
            sb.append(TAB).append("<== Updates: ").append(rest).append(StringPool.NEWLINE);
        }
        log.info(sb.toString());
    }

    /**
     * 根据节点id获取执行的Mapper name和Mapper method
     *
     * @param sqlId 节点id
     * @return
     */
    public static String[] getMapper(String sqlId) {
        int index = sqlId.lastIndexOf(".");
        String mapper = sqlId.substring(0, index);
        String method = sqlId.substring(index + 1);
        return new String[]{mapper, method};
    }

    /**
     * 获取完整的sql实体的信息
     *
     * @param boundSql
     * @return
     */
    private String formatSql(BoundSql boundSql, Configuration configuration) {
        String sql = boundSql.getSql();
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        Object parameterObject = boundSql.getParameterObject();
        // 输入sql字符串空判断
        if (Fc.isBlank(sql)) {
            return "";
        }
        if (configuration == null) {
            return "";
        }
        TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
        sql = beautifySql(sql);
        // 参考mybatis 源码 DefaultParameterHandler
        if (parameterMappings != null) {
            for (ParameterMapping parameterMapping : parameterMappings) {
                if (parameterMapping.getMode() != ParameterMode.OUT) {
                    Object value;
                    String propertyName = parameterMapping.getProperty();
                    if (boundSql.hasAdditionalParameter(propertyName)) {
                        value = boundSql.getAdditionalParameter(propertyName);
                    } else if (parameterObject == null) {
                        value = null;
                    } else if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                        value = parameterObject;
                    } else {
                        MetaObject metaObject = configuration.newMetaObject(parameterObject);
                        value = metaObject.getValue(propertyName);
                    }
                    String paramValueStr = "";
                    if (value instanceof String) {
                        paramValueStr = "'" + value + "'";
                    } else if (value instanceof Date) {
                        paramValueStr = "'" + DateUtil.formatDateTime((Date) value) + "'";
                    } else {
                        paramValueStr = value + "";
                    }

                    sql = sql.replaceFirst("\\?", Matcher.quoteReplacement(paramValueStr));
                }
            }
        }
        return sql;
    }

    private String beautifySql(String sql) {
        sql = sql.replaceAll("[\\s\n ]+", " ");
        return sql;
    }

}
