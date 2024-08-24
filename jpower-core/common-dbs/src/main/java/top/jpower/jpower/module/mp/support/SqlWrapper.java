package top.jpower.jpower.module.mp.support;

import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import top.jpower.core.util.support.XssInjectionUtil;
import top.jpower.core.util.utils.Fc;
import top.jpower.core.util.utils.StringUtil;

import java.util.Map;

import static top.jpower.jpower.module.mp.support.WrapperKeyword.IGNORE;
import static top.jpower.jpower.module.mp.support.WrapperKeyword.LIKE;

/**
 * SQL条件构造器
 *
 * @author mr.g
 */
class SqlWrapper {


    private final static String[] SQL_REGEX = ArrayUtil.append(XssInjectionUtil.SQL_CHAR,"--","count","group","union","alter","grant","execute","exec","xp_cmdshell","call","declare","sql");

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

            String column = filter(keyword.getColumn(k));
            if (Fc.isBlank(column)){
                keyword = IGNORE;
            }
            
            switch (keyword){
                case EQ:
                case DATE_EQ:
                    wrapper.eq(column, v);
                    break;
                case NOT_EQ:
                    wrapper.ne(column, v);
                    break;
                case LIKE:
                    wrapper.like(column, v);
                    break;
                case LEFT_LIKE:
                    wrapper.likeLeft(column, v);
                    break;
                case RIGHT_LIKE:
                    wrapper.likeRight(column, v);
                    break;
                case NOT_LIKE:
                    wrapper.notLike(column, v);
                    break;
                case GT:
                case DATE_GT:
                    wrapper.gt(column, v);
                    break;
                case LT:
                case DATE_LT:
                    wrapper.lt(column, v);
                    break;
                case GE:
                case DATE_GE:
                    wrapper.ge(column, v);
                    break;
                case LE:
                case DATE_LE:
                    wrapper.le(column, v);
                    break;
                case IS_NULL:
                    wrapper.isNull(column);
                    break;
                case NOT_NULL:
                    wrapper.isNotNull(column);
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
                return keyword;
            }
        }

        return LIKE;
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
