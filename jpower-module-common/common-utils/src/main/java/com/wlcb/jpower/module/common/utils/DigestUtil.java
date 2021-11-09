package com.wlcb.jpower.module.common.utils;

import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.StrUtil;
import com.wlcb.jpower.module.common.enums.DigestAlgorithm;
import com.wlcb.jpower.module.common.utils.constants.CharsetKit;
import org.springframework.util.DigestUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * 加密相关工具类
 */
public class DigestUtil extends DigestUtils {

    public static String sha1(String srcStr) {
        return HexUtil.encodeHexStr(digest(DigestAlgorithm.SHA1).digest(StrUtil.bytes(srcStr, CharsetKit.CHARSET_UTF_8)));
    }

    public static String sha256(String srcStr) {
        return HexUtil.encodeHexStr(digest(DigestAlgorithm.SHA256).digest(StrUtil.bytes(srcStr, CharsetKit.CHARSET_UTF_8)));
    }

    public static String sha384(String srcStr) {
        return HexUtil.encodeHexStr(digest(DigestAlgorithm.SHA384).digest(StrUtil.bytes(srcStr, CharsetKit.CHARSET_UTF_8)));
    }

    public static String sha512(String srcStr) {
        return HexUtil.encodeHexStr(digest(DigestAlgorithm.SHA512).digest(StrUtil.bytes(srcStr, CharsetKit.CHARSET_UTF_8)));
    }

    public static MessageDigest digest(DigestAlgorithm algorithm) {
        try {
            return MessageDigest.getInstance(algorithm.getValue());
        } catch (NoSuchAlgorithmException e) {
            throw ExceptionsUtil.unchecked(e);
        }
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
        System.out.println(encrypt(MD5.parseStrToMd5L32("123456").toUpperCase()));
    }

}