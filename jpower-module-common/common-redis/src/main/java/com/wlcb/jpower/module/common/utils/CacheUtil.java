package com.wlcb.jpower.module.common.utils;

import org.springframework.cache.Cache;
import org.springframework.lang.Nullable;

import java.util.Optional;
import java.util.concurrent.Callable;

/**
 * @ClassName CacheUtil
 * @Description TODO
 * @Author 郭丁志
 * @Date 2020-09-01 11:16
 * @Version 1.0
 */
public class CacheUtil {

    public static <T> T get(String cacheName, String keyPrefix, Object key,@Nullable Class<T> clz) {
        return get(cacheName,keyPrefix,key,clz,Cm.TENANT_MODE);
    }

    public static <T> T get(String cacheName, String keyPrefix, Object key, Class<T> clz, Boolean tenantMode) {
        if (Fc.hasEmpty(cacheName,keyPrefix,key)){
            return null;
        }
        return Cm.getInstance().getCache(cacheName,tenantMode).get(keyPrefix.concat(Fc.toStr(key)), clz);
    }

    public static <T> T get(String cacheName, String keyPrefix, Object key, Callable<T> valueLoader, Boolean tenantMode) {
        if (Fc.hasEmpty(cacheName, keyPrefix, key)) {
            return null;
        }
        Cache.ValueWrapper valueWrapper = Cm.getInstance().getCache(cacheName,tenantMode).get(keyPrefix.concat(String.valueOf(key)));
        return Optional.ofNullable(Fc.notNull(valueWrapper) ? (T) valueWrapper.get() : null).orElseGet(() -> {
            try{
                if (Fc.notNull(valueLoader)) {
                    T call = valueLoader.call();
                    if (Fc.isNotEmpty(call)) {
                        Cm.getInstance().getCache(cacheName,tenantMode).put(keyPrefix.concat(String.valueOf(key)), call);
                        return call;
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        });
    }

    /**
     * @Author mr.g
     * @Description //TODO 获取缓存值
     * @Date 11:32 2020-09-01
     **/
    public static <T> T get(String cacheName, String keyPrefix, Object key, Callable<T> valueLoader) {
        return get(cacheName, keyPrefix, key, valueLoader,Cm.TENANT_MODE);
    }

    /**
     * @Author mr.g
     * @Description //TODO 设置缓存
     * @Date 11:32 2020-09-01
     **/
    public static void put(String cacheName, String keyPrefix, Object key, Object value, Boolean tenantMode) {
        if (!Fc.hasEmpty(cacheName, keyPrefix, key)) {
            Cm.getInstance().getCache(cacheName,tenantMode).put(keyPrefix.concat(String.valueOf(key)), value);
        }
    }

    /**
     * @Author mr.g
     * @Description //TODO 设置缓存
     * @Date 11:32 2020-09-01
     **/
    public static void put(String cacheName, String keyPrefix, Object key, @Nullable Object value) {
        put(cacheName, keyPrefix, key, value,Cm.TENANT_MODE);
    }

    /**
     * @Author mr.g
     * @Description //TODO 删除一个缓存key
     * @Date 11:32 2020-09-01
     **/
    public static void evict(String cacheName, String keyPrefix, Object key, Boolean tenantMode) {
        if (!Fc.hasEmpty(cacheName, keyPrefix, key)) {
            Cm.getInstance().getCache(cacheName,tenantMode).evict(keyPrefix.concat(String.valueOf(key)));
        }
    }

    /**
     * @Author mr.g
     * @Description //TODO 删除一个缓存key
     * @Date 11:32 2020-09-01
     **/
    public static void evict(String cacheName, String keyPrefix, Object key) {
        evict(cacheName,keyPrefix,key,Cm.TENANT_MODE);
    }

    /**
     * @Author mr.g
     * @Description //TODO 清空缓存
     * @Date 11:32 2020-09-01
     **/
    public static void clear(String cacheName, Boolean tenantMode) {
        if (Fc.isNotBlank(cacheName)) {
            Cm.getInstance().getCache(cacheName, tenantMode).clear();
        }
    }

    /**
     * @Author mr.g
     * @Description //TODO 清空指定租户缓存
     * @Date 11:32 2020-09-01
     **/
    public static void clear(String cacheName,String... tenantCode) {
        // 非超管不可操作其他租户缓存
        if (SecureUtil.isRoot() && Fc.notNull(tenantCode) && tenantCode.length > 0){
            if (Fc.isNotBlank(cacheName)) {
                for (String code : tenantCode) {
                    Cm.getInstance().getCache(cacheName, code).clear();
                }
            }
        }else {
            clear(cacheName);
        }
    }

    /**
     * @Author mr.g
     * @Description //TODO 清空缓存
     * @Date 11:32 2020-09-01
     **/
    public static void clear(String cacheName) {
        clear(cacheName, Cm.TENANT_MODE);
    }

}
