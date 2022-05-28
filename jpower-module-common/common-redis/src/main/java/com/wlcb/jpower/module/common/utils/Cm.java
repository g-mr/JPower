package com.wlcb.jpower.module.common.utils;

import com.wlcb.jpower.module.common.support.EnvBeanUtil;
import com.wlcb.jpower.module.common.utils.constants.StringPool;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

/**
 * @Author mr.g
 * @Date 2021/5/8 0008 0:34
 */
public class Cm {

    public static final Boolean TENANT_MODE = EnvBeanUtil.get("jpower.tenant.enable", Boolean.class, true);

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
        return getCache(cacheName,TENANT_MODE,Fc.isNoneBlank(tenantCode)?tenantCode:SecureUtil.getTenantCode());
    }

    /**
     * @Author mr.g
     * @Description //TODO 获取cache
     * @Date 11:32 2020-09-01
     **/
    public Cache getCache(String cacheName) {
        return getCache(cacheName,TENANT_MODE);
    }

    /**
     * @Author mr.g
     * @Description //TODO 获取cache
     * @Date 11:32 2020-09-01
     **/
    public Cache getCache(String cacheName,Boolean tenantMode) {
        return getCache(cacheName,tenantMode,SecureUtil.getTenantCode());
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
