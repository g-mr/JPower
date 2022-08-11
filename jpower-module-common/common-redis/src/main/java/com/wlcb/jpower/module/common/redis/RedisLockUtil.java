package com.wlcb.jpower.module.common.redis;

import cn.hutool.core.thread.ThreadUtil;
import com.wlcb.jpower.module.common.utils.Fc;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.listener.adapter.RedisListenerExecutionFailedException;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * redis分布式锁
 *
 * @author mr.g
 * @date 2022-08-10 17:55
 */
@RequiredArgsConstructor
public class RedisLockUtil {

    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 获取锁
     *
     * @author mr.g
     * @param key 锁名称
     * @param expireTime 锁过期时间
     * @param timeUnit 锁过期时间单位
     * @return true为获取到锁，false则没获取到
     **/
    public Boolean lock(String key, long expireTime, TimeUnit timeUnit){
        return redisTemplate.opsForValue().setIfAbsent(key, Fc.randomUUID(), expireTime, timeUnit);
    }

    /**
     * 获取锁
     *
     * @author mr.g
     * @param key 锁名称
     * @return true为获取到锁，false则没获取到
     **/
    public Boolean lock(String key){
        return redisTemplate.opsForValue().setIfAbsent(key, Fc.randomUUID());
    }

    /**
     * 移除锁
     *
     * @author mr.g
     * @param key 锁名称
     * @return 是否删除锁成功
     **/
    public Boolean unlock(String key){
        return redisTemplate.delete(key);
    }

    /**
     * 获取锁并执行
     *
     * @author mr.g
     * @param key 锁名称
     * @param supplier 锁执行函数
     * @param msg 当没有获取到锁时候的提示信息
     * @return V
     **/
    public <V> V lock(String key, Supplier<V> supplier, String msg){
        return lock(key, -1, -1, TimeUnit.SECONDS, supplier,msg);
    }

    /**
     * 获取锁并执行
     *
     * @author mr.g
     * @param key 锁名称
     * @param expireTime 锁过期时间，小于0则代表永不过期
     * @param timeUnit 锁过期时间单位
     * @param supplier 锁执行函数
     * @param msg 当没有获取到锁时候的提示信息
     * @return V
     **/
    public <V> V lock(String key, long expireTime, TimeUnit timeUnit, Supplier<V> supplier, String msg){
        return lock(key, -1, expireTime, timeUnit, supplier,msg);
    }

    /**
     * 获取锁并执行
     *
     * @author mr.g
     * @param key 锁名称
     * @param waitTime 锁等待时间(毫秒)，小于0则代表不等待直接退出
     * @param supplier 锁执行函数
     * @param msg 当没有获取到锁时候的提示信息
     * @return V
     **/
    public <V> V lock(String key, long waitTime, Supplier<V> supplier, String msg){
        return lock(key, waitTime, -1, TimeUnit.SECONDS, supplier,msg);
    }

    /**
     * 获取锁并执行
     *
     * @author mr.g
     * @param key 锁名称
     * @param waitTime 锁等待时间(毫秒)，小于0则代表不等待直接退出
     * @param expireTime 锁过期时间，小于0则代表永不过期
     * @param timeUnit 锁过期时间单位
     * @param supplier 锁执行函数
     * @param msg 当没有获取到锁时候的提示信息，只有waitTime<0会触发
     * @return V
     **/
    public <V> V lock(String key, long waitTime, long expireTime, TimeUnit timeUnit, Supplier<V> supplier, String msg){
        String uidValue = Fc.randomUUID();
        try {
            ValueOperations<String, Object> operations = redisTemplate.opsForValue();
            Boolean is = expireTime >= 0 ? operations.setIfAbsent(key, uidValue, expireTime, timeUnit) : operations.setIfAbsent(key, uidValue);
            if (Fc.toBoolean(is)){
                return supplier.get();
            }else {
                if (waitTime >= 0){
                    ThreadUtil.sleep(waitTime);
                    return lock(key,waitTime,expireTime,timeUnit,supplier,msg);
                } else {
                    throw new RedisListenerExecutionFailedException(msg);
                }
            }
        }finally {
            //释放锁
            if (Fc.equalsValue(uidValue,redisTemplate.opsForValue().get(key))){
                redisTemplate.delete(key);
            }
        }
    }

}
