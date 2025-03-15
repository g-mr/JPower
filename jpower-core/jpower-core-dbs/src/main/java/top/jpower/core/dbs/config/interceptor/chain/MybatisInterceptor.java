package top.jpower.core.dbs.config.interceptor.chain;

import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.ResultHandler;

import java.sql.Statement;

/**
 * @Author mr.g
 * @Date 2021/11/13 0013 1:06
 */
public interface MybatisInterceptor {

    /**
     * 环绕update拦截
     * @Desc chainFilter.proceed表示继续执行
     * @author mr.g
     * @return java.lang.Object sql返回值
     */
    default Object aroundUpdate(ChainFilter chainFilter, final StatementHandler sh, MappedStatement ms, BoundSql boundSql, Statement statement){
        return chainFilter.proceed();
    }

    /**
     * 环绕select拦截
     * @Desc  chainFilter.proceed表示继续执行
     * @author mr.g
     * @return java.lang.Object sql返回值
     */
    default Object aroundQuery(ChainFilter chainFilter, final StatementHandler sh, MappedStatement ms, BoundSql boundSql, Statement statement, ResultHandler resultHandler){
        return chainFilter.proceed();
    }

    /**
     * 结果集拦截
     * @Desc  chainFilter.proceed表示继续执行
     * @author mr.g
     * @return java.lang.Object 结果集
     */
    default Object result(Object result, ResultSetHandler resultSetHandler, Statement statement){
        return result;
    }
}
