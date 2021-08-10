package com.wlcb.jpower.module.common.utils;

import cn.hutool.core.util.HexUtil;
import com.wlcb.jpower.module.common.utils.constants.StringPool;
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

    public static byte[] encryptMode(byte[] src) {
        try {
            byte[] bytesSecret = DigestUtil.hex2Bytes(SECRET);
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

    public static String encrypt(String data) {
        if (Fc.isNull(data)){
            return StringPool.EMPTY;
        }
        return HexUtil.encodeHexStr(encryptMode(data.getBytes(StandardCharsets.UTF_8)));
    }

    // keybyte为加密密钥，长度为24字节
    // src为加密后的缓冲区
    public static byte[] decryptMode(byte[] src) {
        try {
            byte[] bytesSecret = DigestUtil.hex2Bytes(SECRET);
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

    public static String decrypt(String data) {
        return new String(decryptMode(DigestUtil.hex2Bytes(data)), StandardCharsets.UTF_8);
    }


    public static void main(String[] args) {
        String txt = -1+";"+null;
//        byte[] bytes = encryptMode(txt.getBytes(StandardCharsets.UTF_8));
//        System.out.println(DigestUtil.toHex(bytes));
//
//        byte[] bytes1 = decryptMode(bytes);
//        String string = new String(bytes1, StandardCharsets.UTF_8);
//        System.out.println(string);

        String data = encrypt(txt);
        System.out.println(data);


        data = decrypt(data);
        System.out.println(data);
        System.out.println(Fc.equals(data.split(";")[1],"null"));
    }
}