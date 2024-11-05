package top.jpower.core.redis.lock;

/**
 * @author mr.g
 * @date 2024-11-5 22:21
 * @description
 */
public enum LockTypeEnum {

    /**
     * 公平锁
     */
    FAIR,
    /**
     * 重入锁
     **/
    REENTRANT,
    /**
     * 自旋锁
     */
    SPIN,
    /**
     * 读锁
     */
    READ,
    /**
     * 写锁
     */
    WRITE,
    // /**
    //  * fencing token 锁
    //  */
    // FENCED,
    // /**
    //  * 联锁
    //  */
    // MULTI,

}
