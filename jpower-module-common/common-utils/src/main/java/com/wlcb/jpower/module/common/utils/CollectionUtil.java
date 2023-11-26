package com.wlcb.jpower.module.common.utils;

import cn.hutool.core.collection.CollUtil;

import java.util.Collection;
import java.util.HashSet;

/**
 * 集合工具类
 *
 * @author mr.g
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

    /**
     * 判断指定集合是否包含指定值(无视值类型只比较值是否相等)，如果集合为空（null或者空），返回false，否则找到元素返回true
     *
     * @author mr.g
     * @param collection
     * @param value
     * @return boolean
     **/
    public static boolean containsValue(Collection<?> collection, Object value) {
        if (Fc.isEmpty(collection)){
            return false;
        }
        return collection.stream().anyMatch(val->Fc.equalsValue(val,value));
    }
}
