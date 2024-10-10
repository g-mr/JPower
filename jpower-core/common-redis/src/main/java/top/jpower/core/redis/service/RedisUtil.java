package top.jpower.core.redis.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.*;
import top.jpower.core.redis.wrapper.ValueOperationsWrapper;
import top.jpower.core.util.utils.Fc;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * redis服务
 *
 * @author mr.g
 **/
@RequiredArgsConstructor
public class RedisUtil {

    @Getter
    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 分布式锁实现工具类
     **/
    private volatile static RedisLockUtil redisLockUtil;

    /**
     * 分布式锁
     * <br/>
     * 单例模式
     *
     * @author mr.g
     * @return 分布式锁
     **/
    public RedisLockUtil getLock(){
        if (Fc.isNull(redisLockUtil)) {
            synchronized (RedisLockUtil.class) {
                if (Fc.isNull(redisLockUtil)) {
                    redisLockUtil = new RedisLockUtil(getRedisTemplate());
                }
            }
        }
        return redisLockUtil;
    }

    /**
     * Value操作
     *
     * @author mr.g
     * @return org.springframework.data.redis.core.ValueOperations<java.lang.String,java.lang.Object>
     **/
    public ValueOperations<String, Object> value(){
        return redisTemplate.opsForValue();
    }

    /**
     * Value操作
     *
     * @author mr.g
     * @param clz 值类型
     * @return top.jpower.core.redis.wrapper.ValueOperationsWrapper<T>
     **/
    public <T> ValueOperationsWrapper<T> value(Class<T> clz){
        return new ValueOperationsWrapper<>(redisTemplate.opsForValue(), clz);
    }



















































    /**
     * 扫描 实现
     *
     * @param pattern  表达式
     * @param consumer 对迭代到的key进行操作
     */
    public void scan(String pattern, Consumer<byte[]> consumer) {
        getRedisTemplate().execute((RedisConnection connection) -> {
            try (Cursor<byte[]> cursor = connection.scan(ScanOptions.scanOptions().count(1000).match(pattern).build())) {
                cursor.forEachRemaining(consumer);
                return null;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        });
    }

    /**
     * 写入缓存
     *
     * @param key
     * @param value
     * @return
     */
    public boolean set(final String key, Object value) {
        boolean result = false;
        try {
            ValueOperations<String, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 写入缓存设置时效时间
     *
     * @param key
     * @param value
     * @return
     */
    public boolean set(final String key, Object value, Long expireTime, TimeUnit timeUnit) {
        boolean result = false;
        try {
            ValueOperations<String, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value);
            redisTemplate.expire(key, expireTime, timeUnit);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 批量删除对应的value
     *
     * @param keys
     */
    public void remove(final String... keys) {
        for (String key : keys) {
            remove(key);
        }
    }

    /**
     * 批量删除key
     *
     * @param pattern
     */
    public void removePattern(final String pattern) {
        Set<String> keys = redisTemplate.keys(pattern);
        if (keys.size() > 0) {
            redisTemplate.delete(keys);
        }
    }

    /**
     * 删除对应的value
     *
     * @param key
     */
    public void remove(final String key) {
        if (exists(key)) {
            redisTemplate.delete(key);
        }
    }

    /**
     * 判断缓存中是否有对应的value
     *
     * @param key
     * @return
     */
    public boolean exists(final String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * @return java.lang.Long
     * @Author 郭丁志
     * @Description //TODO 获取key的过期时间,并指定返回数据单位
     * @Date 02:29 2020-05-02
     * @Param [key]
     **/
    public Long getExpire(final String key, TimeUnit timeUnit) {
        return redisTemplate.getExpire(key, timeUnit);
    }

    /**
     * @return java.lang.Long
     * @Author 郭丁志
     * @Description //TODO 获取key的过期时间,返回秒
     * @Date 02:29 2020-05-02
     * @Param [key]
     **/
    public Long getExpire(final String key) {
        return redisTemplate.getExpire(key);
    }

    /**
     * 读取缓存
     *
     * @param key
     * @return
     */
    public Object get(final String key) {
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        return operations.get(key);
    }

    /**
     * 批量读取缓存Key
     *
     * @param pattern
     */
    public Set<String> pattern(final String pattern) {
        return redisTemplate.keys(pattern+"*");
    }

    /**
     * 哈希 添加
     *
     * @param key
     * @param hashKey
     * @param value
     */
    public void hmSet(String key, Object hashKey, Object value) {
        HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
        hash.put(key, hashKey, value);
    }

    /**
     * 哈希获取数据
     *
     * @param key
     * @param hashKey
     * @return
     */
    public Object hmGet(String key, Object hashKey) {
        HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
        return hash.get(key, hashKey);
    }

    /**
     * 是否存在HashKey
     *
     * @param key
     * @param hashKey
     * @return
     */
    public Boolean hmHasKey(String key, Object hashKey) {
        HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
        return hash.hasKey(key,hashKey);
    }

    /**
     * 哈希 删除
     *
     * @param key
     * @param hashKey
     */
    public void hmRemove(String key, Object... hashKey) {
        HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
        hash.delete(key, hashKey);
    }

    /**
     * 列表添加
     *
     * @param k
     * @param v
     */
    public void lPush(String k, Object v) {
        ListOperations<String, Object> list = redisTemplate.opsForList();
        list.rightPush(k, v);
    }

    /**
     * 列表获取
     *
     * @param k
     * @param l
     * @param l1
     * @return
     */
    public List<Object> lRange(String k, long l, long l1) {
        ListOperations<String, Object> list = redisTemplate.opsForList();
        return list.range(k, l, l1);
    }

    /**
     * 集合添加
     *
     * @param key
     * @param values
     */
    public void add(String key, Object... values) {
        SetOperations<String, Object> set = redisTemplate.opsForSet();
        set.add(key, values);
    }

    /**
     * 集合获取
     *
     * @param key
     * @return
     */
    public Set<Object> members(String key) {
        SetOperations<String, Object> set = redisTemplate.opsForSet();
        return set.members(key);
    }

    /**
     * 删除集合中的元素
     *
     * @param key
     * @return
     */
    public void removeMembers(String key, Object... values) {
        SetOperations<String, Object> set = redisTemplate.opsForSet();
        set.remove(key, values);
    }

    /**
     * 集合中是否存在元素
     *
     * @param key
     * @return
     */
    public boolean isMember(String key, Object values) {
        SetOperations<String, Object> set = redisTemplate.opsForSet();
        return set.isMember(key, values);
    }

    /**
     * 有序集合添加
     *
     * @param key
     * @param value
     * @param scoure
     */
    public void zAdd(String key, Object value, double scoure) {
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        zset.add(key, value, scoure);
    }

    /**
     * 有序集合获取
     *
     * @param key
     * @param scoure
     * @param scoure1
     * @return
     */
    public Set<Object> rangeByScore(String key, double scoure, double scoure1) {
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        return zset.rangeByScore(key, scoure, scoure1);
    }
}
