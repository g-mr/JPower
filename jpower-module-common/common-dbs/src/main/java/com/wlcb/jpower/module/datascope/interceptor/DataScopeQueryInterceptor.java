package com.wlcb.jpower.module.datascope.interceptor;

import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.SecureUtil;
import com.wlcb.jpower.module.config.QueryInterceptor;
import com.wlcb.jpower.module.datascope.handler.DataScopeHandler;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.StatementType;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

/**
 * @author mr.g
 * @date 2021-04-23 11:56
 */
@RequiredArgsConstructor
public class DataScopeQueryInterceptor implements QueryInterceptor {

    private final DataScopeHandler dataScopeHandler;

    @Override
    public void intercept(Executor executor, MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) {
        if (SecureUtil.isRoot()){
            return;
        }
        if (Fc.isNull(SecureUtil.getUser())){
            return;
        }

        if (SqlCommandType.SELECT != ms.getSqlCommandType() || StatementType.CALLABLE == ms.getStatementType()) {
            return;
        }

        String originalSql = boundSql.getSql();
        String mapperId = ms.getId();


        String sqlCondition = dataScopeHandler.sqlCondition(mapperId,originalSql);
        if (Fc.isNotBlank(sqlCondition)){
            PluginUtils.MPBoundSql mpBoundSql = PluginUtils.mpBoundSql(boundSql);
            mpBoundSql.sql(sqlCondition);
        }
    }

}
