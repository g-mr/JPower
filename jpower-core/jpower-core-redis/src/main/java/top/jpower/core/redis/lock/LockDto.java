package top.jpower.core.redis.lock;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * @author mr.g
 * @date 2024-11-5 22:07
 * @description
 */
@Data
@Accessors(fluent = true)
@Builder
public class LockDto implements Serializable {

    /**
     * 锁名称
     **/
    private String name;

    /**
     * 锁类型
     **/
    private LockTypeEnum type;

    /**
     * 获取锁的最大等待时间
     * <br/>
     * 默认10秒
     **/
    private long waitTime;

    /**
     * 锁离开时间
     * <br/>
     * 默认50秒
     **/
    private long leaveTime;

    /**
     * 时间单位
     **/
    private TimeUnit unit;

}
