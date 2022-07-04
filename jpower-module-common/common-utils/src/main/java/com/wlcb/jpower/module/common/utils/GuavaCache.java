package com.wlcb.jpower.module.common.utils;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * 本地Guava缓存
 *
 * @author mr.g
 **/
public class GuavaCache {

    private final static Map<Long,GuavaCache> CACHE_MAP = new ConcurrentHashMap<>();
    private Cache<String, Object> cache;

    /**
     * 缓存实例
     *
     * @author mr.g
     * @param expireTime 过期时间
     * @param unit 时间单位
     * @return 缓存
     **/
    public static GuavaCache getInstance(Long expireTime,TimeUnit unit){
        Long key = unit.toNanos(expireTime);

        if (CACHE_MAP.get(key) == null){
            Cache<String, Object> cache = CacheBuilder.newBuilder()
                    .initialCapacity(1000)
                    // 设置缓存在写入一天后失效
                    .expireAfterWrite(expireTime, unit)
                    // 设置并发级别为cpu核心数，默认为4
                    .concurrencyLevel(Runtime.getRuntime().availableProcessors())
                    .build();

            GuavaCache guavaCache = new GuavaCache();
            guavaCache.cache = cache;
            CACHE_MAP.put(key,guavaCache);
        }

        return CACHE_MAP.get(key);
    }

    /**
     * 获取缓存值
     *
     * @author mr.g
     * @param key 键
     * @return 值
     **/
    public Object get(String key) {
        return cache.getIfPresent(key);
    }

    /**
     * 设置一个缓存值
     *
     * @author mr.g
     * @param key 键
     * @param value 值
     **/
    public void put(String key, Object value) {
        if (Fc.notNull(value)){
            cache.put(key, value);
        }
    }
}
