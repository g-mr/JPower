package top.jpower.jpower.module.config.interceptor;


import top.jpower.jpower.module.common.utils.ClassUtil;
import top.jpower.jpower.module.common.utils.Fc;
import top.jpower.jpower.module.config.interceptor.chain.ChainFilter;
import top.jpower.jpower.module.config.interceptor.chain.MybatisInterceptor;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;

import java.sql.Statement;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * mybatis拦截器
 * @author mr.g
 * @date 2021-05-20 17:00
 */
@Intercepts(
    {
        @Signature(type = StatementHandler.class, method = "update", args = {Statement.class}),
        @Signature(type = StatementHandler.class, method = "batch", args = {Statement.class}),
        @Signature(type = StatementHandler.class, method = "query", args = {Statement.class, ResultHandler.class}),
        @Signature(type = ResultSetHandler.class, method = "handleResultSets",args = {Statement.class})
    }
)
@AllArgsConstructor
public class JpowerMybatisInterceptor implements Interceptor {

    List<MybatisInterceptor> interceptors;

    @Override
    @SneakyThrows
    public Object intercept(Invocation invocation) {
        Object target = invocation.getTarget();
        if (target instanceof StatementHandler){
            return new ChainFilter(interceptors.iterator(),invocation).proceed();
        }else if (target instanceof ResultSetHandler){
            Object rest = invocation.proceed();
            if (rest instanceof List){
                List list = (List) rest;
                // 不拦截count,sum等只返回一个值得结果
                if (!(list.size() == 1 && Fc.notNull(list.get(0)) && ClassUtil.isSimpleValueType(ClassUtil.getClass(list.get(0))))){
                    Statement statement = (Statement) invocation.getArgs()[0];
                    ResultSetHandler resultSetHandler = (ResultSetHandler) target;
                    AtomicReference<Object> atomicReference = new AtomicReference<>(rest);
                    interceptors.forEach( interceptor -> atomicReference.set(interceptor.result(atomicReference.get(),resultSetHandler,statement)));
                    return atomicReference.get();
                }
            }

            return rest;
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof StatementHandler || target instanceof ResultSetHandler) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

}
