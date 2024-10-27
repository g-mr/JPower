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
    private static final AntPathMatcher antPathMatcher = new AntPathMatcher();

    /**
     * 是否忽略前缀
     *
     * @author mr.g
     * @param key Redis键
     * @return
     **/
    private boolean ignore(String key){
        return Fc.isNull(key) ||
                CachePrefix.isClear() ||
                !prefixProperties.getEnabled() ||
                prefixProperties.getIgnore().stream().anyMatch(pattern -> antPathMatcher.match(pattern, key));
    }

    @Override
    public String map(String name) {

        // 如果清除了缓存前缀 就不处理
        if (ignore(name)){
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


        // todo 这里还要想想    通过第一个:分割之后，判断前缀和获取的前缀是否一致？后面怎么处理

        String prefix = redisPrefixHandler.getPrefix(name);
        if (StringUtil.startWith(name, prefix)){
            String key = StringUtil.removePrefix(name, StringUtil.concat(prefix, StringPool.COLON));
            if (prefixProperties.getIgnore().stream().anyMatch(pattern -> antPathMatcher.match(pattern, key))){
                return name;
            }
            return key;
        }

        return null;
    }
}
