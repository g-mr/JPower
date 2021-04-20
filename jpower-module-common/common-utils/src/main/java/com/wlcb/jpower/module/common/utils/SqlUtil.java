package com.wlcb.jpower.module.common.utils;


import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.parser.SimpleNode;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.Statements;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.*;
import net.sf.jsqlparser.statement.update.Update;
import net.sf.jsqlparser.util.TablesNamesFinder;
import org.apache.commons.lang3.StringUtils;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * sql操作工具类
 * 
 * @author gdz
 */
public class SqlUtil
{
    /**
     * 仅支持字母、数字、下划线、空格、逗号（支持多个字段排序）
     */
    public static String SQL_PATTERN = "[a-zA-Z0-9_\\ \\,]+";

    /**
     * 检查字符，防止注入绕过
     */
    public static String escapeOrderBySql(String value)
    {
        if (StringUtils.isNotEmpty(value) && !isValidOrderBySql(value))
        {
            return StringUtils.EMPTY;
        }
        return value;
    }

    /**
     * 验证 order by 语法是否符合规范
     */
    public static boolean isValidOrderBySql(String value)
    {
        return value.matches(SQL_PATTERN);
    }


    /**
     * 获取查询sql中select部分
     * @Author mr.g
     * @param sql
     * @return java.util.List<java.lang.String>
     **/
    public static List<String> getSelectSQL(String sql){
        try {
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
        } catch (JSQLParserException e) {
            e.printStackTrace();
        }

        return null;
    }

}
