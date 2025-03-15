package top.jpower.core.dbs.mp.methods;

import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils;
import com.baomidou.mybatisplus.extension.injector.methods.AlwaysUpdateSomeColumnById;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

import java.util.function.Predicate;

/**
 * 修改指定字段
 *
 * @see AlwaysUpdateSomeColumnById
 * @author 郭丁志
 * @since 2020-10-14 16:59
 */
public class UpdateAllById extends AbstractMethod {

    /**
     * 字段筛选条件
     */
    @Setter
    @Accessors(chain = true)
    private Predicate<TableFieldInfo> predicate;

    public UpdateAllById() {
        super("updateAllById");
    }

    /**
     * @param predicate 筛选条件
     */
    public UpdateAllById(Predicate<TableFieldInfo> predicate) {
        super("updateAllById");
        this.predicate = predicate;
    }

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        SqlMethod sqlMethod = SqlMethod.UPDATE_BY_ID;
        final String additional = optlockVersion(tableInfo) + tableInfo.getLogicDeleteSql(true, true);
        String sqlSet = this.filterTableFieldInfo(tableInfo.getFieldList(), getPredicate(),
                i -> i.getSqlSet(true, ENTITY_DOT), NEWLINE);
        sqlSet = SqlScriptUtils.convertSet(sqlSet);
        String sql = String.format(sqlMethod.getSql(), tableInfo.getTableName(), sqlSet,
                tableInfo.getKeyColumn(), ENTITY_DOT + tableInfo.getKeyProperty(), additional);
        SqlSource sqlSource = super.createSqlSource(configuration, sql, modelClass);
        return addUpdateMappedStatement(mapperClass, modelClass, methodName, sqlSource);
    }

    private Predicate<TableFieldInfo> getPredicate() {
        Predicate<TableFieldInfo> noLogic = t -> !t.isLogicDelete();
        if (predicate != null) {
            return noLogic.and(predicate);
        }
        return noLogic;
    }

}
