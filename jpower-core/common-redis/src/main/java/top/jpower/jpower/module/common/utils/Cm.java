package top.jpower.jpower.module.common.utils;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import top.jpower.core.util.utils.Fc;
import top.jpower.core.util.utils.SpringUtil;

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
     * @Description //TODO 获取cache
     * @Date 11:32 2020-09-01
     **/
    public Cache getCache(String cacheName) {
        return cacheManager.getCache(cacheName);
    }

}
