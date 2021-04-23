package com.wlcb.jpower.module.common.utils;

import com.wlcb.jpower.module.common.utils.constants.StringPool;
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

    private static final Boolean TENANT_MODE = Boolean.TRUE;

    public static CacheManager getCacheManager() {
        if (cacheManager == null) {
            cacheManager = SpringUtil.getBean(CacheManager.class);
        }

        return cacheManager;
    }

    /**
     * @Author mr.g
     * @Description //TODO 获取指定租户得cache
     * @Date 11:32 2020-09-01
     **/
    public static Cache getCache(String cacheName, String tenantCode) {
        return getCache(cacheName,TENANT_MODE,tenantCode);
    }

    /**
     * @Author mr.g
     * @Description //TODO 获取cache
     * @Date 11:32 2020-09-01
     **/
    public static Cache getCache(String cacheName) {
        return getCache(cacheName,TENANT_MODE);
    }

    /**
     * @Author mr.g
     * @Description //TODO 获取cache
     * @Date 11:32 2020-09-01
     **/
    public static Cache getCache(String cacheName,Boolean tenantMode) {
        return getCache(cacheName,tenantMode,SecureUtil.getTenantCode());
    }

    /**
     * @Author mr.g
     * @Description //TODO 获取cache
     * @Date 11:32 2020-09-01
     **/
    public static Cache getCache(String cacheName,Boolean tenantMode, String tenantCode) {
        if (Fc.isNotBlank(tenantCode) && tenantMode){
            return getCacheManager().getCache(tenantCode.concat(StringPool.COLON).concat(cacheName));
        }
        return getCacheManager().getCache(cacheName);
    }

    public static <T> T get(String cacheName, String keyPrefix, Object key,@Nullable Class<T> clz) {
        return get(cacheName,keyPrefix,key,clz,TENANT_MODE);
    }

    public static <T> T get(String cacheName, String keyPrefix, Object key,@Nullable Class<T> clz, Boolean tenantMode) {
        if (Fc.hasEmpty(cacheName,keyPrefix,key)){
            return null;
        }
        return getCache(cacheName,tenantMode).get(keyPrefix.concat(Fc.toStr(key)), clz);
    }

    public static <T> T get(String cacheName, String keyPrefix, Object key, Callable<T> valueLoader,Boolean tenantMode) {
        if (Fc.hasEmpty(cacheName, keyPrefix, key)) {
            return null;
        }
        try {
            Cache.ValueWrapper valueWrapper = getCache(cacheName,tenantMode).get(keyPrefix.concat(String.valueOf(key)));
            Object value = null;
            if (Fc.isNull(valueWrapper) && !Fc.isNull(valueLoader)) {
                T call = valueLoader.call();
                if (Fc.isNotEmpty(call)) {
                    Field field = ReflectUtil.getAccessibleField(call.getClass(), "id");
                    if (ObjectUtil.isNotEmpty(field) && Fc.isEmpty(ClassUtil.getMethod(call.getClass(), "getId", new Class[0]).invoke(call))) {
                        return null;
                    }

                    getCache(cacheName,tenantMode).put(keyPrefix.concat(String.valueOf(key)), call);
                    value = call;
                }
            } else {
                value = Fc.isNull(valueWrapper)?null:valueWrapper.get();
            }

            return (T) value;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * @Author mr.g
     * @Description //TODO 获取缓存值
     * @Date 11:32 2020-09-01
     **/
    public static <T> T get(String cacheName, String keyPrefix, Object key, Callable<T> valueLoader) {
        return get(cacheName, keyPrefix, key, valueLoader,TENANT_MODE);
    }

    /**
     * @Author mr.g
     * @Description //TODO 设置缓存
     * @Date 11:32 2020-09-01
     **/
    public static void put(String cacheName, String keyPrefix, Object key, @Nullable Object value, Boolean tenantMode) {
        if (!Fc.hasEmpty(cacheName, keyPrefix, key)) {
            getCache(cacheName,tenantMode).put(keyPrefix.concat(String.valueOf(key)), value);
        }
    }

    /**
     * @Author mr.g
     * @Description //TODO 设置缓存
     * @Date 11:32 2020-09-01
     **/
    public static void put(String cacheName, String keyPrefix, Object key, @Nullable Object value) {
        put(cacheName, keyPrefix, key, value,TENANT_MODE);
    }

    /**
     * @Author mr.g
     * @Description //TODO 删除一个缓存key
     * @Date 11:32 2020-09-01
     **/
    public static void evict(String cacheName, String keyPrefix, Object key, Boolean tenantMode) {
        if (!Fc.hasEmpty(cacheName, keyPrefix, key)) {
            getCache(cacheName,tenantMode).evict(keyPrefix.concat(String.valueOf(key)));
        }
    }

    /**
     * @Author mr.g
     * @Description //TODO 删除一个缓存key
     * @Date 11:32 2020-09-01
     **/
    public static void evict(String cacheName, String keyPrefix, Object key) {
        evict(cacheName,keyPrefix,key,TENANT_MODE);
    }

    /**
     * @Author mr.g
     * @Description //TODO 清空缓存
     * @Date 11:32 2020-09-01
     **/
    public static void clear(String cacheName, Boolean tenantMode) {
        if (Fc.isNotBlank(cacheName)) {
            getCache(cacheName, tenantMode).clear();
        }
    }

    /**
     * @Author mr.g
     * @Description //TODO 清空指定租户缓存
     * @Date 11:32 2020-09-01
     **/
    public static void clear(String cacheName,String tenantCode) {
        // 非超管不可操作其他租户缓存
        if (SecureUtil.isRoot()){
            if (Fc.isNotBlank(cacheName)) {
                getCache(cacheName, tenantCode).clear();
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
        clear(cacheName, TENANT_MODE);
    }
}
