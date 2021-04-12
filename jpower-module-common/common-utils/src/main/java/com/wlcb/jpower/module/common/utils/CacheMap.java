package com.wlcb.jpower.module.common.utils;


import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 带过期时间的map
 * @author mr.g
 * @date 2021-04-12 17:17
 */
public class CacheMap<K, V> extends ConcurrentHashMap<K, V> {


    private static final long serialVersionUID = -3618727678077606892L;

    private static Map<String,CacheMap> cacheMaps = new HashMap<>();

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

    private long EXPIRE_TIME = 1000;

    /**
     * 默认过期时间
     */

    public CacheMap(){
        super();
    }

    public CacheMap(long expire){
        this.EXPIRE_TIME = expire;
    }

    public CacheMap(TimeUnit unit, long duration){
        this.EXPIRE_TIME = unit.toMillis(duration);
    }

    @Override
    public V put(K key, V value) {
        if(key == null){
            return null;
        }
        expireMap.put(key, System.currentTimeMillis() + EXPIRE_TIME);
        return super.put(key, value);
    }

    public V put(K key, V value,long expireTime) {
        if(key == null){
            return null;
        }
        expireMap.put(key, System.currentTimeMillis() + expireTime);
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

    private void clearTime(CacheMap cacheMap){
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                if (!Fc.isNull(cacheMap)){
                    cacheMap.removeALLExpired();
                }
            }
        }, 10, 10, TimeUnit.SECONDS);
    }
}
