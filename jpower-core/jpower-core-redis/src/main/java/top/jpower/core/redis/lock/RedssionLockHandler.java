package top.jpower.core.redis.lock;

import lombok.RequiredArgsConstructor;
import org.redisson.api.RedissonClient;
import top.jpower.core.util.support.ThrowableSupplier;

/**
 * @author mr.g
 * @date 2024/11/5 17:57
 */
@RequiredArgsConstructor
public class RedssionLockHandler implements LockHandler{

    private final RedissonClient redissonClient;

    /**
     * lock具体实现
     *
     * @param globalLock 锁参数
     * @param supplier   锁的执行内容
     * @return 执行内容
     * @author mr.g
     **/
    @Override
    public <V> V lock(GlobalLock globalLock, ThrowableSupplier<V> supplier) {

        redissonClient.getSpinLock("").

        return null;
    }

}
