package top.jpower.core.redis.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import top.jpower.core.redis.handler.RedisPrefixHandler;
import top.jpower.core.util.utils.Fc;
import top.jpower.core.util.utils.SpringUtil;

import java.util.concurrent.Callable;

/**
 * 缓存工具
 *
 * @author mr.g
 */
@Slf4j
public class CacheUtil {

    private static final CacheManager CACHE_MANAGER;
    private static final RedisPrefixHandler REDIS_PREFIX_HANDLER;

    static {
        CACHE_MANAGER = SpringUtil.getBean(CacheManager.class);
        REDIS_PREFIX_HANDLER = SpringUtil.getBean(RedisPrefixHandler.class);
    }

    /**
     * 清空key的时候是否要清空全部前缀的
     *
     * @author mr.g
     * @param cacheName 缓存名称
     * @return boolean 是否
     **/
    static boolean clearForAll(String cacheName) {
        return Fc.notNull(REDIS_PREFIX_HANDLER) && REDIS_PREFIX_HANDLER.deleteForAll(cacheName);
    }

    public static Cache getCache(String cacheName){
        if (Fc.isBlank(cacheName)){
            return null;
        }
        return CACHE_MANAGER.getCache(cacheName);
    }

    public static <T> T get(String cacheName, Object key, Class<T> clz) {
        if (Fc.hasEmpty(cacheName, key)){
            return null;
        }
        Cache cache = getCache(cacheName);
        if (cache == null){
            return null;
        }
        return cache.get(key, clz);
    }

    public static <T> T get(String cacheName, String keys, Object key, Callable<T> valueLoader) {
        if (Fc.hasEmpty(cacheName, key)) {
            return null;
        }
        Cache cache = getCache(cacheName);
        if (cache == null){
            return null;
        }
        return cache.get(key, valueLoader);
    }

    /**
     * @Author mr.g
     * @Description //TODO 设置缓存
     * @Date 11:32 2020-09-01
     **/
    public static void put(String cacheName, Object key, Object value) {
        if (!Fc.hasEmpty(cacheName, key)) {
            Cache cache = getCache(cacheName);
            if (cache != null){
                cache.put(key, value);
            }
        }
    }

    /**
     * @Author mr.g
     * @Description //TODO 删除一个缓存key
     * @Date 11:32 2020-09-01
     **/
    public static void remove(String cacheName, Object key) {
        if (!Fc.hasEmpty(cacheName, key)) {
            Cache cache = getCache(cacheName);
            if (cache != null){
                cache.evict(key);
            }
        }
    }

    /**
     * @Author mr.g
     * @Description //TODO 清空缓存
     * @Date 11:32 2020-09-01
     **/
    public static void clear(String cacheName) {
        if (Fc.isNotBlank(cacheName)) {
            Cache cache = getCache(cacheName);
            if (cache != null){
                cache.clear();

                // 删除全部前缀
                if (clearForAll(cacheName)){
                    RedisService.getInstance().delete(cacheName);
                }
            }
        }
    }

}
