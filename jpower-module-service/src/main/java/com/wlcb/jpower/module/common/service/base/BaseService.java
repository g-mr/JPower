package com.wlcb.jpower.module.common.service.base;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wlcb.jpower.module.common.node.Node;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * @author mr.gmac
 */
public interface BaseService<T> extends IService<T> {

    List<Node> tree(Wrapper<T> treeWrapper);

    <V> List<V> listToObjs(Wrapper<T> queryWrapper, Function<T, V> mapper);

    <V extends Node> List<V> listTree(Wrapper<T> queryWrapper,Class<V> clz);

    /**
     * 根据 ID 真实删除
     *
     * @param id 主键ID
     */
    boolean removeRealById(Serializable id);

    /**
     * 根据 entity 条件，删除记录
     *
     * @param queryWrapper 实体包装类 {@link com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
     */
    boolean removeReal(Wrapper<T> queryWrapper);

    /**
     * 删除（根据ID 批量删除）
     *
     * @param idList 主键ID列表
     */
    boolean removeRealByIds(Collection<? extends Serializable> idList);

    /**
     * 根据 columnMap 条件，删除记录
     *
     * @param columnMap 表字段 map 对象
     */
    boolean removeRealByMap(Map<String, Object> columnMap);

}
