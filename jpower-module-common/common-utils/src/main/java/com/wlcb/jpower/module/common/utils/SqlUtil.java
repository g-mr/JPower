package com.wlcb.jpower.module.common.utils;


import lombok.SneakyThrows;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectItem;
import org.apache.commons.lang3.StringUtils;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Sql工具
 *
 * @author mr.g
 */
public class SqlUtil
{
    /**
     * 仅支持字母、数字、下划线、空格、逗号（支持多个字段排序）
     */
    public final static String SQL_PATTERN = "[a-zA-Z\\d_\\ \\,]+";

    /**
     * 检查Order By字符，防止注入绕过
     *
     * @author mr.g
     * @param value ORDER BY SQL
     * @return 转换后的SQL
     **/
    public static String escapeOrderBySql(String value) {
        if (StringUtil.isNotBlank(value) && !isValidOrderBySql(value)) {
            return StringUtils.EMPTY;
        }
        return value;
    }

    /**
     * 验证 order by 语法是否符合规范
     *
     * @author mr.g
     * @param orderBy ORDER BY SQL
     * @return boolean
     **/
    public static boolean isValidOrderBySql(String orderBy) {
        return orderBy.matches(SQL_PATTERN);
    }


    /**
     * 获取查询sql中select部分
     *
     * @author mr.g
     * @param sql SQL语句
     * @return 查询字段
     **/
    @SneakyThrows(JSQLParserException.class)
    public static List<String> getSelectSql(String sql){
        CCJSqlParserManager parserManager = new CCJSqlParserManager();
        Select select = (Select) parserManager.parse(new StringReader(sql));
        PlainSelect plain = (PlainSelect) select.getSelectBody();
        List<SelectItem> selectItems = plain.getSelectItems();
        List<String> items = new ArrayList<>();
        if (selectItems != null) {
            for (SelectItem selectItem : selectItems) {
                items.add(selectItem.toString());
            }
        }
        return items;
    }

}
