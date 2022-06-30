package com.wlcb.jpower.module.common.utils;

import cn.hutool.crypto.digest.BCrypt;

/**
 * 摘要加密
 *
 * @author mr.g
 **/
public class DigestUtil extends cn.hutool.crypto.digest.DigestUtil {

    /**
     * 是否使用新的方式加密<br/>
     * e.g : 从4.1.4版本之后可以使用新的方式；老系统升级到4.1.4或更多版本请设置为false（为了兼容数据库中已存在的密码），否者会导致所有用户密码错误
     */
    private final static Boolean IS_NEW_PWD = Boolean.TRUE;

    /**
     * 密码加密
     *
     * @author mr.g
     * @param pwd 密码原文
     * @return java.lang.String
     **/
    public static String pwdEncrypt(String pwd) {
        if (IS_NEW_PWD){
            return bcrypt(pwd);
        } else {
            return encrypt(pwd);
        }
    }

    /**
     * 检查密码是否正确
     *
     * @author mr.g
     * @param pwd 密码原文
     * @param ciphertext 密码密文
     * @return java.lang.String
     **/
    public static boolean checkPwd(String pwd,String ciphertext) {
        if (IS_NEW_PWD){
            return BCrypt.checkpw(pwd,ciphertext);
        } else {
            return Fc.equalsValue(encrypt(pwd),ciphertext);
        }
    }

    /**
     * 自定义加密 先MD5再SHA1<br/>
     * 原密码加密方式
     *
     * @param data 数据
     * @return String
     */
    public static String encrypt(String data) {
        return sha1Hex(md5Hex(data));
    }

    /**
     * 密码加密<br/>
     * 4.1.3之后支持的密码加密方式，可通过系统参数设置使用原加密方式
     *
     * @author mr.g
     * @param passwordMd5 MD5后的密码
     * @return java.lang.String
     **/
    public static String bcrypt(String passwordMd5) {
        return BCrypt.hashpw(passwordMd5,BCrypt.gensalt(5));
    }
}