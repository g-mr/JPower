package com.wlcb.jpower.module.common.service;

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

    /**
     * @author 郭丁志
     * @Description //TODO 查询树形结构
     * @date 23:22 2020/10/21 0021
     * @param treeWrapper 查询条件
     */
    List<Node> tree(Wrapper<T> treeWrapper);

    /**
     * @author 郭丁志
     * @Description //TODO 把查询结果转换成任何类型
     * @date 23:21 2020/10/21 0021
     * @param queryWrapper 查询条件
     * @param function 转换方法
     * @return java.util.List<V>
     */
    <V> List<V> listConver(Wrapper<T> queryWrapper, Function<T, V> function);

    /**
     * @author 郭丁志
     * @Description 查询树形列表
     * @date 23:20 2020/10/21 0021
     * @param queryWrapper 查询条件
     * @param clz 返回类型
     */
    <V extends Node> List<V> listTree(Wrapper<T> queryWrapper,Class<V> clz);

    /**
     * 根据 ID 真实删除
     * @param id 主键ID
     */
    boolean removeRealById(Serializable id);

    /**
     * @Description 根据 entity 条件，删除记录
     * @param queryWrapper 实体包装类 {@link com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
     */
    boolean removeReal(Wrapper<T> queryWrapper);

    /**
     * @Description 删除（根据ID 批量删除）
     * @param idList 主键ID列表
     */
    boolean removeRealByIds(Collection<? extends Serializable> idList);

    /**
     * 根据 columnMap 条件，删除记录
     * @param columnMap 表字段 map 对象
     */
    boolean removeRealByMap(Map<String, Object> columnMap);

    /**
     * 批量新增指定列
     * @param entityList 实体列表
     */
    boolean addBatchSomeColumn(List<T> entityList);

    /**
     * 根据 ID 更新所有列
     * @param entity 实体
     */
    boolean updateAllById(T entity);

}
