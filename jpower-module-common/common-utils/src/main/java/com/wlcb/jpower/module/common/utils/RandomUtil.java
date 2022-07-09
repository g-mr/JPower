package com.wlcb.jpower.module.common.utils;

/**
 * 随机数工具类
 *
 * @author mr.g
 **/
public class RandomUtil extends cn.hutool.core.util.RandomUtil {

    /**
     * 随机数的样本（大小写字母）
     **/
    private static final String S_STR = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    /**
     * 随机数的样本（大小写字母、数字）
     **/
    private static final String S_ALL = S_STR+BASE_NUMBER;

    /**
     * 6位数字随机数
     *
     * @author mr.g
     * @return 随机数
     **/
    public static String random6Num(){
        return randomNumbers(6);
    }

    /**
     * 随机数(大小写字母、数字)
     *
     * @author mr.g
     * @param length 随机数长度
     * @return 随机数
     **/
    public static String random(int length){
        return randomString(S_ALL, length);
    }

    /**
     * 随机字符串（大小写字母）
     *
     * @author mr.g
     * @param length 随机字符串长度
     * @return 随机字符串
     **/
    public static String randomString(int length) {
        return randomString(S_STR, length);
    }

}
