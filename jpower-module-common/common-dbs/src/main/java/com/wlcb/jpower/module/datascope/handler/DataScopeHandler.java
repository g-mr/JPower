package com.wlcb.jpower.module.datascope.handler;

import com.alibaba.fastjson.JSON;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.StringUtil;
import com.wlcb.jpower.module.common.utils.WebUtil;
import com.wlcb.jpower.module.common.utils.constants.ConstantsEnum;
import com.wlcb.jpower.module.common.utils.constants.TokenConstant;
import com.wlcb.jpower.module.datascope.DataScope;
import org.springframework.util.StringUtils;

import static com.wlcb.jpower.module.common.utils.constants.StringPool.COMMA;
import static com.wlcb.jpower.module.common.utils.constants.StringPool.SINGLE_QUOTE;

/**
 * @Author mr.g
 * @Date 2021/4/23 0023 22:25
 */
public class DataScopeHandler {


    /**
     * 生成sql
     * @author mr.g
     * @param mapperId
     * @param originalSql
     * @return java.lang.String
     */
    public String sql(String mapperId, String originalSql) {
        DataScope dataScope = this.findDataScope();
        if (Fc.isNull(dataScope)) {
            return null;
        }

        if (Fc.equalsValue(mapperId,dataScope.getScopeClass())){
            // 查询全部
            if (Fc.equals(dataScope.getScopeType(), ConstantsEnum.DATA_SCOPE_TYPE.ALL.getValue())){
                return null;
            }

            String sqlCondition = " select {} from ({}) scope where ";
            if (Fc.equals(dataScope.getScopeType(), ConstantsEnum.DATA_SCOPE_TYPE.CUSTOM.getValue())){
                sqlCondition = StringUtil.format(sqlCondition + dataScope.getScopeValue(), Fc.toStr(dataScope.getScopeField(), "*"), originalSql);
            }else {
                sqlCondition = StringUtil.format(sqlCondition + " scope.{} in ({})", Fc.toStr(dataScope.getScopeField(), "*"), originalSql, dataScope.getScopeColumn(), StringUtils.collectionToDelimitedString(dataScope.getIds(), COMMA,SINGLE_QUOTE,SINGLE_QUOTE));
            }
            return sqlCondition;
        }
        return null;
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
