package com.wlcb.jpower.module.common.utils;

import com.wlcb.jpower.module.common.utils.constants.CharsetKit;
import org.springframework.lang.Nullable;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @ClassName DigestUtil
 * @Description TODO 加密相关工具类
 * @Author 郭丁志
 * @Date 2020-07-23 15:44
 * @Version 1.0
 */
public class DigestUtil extends org.springframework.util.DigestUtils {

    /**
     * Calculates the MD5 digest and returns the value as a 32 character hex string.
     *
     * @param data Data to digest
     * @return MD5 digest as a hex string
     */
    public static String md5Hex(final String data) {
        return DigestUtil.md5DigestAsHex(data.getBytes(CharsetKit.CHARSET_UTF_8));
    }

    /**
     * Return a hexadecimal string representation of the MD5 digest of the given bytes.
     *
     * @param bytes the bytes to calculate the digest over
     * @return a hexadecimal digest string
     */
    public static String md5Hex(final byte[] bytes) {
        return DigestUtil.md5DigestAsHex(bytes);
    }

    private static final char[] HEX_DIGITS = "0123456789abcdef".toCharArray();

    public static String sha1(String srcStr) {
        return hash("SHA-1", srcStr);
    }

    public static String sha256(String srcStr) {
        return hash("SHA-256", srcStr);
    }

    public static String sha384(String srcStr) {
        return hash("SHA-384", srcStr);
    }

    public static String sha512(String srcStr) {
        return hash("SHA-512", srcStr);
    }

    public static String hash(String algorithm, String srcStr) {
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            byte[] bytes = md.digest(srcStr.getBytes(CharsetKit.CHARSET_UTF_8));
            return toHex(bytes);
        } catch (NoSuchAlgorithmException e) {
            throw ExceptionsUtil.unchecked(e);
        }
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 把二进制转换成十六进制
     * @Date 00:06 2020-07-24
     * @Param [bytes]
     * @return java.lang.String
     **/
    public static String toHex(byte[] bytes) {
        StringBuilder ret = new StringBuilder(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {
            ret.append(HEX_DIGITS[(bytes[i] >> 4) & 0x0f]);
            ret.append(HEX_DIGITS[bytes[i] & 0x0f]);
        }
        return ret.toString();
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 十六进制转二进制
     * @Date 01:29 2020-07-24
     * @Param [hex]
     * @return byte[]
     **/
    public static byte[] hex2Bytes(String hex) {
        if ((hex.length() % 2) != 0) {
            String errMsg = "hex.length()=" + hex.length() + ", not an even number";
            throw new IllegalArgumentException(errMsg);
        }

        final byte[] result = new byte[hex.length() / 2];
        final char[] enc = hex.toCharArray();
        StringBuilder sb = new StringBuilder(2);
        for (int i = 0; i < enc.length; i += 2) {
            sb.delete(0, sb.length());
            sb.append(enc[i]).append(enc[i + 1]);
            result[i / 2] = (byte) Integer.parseInt(sb.toString(), 16);
        }
        return result;
    }

    public static boolean slowEquals(@Nullable String a, @Nullable String b) {
        if (a == null || b == null) {
            return false;
        }
        return slowEquals(a.getBytes(CharsetKit.CHARSET_UTF_8), b.getBytes(CharsetKit.CHARSET_UTF_8));
    }

    public static boolean slowEquals(@Nullable byte[] a, @Nullable byte[] b) {
        if (a == null || b == null) {
            return false;
        }
        if (a.length != b.length) {
            return false;
        }
        int diff = a.length ^ b.length;
        for (int i = 0; i < a.length && i < b.length; i++) {
            diff |= a[i] ^ b[i];
        }
        return diff == 0;
    }

    /**
     * 自定义加密 先MD5再SHA1
     *
     * @param data 数据
     * @return String
     */
    public static String encrypt(String data) {
        return sha1(md5Hex(data));
    }


    public static void main(String[] args) {
        System.out.println(encrypt("123456"));
    }

}