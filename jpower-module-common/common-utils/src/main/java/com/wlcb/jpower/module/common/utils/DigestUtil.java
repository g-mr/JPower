package com.wlcb.jpower.module.common.utils;

import cn.hutool.crypto.digest.BCrypt;

/**
 * 加密相关工具类
 */
public class DigestUtil extends cn.hutool.crypto.digest.DigestUtil {

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

    public static void main(String[] args) {

        System.out.println(BCrypt.gensalt(15));

        String pwd = BCrypt.hashpw("P@sswordHarbor",BCrypt.gensalt(5));
        System.out.println(pwd);
        System.out.println(BCrypt.checkpw("P@sswordHarbor","$2a$05$FRP.xJhNDqfa4x2AP6luGO8xtszfdWRsA04eJb0QwLD1dH6u5WiDq"));
    }
}