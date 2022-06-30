package com.wlcb.jpower.module.common.utils;

import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.DES;

/**
 * DES加密
 *
 * @author mr.g
 **/
public class DesUtil {

    /**
     * 默认的密钥
     **/
    private static final String KEY = "JPOWER_DES_DEFAULT_KEY";

    /**
     * 对数据进行DES加密
     *
     * @author mr.g
     * @param data 要加密的数据
     * @return java.lang.String
     **/
    public static String encrypt(final String data) {
        return encrypt(data,KEY);
    }

    /**
     * 对数据进行DES解密
     *
     * @author mr.g
     * @param data 要解密的数据
     * @return java.lang.String
     **/
    public static String decrypt(final String data) {
        return decrypt(data,KEY);
    }

    /**
     * 对数据进行DES加密
     *
     * @author mr.g
     * @param data 要加密的数据
     * @param key 密钥
     * @return java.lang.String
     **/
    public static String encrypt(final String data,final String key) {
        return encrypt(Mode.ECB,Padding.PKCS5Padding,data,key);
    }

    /**
     * 对数据进行DES解密
     *
     * @author mr.g
     * @param data 要解密的数据
     * @param key 密钥
     * @return java.lang.String
     **/
    public static String decrypt(final String data, final String key) {
        return decrypt(Mode.ECB,Padding.PKCS5Padding,data,key);
    }

    /**
     * 对数据进行DES加密
     *
     * @author mr.g
     * @param mode 加密模式
     * @param data 要加密的数据
     * @param key 密钥
     * @return java.lang.String
     **/
    public static String encrypt(final Mode mode,final String data,final String key) {
        return encrypt(mode,Padding.PKCS5Padding,data,key);
    }

    /**
     * 对数据进行DES解密
     *
     * @author mr.g
     * @param mode 加密模式
     * @param data 要解密的数据
     * @param key 密钥
     * @return java.lang.String
     **/
    public static String decrypt(final Mode mode,final String data, final String key) {
        return decrypt(mode,Padding.PKCS5Padding,data,key);
    }

    /**
     * 对数据进行DES加密
     *
     * @author mr.g
     * @param padding 补码方式
     * @param data 要加密的数据
     * @param key 密钥
     * @return java.lang.String
     **/
    public static String encrypt(final Padding padding,final String data,final String key) {
        return encrypt(Mode.ECB,padding,data,key);
    }

    /**
     * 对数据进行DES解密
     *
     * @author mr.g
     * @param padding 补码方式
     * @param data 要解密的数据
     * @param key 密钥
     * @return java.lang.String
     **/
    public static String decrypt(final Padding padding,final String data, final String key) {
        return decrypt(Mode.ECB,padding,data,key);
    }

    /**
     * 对数据进行DES加密
     *
     * @author mr.g
     * @param mode 加密模式
     * @param padding 补码方式
     * @param data 要加密的数据
     * @param key 密钥
     * @return java.lang.String
     **/
    public static String encrypt(final Mode mode, final Padding padding,final String data,final String key) {
        return new DES(mode, padding, StringUtil.utf8Bytes(key)).encryptHex(data);
    }

    /**
     * 对数据进行DES解密
     *
     * @author mr.g
     * @param mode 加密模式
     * @param padding 补码方式
     * @param data 要解密的数据
     * @param key 密钥
     * @return java.lang.String
     **/
    public static String decrypt(final Mode mode, final Padding padding,final String data, final String key) {
        return StringUtil.trim(new DES(mode, padding, StringUtil.utf8Bytes(key)).decryptStr(data));
    }

}