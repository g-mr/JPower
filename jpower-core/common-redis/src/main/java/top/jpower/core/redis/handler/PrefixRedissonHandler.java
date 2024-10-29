package top.jpower.core.redis.handler;

import lombok.AllArgsConstructor;
import org.redisson.api.NameMapper;
import org.springframework.util.AntPathMatcher;
import top.jpower.core.redis.properties.RedisProperties;
import top.jpower.core.redis.utils.CachePrefix;
import top.jpower.core.util.constants.StringPool;
import top.jpower.core.util.utils.Fc;
import top.jpower.core.util.utils.StringUtil;

/**
 * 前缀生成器
 *
 * @author mr.g
 * @date 2024-10-24 23:35
 */
@AllArgsConstructor
public class PrefixRedissonHandler implements NameMapper {

    private final RedisProperties.Prefix prefixProperties;
    private final RedisPrefixHandler redisPrefixHandler;
    private static final AntPathMatcher ANT_PATH_MATCHER = new AntPathMatcher();

    /**
     * 是否忽略前缀
     *
     * @author mr.g
     * @param key Redis键
     * @return
     **/
    private boolean ignore(String key){
        return Fc.isNull(redisPrefixHandler) || Fc.isNull(key) ||
                CachePrefix.isClear() ||
                !prefixProperties.getEnabled();
    }

    @Override
    public String map(String name) {

        // 如果清除了缓存前缀 就不处理
        if (ignore(name) || prefixProperties.getIgnore().stream().anyMatch(pattern -> ANT_PATH_MATCHER.match(pattern, name))){
            return name;
        }

        return StringUtil.concat(redisPrefixHandler.getPrefix(name), StringPool.COLON, name);
    }

    @Override
    public String unmap(String name) {

        // 如果清除了缓存前缀 就不处理
        if (ignore(name) || !StringUtil.contains(name, StringPool.COLON)){
            return name;
        }

        String keyAfter = StringUtil.subAfter(name, StringPool.COLON, false);
        if (prefixProperties.getIgnore().stream().anyMatch(pattern -> ANT_PATH_MATCHER.match(pattern, keyAfter))){
            return name;
        }
        String keyBefore = StringUtil.subBefore(name, StringPool.COLON, false);
        String prefix = redisPrefixHandler.getPrefix(keyAfter);

        if (Fc.equalsValue(keyBefore, prefix)){
            return keyAfter;
        } else {
            if (prefixProperties.getIgnore().stream().anyMatch(pattern -> ANT_PATH_MATCHER.match(pattern, name))){
                return name;
            }
        }

        return null;
    }
}
