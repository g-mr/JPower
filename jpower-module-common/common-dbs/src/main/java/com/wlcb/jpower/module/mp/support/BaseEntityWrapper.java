package com.wlcb.jpower.module.mp.support;

import com.wlcb.jpower.module.common.utils.BeanUtil;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName BaseEntityWrapper
 * @Description TODO entity转换基础实现（包含实现通过注解实现查字典）
 * @Author 郭丁志
 * @Date 2020-10-16 15:37
 * @Version 1.0
 */
public abstract class BaseEntityWrapper <T, V> {

    protected abstract V conver(T entity);

    public V entityVO(T entity){
        V v = conver(entity);
        dict(v);
        return v;
    }

    public List<V> listVO(List<T> list) {
        return list.stream().map(this::entityVO).collect(Collectors.toList());
    }

    /**
     * @author 郭丁志
     * @Description //TODO 查询字典值
     * @date 0:27 2020/10/22 0022
     */
    private static <V> void dict(V v){
    }

    public static <T,V> V dict(T entity,Class<V> v){
        V v1 = BeanUtil.copy(entity,v);
        dict(v1);
        return v1;
    }

    public static <T,V> List<V> dict(List<T> list,Class<V> v){
        return list.stream().map(t -> dict(t,v)).collect(Collectors.toList());
    }
}
