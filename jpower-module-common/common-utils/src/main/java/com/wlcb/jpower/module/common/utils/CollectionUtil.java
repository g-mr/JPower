package com.wlcb.jpower.module.common.utils;

import cn.hutool.core.collection.CollUtil;

import java.util.Collection;
import java.util.HashSet;

/**
 * 集合工具类
 *
 * @author 郭丁志
 */
public class CollectionUtil extends CollUtil {

    /**
     * 新建一个HashSet
     *
     * @param <T>        集合元素类型
     * @param collection 集合
     * @return HashSet对象
     */
    public static <T> HashSet<T> newHashSet(Collection<T> collection) {
        return isEmpty(collection) ? newHashSet() : newHashSet(false, collection);
    }

}
