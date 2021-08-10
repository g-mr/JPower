package com.wlcb.jpower.module.common.utils;

import cn.hutool.core.util.HexUtil;
import com.wlcb.jpower.module.common.utils.constants.CharsetKit;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * 加密相关工具类
 */
public class DigestUtil extends org.springframework.util.DigestUtils {

    public static String sha1(String srcStr) {
        try {
            return hash(MessageDigest.getInstance("SHA-1"), srcStr);
        } catch (NoSuchAlgorithmException e) {
            throw ExceptionsUtil.unchecked(e);
        }
    }

    public static String sha256(String srcStr) {
        try {
            return hash(MessageDigest.getInstance("SHA-256"), srcStr);
        } catch (NoSuchAlgorithmException e) {
            throw ExceptionsUtil.unchecked(e);
        }
    }

    public static String sha384(String srcStr) {
        try {
            return hash(MessageDigest.getInstance("SHA-384"), srcStr);
        } catch (NoSuchAlgorithmException e) {
            throw ExceptionsUtil.unchecked(e);
        }
    }

    public static String sha512(String srcStr) {
        try {
            return hash(MessageDigest.getInstance("SHA-512"), srcStr);
        } catch (NoSuchAlgorithmException e) {
            throw ExceptionsUtil.unchecked(e);
        }
    }

    public static String hash(MessageDigest digest, String srcStr) {
        byte[] bytes = digest.digest(srcStr.getBytes(CharsetKit.CHARSET_UTF_8));
        return HexUtil.encodeHexStr(bytes);
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 十六进制转二进制
     * @Date 01:29 2020-07-24
     * @Param [hex]
     * @return byte[]
     **/
    public static byte[] hex2Bytes(String hex) {
        return HexUtil.decodeHex(hex);
    }

    /**
     * 自定义加密 先MD5再SHA1
     *
     * @param data 数据
     * @return String
     */
    public static String encrypt(String data) {
        return sha1(MD5.parseStrToMd5L32(data));
    }

    public static void main(String[] args) {
        System.out.println(MD5.parseStrToMd5L32("123456").toUpperCase());
        System.out.println(encrypt(MD5.parseStrToMd5L32("123456").toUpperCase()));
    }

}