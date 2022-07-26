package com.wlcb.jpower.module.datascope.handler;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.LambdaUtils;
import com.baomidou.mybatisplus.extension.plugins.handler.DataPermissionHandler;
import com.wlcb.jpower.module.common.auth.UserInfo;
import com.wlcb.jpower.module.common.support.ChainMap;
import com.wlcb.jpower.module.common.utils.*;
import com.wlcb.jpower.module.common.utils.constants.CharPool;
import com.wlcb.jpower.module.common.utils.constants.ConstantsEnum;
import com.wlcb.jpower.module.common.utils.constants.StringPool;
import com.wlcb.jpower.module.common.utils.constants.TokenConstant;
import com.wlcb.jpower.module.datascope.DataScope;
import com.wlcb.jpower.module.dbs.config.LoginUserContext;
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
import org.apache.ibatis.reflection.property.PropertyNamer;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
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
        //超级管理员不判断数据权限
        if (Fc.isNull(dataScope) || SecureUtil.isRoot()) {
            return where;
        }

        if (Fc.equalsValue(mapperId,dataScope.getScopeClass())){

            if (Fc.isNull(LoginUserContext.get())){
                log.warn("未获取到用户，无法进行数据权限过滤");
                return CCJSqlParserUtil.parseCondExpression("1 = 2");
            }

            // 查询全部
            if (Fc.equals(dataScope.getScopeType(), ConstantsEnum.DATA_SCOPE_TYPE.ALL.getValue())){
                return where;
            }

            Expression andWhere;
            if (Fc.equals(dataScope.getScopeType(), ConstantsEnum.DATA_SCOPE_TYPE.CUSTOM.getValue())){
                Map<String,Object> userMap = ChainMap.<String,Object>create().build();

                BeanUtil.beanToMap(LoginUserContext.get(),userMap,new CopyOptions(){
                    @Override
                    protected Object editFieldValue(String fieldName, Object fieldValue) {

                        switch (fieldName){
                            case "birthday":
                            case "lastLoginTime":
                                fieldValue = Fc.isNull(fieldValue) ? DateUtil.now() : fieldValue;
                                break;
                            case "idType":
                            case "loginCount":
                            case "userType":
                                fieldValue = Fc.isNull(fieldValue) ? -999 : fieldValue;
                                break;
                            case "roleIds":
                            case "childOrgId":
                                fieldValue = Fc.isNull(fieldValue) ? new ArrayList<>() : fieldValue;
                                break;
                            default:
                                fieldValue = Fc.isNull(fieldValue) ? StringPool.EMPTY : fieldValue;
                                break;
                        }

                        return fieldValue;
                    }
                });

                userMap.put(PropertyNamer.methodToProperty(LambdaUtils.extract(UserInfo::getRoleIds).getImplMethodName()), StringUtils.collectionToDelimitedString(LoginUserContext.get().getRoleIds(), StringPool.COMMA,StringPool.SINGLE_QUOTE,StringPool.SINGLE_QUOTE));

                Set<String> listOrgId = CollectionUtil.newHashSet(LoginUserContext.get().getChildOrgId());
                listOrgId.add(LoginUserContext.getOrgId());
                userMap.put(PropertyNamer.methodToProperty(LambdaUtils.extract(UserInfo::getChildOrgId).getImplMethodName()), StringUtils.collectionToDelimitedString(listOrgId, StringPool.COMMA,StringPool.SINGLE_QUOTE,StringPool.SINGLE_QUOTE));

                andWhere = CCJSqlParserUtil.parseCondExpression(StringUtil.format(Fc.toStr(dataScope.getScopeValue(),"1=1"),userMap));
            }else if (Fc.equals(dataScope.getScopeType(), ConstantsEnum.DATA_SCOPE_TYPE.OWN.getValue())){
                andWhere = new EqualsTo().withLeftExpression(new Column(dataScope.getScopeColumn())).withRightExpression(new StringValue(LoginUserContext.getUserId()));
            }else if (Fc.equals(dataScope.getScopeType(), ConstantsEnum.DATA_SCOPE_TYPE.OWN_ORG.getValue())){
                andWhere = new EqualsTo().withLeftExpression(new Column(dataScope.getScopeColumn())).withRightExpression(new StringValue(LoginUserContext.getOrgId()));
            }else if (Fc.equals(dataScope.getScopeType(), ConstantsEnum.DATA_SCOPE_TYPE.OWN_ORG_CHILD.getValue())){
                Set<String> listOrgId = CollectionUtil.newHashSet(LoginUserContext.get().getChildOrgId());
                listOrgId.add(LoginUserContext.getOrgId());
                //如果没有部门就什么都不要查出来
                listOrgId.add("-1");
                ItemsList itemsList = new ExpressionList(listOrgId.stream().filter(Fc::isNotBlank).map(StringValue::new).collect(Collectors.toList()));
                andWhere = new InExpression(new Column(dataScope.getScopeColumn()),itemsList);
            }else {
                return where;
            }

            andWhere = CCJSqlParserUtil.parseCondExpression(StringPool.LEFT_BRACKET+andWhere.toString()+ StringPool.RIGHT_BRACKET);
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
            List<DataScope> dataScopeList = JSON.parseArray(data,DataScope.class);
            DataScope dataScope =  dataScopeList.stream().filter(d -> Fc.equalsValue(d.getScopeClass(),mapperId)).findFirst().orElse(null);
            if (Fc.notNull(dataScope) && Fc.isNotBlank(dataScope.getScopeClass())){
                return dataScope;
            }
        }

        //从注解获取数据权限
        try {
            String methodName = StringUtil.subAfter(mapperId, CharPool.DOT, Boolean.TRUE);
            String className = StringUtil.subBefore(mapperId, CharPool.DOT, Boolean.TRUE);
            com.wlcb.jpower.module.datascope.annotation.DataScope dataScopeAnnotation = AnnotationUtil.getAnnotation(ReflectUtil.getMethodByName(Class.forName(className),methodName), com.wlcb.jpower.module.datascope.annotation.DataScope.class);
            if (Fc.notNull(dataScopeAnnotation)){
                DataScope dataScope = new DataScope();
                dataScope.setScopeType(dataScopeAnnotation.type().getValue());
                dataScope.setScopeColumn(dataScopeAnnotation.column());
                dataScope.setScopeClass(mapperId);
                dataScope.setScopeValue(dataScopeAnnotation.sql());
                return dataScope;
            }
        } catch (ClassNotFoundException e) {
            log.warn("数据权限未找到类={}",mapperId);
        }

        return null;

    }

}
