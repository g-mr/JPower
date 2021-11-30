package com.wlcb.jpower.module.mp.support;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.wlcb.jpower.module.common.support.ChainMap;
import com.wlcb.jpower.module.common.utils.BeanUtil;

import java.util.Map;

/**
 * @ClassName Condition
 * @Description TODO 扩展Wrapper
 * @Author 郭丁志
 * @Date 2020-07-23 15:01
 * @Version 1.0
 */
public class Condition<T> {

    public Condition() {
    }

    public static <T> QueryWrapper<T> getQueryWrapper() {
        return new QueryWrapper<T>();
    }

    public static <T> QueryWrapper<T> getQueryWrapper(Class<T> clz) {
        QueryWrapper<T> qw = new QueryWrapper();
        qw.setEntity(BeanUtil.newInstance(clz));
        return qw;
    }

    public static <T> QueryWrapper<T> getQueryWrapper(T entity) {
        return new QueryWrapper<T>(entity);
    }

    public static <T> QueryWrapper<T> getQueryWrapper(Map<String, Object> query, Class<T> clazz) {
        ChainMap exclude = ChainMap.init().set("pageNum", "pageNum").set("pageSize", "pageSize").set("asc", "asc").set("desc", "desc");
        return getQueryWrapper(query, exclude, clazz);
    }

    public static <T> QueryWrapper<T> getQueryWrapper(Map<String, Object> query, Map<String, Object> exclude, Class<T> clazz) {
        exclude.forEach((k, v) -> {
            query.remove(k);
        });
        QueryWrapper<T> qw = new QueryWrapper();
        qw.setEntity(BeanUtil.newInstance(clazz));
        SqlKeyword.buildCondition(query, qw);
        return qw;
    }

    public static <T> TreeWrapper<T> getTreeWrapper(Class<T> clz,String id,String parentId) {
        TreeWrapper<T> qw = new TreeWrapper(clz,id,parentId);
        qw.setEntity(BeanUtil.newInstance(clz));
        return qw;
    }

    public static <T> TreeWrapper<T> getTreeWrapper(T entity,String id,String parentId) {
        return new TreeWrapper<T>(entity, id, parentId);
    }

    public static <T> LambdaTreeWrapper<T> getLambdaTreeWrapper(Class<T> clz,SFunction<T, ?> id,SFunction<T, ?> parentId) {
        LambdaTreeWrapper<T> qw = new LambdaTreeWrapper(clz,id,parentId);
        qw.setEntity(BeanUtil.newInstance(clz));
        return qw;
    }

    public static <T> LambdaTreeWrapper<T> getLambdaTreeWrapper(T entity, SFunction<T, ?> id, SFunction<T, ?> parentId) {
        return new LambdaTreeWrapper<T>(entity, id, parentId);
    }
}
