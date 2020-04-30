package com.wlcb.jpower.module.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

/**
 * @Author 郭丁志
 * @Description //TODO 3DES
 * @Date 12:05 2020-03-26
 * @Param 
 * @return 
 **/
public class ThreeDESUtil {

    private static final Logger logger = LoggerFactory.getLogger(ThreeDESUtil.class);

    private static final String Algorithm = "DESede"; // 定义 加密算法,可用

    // 下发给省分的3DES密钥样例 (24字节转为48位hex串)
    private static final String SECRET = "0E7C141F55AC24A709D4DBDF609830262CC2B89A719F1411";
    //  87F87B2496D7D4B29F89A58ADDF4D593BB5F730821AD8CE8 联调
    //  0E7C141F55AC24A709D4DBDF609830262CC2B89A719F1411 生产
    public static byte[] encryptMode(byte[] src) {
        try {
            byte[] bytesSecret = hex2Bytes(SECRET);
            // 生成密钥
            SecretKey deskey = new SecretKeySpec(bytesSecret, Algorithm);
            // 加密
            Cipher c1 = Cipher.getInstance(Algorithm);
            c1.init(Cipher.ENCRYPT_MODE, deskey);
            return c1.doFinal(src);
        } catch (Exception e1) {
            logger.error(e1.getMessage(), e1);
        }
        return null;
    }

    // keybyte为加密密钥，长度为24字节
    // src为加密后的缓冲区
    public static byte[] decryptMode(byte[] src) {
        try {
            byte[] bytesSecret = hex2Bytes(SECRET);
            // 生成密钥
            SecretKey deskey = new SecretKeySpec(bytesSecret, Algorithm);
            // 解密
            Cipher c1 = Cipher.getInstance(Algorithm);
            c1.init(Cipher.DECRYPT_MODE, deskey);
            return c1.doFinal(src);
        } catch (Exception e1) {
            logger.error(e1.getMessage(), e1);
        }
        return null;
    }

    //二进制转十六进制
    public static String bytes2Hex(byte[] bytes, boolean upperCase) {
        if (bytes == null || bytes.length <= 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return upperCase ? sb.toString().toUpperCase() : sb.toString();
    }

    //十六进制转二进制
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


    public static void main(String[] args) {
        String txt = "成都市发展公司";
        byte[] bytes = encryptMode(txt.getBytes(StandardCharsets.UTF_8));
        System.out.println(bytes2Hex(bytes,false));

        byte[] bytes1 = decryptMode(bytes);
        String string = new String(bytes1, StandardCharsets.UTF_8);
        System.out.println(string);

    }
}