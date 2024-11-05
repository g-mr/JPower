package top.jpower.core.redis.lock;

import top.jpower.core.util.support.ThrowableSupplier;

/**
 * @author mr.g
 * @date 2024/11/5 17:51
 */
public interface LockHandler {

    /**
     * lock具体实现
     * @author mr.g
     * @param lockDto 锁参数
     * @param supplier 锁的执行内容
     * @return
     **/
    <V> V lock(LockDto lockDto, ThrowableSupplier<V> supplier) throws Throwable;
}
