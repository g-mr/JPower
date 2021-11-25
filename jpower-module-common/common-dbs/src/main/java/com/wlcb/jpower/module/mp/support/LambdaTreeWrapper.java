package com.wlcb.jpower.module.mp.support;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.core.conditions.AbstractLambdaWrapper;
import com.baomidou.mybatisplus.core.conditions.SharedString;
import com.baomidou.mybatisplus.core.conditions.query.Query;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.ArrayUtils;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
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
 * Lambda 语法使用 Wrapper
 * @author mr.gmac
 */
@SuppressWarnings("serial")
public class LambdaTreeWrapper<T> extends AbstractLambdaWrapper<T, LambdaTreeWrapper<T>>
    implements Query<LambdaTreeWrapper<T>, T, SFunction<T, ?>> {

    private SFunction<T, ?> id;
    private SFunction<T, ?> parentId;

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

//    public LambdaTreeWrapper() {
//        this((T) null);
//    }

    public LambdaTreeWrapper(T entity,SFunction<T, ?> id,SFunction<T, ?> parentId) {
        super.setEntity(entity);
        this.id = id;
        this.parentId = parentId;
        super.initNeed();
        this.select(StringUtil.split(TableInfoHelper.getTableInfo(getEntityClass()).getAllSqlSelect(),StringPool.COMMA));
    }

    public LambdaTreeWrapper(Class<T> entityClass,SFunction<T, ?> id,SFunction<T, ?> parentId) {

        super.setEntityClass(entityClass);
        this.id = id;
        this.parentId = parentId;
        super.initNeed();
        this.select(StringUtil.split(TableInfoHelper.getTableInfo(getEntityClass()).getAllSqlSelect(),StringPool.COMMA));
    }

    private LambdaTreeWrapper(T entity, Class<T> entityClass, SharedString sqlSelect, AtomicInteger paramNameSeq,
                      Map<String, Object> paramNameValuePairs, MergeSegments mergeSegments, SharedString paramAlias,
                      SharedString lastSql, SharedString sqlComment, SharedString sqlFirst,
                      SFunction<T, ?> id,SFunction<T, ?> parentId,String hasChildren,List<String> list) {
        super.setEntity(entity);
        super.setEntityClass(entityClass);
        this.paramNameSeq = paramNameSeq;
        this.paramNameValuePairs = paramNameValuePairs;
        this.expression = mergeSegments;
        this.sqlSelect = sqlSelect;
        this.paramAlias = paramAlias;
        this.lastSql = lastSql;
        this.sqlComment = sqlComment;
        this.sqlFirst = sqlFirst;
        this.id = id;
        this.parentId = parentId;
        this.hasChildren = hasChildren;
        this.list = list;
    }

    private LambdaTreeWrapper<T> select(String... columns) {
        if (ArrayUtils.isNotEmpty(columns)) {
            this.list = ListUtil.toList(columns);
        }

        String id = columnToString(this.id,false);
        String parentId = columnToString(this.parentId,false);

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

    /**
     * SELECT 部分 SQL 设置
     *
     * @param columns 查询字段
     */
    @SafeVarargs
    @Override
    public final LambdaTreeWrapper<T> select(SFunction<T, ?>... columns) {
        String select = columnsToString(false, columns);
        select(StringUtil.split(select,StringPool.COMMA));
        return typedThis;
    }

    /**
     * 过滤查询的字段信息(主键除外!)
     * <p>例1: 只要 java 字段名以 "test" 开头的             -> select(i -&gt; i.getProperty().startsWith("test"))</p>
     * <p>例2: 只要 java 字段属性是 CharSequence 类型的     -> select(TableFieldInfo::isCharSequence)</p>
     * <p>例3: 只要 java 字段没有填充策略的                 -> select(i -&gt; i.getFieldFill() == FieldFill.DEFAULT)</p>
     * <p>例4: 要全部字段                                   -> select(i -&gt; true)</p>
     * <p>例5: 只要主键字段                                 -> select(i -&gt; false)</p>
     *
     * @param predicate 过滤方式
     * @return this
     */
    @Override
    public LambdaTreeWrapper<T> select(Class<T> entityClass, Predicate<TableFieldInfo> predicate) {
        if (entityClass == null) {
            entityClass = getEntityClass();
        } else {
            setEntityClass(entityClass);
        }
        String select = TableInfoHelper.getTableInfo(entityClass).chooseSelect(predicate);
        select(StringUtil.split(select,StringPool.COMMA));
        return typedThis;
    }

    public LambdaTreeWrapper<T> lazy(String parentIdValue){
        String tableName = TableInfoHelper.getTableInfo(getEntityClass()).getTableName();

        this.hasChildren = "( SELECT CASE WHEN count( 1 ) > 0 THEN 1 ELSE 0 END FROM "+tableName+" as c WHERE "+columnToString(this.parentId,false)+" = "+tableName+"."+columnToString(this.id,false)+" ) AS "+ForestNodeMerger.HAS_CHILDREN;
        select(ArrayUtil.toArray(this.list,String.class));
        eq(this.parentId,StringUtil.isBlank(parentIdValue)? JpowerConstants.TOP_CODE:parentIdValue);
        return typedThis;
    }

    @Override
    public String getSqlSelect() {
        return sqlSelect.getStringValue();
    }

    /**
     * 用于生成嵌套 sql
     * <p>故 sqlSelect 不向下传递</p>
     */
    @Override
    protected LambdaTreeWrapper<T> instance() {
        return new LambdaTreeWrapper<>(getEntity(), getEntityClass(), null, paramNameSeq, paramNameValuePairs,
            new MergeSegments(), paramAlias, SharedString.emptyString(), SharedString.emptyString(), SharedString.emptyString(),
                id,parentId,hasChildren,list);
    }

    /**
     * 返回一个不是 lambda 函数写法的 wrapper
     */
    public TreeWrapper<T> unLambda() {
        String id = columnToString(this.id,false);
        String parentId = columnToString(this.parentId,false);

        return new TreeWrapper<T>(getEntity(), getEntityClass(), paramNameSeq,
                paramNameValuePairs, expression, paramAlias,
                lastSql, sqlComment, sqlFirst,
                id,parentId,hasChildren,list);
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
        this.select(StringUtil.split(TableInfoHelper.getTableInfo(getEntityClass()).getAllSqlSelect(), StringPool.COMMA));
    }
}
