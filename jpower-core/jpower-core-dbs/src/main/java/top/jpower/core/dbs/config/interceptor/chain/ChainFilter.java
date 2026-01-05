package top.jpower.core.dbs.config.interceptor.chain;

import lombok.SneakyThrows;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.session.ResultHandler;
import top.jpower.core.dbs.util.PluginUtils;

import java.sql.Statement;
import java.util.Iterator;

/**
 * @Author mr.g
 * @Date 2021/11/14 0014 20:58
 */
public class ChainFilter {

    private final Iterator<MybatisInterceptor> interceptors;
    private final Invocation invocation;
    private final MappedStatement ms;
    private final boolean isUpdate;
    private final BoundSql boundSql;
    private final StatementHandler sh;
    private final Statement statement;
    private ResultHandler resultHandler;

    public ChainFilter(Iterator<MybatisInterceptor> interceptors,Invocation invocation){
        this.interceptors = interceptors;
        this.invocation = invocation;
        this.sh = (StatementHandler) invocation.getTarget();
        Object[] args = invocation.getArgs();
        this.boundSql = sh.getBoundSql();
        PluginUtils.MPStatementHandler mpSh = PluginUtils.mpStatementHandler(sh);
        this.ms = mpSh.mappedStatement();
        SqlCommandType sct = ms.getSqlCommandType();
        this.isUpdate = sct == SqlCommandType.INSERT || sct == SqlCommandType.UPDATE || sct == SqlCommandType.DELETE;

        this.statement = (Statement) args[0];
        if (ms.getSqlCommandType() == SqlCommandType.SELECT) {
            this.resultHandler = (ResultHandler) args[1];
        }
    }

    // 获取MappedStatement的方法
    private MappedStatement getMappedStatement(StatementHandler sh) {
        // 在Mybatis中，StatementHandler通常是一个RoutingStatementHandler，它包装了实际的StatementHandler
        // 实际的StatementHandler通常是MybatisFlex生成的，可以通过反射获取内部的MappedStatement
        if (sh instanceof org.apache.ibatis.executor.statement.RoutingStatementHandler) {
            try {
                java.lang.reflect.Field delegateField = sh.getClass().getDeclaredField("delegate");
                delegateField.setAccessible(true);
                StatementHandler delegate = (StatementHandler) delegateField.get(sh);
                return getMappedStatementFromTarget(delegate);
            } catch (Exception e) {
                throw new RuntimeException("无法获取MappedStatement", e);
            }
        } else {
            return getMappedStatementFromTarget(sh);
        }
    }

    // 从目标StatementHandler获取MappedStatement
    private MappedStatement getMappedStatementFromTarget(StatementHandler target) {
        try {
            java.lang.reflect.Field mappedStatementField = target.getClass().getDeclaredField("mappedStatement");
            mappedStatementField.setAccessible(true);
            return (MappedStatement) mappedStatementField.get(target);
        } catch (Exception e) {
            throw new RuntimeException("无法获取MappedStatement", e);
        }
    }

    @SneakyThrows
    public Object proceed(){
        if (interceptors.hasNext()){
            if (isUpdate){
                return interceptors.next().aroundUpdate(this, sh, ms, boundSql, statement);
            }else if (ms.getSqlCommandType() == SqlCommandType.SELECT){
                return interceptors.next().aroundQuery(this, sh, ms, boundSql, statement, resultHandler);
            }
        }
        return invocation.proceed();
    }

}
