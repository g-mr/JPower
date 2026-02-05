package top.jpower.core.dbs.dbs.dao;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.tree.Tree;
import com.mybatisflex.core.query.QueryCondition;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.util.LambdaGetter;
import com.mybatisflex.core.util.SqlUtil;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import top.jpower.core.dbs.dbs.dao.mapper.base.JpowerBaseMapper;
import top.jpower.core.dbs.dbs.entity.base.BaseEntity;
import top.jpower.core.dbs.support.ForestNodeMerger;
import top.jpower.core.dbs.support.TreeWrapper;
import top.jpower.core.dbs.support.Wrappers;
import top.jpower.core.util.utils.Fc;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 抽象Service实现类
 * @param <M>
 * @param <T>
 */
public class JpowerServiceImpl<M extends JpowerBaseMapper<T>, T extends BaseEntity> extends ServiceImpl<M, T> {

    @Override
    public boolean save(T entity) {
        return super.save(entity);
    }

    @Override
    public boolean saveBatch(Collection<T> entityList, int batchSize) {
        return super.saveBatch(entityList,batchSize);
    }

    @Override
    public boolean saveOrUpdate(T entity) {
        return super.saveOrUpdate(entity);
    }

    @Override
    public boolean saveOrUpdateBatch(Collection<T> entityList, int batchSize) {
        return super.saveOrUpdateBatch(entityList,batchSize);
    }


    @Override
    public boolean updateById(T entity) {
        return super.updateById(entity);
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 根据ID 真实删除
     * @Date 16:00 2020-08-11
     **/
    public boolean removeRealById(Serializable id) {
        return SqlUtil.toBool(getMapper().deleteRealById(id));
    }

    /**
     * 根据 entity 条件，真实删除记录
     *
     */
    public boolean removeReal(QueryWrapper queryWrapper) {
        return SqlUtil.toBool(getMapper().deleteReal(queryWrapper));
    }

    /**
     * 真实删除（根据ID 批量删除）
     *
     * @param idList 主键ID列表
     */
    public boolean removeRealByIds(Collection<? extends Serializable> idList) {
        if (Fc.isEmpty(idList)) {
            return false;
        }
        return SqlUtil.toBool(getMapper().deleteRealBatchIds(idList));
    }

    /**
     * 根据 columnMap 条件，真实删除记录
     *
     * @param columnMap 表字段 map 对象
     */
    public boolean removeRealByMap(Map<String, Object> columnMap) {
        Assert.notEmpty(columnMap, "error: columnMap must not be empty");
        return SqlUtil.toBool(getMapper().deleteRealByMap(columnMap));
    }

    public boolean deleteRealByCondition(QueryCondition whereConditions) {
        return SqlUtil.toBool(getMapper().deleteRealByCondition(whereConditions));
    }

    /**
     * 根据 ID 更新所有列
     *
     * @param entity 实体
     */
    public boolean updateAllById(T entity) {
        return SqlUtil.toBool(getMapper().update(entity, false));
    }

    /**
     * 批量新增指定列
     *
     * @param entityList 实体列表
     */
    public boolean addBatchSomeColumn(List<T> entityList) {
        return SqlUtil.toBool(getMapper().insertBatchSelective(entityList));
    }

    /**
     * 通过一个字段查询
     *
     * @author mr.g
     * @param column 字段
     * @param value 值
     * @return 数据
     **/
    public List<T> listByField(LambdaGetter<T> column, Object value) {
        return super.list(Wrappers.getQueryWrapper().eq(column, value));
    }

    /**
     * 通过一个字段查询数据
     *
     * @author mr.g
     * @param column 字段
     * @param value 值
     * @return java.util.List<T>
     **/
    public T getOneByField(LambdaGetter<T> column, Object value) {
        return super.getOne(Wrappers.getQueryWrapper().eq(column, value));
    }

    /**
     * 把查询结果转换成任何类型
     *
     * @param queryWrapper
     * @param function
     * @return
     * @param <V>
     */
    public <V> List<V> listConver(QueryWrapper queryWrapper, Function<T, V> function) {
        return list(queryWrapper).stream().filter(Objects::nonNull).map(function).collect(Collectors.toList());
    }

    public <E extends Serializable> List<Tree<E>> tree(TreeWrapper treeWrapper) {
        List<Map> list = listAs(treeWrapper, Map.class);
        return ForestNodeMerger.mergeTree(list);
    }

}
