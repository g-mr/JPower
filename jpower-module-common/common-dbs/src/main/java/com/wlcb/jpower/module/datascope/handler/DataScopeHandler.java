package com.wlcb.jpower.module.datascope.handler;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.handler.DataPermissionHandler;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.WebUtil;
import com.wlcb.jpower.module.common.utils.constants.ConstantsEnum;
import com.wlcb.jpower.module.common.utils.constants.TokenConstant;
import com.wlcb.jpower.module.datascope.DataScope;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.expression.operators.relational.ItemsList;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;

import java.util.stream.Collectors;

/**
 * @Author mr.g
 * @Date 2021/4/23 0023 22:25
 */
@Slf4j
public class DataScopeHandler implements DataPermissionHandler {


    @SneakyThrows
    @Override
    public Expression getSqlSegment(Expression where, String mapperId) {
        DataScope dataScope = this.findDataScope();
        if (Fc.isNull(dataScope)) {
            return where;
        }

        if (Fc.equalsValue(mapperId,dataScope.getScopeClass())){
            // 查询全部
            if (Fc.equals(dataScope.getScopeType(), ConstantsEnum.DATA_SCOPE_TYPE.ALL.getValue())){
                return where;
            }

            Expression andWhere;
            if (Fc.equals(dataScope.getScopeType(), ConstantsEnum.DATA_SCOPE_TYPE.CUSTOM.getValue())){
                andWhere = CCJSqlParserUtil.parseCondExpression(Fc.toStr(dataScope.getScopeValue(),"1=1"));
            }else {
                if (dataScope.getIds().size() > 0){
                    ItemsList itemsList = new ExpressionList(dataScope.getIds().stream().map(StringValue::new).collect(Collectors.toList()));
                    andWhere = new InExpression(new Column(dataScope.getScopeColumn()),itemsList);
                }else {
                    return where;
                }
            }
            log.debug("DATASCOPE WHERE : {}",andWhere.toString());
            return where==null?andWhere:new AndExpression(where,andWhere);
        }
        return where;
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
