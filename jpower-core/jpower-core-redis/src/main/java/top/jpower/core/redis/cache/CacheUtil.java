package top.jpower.core.redis.cache;

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

    /**
     * 获取缓存
     *
     * @author mr.g
     * @param cacheName 缓存名称
     * @param keyPrefix KEY前缀
     * @param key KEY
     * @param clz 返回类型
     * @return T
     **/
    public static <T> T get(String cacheName, String keyPrefix, Object key, Class<T> clz) {
        return get(cacheName, keyPrefix.concat(Fc.toStr(key)), clz);
    }

    /**
     * 获取缓存
     *
     * @author mr.g
     * @param cacheName 缓存名称
     * @param key KEY
     * @param clz 返回类型
     * @return T
     **/
    public static <T> T get(String cacheName, String key, Class<T> clz) {
        if (Fc.hasEmpty(cacheName, key)){
            return null;
        }
        Cache cache = getCache(cacheName);
        if (cache == null){
            return null;
        }
        return cache.get(key, clz);
    }

    /**
     * 获取缓存
     *
     * @author mr.g
     * @param cacheName 缓存名称
     * @param keyPrefix KEY前缀
     * @param key KEY
     * @param valueLoader 默认返回操作
     * @return T
     **/
    public static <T> T get(String cacheName, String keyPrefix, Object key, Callable<T> valueLoader) {
        return get(cacheName, keyPrefix.concat(Fc.toStr(key)), valueLoader);
    }

    /**
     * 获取缓存
     *
     * @author mr.g
     * @param cacheName 缓存名称
     * @param key KEY
     * @param valueLoader 默认返回操作
     * @return T
     **/
    public static <T> T get(String cacheName, String key, Callable<T> valueLoader) {
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
     * 设置缓存
     *
     * @author mr.g
     * @param cacheName 缓存名称
     * @param keyPrefix 缓存前缀
     * @param key KEY
     * @param value 缓存值
     **/
    public static void put(String cacheName, String keyPrefix, Object key, Object value) {
        put(cacheName, keyPrefix.concat(Fc.toStr(key)), value);
    }

    /**
     * 设置缓存
     *
     * @author mr.g
     * @param cacheName 缓存名称
     * @param key KEY
     * @param value 缓存值
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
     * 删除一个缓存key
     *
     * @author mr.g
     * @param cacheName 缓存名称
     * @param key KEY
     **/
    public static void remove(String cacheName, String key) {
        if (!Fc.hasEmpty(cacheName, key)) {
            Cache cache = getCache(cacheName);
            if (cache != null){
                cache.evict(key);
            }
        }
    }

    /**
     * 清空缓存
     *
     * @author mr.g
     * @param cacheName 缓存名称
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
