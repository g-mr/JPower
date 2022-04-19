package com.wlcb.jpower.module.datascope.handler;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.handler.DataPermissionHandler;
import com.sun.org.apache.xpath.internal.operations.Equals;
import com.wlcb.jpower.module.common.utils.*;
import com.wlcb.jpower.module.common.utils.constants.ConstantsEnum;
import com.wlcb.jpower.module.common.utils.constants.StringPool;
import com.wlcb.jpower.module.common.utils.constants.TokenConstant;
import com.wlcb.jpower.module.datascope.DataScope;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.expression.operators.relational.ItemsList;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import org.apache.xmlbeans.impl.common.SystemCache;
import org.springframework.cache.CacheManager;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Set;
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
        DataScope dataScope = this.findDataScope(mapperId);
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
                Map<String,Object> userMap = BeanUtil.getFieldValueMap(SecureUtil.getUser());

                userMap.put("roleIds", StringUtils.collectionToDelimitedString(SecureUtil.getUserRole(), StringPool.COMMA,StringPool.SINGLE_QUOTE,StringPool.SINGLE_QUOTE));

                Set<String> listOrgId = CollectionUtil.newHashSet(SecureUtil.getUser().getChildOrgId());
                listOrgId.add(SecureUtil.getOrgId());
                userMap.put("childOrgId", StringUtils.collectionToDelimitedString(listOrgId, StringPool.COMMA,StringPool.SINGLE_QUOTE,StringPool.SINGLE_QUOTE));

                andWhere = CCJSqlParserUtil.parseCondExpression(StringUtil.format(Fc.toStr(dataScope.getScopeValue(),"1=1"),userMap));
            }else if (Fc.equals(dataScope.getScopeType(), ConstantsEnum.DATA_SCOPE_TYPE.OWN.getValue())){
                andWhere = new EqualsTo().withLeftExpression(new Column(dataScope.getScopeColumn())).withRightExpression(new StringValue(SecureUtil.getUserId()));
            }else if (Fc.equals(dataScope.getScopeType(), ConstantsEnum.DATA_SCOPE_TYPE.OWN_ORG.getValue())){
                andWhere = new EqualsTo().withLeftExpression(new Column(dataScope.getScopeColumn())).withRightExpression(new StringValue(SecureUtil.getOrgId()));
            }else if (Fc.equals(dataScope.getScopeType(), ConstantsEnum.DATA_SCOPE_TYPE.OWN_ORG_CHILD.getValue())){
                Set<String> listOrgId = CollectionUtil.newHashSet(SecureUtil.getUser().getChildOrgId());
                listOrgId.add(SecureUtil.getOrgId());
                ItemsList itemsList = new ExpressionList(listOrgId.stream().map(StringValue::new).collect(Collectors.toList()));
                andWhere = new InExpression(new Column(dataScope.getScopeColumn()),itemsList);
            }else {
                return where;
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
    private DataScope findDataScope(String mapperId) {
        //没有HttpServletRequest不走数据权限
        if (Fc.isNull(WebUtil.getRequest())){
            return null;
        }

        //从WEB获取数据权限
        String data = WebUtil.getRequest().getHeader(TokenConstant.DATA_SCOPE_NAME);
        if (Fc.isNotBlank(data)){
            return JSON.parseObject(data,DataScope.class);
        }

        //从注解获取数据权限


        return null;

    }

}
