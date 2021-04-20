package com.wlcb.jpower.module.datascope.interceptor;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.parser.ISqlParser;
import com.baomidou.mybatisplus.core.parser.SqlInfo;
import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.baomidou.mybatisplus.extension.handlers.AbstractSqlParserHandler;
import com.baomidou.mybatisplus.extension.plugins.pagination.optimize.JsqlParserCountOptimize;
import com.wlcb.jpower.module.common.utils.*;
import com.wlcb.jpower.module.common.utils.constants.ConstantsEnum;
import com.wlcb.jpower.module.common.utils.constants.TokenConstant;
import com.wlcb.jpower.module.datascope.DataScope;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.parser.JSqlParser;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

import java.sql.Connection;
import java.util.List;

import static com.wlcb.jpower.module.common.utils.constants.StringPool.*;

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

    private final String COUNT = "_COUNT";

    @Override
    @SneakyThrows
    public Object intercept(Invocation invocation) {
        if (SecureUtil.isRoot()){
            return invocation.proceed();
        }

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
        if (Fc.equalsValue(mapperId,dataScope.getScopeClass()) || Fc.equalsValue(mapperId,dataScope.getScopeClass().concat(COUNT))){
            // 查询全部
            if (Fc.equals(dataScope.getScopeType(), ConstantsEnum.DATA_SCOPE_TYPE.ALL.getValue())){
                return invocation.proceed();
            }

            String sqlCondition = " select {} from ({}) scope where ";
            //分页查询
            if (Fc.equalsValue(mapperId,dataScope.getScopeClass().concat(COUNT))){
                //联合查询需要谨慎使用
                List<String> columns = SqlUtil.getSelectSQL(mappedStatement.getSqlSource().getBoundSql(statementHandler.getParameterHandler().getParameterObject()).getSql());
                String column = Fc.isNull(columns) || columns.size() <= 0?"*":Fc.join(columns);
                originalSql = originalSql.replaceFirst("count\\(([0-9]|\\*)\\)",column);
                dataScope.setScopeField("count(0)");
            }

            if (Fc.equals(dataScope.getScopeType(), ConstantsEnum.DATA_SCOPE_TYPE.CUSTOM.getValue())){
                sqlCondition = StringUtil.format(sqlCondition + dataScope.getScopeValue(), Fc.toStr(dataScope.getScopeField(), "*"), originalSql);
            }else {
                sqlCondition = StringUtil.format(sqlCondition + " scope.{} in ({})", Fc.toStr(dataScope.getScopeField(), "*"), originalSql, dataScope.getScopeColumn(), StringUtil.collectionToDelimitedString(dataScope.getIds(), COMMA,SINGLE_QUOTE,SINGLE_QUOTE));
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
