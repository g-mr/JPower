package com.wlcb.jpower.module.common.utils;

import java.util.UUID;

/**
 * UUID 工具
 *
 * @author mr.g
 **/
public class UuidUtil extends cn.hutool.core.lang.UUID {

    private static final long serialVersionUID = -8620701627379245225L;

    /**
     * 使用指定的数据构造新的 UUID。
     *
     * @param mostSigBits  用于 {@code UUID} 的最高有效 64 位
     * @param leastSigBits 用于 {@code UUID} 的最低有效 64 位
     */
    public UuidUtil(long mostSigBits, long leastSigBits) {
        super(mostSigBits, leastSigBits);
    }

    /**
     * 获取UUID
     *
     * @author mr.g
     * @return java.lang.String
     **/
    public static String getUUID() {
        return fastUUID().toString(Boolean.TRUE);
    }

    /**
     * 生成10位数字型UUId
     *
     * @author mr.g
     * @return java.lang.String
     **/
    public static String create10UUidNum() {
        int hashCodeV = UUID.randomUUID().toString().hashCode();
        //有可能是负数
        if(hashCodeV < 0) {
            hashCodeV = - hashCodeV;
        }
        // 0 代表前面补充0
        // 4 代表长度为4
        // d 代表参数为正数型
        return String.format("%010d", hashCodeV);
    }
}
