package com.wlcb.jpower.module.mp.support;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName BaseEntityWrapper
 * @Description TODO
 * @Author 郭丁志
 * @Date 2020-10-16 15:37
 * @Version 1.0
 */
public abstract class BaseEntityWrapper <E, V> {

    public abstract V entityVO(E entity);

    public List<V> listVO(List<E> list) {
        return list.stream().map(this::entityVO).collect(Collectors.toList());
    }
}
