package com.wlcb.jpower.module.common.utils;

import java.security.MessageDigest;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

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
        String reStr = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(str.getBytes("UTF-8"));
            StringBuffer stringBuffer = new StringBuffer();
            for (byte b : bytes){
                int bt = b&0xff;
                if (bt < 16){
                    stringBuffer.append(0);
                }
                stringBuffer.append(Integer.toHexString(bt));
            }
            reStr = stringBuffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reStr;
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
//
//    /**
//     * @param str
//     * @return
//     * @Description: 16位小写MD5
//     */
//    public static String parseStrToMd5U16(String str){
//        String reStr = parseStrToMd5L32(str);
//        if (reStr != null){
//            reStr = reStr.toUpperCase().substring(8, 24);
//        }
//        return reStr;
//    }
//
//    /**
//     * @param str
//     * @return
//     * @Description: 16位大写MD5
//     */
//    public static String parseStrToMd5L16(String str){
//        String reStr = parseStrToMd5L32(str);
//        if (reStr != null){
//            reStr = reStr.substring(8, 24);
//        }
//        return reStr;
//    }
    
    /**
     * 加密处理
     * @param map
     * @param signKey
     * @return
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static String sign(LinkedHashMap<String, String> map, String signKey){
      StringBuffer text = new StringBuffer();
      Set set = map.entrySet();
      Iterator i = set.iterator();         
      while(i.hasNext()){      
           Map.Entry<String, String> entry1=(Map.Entry<String, String>)i.next();    
//           System.out.println(entry1.getKey()+"=="+entry1.getValue());   
           text.append(entry1.getValue());
      }
      String str = text.toString();
      System.out.println(str);
//      System.out.println(str+signKey);
      return parseStrToMd5U32(str+signKey);
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
	}
    
}
