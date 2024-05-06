package top.jpower.jpower.module.datascope.handler;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.toolkit.LambdaUtils;
import com.baomidou.mybatisplus.extension.plugins.handler.DataPermissionHandler;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.expression.operators.relational.ItemsList;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import org.apache.ibatis.reflection.property.PropertyNamer;
import org.springframework.util.StringUtils;
import top.jpower.core.util.constants.CharPool;
import top.jpower.core.util.constants.StringPool;
import top.jpower.core.util.constants.TokenConstant;
import top.jpower.core.util.utils.*;
import top.jpower.jpower.module.common.auth.UserInfo;
import top.jpower.jpower.module.common.utils.ShieldUtil;
import top.jpower.jpower.module.constants.DataScopeConstant;
import top.jpower.jpower.module.datascope.DataScope;
import top.jpower.jpower.module.dbs.config.LoginUserContext;

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
        if (Fc.isNull(dataScope) || ShieldUtil.isRoot()) {
            return where;
        }

        if (Fc.equalsValue(mapperId,dataScope.getScopeClass())){

            if (Fc.isNull(LoginUserContext.get())){
                log.warn("未获取到用户，无法进行数据权限过滤");
                return CCJSqlParserUtil.parseCondExpression("1 = 2");
            }

            // 查询全部
            if (Fc.equalsValue(dataScope.getScopeType(), DataScopeConstant.ALL)){
                return where;
            }

            Expression andWhere;
            if (Fc.equalsValue(dataScope.getScopeType(), DataScopeConstant.CUSTOM)){
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
                            case "orgId":
                            case "userId":
                                fieldValue = Fc.isNull(fieldValue) ? -1L : fieldValue;
                                break;
                            default:
                                fieldValue = Fc.isNull(fieldValue) ? StringPool.EMPTY : fieldValue;
                                break;
                        }

                        return fieldValue;
                    }
                });

                Set<Long> roleIds = CollectionUtil.newHashSet(LoginUserContext.get().getRoleIds());
                if (Fc.isEmpty(roleIds)){
                    //如果没有角色证明看不到数据
                    roleIds.add(-1L);
                }
                userMap.put(PropertyNamer.methodToProperty(LambdaUtils.extract(UserInfo::getRoleIds).getImplMethodName()), StringUtils.collectionToDelimitedString(roleIds, StringPool.COMMA,StringPool.SINGLE_QUOTE,StringPool.SINGLE_QUOTE));

                Set<Long> listOrgId = CollectionUtil.newHashSet(LoginUserContext.get().getChildOrgId());
                if (Fc.isEmpty(listOrgId)){
                    //如果没有子级部门证明看不到数据
                    listOrgId.add(-1L);
                }
                userMap.put(PropertyNamer.methodToProperty(LambdaUtils.extract(UserInfo::getChildOrgId).getImplMethodName()), StringUtils.collectionToDelimitedString(listOrgId, StringPool.COMMA,StringPool.SINGLE_QUOTE,StringPool.SINGLE_QUOTE));

                andWhere = CCJSqlParserUtil.parseCondExpression(StringUtil.formatMap(Fc.toStr(dataScope.getScopeValue(),"1=1"),userMap));
            }else if (Fc.equalsValue(dataScope.getScopeType(), DataScopeConstant.OWN)){
                andWhere = new EqualsTo().withLeftExpression(new Column(dataScope.getScopeColumn())).withRightExpression(new LongValue(Fc.toLong(LoginUserContext.getUserId(), -1L)));
            }else if (Fc.equalsValue(dataScope.getScopeType(), DataScopeConstant.OWN_ORG)){
                andWhere = new EqualsTo().withLeftExpression(new Column(dataScope.getScopeColumn())).withRightExpression(new LongValue(Fc.toLong(LoginUserContext.getOrgId(), -1L)));
            }else if (Fc.equalsValue(dataScope.getScopeType(), DataScopeConstant.OWN_ORG_CHILD)){
                Set<Long> listOrgId = CollectionUtil.newHashSet(LoginUserContext.get().getChildOrgId());
                listOrgId.add(LoginUserContext.getOrgId());
                //如果没有部门就什么都不要查出来
                listOrgId.add(-1L);
                ItemsList itemsList = new ExpressionList(listOrgId.stream().filter(Fc::notNull).map(LongValue::new).collect(Collectors.toList()));
                andWhere = new InExpression(new Column(dataScope.getScopeColumn()),itemsList);
            }else {
                return where;
            }

            andWhere = CCJSqlParserUtil.parseCondExpression(StringPool.LEFT_BRACKET+andWhere.toString()+ StringPool.RIGHT_BRACKET);
            if (log.isDebugEnabled()){
                log.info("DATASCOPE WHERE : {}",andWhere.toString());
            }
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
            top.jpower.jpower.module.datascope.annotation.DataScope dataScopeAnnotation = AnnotationUtil.getAnnotation(ReflectUtil.getMethodByName(Class.forName(className),methodName), top.jpower.jpower.module.datascope.annotation.DataScope.class);
            if (Fc.notNull(dataScopeAnnotation)){
                DataScope dataScope = new DataScope();
                dataScope.setScopeType(dataScopeAnnotation.type());
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
