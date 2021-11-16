package com.wlcb.jpower.module.common.utils;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * 本地Guava缓存
 * @Author mr.g
 * @Date 2021/11/16 0016 23:36
 */
public class GuavaCache {

    private final static Map<Long,GuavaCache> cacheMaps = new ConcurrentHashMap<>();
    private Cache<String, Object> CACHE;

    public static GuavaCache getInstance(Long expireTime,TimeUnit unit){
        Long key = unit.toNanos(expireTime);

        if (cacheMaps.get(key) == null){
            Cache<String, Object> cache = CacheBuilder.newBuilder()
                    .initialCapacity(1000)
                    // 设置缓存在写入一天后失效
                    .expireAfterWrite(expireTime, unit)
                    // 设置并发级别为cpu核心数，默认为4
                    .concurrencyLevel(Runtime.getRuntime().availableProcessors())
                    .build();

            GuavaCache guavaCache = new GuavaCache();
            guavaCache.CACHE = cache;
            cacheMaps.put(key,guavaCache);
        }

        return cacheMaps.get(key);
    }

    public Object get(String key) {
        return CACHE.getIfPresent(key);
    }

    public void put(String key, Object value) {
        CACHE.put(key, value);
    }
}
