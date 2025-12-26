package top.jpower.core.dbs.config.interceptor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.springframework.util.AntPathMatcher;
import top.jpower.core.dbs.config.interceptor.chain.ChainFilter;
import top.jpower.core.dbs.config.interceptor.chain.MybatisInterceptor;
import top.jpower.core.dbs.config.properties.DemoProperties;
import top.jpower.core.util.utils.Fc;
import top.jpower.core.util.utils.WebUtil;

import java.sql.Statement;

/**
 * @ClassName DemoInterceptor
 * @Description TODO 演示环境拦截器
 * @Author 郭丁志
 * @Date 2021/3/5 0005 22:31
 * @Version 1.0
 */
@Slf4j
@RequiredArgsConstructor
public class DemoInterceptor implements MybatisInterceptor {

    private final DemoProperties properties;
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public Object aroundUpdate(ChainFilter chainFilter, final StatementHandler sh, MappedStatement ms, BoundSql boundSql, Statement statement) {

        if (properties.isEnable()){
            String path = Fc.notNull(WebUtil.getRequest()) ? WebUtil.getRequest().getServletPath() : null;

            // 匹配的接口进行放行
            if (Fc.notNull(path) && properties.getSkipUrl().stream().anyMatch(pattern -> antPathMatcher.match(pattern, path))){
                return chainFilter.proceed();
            }

            log.warn("拦截到操作数据得SQL,演示环境不可操作数据");
            return 0;
        }
        return chainFilter.proceed();
    }
}
