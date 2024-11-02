package top.jpower.core.redis.handler;

/**
 * @author mr.g
 * @date 2024-9-22 19:10
 * @description
 */
public interface RedisPrefixHandler {

    /**
     * 生成前缀
     *
     * @author mr.g
     * @param key 缓存KEY
     * @return 前缀
     **/
    String getPrefix(String key);

    /**
     * 删除KEY的时候是否删除所有的前缀
     * <br/>
     * 删除缓存的时候是否要忽略前缀把所有的KEY都删调
     *
     * @author mr.g
     * @param key 缓存KEY
     * @return 是否忽略
     **/
    boolean deleteForAll(String key);
}
