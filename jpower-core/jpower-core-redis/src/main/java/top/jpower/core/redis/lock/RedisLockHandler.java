package top.jpower.core.redis.lock;

import lombok.RequiredArgsConstructor;
import top.jpower.core.redis.cache.RedisService;
import top.jpower.core.util.support.ThrowableSupplier;

/**
 * @author mr.g
 * @date 2024/11/5 17:57
 */
@RequiredArgsConstructor
public class RedisLockHandler implements LockHandler{

    private final RedisService redisService;

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
        return redisService.lockOps().lock(globalLock.name(), globalLock.waitTime(),
                globalLock.leaveTime(), globalLock.unit(), supplier, "数据处理中，不可重复提交");
    }

}
