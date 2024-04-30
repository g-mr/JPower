package top.jpower.jpower.module.config.interceptor;


import cn.hutool.core.annotation.AnnotationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;
import top.jpower.core.utils.constants.CharPool;
import top.jpower.core.utils.utils.*;
import top.jpower.jpower.module.annotation.NoSqlLog;
import top.jpower.jpower.module.config.interceptor.chain.ChainFilter;
import top.jpower.jpower.module.config.interceptor.chain.MybatisInterceptor;
import top.jpower.jpower.module.config.properties.MybatisProperties;

import java.sql.Statement;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;

import static top.jpower.core.utils.constants.StringPool.NEWLINE;
import static top.jpower.core.utils.constants.StringPool.TAB;

/**
 * @author mr.g
 * @date 2021-05-20 17:00
 */
@Slf4j
@RequiredArgsConstructor
public class MybatisSqlPrintInterceptor implements MybatisInterceptor {

    private final MybatisProperties.Sql sqlProperties;

    @Override
    public Object aroundUpdate(ChainFilter chainFilter, final StatementHandler sh, MappedStatement ms, BoundSql boundSql, Statement statement){
        return printSql(chainFilter, ms.getConfiguration(), ms.getId(), boundSql, true);
    }

    @Override
    public Object aroundQuery(ChainFilter chainFilter, final StatementHandler sh, MappedStatement ms, BoundSql boundSql, Statement statement, ResultHandler resultHandler){
        return printSql(chainFilter, ms.getConfiguration(), ms.getId(), boundSql, false);
    }

    public Object printSql(ChainFilter chainFilter, Configuration configuration,String mpId, BoundSql boundSql, boolean isUpdate) {

        if (isLog(mpId)){
            long startTime = System.currentTimeMillis();
            Object rest = null;
            String error = null;
            try {
                rest = chainFilter.proceed();
            } catch (Exception e){
                error = ExceptionUtil.getRootCauseMessage(e);
                throw e;
            } finally {
                try {

                    long time = System.currentTimeMillis() - startTime;
                    // 超过超时时长则打印
                    if(time >= sqlProperties.getPrintTimeout()) {
                        printSql(boundSql,configuration,mpId,time,rest,isUpdate,error);
                    }
                } catch (Exception e) {
                    log.error("==> 打印sql 日志异常 {}", NEWLINE+ExceptionUtil.getStackTraceAsString(e));
                }
            }
            return rest;
        }

        return chainFilter.proceed();
    }

    public void printSql(BoundSql boundSql,Configuration configuration,String sqlId,long time,Object rest, boolean isUpdate,String error) {
        // 替换参数格式化Sql语句，去除换行符
        String sql = formatSql(boundSql, configuration).concat(";");

        String[] mappers = getMapper(sqlId);

        StringBuilder sb = new StringBuilder(NEWLINE)
                .append(TAB).append("==> Mapper name：").append(mappers[0]).append(NEWLINE)
                .append(TAB).append("==> Mapper method：").append(mappers[1]).append(NEWLINE)
                .append(TAB).append("==> Execute SQL：").append(sql).append(NEWLINE)
                .append(TAB).append("<== Time：").append(time).append(" ms ").append(NEWLINE);

        if (isUpdate){
            sb.append(TAB).append("<== Updates: ").append(rest).append(NEWLINE);
        }else {
            if (rest instanceof List){
                List list = (List) rest;
                if (list.size() == 1){
                    sb.append(TAB).append("<== Result: ").append(list.get(0)).append(NEWLINE);
                }
                sb.append(TAB).append("<== Total: ").append(((List) rest).size()).append(NEWLINE);
            }else {
                sb.append(TAB).append("<== Result: ").append(rest).append(NEWLINE);
            }
        }

        if (Fc.isNotBlank(error)){
            sb.append(TAB).append("<== errorSqlInfo: ").append(error).append(NEWLINE);
        }
        log.info(sb.toString());
    }

    /**
     * 是否打印日志
     * @Author mr.g
     * @param mpId
     * @return boolean
     **/
    private boolean isLog(String mpId) {
        String[] mappers =  getMapper(mpId);
        NoSqlLog noSqlLog = null;
        try {
            noSqlLog = AnnotationUtil.getAnnotation(ReflectUtil.getMethodByName(Class.forName(mappers[0]),mappers[1]), NoSqlLog.class);
        } catch (ClassNotFoundException e) {
            return true;
        }
        return Fc.isNull(noSqlLog);
    }

    /**
     * 根据节点id获取执行的Mapper name和Mapper method
     *
     * @param mapperId 节点id
     * @return
     */
    private static String[] getMapper(String mapperId) {
        String methodName = StringUtil.subAfter(mapperId, CharPool.DOT, Boolean.TRUE);
        String className = StringUtil.subBefore(mapperId, CharPool.DOT, Boolean.TRUE);
        return new String[]{className, methodName};
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
