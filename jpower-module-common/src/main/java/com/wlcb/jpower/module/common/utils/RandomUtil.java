package com.wlcb.jpower.module.common.utils;

import java.util.Random;

/**
 * @ClassName RandomUtil
 * @Description TODO 随机数工具类
 * @Author 郭丁志
 * @Date 2020-03-28 22:58
 * @Version 1.0
 */
public class RandomUtil {

    /**
     * @Author 郭丁志
     * @Description //TODO 6位随机数
     * @Date 14:51 2020-07-31
     * @Param []
     * @return java.lang.String
     **/
    public static String random6Num(){
        return String.valueOf(new Random().nextInt(899999) + 100000);
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 6位随机字符串
     * @Date 15:00 2020-07-31
     * @Param [length]
     * @return java.lang.String
     **/
    public static String randomString(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

}
