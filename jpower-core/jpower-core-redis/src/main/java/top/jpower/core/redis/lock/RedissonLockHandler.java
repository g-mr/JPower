package top.jpower.core.redis.lock;

import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import top.jpower.core.util.support.ThrowableSupplier;

/**
 * Redisson锁具体实现
 *
 * @author mr.g
 * @date 2024/11/5 17:57
 */
@RequiredArgsConstructor
public class RedissonLockHandler implements LockHandler{

    private final RedissonClient redissonClient;

    /**
     * lock具体实现
     *
     * @param lockDto 锁参数
     * @param supplier   锁的执行内容
     * @return 执行内容
     * @author mr.g
     **/
    @Override
    public <V> V lock(LockDto lockDto, ThrowableSupplier<V> supplier) throws Throwable {

        RLock lock = getLock(lockDto);

        try {
            if (lock.tryLock(lockDto.waitTime(), lockDto.leaveTime(), lockDto.unit())) {
                return supplier.get();
            } else {
                throw new RuntimeException("数据处理中，不可重复请求");
            }
        } finally {
            if (lock.isLocked() && lock.isHeldByCurrentThread()){
                lock.unlock();
            }
        }
    }

    /**
     * 获取锁
     *
     * @author mr.g
     * @param lockDto 锁参数
     * @return org.redisson.api.RLock
     **/
    private RLock getLock(LockDto lockDto) {

        RLock lock;
        switch (lockDto.type()) {
            case FAIR:
                lock = redissonClient.getFairLock(lockDto.name());
                break;
            case SPIN:
                lock = redissonClient.getSpinLock(lockDto.name());
                break;
            case READ:
                lock = redissonClient.getReadWriteLock(lockDto.name()).readLock();
                break;
            case WRITE:
                lock = redissonClient.getReadWriteLock(lockDto.name()).writeLock();
                break;
            case REENTRANT:
            default:
                lock = redissonClient.getLock(lockDto.name());
        }

        return lock;
    }

}
