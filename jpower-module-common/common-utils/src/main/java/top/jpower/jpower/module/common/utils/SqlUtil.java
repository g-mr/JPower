package top.jpower.jpower.module.common.utils;


import cn.hutool.core.bean.BeanDesc;
import lombok.SneakyThrows;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectItem;
import org.apache.commons.lang3.StringUtils;
import top.jpower.jpower.module.common.utils.constants.StringPool;

import java.io.Serializable;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
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

    /**
     * 根据一个Bean来生成一个mysql的create table sql;
     *
     * @param clz   beanClass
     * @param tableDesc 表描述
     * @param primary 主键
     * @return create table sql
     * @author mr.g
     **/
    public static String buildCreateMysqlTable(Class<?> clz, Field primary, String tableDesc) {

        if (BeanUtil.isBean(clz)){
            BeanDesc beanDesc = BeanUtil.getBeanDesc(clz);
            StringBuilder builder = new StringBuilder("CREATE TABLE `").append(StringUtil.humpToUnderline(beanDesc.getSimpleName())).append("`(");
            beanDesc.getProps().forEach(propDesc -> {
                builder.append("`").append(StringUtil.humpToUnderline(propDesc.getRawFieldName())).append("`");
                //生成类型
                if (StringUtil.equals(propDesc.getFieldType().getTypeName(), ClassUtil.getClassName(Long.class, false)) ||
                    StringUtil.equals(propDesc.getFieldType().getTypeName(), ClassUtil.getClassName(long.class, false))) {
                    builder.append(" bigint(20)");
                } else if (StringUtil.equals(propDesc.getFieldType().getTypeName(), ClassUtil.getClassName(Integer.class, false)) ||
                            StringUtil.equals(propDesc.getFieldType().getTypeName(), ClassUtil.getClassName(int.class, false))) {
                    builder.append(" int(11)");
                } else if (StringUtil.equals(propDesc.getFieldType().getTypeName(), ClassUtil.getClassName(Boolean.class, false)) ||
                        StringUtil.equals(propDesc.getFieldType().getTypeName(), ClassUtil.getClassName(boolean.class, false))) {
                    builder.append(" tinyint(1)");
                } else if (StringUtil.equals(propDesc.getFieldType().getTypeName(), ClassUtil.getClassName(Date.class, false))) {
                    builder.append(" datetime");
                } else if (StringUtil.equals(propDesc.getFieldType().getTypeName(), ClassUtil.getClassName(Double.class, false)) ||
                        StringUtil.equals(propDesc.getFieldType().getTypeName(), ClassUtil.getClassName(double.class, false))) {
                    builder.append(" double(10,6)");
                } else if (StringUtil.equals(propDesc.getFieldType().getTypeName(), ClassUtil.getClassName(BigDecimal.class, false))) {
                    builder.append(" decimal(10,6)");
                } else {
                    builder.append(" varchar(255)");
                }
                //生成默认值
                if (Fc.notNull(primary) && StringUtil.equals(propDesc.getRawFieldName(), primary.getName())){
                    builder.append(" NOT NULL");
                } else if(propDesc.getFieldClass().isPrimitive()){
                    builder.append(" NOT NULL");
                    if (StringUtil.equals(propDesc.getFieldType().getTypeName(), ClassUtil.getClassName(Date.class, false))) {
                        builder.append(" DEFAULT CURRENT_TIMESTAMP");
                    } else {
                        builder.append(" DEFAULT 0");
                    }
                } else if(StringUtil.equalsIgnoreCase(propDesc.getRawFieldName(), "createTime")){
                    builder.append(" NOT NULL DEFAULT CURRENT_TIMESTAMP");
                } else if(StringUtil.equalsIgnoreCase(propDesc.getRawFieldName(), "updateTime")){
                    builder.append(" NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP");
                } else if (StringUtil.equals(propDesc.getFieldType().getTypeName(), ClassUtil.getClassName(Boolean.class, false))) {
                    builder.append(" NOT NULL DEFAULT 0");
                } else {
                    builder.append(" DEFAULT NULL");
                }
                builder.append(",");
            });

            if (Fc.notNull(primary)){
                builder.append("PRIMARY KEY (`").append(StringUtil.humpToUnderline(primary.getName())).append("`) USING BTREE");
            } else {
                builder.deleteCharAt(builder.length()-1);
            }
            builder.append(") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='").append(Fc.toStr(tableDesc, StringPool.EMPTY)).append("'");

            return builder.toString();
        }
        return null;
    }

    /**
     * 生成Mysql的insert语句
     *
     * @author mr.g
     * @param clz 需要生成的Bean
     * @return INSERT SQL语句
     **/
    public static String buildInsertMysql(Class<? extends Serializable> clz) {
        if (BeanUtil.isBean(clz)){
            BeanDesc beanDesc = BeanUtil.getBeanDesc(clz);

            StringBuilder columns = new StringBuilder("(");
            StringBuilder values = new StringBuilder("(");
            beanDesc.getProps().forEach(propDesc -> {
                columns.append(" `")
                        .append(StringUtil.humpToUnderline(propDesc.getRawFieldName()))
                        .append("`,");
                values.append(" :")
                        .append(propDesc.getRawFieldName())
                        .append(",");
            });
            columns.deleteCharAt(columns.length()-1);
            columns.append(")");
            values.deleteCharAt(values.length()-1);
            values.append(")");

            return StringUtil.format("INSERT INTO {} {} VALUES {}",StringUtil.humpToUnderline(beanDesc.getSimpleName()), columns.toString(), values.toString());
        }
        return null;
    }
}
