package top.jpower.jpower.module.dbs.dao;

import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import top.jpower.core.util.utils.BeanUtil;
import top.jpower.core.util.utils.Fc;
import top.jpower.core.util.utils.ReflectUtil;
import top.jpower.jpower.module.common.node.ForestNodeMerger;
import top.jpower.jpower.module.dbs.dao.mapper.base.JpowerBaseMapper;
import top.jpower.jpower.module.dbs.entity.base.BaseEntity;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @ClassName JpowerServiceImpl
 * @Description TODO
 * @Author 郭丁志
 * @Date 2020-07-03 14:02
 * @Version 1.0
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
    public boolean updateBatchById(Collection<T> entityList) {
        return super.updateBatchById(entityList);
    }

    @Override
    public boolean updateById(T entity) {
        return super.updateById(entity);
    }

    @Override
    public boolean update(T entity, Wrapper<T> updateWrapper) {
        if (Fc.isNull(entity)){
            entity = BeanUtil.newBean(ReflectUtil.getClassGenricType(this.getClass(),1));
        }
        return super.update(entity,updateWrapper);
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 根据ID 真实删除
     * @Date 16:00 2020-08-11
     **/
    public boolean removeRealById(Serializable id) {
        return SqlHelper.retBool(getBaseMapper().deleteRealById(id));
    }

    /**
     * 根据 entity 条件，真实删除记录
     *
     * @param queryWrapper 实体包装类 {@link com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
     */
    public boolean removeReal(Wrapper<T> queryWrapper) {
        return SqlHelper.retBool(getBaseMapper().deleteReal(queryWrapper));
    }

    /**
     * 真实删除（根据ID 批量删除）
     *
     * @param idList 主键ID列表
     */
    public boolean removeRealByIds(Collection<? extends Serializable> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return false;
        }
        return SqlHelper.retBool(getBaseMapper().deleteRealBatchIds(idList));
    }

    /**
     * 根据 columnMap 条件，真实删除记录
     *
     * @param columnMap 表字段 map 对象
     */
    public boolean removeRealByMap(Map<String, Object> columnMap) {
        Assert.notEmpty(columnMap, "error: columnMap must not be empty");
        return SqlHelper.retBool(getBaseMapper().deleteRealByMap(columnMap));
    }

    /**
     * 根据 ID 更新所有列
     *
     * @param entity 实体
     */
    public boolean updateAllById(T entity) {
        return SqlHelper.retBool(getBaseMapper().updateAllById(entity));
    }

    /**
     * 批量新增指定列
     *
     * @param entityList 实体列表
     */
    public boolean addBatchSomeColumn(List<T> entityList) {
        return SqlHelper.retBool(getBaseMapper().insertBatchSomeColumn(entityList));
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 把查询结果转换成任何类型
     * @Date 21:22 2020-07-30
     * @Param [queryWrapper, mapper]
     * @return java.util.List<V>
     **/
    public <V> List<V> listConver(Wrapper<T> queryWrapper, Function<T, V> function) {
        return list(queryWrapper).stream().filter(Objects::nonNull).map(function).collect(Collectors.toList());
    }

    public <E extends Serializable> List<Tree<E>> tree(Wrapper<T> treeWrapper) {
        List<Map<String,Object>> list = listMaps(treeWrapper);
        return ForestNodeMerger.mergeTree(list);
    }

}
