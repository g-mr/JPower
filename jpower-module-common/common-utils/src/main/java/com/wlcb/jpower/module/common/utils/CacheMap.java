package com.wlcb.jpower.module.common.utils;


import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

/**
 * 带过期时间的map
 * @author mr.g
 * @date 2021-04-12 17:17
 */
public class CacheMap<K, V> extends ConcurrentHashMap<K, V> {


    private static final long serialVersionUID = -3618727678077606892L;

    private static Map<String,CacheMap> cacheMaps = new ConcurrentHashMap<>();

    public static <K, V> CacheMap<K, V> getInstance(String cacheName){
        if (cacheMaps.get(cacheName) == null){
            CacheMap<K, V> cacheMap = new CacheMap();
            cacheMaps.put(cacheName,cacheMap);
            cacheMap.clearTime(cacheMap);
        }

        return cacheMaps.get(cacheName);
    }

    /**
     * 保存key和对应的过期时间
     */
    private Map<K,Long> expireMap = new HashMap<K, Long>();

    /**
     * 默认过期时间
     */

    public CacheMap(){
        super();
    }

    @Override
    public V put(K key, V value) {
        if(key == null){
            return null;
        }
        expireMap.put(key, 0L);
        return super.put(key, value);
    }

    public V put(K key, V value,long expireTime) {
        if(key == null){
            return null;
        }
        expireMap.put(key, System.currentTimeMillis() + expireTime);
        return super.put(key, value);
    }

    public V put(K key, V value,long expireTime,TimeUnit unit) {
        if(key == null){
            return null;
        }
        expireMap.put(key, System.currentTimeMillis() + unit.toMillis(expireTime));
        return super.put(key, value);
    }

    @Override
    public V get(Object key) {
        if(key == null){
            return null;
        }
        return super.get(key);
    }

    @Override
    public V remove(Object key) {
        if(key == null){
            return null;
        }
        expireMap.remove(key);
        return super.remove(key);
    }

    /**
     * 是否过期
     * @param key
     * @return
     */
    public boolean isExpired(K key){
        if(key == null || !containsKey(key)){
            return false;
        }else {
            long time1 = expireMap.get(key);
            if (time1 == 0L){
                return false;
            }
            long time2 = System.currentTimeMillis();
            return (time2 - time1) > 0;
        }
    }

    /**
     * 移除所有过期键值对
     * @return
     */
    public void removeALLExpired(){
        for(K key : this.keySet()){
            if(isExpired(key)){
                remove(key);
            }
        }
    }

    private void clearTime(CacheMap<K, V> cacheMap){
        new ScheduledThreadPoolExecutor(3, new ThreadFactoryBuilder().setNameFormat("remove-cacheMap-pool-%d").build())
                .scheduleAtFixedRate(()->{
                    if (!Fc.isNull(cacheMap)){
                        cacheMap.removeALLExpired();
                    }
                }, 1, 1, TimeUnit.SECONDS);
    }
}
