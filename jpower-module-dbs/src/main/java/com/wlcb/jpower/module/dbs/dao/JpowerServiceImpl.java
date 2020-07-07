package com.wlcb.jpower.module.dbs.dao;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

/**
 * @ClassName JpowerServiceImpl
 * @Description TODO
 * @Author 郭丁志
 * @Date 2020-07-03 14:02
 * @Version 1.0
 */
public class JpowerServiceImpl<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> {

    @Override
    @Transactional(
            rollbackFor = {Exception.class}
    )
    public boolean save(T entity) {
        return super.save(entity);
    }

    @Override
    @Transactional(
            rollbackFor = {Exception.class}
    )
    public boolean saveBatch(Collection<T> entityList, int batchSize) {
        return super.saveBatch(entityList,batchSize);
    }

    @Override
    @Transactional(
            rollbackFor = {Exception.class}
    )
    public boolean saveOrUpdate(T entity) {
        return super.saveOrUpdate(entity);
    }

    @Override
    @Transactional(
            rollbackFor = {Exception.class}
    )
    public boolean saveOrUpdateBatch(Collection<T> entityList, int batchSize) {
        return saveOrUpdateBatch(entityList,batchSize);
    }

    @Override
    @Transactional(
            rollbackFor = {Exception.class}
    )
    public boolean removeById(Serializable id) {
        return super.removeById(id);
    }

    @Override
    @Transactional(
            rollbackFor = {Exception.class}
    )
    public boolean removeByMap(Map<String, Object> columnMap) {
        return super.removeByMap(columnMap);
    }

    @Override
    @Transactional(
            rollbackFor = {Exception.class}
    )
    public boolean remove(Wrapper<T> wrapper) {
        return super.remove(wrapper);
    }

    @Override
    @Transactional(
            rollbackFor = {Exception.class}
    )
    public boolean removeByIds(Collection<? extends Serializable> idList) {
        return super.removeByIds(idList);
    }

    @Override
    @Transactional(
            rollbackFor = {Exception.class}
    )
    public boolean updateById(T entity) {
        return super.updateById(entity);
    }

    @Override
    @Transactional(
            rollbackFor = {Exception.class}
    )
    public boolean update(T entity, Wrapper<T> updateWrapper) {
        return super.update(entity,updateWrapper);
    }

    @Override
    @Transactional(
            rollbackFor = {Exception.class}
    )
    public boolean updateBatchById(Collection<T> entityList, int batchSize) {
        return super.updateBatchById(entityList,batchSize);
    }

}
