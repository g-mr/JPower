package com.wlcb.jpower.core.login.utils;

import com.wlcb.jpower.module.base.properties.SysProperties;
import com.wlcb.jpower.module.common.utils.param.ParamConfig;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import java.io.*;
import java.net.URLDecoder;
import java.security.*;
import java.security.interfaces.RSAPublicKey;


public class RSAUtil {

    private static final Logger logger = LoggerFactory.getLogger(RSAUtil.class);

    //加密文件路径
//    public static String filePath = SysProperties.getInstance().getProperties("rsa_file_path");
    public static String filePath = "rsa_file_path";

     //密钥对
    public static  KeyPair keyPair;

    //获取私钥
    public static  PrivateKey getPrivateKey(){

        if(null == keyPair){
            keyPair = initKeyPair();
        }
        return keyPair.getPrivate();
    }

    //初始化密钥对
    public static KeyPair initKeyPair(){
        try {
            FileInputStream fis = new FileInputStream(ParamConfig.getString(filePath));
            ObjectInputStream oos = new ObjectInputStream(fis);
            keyPair = (KeyPair) oos.readObject();
            oos.close();
            fis.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return keyPair;
    }

    /**
     * * 生成密钥对 *
     */
//    public static KeyPair generateKeyPair() throws Exception {
//        try {
//            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA",
//                    new BouncyCastleProvider());
//            final int KEY_SIZE = 1024;
//            keyPairGen.initialize(KEY_SIZE, new SecureRandom());
//            KeyPair keyPair = keyPairGen.generateKeyPair();
//
//            System.out.println("private----" + keyPair.getPrivate());
//            System.out.println("public----" + keyPair.getPublic());
//
//            saveKeyPair(keyPair);
//            return keyPair;
//        } catch (Exception e) {
//            throw new Exception(e.getMessage());
//        }
//    }

    public static KeyPair getKeyPair(String url) throws Exception {
        FileInputStream fis = new FileInputStream(url);
        ObjectInputStream oos = new ObjectInputStream(fis);
        KeyPair kp = (KeyPair) oos.readObject();
        oos.close();
        fis.close();
        return kp;
    }
//    public static void main(String[] args) throws Exception {
//      getKeyPair("/RSAKey.txt");
//    }

//    public static void saveKeyPair(KeyPair kp) throws Exception {
//
//        FileOutputStream fos = new FileOutputStream(RSAKeyStore);
//        ObjectOutputStream oos = new ObjectOutputStream(fos);
//        // 生成密钥
//        oos.writeObject(kp);
//        oos.close();
//        fos.close();
//    }

    /**
     * * 生成公钥 *
     */
   /* public static RSAPublicKey generateRSAPublicKey(byte[] modulus,
                                                    byte[] publicExponent) throws Exception {
        KeyFactory keyFac = null;
        try {
            keyFac = KeyFactory.getInstance("RSA",
                    new org.bouncycastle.jce.provider.BouncyCastleProvider());
        } catch (NoSuchAlgorithmException ex) {
            throw new Exception(ex.getMessage());
        }

        RSAPublicKeySpec pubKeySpec = new RSAPublicKeySpec(new BigInteger(
                modulus), new BigInteger(publicExponent));
        try {
            return (RSAPublicKey) keyFac.generatePublic(pubKeySpec);
        } catch (InvalidKeySpecException ex) {
            throw new Exception(ex.getMessage());
        }
    }*/

    /**
     * * 生成私钥 *
     */
   /* public static RSAPrivateKey generateRSAPrivateKey(byte[] modulus,
                                                      byte[] privateExponent) throws Exception {
        KeyFactory keyFac = null;
        try {
            keyFac = KeyFactory.getInstance("RSA",
                    new org.bouncycastle.jce.provider.BouncyCastleProvider());
        } catch (NoSuchAlgorithmException ex) {
            throw new Exception(ex.getMessage());
        }

        RSAPrivateKeySpec priKeySpec = new RSAPrivateKeySpec(new BigInteger(
                modulus), new BigInteger(privateExponent));
        try {
            return (RSAPrivateKey) keyFac.generatePrivate(priKeySpec);
        } catch (InvalidKeySpecException ex) {
            throw new Exception(ex.getMessage());
        }
    }*/

    /**
     * * 加密
     ***/

    public static byte[] encrypt(PublicKey pk, byte[] data) throws Exception {
        try {
            Cipher cipher = Cipher.getInstance("RSA",
                    new org.bouncycastle.jce.provider.BouncyCastleProvider());
            cipher.init(Cipher.ENCRYPT_MODE, pk);
            int blockSize = cipher.getBlockSize();// 获得加密块大小，如：加密前数据为128个byte，而key_size=1024
            // 加密块大小为127
            // byte,加密后为128个byte;因此共有2个加密块，第一个127
            // byte第二个为1个byte
            int outputSize = cipher.getOutputSize(data.length);// 获得加密块加密后块大小
            int leavedSize = data.length % blockSize;
            int blocksSize = leavedSize != 0 ? data.length / blockSize + 1
                    : data.length / blockSize;
            byte[] raw = new byte[outputSize * blocksSize];
            int i = 0;
            while (data.length - i * blockSize > 0) {
                if (data.length - i * blockSize > blockSize) {
                    cipher.doFinal(data, i * blockSize, blockSize, raw, i
                            * outputSize);
                } else {
                    cipher.doFinal(data, i * blockSize, data.length - i
                            * blockSize, raw, i * outputSize);
                }
                i++;
            }
            return raw;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    /**
     * * 解密
     ***/
    public static byte[] decrypt(PrivateKey pk, byte[] raw) throws Exception {
        try {
            Cipher cipher = Cipher.getInstance("RSA",
                    new org.bouncycastle.jce.provider.BouncyCastleProvider());
            cipher.init(Cipher.DECRYPT_MODE, pk);
            int blockSize = cipher.getBlockSize();
            ByteArrayOutputStream bout = new ByteArrayOutputStream(64);
            int j = 0;

            while (raw.length - j * blockSize > 0) {
                bout.write(cipher.doFinal(raw));
                j++;
            }
            return bout.toByteArray();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    /**
     * 
     * @param hexString
     * @return
     */
    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || "".equals(hexString)) {
            return null;
        }
        hexString = hexString.toLowerCase();
        final byte[] byteArray = new byte[hexString.length() / 2];
        int k = 0;
        for (int i=0;i<byteArray.length;i++) {
            //因为是16进制，最多只会占用4位，转换成字节需要两个16进制的字符，高位在先  
            //将hex 转换成byte   "&" 操作为了防止负数的自动扩展  
            // hex转换成byte 其实只占用了4位，然后把高位进行右移四位  
            // 然后“|”操作  低四位 就能得到 两个 16进制数转换成一个byte.  
            //  
            byte high = (byte) (Character.digit(hexString.charAt(k), 16) & 0xff);
            byte low = (byte) (Character.digit(hexString.charAt(k + 1), 16) & 0xff);
            byteArray[i] = (byte) (high << 4 | low);
            k += 2;
        }
        return byteArray;
    }

//    /**
//     * Convert char to byte
//     *
//     * @param c char
//     * @return byte
//     */
//    private static byte charToByte(char c) {
//        return (byte) "0123456789ABCDEF".indexOf(c);
//    }

    /**
     * * *
     *
     * @param args *
     */
    public static void main(String[] args) throws Exception {
//        RSAPublicKey rsap = (RSAPublicKey) RSAUtil.generateKeyPair().getPublic();
        String test = "92D7DDD2A010C59511DC2905B7E14F64";

        String keyUrl = "/Users/mr.gmac/RSAKey.txt";

        System.out.println(getKeyPair(keyUrl).getPublic());
        System.out.println("-------------------");

        byte[] en_test = encrypt(getKeyPair(keyUrl).getPublic(), test.getBytes());
        byte[] de_test = decrypt(getKeyPair(keyUrl).getPrivate(), en_test);
        System.out.println(new String(en_test));
        System.out.println(new String(de_test));

        byte[] aa = hexStringToBytes("44d142f966d88cc34e49882c848d1c46b16003b4fde1830779a9beb4d1fca6f500e8cb199d10ae5211a1a311287ae4f5850401dcdafe4f00d46edafbe36530298c1539050bfbd3cdcd0881cf6d8eb8231bbe6637338bac5f9bb0c6edfcdcc20aac2902a9adda3e287b2537ad752982eb33656d192e6d408b08d8f3c521c960e4");
        System.out.println(aa.toString());
        byte[] bb = decrypt(getKeyPair(keyUrl).getPrivate(), aa);
        System.out.println(new StringBuilder(new StringBuilder(new String(bb, "UTF-8")).reverse().toString()));
    }

}
