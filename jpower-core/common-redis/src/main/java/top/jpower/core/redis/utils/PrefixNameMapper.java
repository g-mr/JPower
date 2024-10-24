package top.jpower.core.redis.utils;

import org.redisson.api.NameMapper;

/**
 * 前缀生成器
 *
 * @author mr.g
 * @date 2024-10-24 23:35
 */
public class PrefixNameMapper implements NameMapper {
    @Override
    public String map(String name) {

        if (CachePrefix.isClear()){

        }

        return name;
    }

    @Override
    public String unmap(String name) {

        if (CachePrefix.isClear()){

        }

        return name;
    }
}
