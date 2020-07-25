package com.wlcb.jpower.module.mp.support;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wlcb.jpower.module.common.support.ChainMap;
import com.wlcb.jpower.module.common.utils.BeanUtil;
import com.wlcb.jpower.module.common.utils.Fc;

import java.util.Map;

/**
 * @ClassName Condition
 * @Description TODO 扩展QueryWrapper
 * @Author 郭丁志
 * @Date 2020-07-23 15:01
 * @Version 1.0
 */
public class Condition<T> {

    public Condition() {
    }

    public static <T> QueryWrapper<T> getQueryWrapper() {
        return new QueryWrapper<T>().eq("status",1);
    }

    public static <T> QueryWrapper<T> getQueryWrapper(T entity) {
        return new QueryWrapper<T>(entity).eq("status",1);
    }

    public static <T> QueryWrapper<T> getQueryWrapper(Map<String, Object> query, Class<T> clazz) {
        ChainMap exclude = ChainMap.init().set("pageNum", "pageNum").set("pageSize", "pageSize").set("orderBy", "orderBy");
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
}
