package top.jpower.jpower.module.common.utils;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import top.jpower.core.util.constants.StringPool;
import top.jpower.core.util.utils.Fc;
import top.jpower.core.util.utils.SpringUtil;

import static top.jpower.jpower.module.common.utils.CacheUtil.TENANT_MODE;

/**
 * @Author mr.g
 * @Date 2021/5/8 0008 0:34
 */
public class Cm {

    private CacheManager cacheManager;

    private static Cm instance;

    public static Cm getInstance(){
        if (Fc.isNull(instance)){
            instance = new Cm();
            instance.cacheManager = SpringUtil.getBean(CacheManager.class);
        }
        return instance;
    }

    /**
     * @Author mr.g
     * @Description //TODO 获取指定租户得cache
     * @Date 11:32 2020-09-01
     **/
    public Cache getCache(String cacheName, String tenantCode) {
        return getCache(cacheName, Boolean.TRUE, tenantCode);
    }

    /**
     * @Author mr.g
     * @Description //TODO 获取cache
     * @Date 11:32 2020-09-01
     **/
    public Cache getCache(String cacheName) {
        return getCache(cacheName, TENANT_MODE);
    }

    /**
     * @Author mr.g
     * @Description //TODO 获取cache
     * @Date 11:32 2020-09-01
     **/
    public Cache getCache(String cacheName,Boolean tenantMode) {
        if (tenantMode){
            try {
                return getCache(cacheName, ShieldUtil.getTenantCode());
            } catch (Exception ignored){}
        }

        return getCache(cacheName, Boolean.FALSE, StringPool.EMPTY);
    }

    /**
     * @Author mr.g
     * @Description //TODO 获取cache
     * @Date 11:32 2020-09-01
     **/
    public Cache getCache(String cacheName,Boolean tenantMode, String tenantCode) {
        if (Fc.isNotBlank(tenantCode) && tenantMode){
            return cacheManager.getCache(tenantCode.concat(StringPool.COLON).concat(cacheName));
        }
        return cacheManager.getCache(cacheName);
    }

}
