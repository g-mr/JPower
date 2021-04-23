package com.wlcb.jpower.module.datascope.interceptor;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.wlcb.jpower.module.common.utils.*;
import com.wlcb.jpower.module.common.utils.constants.ConstantsEnum;
import com.wlcb.jpower.module.common.utils.constants.TokenConstant;
import com.wlcb.jpower.module.config.QueryInterceptor;
import com.wlcb.jpower.module.datascope.DataScope;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.StatementType;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

import static com.wlcb.jpower.module.common.utils.constants.StringPool.COMMA;
import static com.wlcb.jpower.module.common.utils.constants.StringPool.SINGLE_QUOTE;

/**
 * @author mr.g
 * @date 2021-04-23 11:56
 */
public class DataScopeQueryInterceptor implements QueryInterceptor {
    @Override
    public void intercept(Executor executor, MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) {
        if (SecureUtil.isRoot()){
            return;
        }
//        if (Fc.isNull(SecureUtil.getUser())){
//            return;
//        }

        if (SqlCommandType.SELECT != ms.getSqlCommandType() || StatementType.CALLABLE == ms.getStatementType()) {
            return;
        }

        DataScope dataScope = this.findDataScope();
        if (Fc.isNull(dataScope)) {
            return;
        }

        String originalSql = boundSql.getSql();
        String mapperId = ms.getId();

        if (Fc.equalsValue(mapperId,dataScope.getScopeClass())){
            // 查询全部
            if (Fc.equals(dataScope.getScopeType(), ConstantsEnum.DATA_SCOPE_TYPE.ALL.getValue())){
                return;
            }

            String sqlCondition = " select {} from ({}) scope where ";

            if (Fc.equals(dataScope.getScopeType(), ConstantsEnum.DATA_SCOPE_TYPE.CUSTOM.getValue())){
                sqlCondition = StringUtil.format(sqlCondition + dataScope.getScopeValue(), Fc.toStr(dataScope.getScopeField(), "*"), originalSql);
            }else {
                sqlCondition = StringUtil.format(sqlCondition + " scope.{} in ({})", Fc.toStr(dataScope.getScopeField(), "*"), originalSql, dataScope.getScopeColumn(), StringUtil.collectionToDelimitedString(dataScope.getIds(), COMMA,SINGLE_QUOTE,SINGLE_QUOTE));
            }

            PluginUtils.MPBoundSql mpBoundSql = PluginUtils.mpBoundSql(boundSql);
            mpBoundSql.sql(sqlCondition);
        }
    }

    /**
     * 获取数据权限
     *
     * @Author ding
     * @Date 14:46 2020-11-05
     **/
    private DataScope findDataScope() {
        if (Fc.isNull(WebUtil.getRequest())){
            return null;
        }

        String data = WebUtil.getRequest().getHeader(TokenConstant.DATA_SCOPE_NAME);
        if (Fc.isNotBlank(data)){
            return JSON.parseObject(data,DataScope.class);
        }

        return null;
    }
}
