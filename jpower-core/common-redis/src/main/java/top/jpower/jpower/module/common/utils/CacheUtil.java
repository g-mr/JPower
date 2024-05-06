package top.jpower.jpower.module.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.lang.Nullable;
import top.jpower.core.util.constants.StringPool;
import top.jpower.core.util.utils.Fc;
import top.jpower.core.util.utils.SpringUtil;
import top.jpower.jpower.module.tenant.JpowerTenantProperties;

import java.util.Optional;
import java.util.concurrent.Callable;

/**
 * 缓存工具
 *
 * @author mr.g
 */
@Slf4j
public class CacheUtil {

    public static Boolean TENANT_MODE = Boolean.FALSE;

    static {
        try {
            TENANT_MODE = SpringUtil.getBean(JpowerTenantProperties.class).getEnable();
        } catch (Exception e){
            log.warn("未获取到JpowerTenantProperties，无法启动租户自动区分...");
        }
    }

    private static Boolean isRoot(){
        try {
            return ShieldUtil.isRoot();
        } catch (Exception e){
            return Boolean.FALSE;
        }
    }

    public static <T> T get(String cacheName, String keyPrefix, Object key,@Nullable Class<T> clz) {
        return get(cacheName,keyPrefix,key,clz, TENANT_MODE);
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
                        Cm.getInstance().getCache(cacheName, tenantMode).put(keyPrefix.concat(String.valueOf(key)), call);
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
     * 获取缓存值
     *
     * @author mr.g
     **/
    public static <T> T get(String cacheName, String keyPrefix, Object key, Callable<T> valueLoader) {
        return get(cacheName, keyPrefix, key, valueLoader, TENANT_MODE);
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
        put(cacheName, keyPrefix, key, value, TENANT_MODE);
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
        evict(cacheName,keyPrefix,key, TENANT_MODE);
    }

    /**
     * @Author mr.g
     * @Description //TODO 清空缓存
     * @Date 11:32 2020-09-01
     **/
    public static void clear(String cacheName, Boolean tenantMode) {
        if (Fc.isNotBlank(cacheName)) {
            if (tenantMode && isRoot()){
                Cm.getInstance().getCache("*" + StringPool.COLON + cacheName, Boolean.FALSE).clear();
            } else {
                Cm.getInstance().getCache(cacheName, tenantMode).clear();
            }
        }
    }

    /**
     * @Author mr.g
     * @Description //TODO 清空指定租户缓存
     * @Date 11:32 2020-09-01
     **/
    public static void clear(String cacheName,String... tenantCode) {
        if (Fc.isNotEmpty(tenantCode)){
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
        clear(cacheName, TENANT_MODE);
    }

}
