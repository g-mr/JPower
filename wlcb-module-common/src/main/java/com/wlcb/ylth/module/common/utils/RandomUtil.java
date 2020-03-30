package com.wlcb.ylth.module.common.utils;

import java.util.Random;

/**
 * @ClassName RandomUtil
 * @Description TODO 随机数工具类
 * @Author 郭丁志
 * @Date 2020-03-28 22:58
 * @Version 1.0
 */
public class RandomUtil {

    public static String random6Num(){
        return String.valueOf(new Random().nextInt(899999) + 100000);
    }

}
