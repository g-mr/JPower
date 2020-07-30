package com.wlcb.jpower.module.common.service.base;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wlcb.jpower.module.common.node.Node;

import java.util.List;
import java.util.function.Function;

/**
 * @author mr.gmac
 */
public interface BaseService<T> extends IService<T> {

    List<Node> tree(Wrapper<T> treeWrapper);

    <V> List<V> listToObjs(Wrapper<T> queryWrapper, Function<T, V> mapper);

    <V extends Node> List<V> listTree(Wrapper<T> queryWrapper,Class<V> clz);

}
