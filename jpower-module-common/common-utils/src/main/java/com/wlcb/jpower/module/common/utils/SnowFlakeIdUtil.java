package com.wlcb.jpower.module.common.utils;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author mr.g
 * @Description 雪花ID生成器
 */
public class SnowFlakeIdUtil {

    // ==============================Fields===========================================
    //起始时间戳 2020-01-01 00:00:00
    private final static long twepoch = 1483200000000L;

    /**
     * 机器id所占的位数
     */
    private final long appIdBits = 5L;

    /**
     * 数据标识id所占的位数
     */
    private final long instanceIdBits = 5L;

    /**
     * 支持的最大机器id，结果是31 (这个移位算法可以很快的计算出几位二进制数所能表示的最大十进制数)
     */
    private final long maxAppId = -1L ^ (-1L << appIdBits);

    /**
     * 支持的最大数据标识id，结果是31
     */
    private final long maxInstanceId = -1L ^ (-1L << instanceIdBits);

    /**
     * 序列在id中占的位数
     */
    private final long sequenceBits = 12L;

    /**
     * 机器ID向左移12位
     */
    private final long appIdShift = sequenceBits;

    /**
     * 数据标识id向左移17位(12+5)
     */
    private final long instanceIdShift = sequenceBits + appIdBits;

    /**
     * 时间截向左移22位(5+5+12)
     */
    private final long timestampLeftShift = sequenceBits + appIdBits + instanceIdBits;

    /**
     * 生成序列的掩码，这里为4095 (0b111111111111=0xfff=4095)
     */
    private final long sequenceMask = -1L ^ (-1L << sequenceBits);

    /**
     * 工作机器ID(0~31)
     */
    private long appId;

    /**
     * 数据中心ID(0~31)
     */
    private long instanceId;

    /**
     * 毫秒内序列(0~4095)
     */
    private AtomicLong sequence = new AtomicLong(0L);

    /**
     * 上次生成ID的时间截
     */
    private long lastTimestamp = -1L;

    /**
     * 测试snowflake算法生成id的速度
     */
    public static void main(String[] args) {

        SnowFlakeIdUtil instance = SnowFlakeIdUtil.getInstance();
        instance.init(1, 1);
        long l = instance.nextId();
        System.out.println(l);

        System.out.println(SnowFlakeIdUtil.getInstance().nextId());
        System.out.println(SnowFlakeIdUtil.getInstance().nextId());
        System.out.println(SnowFlakeIdUtil.getInstance().nextId());
        System.out.println(SnowFlakeIdUtil.getInstance().nextId());
    }

    /**
     * 获得下一个ID (该方法是线程安全的)
     *
     * @return SnowflakeId
     */
    public synchronized long nextId() {
        long timestamp = timeGen();

        //如果当前时间小于上一次ID生成的时间戳，说明系统时钟回退过这个时候应当抛出异常
        if (timestamp < lastTimestamp) {
            throw new RuntimeException(
                    String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }

        //如果是同一时间生成的，则进行毫秒内序列
        if (lastTimestamp == timestamp) {
//            sequence = (sequence + 1) & sequenceMask;
            sequence.set(sequence.incrementAndGet() & sequenceMask);

            //毫秒内序列溢出
//            if (sequence == 0) {
            if (sequence.intValue() == 0) {
                //阻塞到下一个毫秒,获得新的时间戳
                timestamp = tilNextMillis(lastTimestamp);
            }
        }
        //时间戳改变，毫秒内序列重置
        else {
//            sequence = 0L;
            sequence.set(0);
        }

        //上次生成ID的时间截
        lastTimestamp = timestamp;

        //移位并通过或运算拼到一起组成64位的ID
        return ((timestamp - twepoch) << timestampLeftShift) //
                | (instanceId << instanceIdShift) //
                | (appId << appIdShift) //
                | sequence.intValue();
    }

    /**
     * 阻塞到下一个毫秒，直到获得新的时间戳
     *
     * @param lastTimestamp 上次生成ID的时间截
     * @return 当前时间戳
     */
    protected long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    /**
     * 返回以毫秒为单位的当前时间
     *
     * @return 当前时间(毫秒)
     */
    protected long timeGen() {
        return System.currentTimeMillis();
    }


    /**
     * 使用静态内部类实现单例
     */
    private static class SnowFlakeIdUtilInstance {
        private static SnowFlakeIdUtil INSTANCE = new SnowFlakeIdUtil();
    }

    public static SnowFlakeIdUtil getInstance() {
        return SnowFlakeIdUtilInstance.INSTANCE;
    }

    /**
     * 初始化并配置机器码id和数据id
     *
     * @param appId 0-31
     * @param instanceId   0-31
     */
    public void init(long appId, long instanceId) {
        if (appId > maxAppId || appId < 0) {
            throw new IllegalArgumentException(String.format("worker Id can't greater than %d or less than 0", maxAppId));
        }
        if (instanceId > maxInstanceId || instanceId < 0) {
            throw new IllegalArgumentException(String.format("data Id can't greater than %d or less than 0", maxInstanceId));
        }
        this.appId = appId;
        this.instanceId = instanceId;
    }

}
