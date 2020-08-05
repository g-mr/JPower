package com.wlcb.jpower.module.common.utils;

/**
 * MD5
 * MD5加密算法
 */
public class MD5 {
    /**
     * @param str
     * @return
     * @Description: 32位小写MD5
     */
    public static String parseStrToMd5L32(String str){
        return Fc.md5Hex(str);
    }

    /**
     * @param str
     * @return
     * @Description: 32位大写MD5
     */
    public static String parseStrToMd5U32(String str){
        String reStr = parseStrToMd5L32(str);
        if (reStr != null){
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

        String keyBeforeMd5 = "测试";
//        String keyBeforeMd5 = "123456";
        System.out.println(parseStrToMd5L32(keyBeforeMd5));
        System.out.println(Fc.md5Hex(keyBeforeMd5));
	}
    
}
