package com.wlcb.jpower.module.common.utils;

import cn.hutool.core.date.DateTime;

/**
 * 时间工具类
 *
 * @author mr.g
 **/
public class DateUtil extends cn.hutool.core.date.DateUtil {

    public static final String[] PARSE_PATTERNS = {
            "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
            "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};


    /**
     * 获取当前时间<br/>
     * HH:mm:ss
     *
     * @author mr.g
     * @return java.lang.String
     **/
    public static String time() {
        return formatTime(new DateTime());
    }

}
