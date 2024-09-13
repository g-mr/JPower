package top.jpower.core.redis.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import top.jpower.core.util.utils.Fc;

import java.util.Optional;
import java.util.concurrent.Callable;

/**
 * 缓存工具
 *
 * @author mr.g
 */
@Slf4j
public class CacheUtil {



    public static <T> T get(String cacheName, String keyPrefix, Object key, Class<T> clz) {
        if (Fc.hasEmpty(cacheName,keyPrefix,key)){
            return null;
        }
        return Cm.getInstance().getCache(cacheName).get(keyPrefix.concat(Fc.toStr(key)), clz);
    }

    public static <T> T get(String cacheName, String keyPrefix, Object key, Callable<T> valueLoader) {
        if (Fc.hasEmpty(cacheName, keyPrefix, key)) {
            return null;
        }
        Cache.ValueWrapper valueWrapper = Cm.getInstance().getCache(cacheName).get(keyPrefix.concat(String.valueOf(key)));
        return Optional.ofNullable(Fc.notNull(valueWrapper) ? (T) valueWrapper.get() : null).orElseGet(() -> {
            try{
                if (Fc.notNull(valueLoader)) {
                    T call = valueLoader.call();
                    if (Fc.isNotEmpty(call)) {
                        Cm.getInstance().getCache(cacheName).put(keyPrefix.concat(String.valueOf(key)), call);
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
     * @Description //TODO 设置缓存
     * @Date 11:32 2020-09-01
     **/
    public static void put(String cacheName, String keyPrefix, Object key, Object value) {
        if (!Fc.hasEmpty(cacheName, keyPrefix, key)) {
            Cm.getInstance().getCache(cacheName).put(keyPrefix.concat(String.valueOf(key)), value);
        }
    }

    /**
     * @Author mr.g
     * @Description //TODO 删除一个缓存key
     * @Date 11:32 2020-09-01
     **/
    public static void evict(String cacheName, String keyPrefix, Object key) {
        if (!Fc.hasEmpty(cacheName, keyPrefix, key)) {
            Cm.getInstance().getCache(cacheName).evict(keyPrefix.concat(String.valueOf(key)));
        }
    }

    /**
     * @Author mr.g
     * @Description //TODO 清空缓存
     * @Date 11:32 2020-09-01
     **/
    public static void clear(String cacheName) {
        if (Fc.isNotBlank(cacheName)) {
            Cm.getInstance().getCache(cacheName).clear();
        }
    }

}
