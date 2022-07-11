package com.wlcb.jpower.module.common.utils;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.IdUtil;

/**
 * 雪花ID生成工具
 *
 * @author mr.g
 */
public class SnowFlakeIdUtil {

    /**
     * 生成ID
     *
     * @param workerId 终端ID
     * @param dataCenterId 数据中心ID
     * @author mr.g
     * @return 雪花ID
     **/
    public static long nextId(long workerId, long dataCenterId){
        return IdUtil.getSnowflake(workerId, dataCenterId).nextId();
    }

    /**
     * 生成ID
     *
     * @author mr.g
     * @return 雪花ID
     **/
    public static long nextId(){
        return IdUtil.getSnowflake().nextId();
    }

    /**
     * 生成ID(字符串形式)
     *
     * @author mr.g
     * @return ID 字符串形式
     **/
    public static String nextIdStr(){
        return IdUtil.getSnowflake().nextIdStr();
    }

    /**
     * 根据Snowflake的ID，获取生成时间
     *
     * @param id snowflake算法生成的id
     * @return 生成的时间戳
     */
    public static long getGenerateTimestamp(long id){
        return IdUtil.getSnowflake().getGenerateDateTime(id);
    }

    /**
     * 根据Snowflake的ID，获取生成时间
     *
     * @param id snowflake算法生成的id
     * @return 生成的时间
     */
    public static DateTime getGenerateDateTime(long id){
        return DateUtil.date(IdUtil.getSnowflake().getGenerateDateTime(id));
    }

    /**
     * 根据Snowflake的ID，获取数据中心id
     *
     * @param id snowflake算法生成的id
     * @return 所属数据中心
     */
    public static long getDataCenterId(long id){
        return IdUtil.getSnowflake().getDataCenterId(id);
    }

    /**
     * 根据Snowflake的ID，获取机器id
     *
     * @param id snowflake算法生成的id
     * @return 所属机器的id
     */
    public static long getWorkerId(long id){
        return IdUtil.getSnowflake().getWorkerId(id);
    }
}
