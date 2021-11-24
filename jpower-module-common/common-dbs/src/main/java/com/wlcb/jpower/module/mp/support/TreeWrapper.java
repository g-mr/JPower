package com.wlcb.jpower.module.mp.support;

import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.SharedString;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.Query;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.wlcb.jpower.module.common.utils.Fc;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

import static com.wlcb.jpower.module.common.utils.constants.StringPool.COMMA;
import static com.wlcb.jpower.module.common.utils.constants.StringPool.EMPTY;

/**
 * Entity 对象封装操作类
 *
 * @author hubin miemie HCL
 * @since 2018-05-25
 */
@SuppressWarnings("serial")
public class TreeWrapper<T> extends AbstractWrapper<T, String, TreeWrapper<T>>
        implements Query<TreeWrapper<T>, T, String> {

    private String id;
    private String parentId;

    /**
     * 查询字段
     */
    private final SharedString sqlSelect = new SharedString();

    public TreeWrapper(T entity,String id,String parentId) {
        super.setEntity(entity);
        this.id = id;
        this.parentId = parentId;
        super.initNeed();
    }

    public TreeWrapper(T entity,String id,String parentId, String... columns) {
        super.setEntity(entity);
        this.id = id;
        this.parentId = parentId;
        super.initNeed();
        this.select(columns);
    }

    /**
     * 非对外公开的构造方法,只用于生产嵌套 sql
     *
     * @param entityClass 本不应该需要的
     */
    private TreeWrapper(T entity, Class<T> entityClass, AtomicInteger paramNameSeq,
                         Map<String, Object> paramNameValuePairs, MergeSegments mergeSegments, SharedString paramAlias,
                         SharedString lastSql, SharedString sqlComment, SharedString sqlFirst,String id,String parentId) {
        super.setEntity(entity);
        super.setEntityClass(entityClass);
        this.paramNameSeq = paramNameSeq;
        this.paramNameValuePairs = paramNameValuePairs;
        this.expression = mergeSegments;
        this.paramAlias = paramAlias;
        this.lastSql = lastSql;
        this.sqlComment = sqlComment;
        this.sqlFirst = sqlFirst;
        this.id = id;
        this.parentId = parentId;
    }

    @Override
    public TreeWrapper<T> select(String... columns) {
        if (Fc.isNoneBlank(id,parentId)){
            String select = Fc.join(columns);
            this.sqlSelect.setStringValue(Fc.isNotBlank(select)?select+COMMA:EMPTY+id+" as id,"+parentId+" as parentId");
        }else {
            this.sqlSelect.setStringValue(String.join(StringPool.COMMA, columns));
        }
        return typedThis;
    }

    @Override
    public TreeWrapper<T> select(Class<T> entityClass, Predicate<TableFieldInfo> predicate) {
        super.setEntityClass(entityClass);
        this.sqlSelect.setStringValue(TableInfoHelper.getTableInfo(getEntityClass()).chooseSelect(predicate));
        return typedThis;
    }

    @Override
    public String getSqlSelect() {
        return sqlSelect.getStringValue();
    }

    @Override
    protected String columnSqlInjectFilter(String column) {
        return StringUtils.sqlInjectionReplaceBlank(column);
    }

    /**
     * 返回一个支持 lambda 函数写法的 wrapper
     */
    public LambdaQueryWrapper<T> lambda() {
//        return new LambdaQueryWrapper<>(getEntity(), getEntityClass(), sqlSelect, paramNameSeq, paramNameValuePairs,
//                expression, paramAlias, lastSql, sqlComment, sqlFirst);
        return null;
    }

    /**
     * 用于生成嵌套 sql
     * <p>
     * 故 sqlSelect 不向下传递
     * </p>
     */
    @Override
    protected TreeWrapper<T> instance() {
        return new TreeWrapper<>(getEntity(), getEntityClass(), paramNameSeq, paramNameValuePairs, new MergeSegments(),
                paramAlias, SharedString.emptyString(), SharedString.emptyString(), SharedString.emptyString(), id, parentId);
    }

    @Override
    public void clear() {
        paramNameSeq.set(0);
        paramNameValuePairs.clear();
        expression.clear();
        lastSql.toEmpty();
        sqlComment.toEmpty();
        sqlFirst.toEmpty();
        this.sqlSelect.setStringValue(id+" as id,"+parentId+" as parentId");
    }
}
