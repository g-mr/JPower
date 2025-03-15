package top.jpower.core.dbs.config.interceptor.chain;

import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import lombok.SneakyThrows;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.session.ResultHandler;

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
