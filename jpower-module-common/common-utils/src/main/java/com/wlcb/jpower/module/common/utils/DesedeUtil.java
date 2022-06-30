package com.wlcb.jpower.module.common.utils;

import cn.hutool.core.util.HexUtil;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.DESede;

/**
 * 3DES加密
 *
 * @author mr.g
 **/
public class DesedeUtil {

    /**
     * 默认的密钥
     **/
    private static final String KEY = "0E7C141F55AC24A709D4DBDF609830262CC2B89A719F1411";

    /**
     * 对数据进行3DES加密
     *
     * @author mr.g
     * @param data 要加密的数据
     * @return java.lang.String
     **/
    public static String encrypt(final String data) {
        return SecureUtil.desede(HexUtil.decodeHex(KEY)).encryptHex(data);
    }

    /**
     * 对数据进行3DES解密
     *
     * @author mr.g
     * @param data 要解密的数据
     * @return java.lang.String
     **/
    public static String decrypt(final String data) {
        return SecureUtil.desede(HexUtil.decodeHex(KEY)).decryptStr(data);
    }

    /**
     * 对数据进行3DES加密
     *
     * @author mr.g
     * @param data 要加密的数据
     * @param key 密钥
     * @return java.lang.String
     **/
    public static String encrypt(final String data,final String key) {
        return SecureUtil.desede(HexUtil.decodeHex(key)).encryptHex(data);
    }

    /**
     * 对数据进行3DES解密
     *
     * @author mr.g
     * @param data 要解密的数据
     * @param key 密钥
     * @return java.lang.String
     **/
    public static String decrypt(final String data, final String key) {
        return SecureUtil.desede(HexUtil.decodeHex(key)).decryptStr(data);
    }

    /**
     * 对数据进行3DES加密
     *
     * @author mr.g
     * @param mode 加密模式
     * @param padding 补码方式
     * @param data 要加密的数据
     * @param key 密钥
     * @return java.lang.String
     **/
    public static String encrypt(final Mode mode, final Padding padding,final String data,final String key) {
        return new DESede(mode,padding,HexUtil.decodeHex(key)).encryptHex(data);
    }

    /**
     * 对数据进行3DES解密
     *
     * @author mr.g
     * @param mode 加密模式
     * @param padding 补码方式
     * @param data 要解密的数据
     * @param key 密钥
     * @return java.lang.String
     **/
    public static String decrypt(final Mode mode, final Padding padding,final String data, final String key) {
        return new DESede(mode,padding,HexUtil.decodeHex(key)).decryptStr(data);
    }
}