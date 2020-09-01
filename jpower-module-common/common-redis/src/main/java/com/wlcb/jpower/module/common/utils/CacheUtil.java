package com.wlcb.jpower.module.common.utils;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.lang.Nullable;

import java.lang.reflect.Field;
import java.util.concurrent.Callable;

/**
 * @ClassName CacheUtil
 * @Description TODO
 * @Author 郭丁志
 * @Date 2020-09-01 11:16
 * @Version 1.0
 */
public class CacheUtil {

    private static CacheManager cacheManager;


    private static CacheManager getCacheManager() {
        if (cacheManager == null) {
            cacheManager = SpringUtil.getBean(CacheManager.class);
        }

        return cacheManager;
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 获取cache
     * @Date 11:32 2020-09-01
     **/
    public static Cache getCache(String cacheName) {
        return getCacheManager().getCache(cacheName);
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 获取缓存值
     * @Date 11:32 2020-09-01
     **/
    public static <T> T get(String cacheName, String keyPrefix, Object key, Callable<T> valueLoader) {
        if (Fc.hasEmpty(cacheName, keyPrefix, key)) {
            return null;
        }
        try {
            Cache.ValueWrapper valueWrapper = getCache(cacheName).get(keyPrefix.concat(String.valueOf(key)));
            Object value = null;
            if (valueWrapper == null) {
                T call = valueLoader.call();
                if (Fc.isNotEmpty(call)) {
                    Field field = ReflectUtil.getAccessibleField(call.getClass(), "id");
                    if (ObjectUtil.isNotEmpty(field) && Fc.isEmpty(ClassUtil.getMethod(call.getClass(), "getId", new Class[0]).invoke(call))) {
                        return null;
                    }

                    getCache(cacheName).put(keyPrefix.concat(String.valueOf(key)), call);
                    value = call;
                }
            } else {
                value = valueWrapper.get();
            }

            return (T) value;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }

    /**
     * @Author 郭丁志
     * @Description //TODO 设置缓存
     * @Date 11:32 2020-09-01
     **/
    public static void put(String cacheName, String keyPrefix, Object key, @Nullable Object value) {
        if (!Fc.hasEmpty(cacheName, keyPrefix, key)) {
            getCache(cacheName).put(keyPrefix.concat(String.valueOf(key)), value);
        }
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 删除一个缓存key
     * @Date 11:32 2020-09-01
     **/
    public static void evict(String cacheName, String keyPrefix, Object key) {
        if (!Fc.hasEmpty(cacheName, keyPrefix, key)) {
            getCache(cacheName).evict(keyPrefix.concat(String.valueOf(key)));
        }
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 清空缓存
     * @Date 11:32 2020-09-01
     **/
    public static void clear(String cacheName) {
        if (Fc.isNotBlank(cacheName)) {
            getCache(cacheName).clear();
        }
    }
}
