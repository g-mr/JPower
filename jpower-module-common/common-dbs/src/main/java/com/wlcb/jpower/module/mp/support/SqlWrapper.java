package com.wlcb.jpower.module.mp.support;

import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.SqlInjectionUtil;
import com.wlcb.jpower.module.common.utils.StringUtil;

import java.util.Map;

import static com.wlcb.jpower.module.mp.support.WrapperKeyword.LIKE;

/**
 * 定义常用的 sql关键字
 *
 * @author mr.g
 */
public class SqlWrapper {


    private final static String[] SQL_REGEX = ArrayUtil.append(SqlInjectionUtil.SQL_CHAR,"--","count","group","union","alter","grant","execute","exec","xp_cmdshell","call","declare","sql");

    /**
     * 条件构造器
     *
     * @param wrapper   查询包装类
     * @param query     查询字段
     */
    static void buildCondition(AbstractWrapper<?, String, ?> wrapper, Map<String, Object> query) {
        if (Fc.isEmpty(query)) {
            return;
        }
        query.forEach((k, v) -> {
            if (Fc.hasEmpty(k, v)) {
                return;
            }

            WrapperKeyword keyword = getKeyword(k);

            switch (keyword){
                case EQ:
                case DATE_EQ:
                    wrapper.eq(keyword.getColumn(), v);
                    break;
                case NOT_EQ:
                    wrapper.ne(keyword.getColumn(), v);
                    break;
                case LIKE:
                    wrapper.like(keyword.getColumn(), v);
                    break;
                case NOT_LIKE:
                    wrapper.notLike(keyword.getColumn(), v);
                    break;
                case GT:
                case DATE_GT:
                    wrapper.gt(keyword.getColumn(), v);
                    break;
                case LT:
                case DATE_LT:
                    wrapper.lt(keyword.getColumn(), v);
                    break;
                case GE:
                case DATE_GE:
                    wrapper.ge(keyword.getColumn(), v);
                    break;
                case LE:
                case DATE_LE:
                    wrapper.le(keyword.getColumn(), v);
                    break;
                case IS_NULL:
                    wrapper.isNull(keyword.getColumn());
                    break;
                case NOT_NULL:
                    wrapper.isNotNull(keyword.getColumn());
                case IGNORE:
                default:
                    break;
            }
        });
    }

    /**
     * 获取数据库字段
     *
     * @param column  字段名
     * @return String
     */
    private static WrapperKeyword getKeyword(String column) {

        for (WrapperKeyword keyword : WrapperKeyword.values()){
            if (StringUtil.endWith(column,keyword.getSuffixKeyword())){
                column = StringUtil.humpToUnderline(StringUtil.removeSuffix(column, keyword.getSuffixKeyword()));
                keyword.setColumn(filter(column));
                return keyword;
            }
        }

        WrapperKeyword keyword = LIKE;
        keyword.setColumn(filter(StringUtil.humpToUnderline(column)));
        return keyword;
    }

    /**
     * 安全校验
     *
     * @param param 关键字
     * @return string
     */
    private static String filter(String param) {
        if (Fc.isBlank(param)) {
            return null;
        }

        param = StringUtil.cleanBlank(param);

        if (StringUtil.containsAny(param,SQL_REGEX)){
            param = "`"+param+"`";
        }

        return param;
    }

}
