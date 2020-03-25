package com.wlcb.ylth.module.common.utils.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.concurrent.TimeUnit;

/**
 * @Author 郭丁志
 * @Description //TODO
 * @Date 11:02 2019-11-26
 **/
public class LoginTokenCache {

    /**
     * @Author 郭丁志
     * @Description //TODO 缓存过期时间
     * @Date 19:39 2019-11-26
     **/
    private static final Long CACHE_TIMEOUT = 10 * 60 * 1000L;

    private static volatile Cache<String, String> cache = null;

    static {
        cache = CacheBuilder.newBuilder()
                // 设置初始容量为100
                .initialCapacity(2000)
                // 设置缓存的最大容量
                .maximumSize(10000)
                // TODO: 2019-11-26 设置缓存过期时间为10分钟
                .expireAfterWrite(CACHE_TIMEOUT, TimeUnit.MILLISECONDS)
//                .expireAfterWrite(10, TimeUnit.SECONDS)
                // 设置并发级别为10
                .concurrencyLevel(Runtime.getRuntime().availableProcessors())
                .recordStats() // 开启缓存统计
                .build();
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 在缓存中赋值
     * @Date 11:04 2019-11-26
     * @Param [key, value]
     * @return void
     **/
    public static void put(String key, String value){
        if(key != null && value != null){
            cache.put(key, value);
        }
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 从缓存中取值
     * @Date 11:04 2019-11-26
     * @Param [key]
     * @return java.lang.String
     **/
    public static String get(String key){
        return cache.getIfPresent(key);
    }

    public static void main(String[] args) {
        LoginTokenCache.put("ceshi","123");
        while (true){
            try {
                Thread.sleep(3000);
                System.out.println(LoginTokenCache.get("ceshi"));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

}
