package top.jpower.core.redis.service;

import cn.hutool.core.lang.Singleton;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import top.jpower.core.redis.handler.RedisPrefixHandler;
import top.jpower.core.util.utils.Fc;
import top.jpower.core.util.utils.SpringUtil;

/**
 * @Author mr.g
 * @Date 2021/5/8 0008 0:34
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Cm {

    private final CacheManager cacheManager;
    private final RedisPrefixHandler redisPrefixHandler;

    public static Cm getInstance(){
        return Singleton.get(Cm.class.getName(), () -> new Cm(SpringUtil.getBean(CacheManager.class), SpringUtil.getBean(RedisPrefixHandler.class)));
    }

    /**
     * 清空key的时候是否要清空全部前缀的
     *
     * @author mr.g
     * @param cacheName 缓存名称
     * @return boolean 是否
     **/
    public boolean clearForAll(String cacheName) {
        return Fc.notNull(redisPrefixHandler) && redisPrefixHandler.deleteForAll(cacheName);
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
