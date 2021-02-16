package com.wlcb.jpower.module.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName StrUtil
 * @Description TODO 字符串正则工具类
 * @Author 郭丁志
 * @Date 2020-03-03 20:36
 * @Version 1.0
 */
public class StrUtil {

    private static Logger logger = LoggerFactory.getLogger(StrUtil.class);

    /**第一代身份证正则表达式(15位)*/
    private static final String IS_ID_CARD1 = "^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$";
    /**第二代身份证正则表达式(18位)*/
    private static final String IS_ID_CARD2 ="^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])((\\d{4})|\\d{3}[A-Z])$";
    /**手机号正则*/
    private static final String PHONE_REGEX = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";
    /**邮箱正则*/
    private static final String EMAIL_REGEX = "^([a-z0-9A-Z]+[-|\\.]?[_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";


    /**
     * @Author 郭丁志
     * @Description //TODO 正则校验身份证是否符合第一代第二代标准
     * @Date 20:39 2020-03-03
     * @Param [cardcode]
     * @return boolean
     **/
    public static boolean cardCodeVerifySimple(String cardcode) {
        //验证身份证
        if (Fc.isNoneBlank(cardcode) && (cardcode.matches(IS_ID_CARD1) || cardCodeVerify(cardcode))) {
            return true;
        }
        return false;
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 验证第二代身份证是否符合国家规范
     * @Date 20:40 2020-03-03
     * @Param [cardcode]
     * @return boolean
     **/
    public static boolean cardCodeVerify(String cardcode) {
        int i = 0;
        String r = "error";
        String lastnumber = "";

        try {
            i += Integer.parseInt(cardcode.substring(0, 1)) * 7;
            i += Integer.parseInt(cardcode.substring(1, 2)) * 9;
            i += Integer.parseInt(cardcode.substring(2, 3)) * 10;
            i += Integer.parseInt(cardcode.substring(3, 4)) * 5;
            i += Integer.parseInt(cardcode.substring(4, 5)) * 8;
            i += Integer.parseInt(cardcode.substring(5, 6)) * 4;
            i += Integer.parseInt(cardcode.substring(6, 7)) * 2;
            i += Integer.parseInt(cardcode.substring(7, 8)) * 1;
            i += Integer.parseInt(cardcode.substring(8, 9)) * 6;
            i += Integer.parseInt(cardcode.substring(9, 10)) * 3;
            i += Integer.parseInt(cardcode.substring(10,11)) * 7;
            i += Integer.parseInt(cardcode.substring(11,12)) * 9;
            i += Integer.parseInt(cardcode.substring(12,13)) * 10;
            i += Integer.parseInt(cardcode.substring(13,14)) * 5;
            i += Integer.parseInt(cardcode.substring(14,15)) * 8;
            i += Integer.parseInt(cardcode.substring(15,16)) * 4;
            i += Integer.parseInt(cardcode.substring(16,17)) * 2;
            i = i % 11;
            lastnumber =cardcode.substring(17,18);
            if (i == 0) {
                r = "1";
            }
            if (i == 1) {
                r = "0";
            }
            if (i == 2) {
                r = "x";
            }
            if (i == 3) {
                r = "9";
            }
            if (i == 4) {
                r = "8";
            }
            if (i == 5) {
                r = "7";
            }
            if (i == 6) {
                r = "6";
            }
            if (i == 7) {
                r = "5";
            }
            if (i == 8) {
                r = "4";
            }
            if (i == 9) {
                r = "3";
            }
            if (i == 10) {
                r = "2";
            }
            if (r.equals(lastnumber.toLowerCase())) {
                return true;
            }
        }catch (Exception e){
            logger.error("身份证识别错误：{},error={}",cardcode,e.getMessage());
        }
        return false;
    }

    public static Integer sexCard(String cardCode) {
        if (StringUtils.isBlank(cardCode) || cardCode.length()< 15){
            return 0;
        }

        if (cardCode.length() == 18){
            return sexCard18(cardCode);
        }else {
            return sexCard15(cardCode);
        }
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 判断18位身份证性别
     * @Date 16:34 2020-05-21
     * @Param [cardCode]
     * @return java.lang.Integer
     **/
    public static Integer sexCard18(String cardCode) {
        Integer sex;
        // 判断性别
        if (Integer.parseInt(cardCode.substring(16).substring(0, 1)) % 2 == 0) {
            sex = 2;
        } else {
            sex = 1;
        }
        return sex;
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 判断15位身份证性别
     * @Date 16:33 2020-05-21
     * @Param [card]
     * @return java.lang.Integer
     **/
    public static Integer sexCard15(String card) {
        Integer sex;
        if (Integer.parseInt(card.substring(14, 15)) % 2 == 0) {
            sex = 2;
        } else {
            sex = 1;
        }
        return sex;
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 判断是否是手机号
     * @Date 20:40 2020-03-03
     * @Param [phone]
     * @return boolean
     **/
    public static boolean isPhone(String phone) {
        if (phone.length() != 11) {
            return false;
        } else {
            return phone.matches(PHONE_REGEX);
        }
    }

    public static boolean isEmail(String email) {
        if (email.split("@").length != 2) {
            return false;
        } else {
            return email.matches(EMAIL_REGEX);
        }
    }

}
