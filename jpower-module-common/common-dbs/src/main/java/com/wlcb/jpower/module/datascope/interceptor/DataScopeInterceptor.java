package com.wlcb.jpower.module.datascope.interceptor;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.baomidou.mybatisplus.extension.handlers.AbstractSqlParserHandler;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.StringUtil;
import com.wlcb.jpower.module.common.utils.WebUtil;
import com.wlcb.jpower.module.common.utils.constants.ConstantsEnum;
import com.wlcb.jpower.module.common.utils.constants.TokenConstant;
import com.wlcb.jpower.module.datascope.DataScope;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

import java.sql.Connection;

/**
 * mybatis 数据权限拦截器
 * <p>
 * <p>
 * 1，全部：
 * 2，本级：当前用户的orgId
 * 3，本级以及子级：当前用户的orgId以及子级的orgId
 * 4，自定义：
 * 5，个人：createUser
 * @author ding
 * @date 2020-11-04 17:16
 */
@Slf4j
@AllArgsConstructor
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})})
public class DataScopeInterceptor extends AbstractSqlParserHandler implements Interceptor {


    @Override
    @SneakyThrows
    public Object intercept(Invocation invocation) {

        StatementHandler statementHandler = PluginUtils.realTarget(invocation.getTarget());
        MetaObject metaObject = SystemMetaObject.forObject(statementHandler);
        this.sqlParser(metaObject);
        // 先判断是不是SELECT操作
        MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
        if (!SqlCommandType.SELECT.equals(mappedStatement.getSqlCommandType())) {
            return invocation.proceed();
        }

        String originalSql = ((BoundSql) metaObject.getValue("delegate.boundSql")).getSql();
        DataScope dataScope = this.findDataScope();
        if (dataScope == null) {
            return invocation.proceed();
        }

        String mapperId = mappedStatement.getId();
        if (Fc.equalsValue(mapperId,dataScope.getScopeClass())){
            // 查询全部
            if (Fc.equals(dataScope.getScopeType(), ConstantsEnum.DATA_SCOPE_TYPE.ALL.getValue())){
                return invocation.proceed();
            }

            String sqlCondition = " select {} from ({}) scope where ";
            if (Fc.equals(dataScope.getScopeType(), ConstantsEnum.DATA_SCOPE_TYPE.CUSTOM.getValue())){
                sqlCondition = StringUtil.format(sqlCondition + dataScope.getScopeValue(), Fc.toStr(dataScope.getScopeField(), "*"), originalSql);
            }else {
                sqlCondition = StringUtil.format(sqlCondition + " scope.{} in ({})", Fc.toStr(dataScope.getScopeField(), "*"), originalSql, dataScope.getScopeColumn(), StringUtil.join(dataScope.getIds()));
            }

            metaObject.setValue("delegate.boundSql.sql", sqlCondition);
        }

        return invocation.proceed();
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

    /**
     * 生成拦截对象的代理
     *
     * @param target 目标对象
     * @return 代理对象
     */
    @Override
    public Object plugin(Object target) {
        if (target instanceof StatementHandler) {
            return Plugin.wrap(target, this);
        }
        return target;
    }
}
