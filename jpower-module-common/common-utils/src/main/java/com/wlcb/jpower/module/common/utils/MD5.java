package com.wlcb.jpower.module.common.utils;

import com.wlcb.jpower.module.common.utils.constants.CharsetKit;

/**
 * MD5
 * MD5加密算法
 */
public class MD5 {

    /**
     * 返回给定字节的MD5摘要的十六进制字符串表示形式。
     * @param bytes 计算的字节
     * @return 十六进制字符串
     */
    public static String md5Hex(final byte[] bytes) {
        return DigestUtil.md5DigestAsHex(bytes);
    }

    /**
     * @param str
     * @return
     * @Description: 32位小写MD5
     */
    public static String parseStrToMd5L32(final String str){
        return DigestUtil.md5DigestAsHex(str.getBytes(CharsetKit.CHARSET_UTF_8));
    }

    /**
     * @param str
     * @return
     * @Description: 32位大写MD5
     */
    public static String parseStrToMd5U32(String str){
        String reStr = parseStrToMd5L32(str);
        if (Fc.notNull(reStr)){
            reStr = reStr.toUpperCase();
        }
        return reStr;
    }

    public static void main(String[] args) {

//    	LinkedHashMap<String, String> map = new LinkedHashMap<>();
//    	map.put("CustomerNumber", "N0002017010610000307");
//    	map.put("systemType", "CSMP");
//    	map.put("customerName", "测试验证5");
//    	System.out.println(sign(map,"icloudsystem").toLowerCase());

        String keyBeforeMd5 = "123456";
//        String keyBeforeMd5 = "123456";
        System.out.println(parseStrToMd5U32(keyBeforeMd5));
        System.out.println(Fc.md5(keyBeforeMd5));
	}
    
}
