package com.wlcb.jpower.module.mp.support;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName BaseEntityWrapper
 * @Description TODO entity转换基础实现
 * @Author 郭丁志
 * @Date 2020-10-16 15:37
 * @Version 1.0
 */
public abstract class BaseWrapper <T, V> {

    protected abstract V conver(T entity);

    public V entityVO(T entity){
        return conver(entity);
    }

    public List<V> listVO(List<T> list) {
        return list.stream().map(this::entityVO).collect(Collectors.toList());
    }

}
