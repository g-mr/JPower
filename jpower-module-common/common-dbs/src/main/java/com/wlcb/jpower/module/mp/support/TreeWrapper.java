package com.wlcb.jpower.module.mp.support;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.SharedString;
import com.baomidou.mybatisplus.core.conditions.query.Query;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.ArrayUtils;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.wlcb.jpower.module.common.node.ForestNodeMerger;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.StringUtil;
import com.wlcb.jpower.module.common.utils.constants.JpowerConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

/**
 * 树形条件构造器
 *
 * @author mr.g
 **/
@SuppressWarnings("serial")
public class TreeWrapper<T> extends AbstractWrapper<T, String, TreeWrapper<T>>
        implements Query<TreeWrapper<T>, T, String> {

    private String id;
    private String parentId;

    private final String idAlias = ForestNodeMerger.CONFIG.getIdKey();
    private final String parentIdAlias = ForestNodeMerger.CONFIG.getParentIdKey();

    private String hasChildren;
    /**
     * 查询字段
     */
    private SharedString sqlSelect = new SharedString();

    /**
     * 存储用户的查询字段
     **/
    private List<String> list = new ArrayList<>();

    public TreeWrapper(T entity,String id,String parentId) {
        super.setEntity(entity);
        this.id = id;
        this.parentId = parentId;
        super.initNeed();

        this.select(Fc.toStrArray(TableInfoHelper.getTableInfo(getEntityClass()).getAllSqlSelect()));
    }

    public TreeWrapper(Class<T> entityClass,String id,String parentId) {
        super.setEntityClass(entityClass);
        this.id = id;
        this.parentId = parentId;
        super.initNeed();
        this.select(Fc.toStrArray(TableInfoHelper.getTableInfo(getEntityClass()).getAllSqlSelect()));
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
    TreeWrapper(T entity, Class<T> entityClass, AtomicInteger paramNameSeq,SharedString sqlSelect,
                         Map<String, Object> paramNameValuePairs, MergeSegments mergeSegments, SharedString paramAlias,
                         SharedString lastSql, SharedString sqlComment, SharedString sqlFirst,
                         String id,String parentId,String hasChildren,List<String> list) {
        super.setEntity(entity);
        super.setEntityClass(entityClass);
        this.paramNameSeq = paramNameSeq;
        this.paramNameValuePairs = paramNameValuePairs;
        this.expression = mergeSegments;
        this.paramAlias = paramAlias;
        this.lastSql = lastSql;
        this.sqlComment = sqlComment;
        this.sqlFirst = sqlFirst;
        this.sqlSelect = sqlSelect;
        this.id = id;
        this.parentId = parentId;
        this.hasChildren = hasChildren;
        this.list = list;
    }

    @Override
    public TreeWrapper<T> select(String... columns) {
        if (ArrayUtils.isNotEmpty(columns)) {
            this.list = ListUtil.toList(columns);
        }

        List<String> list = ListUtil.toCopyOnWriteArrayList(this.list);
        if (Fc.isNoneBlank(id,parentId)){
            list.remove(id);
            list.add(id+" AS "+idAlias);
            list.remove(parentId);
            list.add(parentId+" AS "+parentIdAlias);
        }

        if (Fc.isNoneBlank(hasChildren)){
            list.add(hasChildren);
        }

        if (Fc.isNotEmpty(list)) {
            this.sqlSelect.setStringValue(Fc.join(list,StringPool.COMMA));
        }

        return typedThis;
    }

    @Override
    public TreeWrapper<T> select(Class<T> entityClass, Predicate<TableFieldInfo> predicate) {
        if (entityClass == null) {
            entityClass = getEntityClass();
        } else {
            setEntityClass(entityClass);
        }
        String select = TableInfoHelper.getTableInfo(entityClass).chooseSelect(predicate);
        select(Fc.toStrArray(select));
        return typedThis;
    }

    /**
     * 懒加载
     * @Author mr.g
     **/
    public TreeWrapper<T> lazy(String parentIdValue){
        String tableName = TableInfoHelper.getTableInfo(getEntityClass()).getTableName();
        this.hasChildren = "( SELECT CASE WHEN count( 1 ) > 0 THEN 1 ELSE 0 END FROM "+tableName+" as c WHERE "+this.parentId+" = "+tableName+"."+this.id+" ) AS "+ForestNodeMerger.HAS_CHILDREN;
        select(ArrayUtil.toArray(this.list,String.class));
        eq(this.parentId,StringUtil.isBlank(parentIdValue)? JpowerConstants.TOP_CODE:parentIdValue);
        return typedThis;
    }

    /**
     * Map条件增强
     * @Author mr.g
     **/
    public TreeWrapper<T> map(Map<String,Object> query){
        SqlKeyword.buildCondition(query, this);
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
     * 用于生成嵌套 sql
     * <p>
     * 故 sqlSelect 不向下传递
     * </p>
     */
    @Override
    protected TreeWrapper<T> instance() {
        return new TreeWrapper<>(getEntity(), getEntityClass(), paramNameSeq, sqlSelect, paramNameValuePairs, new MergeSegments(),
                paramAlias, SharedString.emptyString(), SharedString.emptyString(), SharedString.emptyString(),
                id, parentId,hasChildren,list);
    }

    @Override
    public void clear() {
        paramNameSeq.set(0);
        paramNameValuePairs.clear();
        expression.clear();
        lastSql.toEmpty();
        sqlComment.toEmpty();
        sqlFirst.toEmpty();
        this.hasChildren = null;
        this.list.clear();
        this.select(Fc.toStrArray(TableInfoHelper.getTableInfo(getEntityClass()).getAllSqlSelect()));
    }
}
