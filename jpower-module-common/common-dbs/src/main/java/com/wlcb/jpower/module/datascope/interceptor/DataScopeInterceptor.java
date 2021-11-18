package com.wlcb.jpower.module.datascope.interceptor;

import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.SecureUtil;
import com.wlcb.jpower.module.datascope.handler.DataScopeHandler;
import lombok.AllArgsConstructor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.StatementType;
import org.springframework.core.Ordered;

/**
 * @author mr.g
 * @date 2021-04-23 11:56
 */
@AllArgsConstructor
public class DataScopeInterceptor implements Ordered {

    private final DataScopeHandler dataScopeHandler;

    public void intercept(MappedStatement ms, BoundSql boundSql) {
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


//        String sqlCondition = dataScopeHandler.sql(mapperId,originalSql);
//        if (Fc.isNotBlank(sqlCondition)){
//            PluginUtils.MPBoundSql mpBoundSql = PluginUtils.mpBoundSql(boundSql);
//            mpBoundSql.sql(sqlCondition);
//        }
    }

    @Override
    public int getOrder(){
        return Ordered.LOWEST_PRECEDENCE;
    };
}
