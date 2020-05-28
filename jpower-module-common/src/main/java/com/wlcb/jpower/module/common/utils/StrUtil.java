package com.wlcb.jpower.module.common.utils;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * @ClassName StrUtil
 * @Description TODO
 * @Author 郭丁志
 * @Date 2020-03-03 20:36
 * @Version 1.0
 */
public class StrUtil {

    private static Logger logger = LoggerFactory.getLogger(StrUtil.class);

    /**普通的英文半角空格Unicode编码*/
    private static final int SPACE_32 = 32;

    /**中文全角空格Unicode编码(一个中文宽度)*/
    private static final int SPACE_12288 = 12288;

    /**普通的英文半角空格但不换行Unicode编码(== &nbsp; == &#xA0; == no-break space)*/
    private static final int SPACE_160 = 160;

    /**半个中文宽度(== &ensp; == en空格)*/
    private static final int SPACE_8194 = 8194;

    /**一个中文宽度(== &emsp; == em空格)*/
    private static final int SPACE_8195 = 8195;

    /**四分之一中文宽度(四分之一em空格)*/
    private static final int SPACE_8197 = 8197;

    /**窄空格*/
    private static final int SPACE_8201 = 8201;

    //第一代身份证正则表达式(15位)
    private static final String isIDCard1 = "^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$";
    //第二代身份证正则表达式(18位)
    private static final String isIDCard2 ="^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])((\\d{4})|\\d{3}[A-Z])$";
    //手机号正则
    private static final String phoneRegex = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";
    //邮箱正则
    private static final String emailRegex = "^([a-z0-9A-Z]+[-|\\.]?[_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";

    /**
     * 去除字符串前后的空格, 包括半角空格和全角空格(中文)等各种空格, java的string.trim()只能去英文半角空格
     * 以及去除字符串当中的空白字符
     * @param str
     */
    public static String trim(String str) {
        if (StringUtils.isBlank(str)) {
            return str;
        }

        str = StringUtils.trim(str.trim());

        str = str.replace("\n","").replace("\t","").replace("\r","");

        char[] val = str.toCharArray();
        int st = 0;
        int len=val.length;
        while ((st < len) && isSpace(val[st])) {
            st++;
        }
        while ((st < len) && isSpace(val[len - 1])) {
            len--;
        }
        return ((st > 0) || (len < val.length)) ? str.substring(st, len) : str;
    }

    public static boolean isSpace(char aChar) {
        return aChar == SPACE_32 || aChar == SPACE_12288 || aChar == SPACE_160 || aChar == SPACE_8194
                || aChar == SPACE_8195 || aChar == SPACE_8197 || aChar == SPACE_8201;
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 正则校验身份证是否符合第一代第二代标准
     * @Date 20:39 2020-03-03
     * @Param [cardcode]
     * @return boolean
     **/
    public static boolean cardCodeVerifySimple(String cardcode) {
        //验证身份证
//        if (cardcode.matches(isIDCard1) || cardcode.matches(isIDCard2)) {
        if (cardcode.matches(isIDCard1) || cardCodeVerify(cardcode)) {
            return true;
        }
        return false;
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 验证第二代身份证是否符合国家规范
     * @Date 20:40 2020-03-03
     * @Param [cardcode]
     * @return boolean
     **/
    public static boolean cardCodeVerify(String cardcode) {
        int i = 0;
        String r = "error";
        String lastnumber = "";

        try {
            i += Integer.parseInt(cardcode.substring(0, 1)) * 7;
            i += Integer.parseInt(cardcode.substring(1, 2)) * 9;
            i += Integer.parseInt(cardcode.substring(2, 3)) * 10;
            i += Integer.parseInt(cardcode.substring(3, 4)) * 5;
            i += Integer.parseInt(cardcode.substring(4, 5)) * 8;
            i += Integer.parseInt(cardcode.substring(5, 6)) * 4;
            i += Integer.parseInt(cardcode.substring(6, 7)) * 2;
            i += Integer.parseInt(cardcode.substring(7, 8)) * 1;
            i += Integer.parseInt(cardcode.substring(8, 9)) * 6;
            i += Integer.parseInt(cardcode.substring(9, 10)) * 3;
            i += Integer.parseInt(cardcode.substring(10,11)) * 7;
            i += Integer.parseInt(cardcode.substring(11,12)) * 9;
            i += Integer.parseInt(cardcode.substring(12,13)) * 10;
            i += Integer.parseInt(cardcode.substring(13,14)) * 5;
            i += Integer.parseInt(cardcode.substring(14,15)) * 8;
            i += Integer.parseInt(cardcode.substring(15,16)) * 4;
            i += Integer.parseInt(cardcode.substring(16,17)) * 2;
            i = i % 11;
            lastnumber =cardcode.substring(17,18);
            if (i == 0) {
                r = "1";
            }
            if (i == 1) {
                r = "0";
            }
            if (i == 2) {
                r = "x";
            }
            if (i == 3) {
                r = "9";
            }
            if (i == 4) {
                r = "8";
            }
            if (i == 5) {
                r = "7";
            }
            if (i == 6) {
                r = "6";
            }
            if (i == 7) {
                r = "5";
            }
            if (i == 8) {
                r = "4";
            }
            if (i == 9) {
                r = "3";
            }
            if (i == 10) {
                r = "2";
            }
            if (r.equals(lastnumber.toLowerCase())) {
                return true;
            }
        }catch (Exception e){
            logger.error("身份证识别错误：{},error={}",cardcode,e.getMessage());
        }
        return false;
    }

    public static Integer sexCard(String cardCode) {
        if (StringUtils.isBlank(cardCode) || cardCode.length()< 15){
            return 0;
        }

        if (cardCode.length() == 18){
            return sexCard18(cardCode);
        }else {
            return sexCard15(cardCode);
        }
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 判断18位身份证性别
     * @Date 16:34 2020-05-21
     * @Param [cardCode]
     * @return java.lang.Integer
     **/
    public static Integer sexCard18(String cardCode) {
        Integer sex;
        // 判断性别
        if (Integer.parseInt(cardCode.substring(16).substring(0, 1)) % 2 == 0) {
            sex = 2;
        } else {
            sex = 1;
        }
        return sex;
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 判断15位身份证性别
     * @Date 16:33 2020-05-21
     * @Param [card]
     * @return java.lang.Integer
     **/
    public static Integer sexCard15(String card) {
        Integer sex;
        if (Integer.parseInt(card.substring(14, 15)) % 2 == 0) {
            sex = 2;
        } else {
            sex = 1;
        }
        return sex;
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 判断是否是手机号
     * @Date 20:40 2020-03-03
     * @Param [phone]
     * @return boolean
     **/
    public static boolean isPhone(String phone) {
        if (phone.length() != 11) {
            return false;
        } else {
            return phone.matches(phoneRegex);
        }
    }

    public static boolean isEmail(String email) {
        if (email.split("@").length != 2) {
            return false;
        } else {
            return email.matches(emailRegex);
        }
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 将map转为xml格式字符串
     * @Date 17:18 2020-03-21
     * @Param [map]
     * @return java.lang.String
     **/
    public static String mapToXmlStr(Map<String,String> map){

        StringBuffer stringBuffer = new StringBuffer("<xml>");

        for (String key : map.keySet()) {
            stringBuffer.append("<").append(key).append(">").append(map.get(key)).append("</").append(key).append(">");
        }
        stringBuffer.append("</xml>");
        return stringBuffer.toString();
    }

    /**
     * @Author 郭丁志
     * @Description //TODO xml转map
     * @Date 15:56 2020-03-22
     * @Param [root]
     * @return java.util.Map
     **/
    public static Map iterateElement(Element root) {
        List childrenList = root.elements();
        Element element = null;
        Map map = new HashMap();
        List list = null;
        for (int i = 0; i < childrenList.size(); i++) {
            list = new ArrayList();
            element = (Element) childrenList.get(i);
            if(element.elements().size()>0){
                if(root.elements(element.getName()).size()>1){
                    if (map.containsKey(element.getName())) {
                        list = (List) map.get(element.getName());
                    }
                    list.add(iterateElement(element));
                    map.put(element.getName(), list);
                }else{
                    map.put(element.getName(), iterateElement(element));
                }
            }else {
                if(root.elements(element.getName()).size()>1){
                    if (map.containsKey(element.getName())) {
                        list = (List) map.get(element.getName());
                    }
                    list.add(element.getTextTrim());
                    map.put(element.getName(), list);
                }else{
                    map.put(element.getName(), element.getTextTrim());
                }
            }
        }

        return map;
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 生成唯一的订单号
     * @Date 17:30 2020-03-21
     * @Param []
     * @return java.lang.String
     **/
    public static String createOrderId(String mchid) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(mchid);
        buffer.append(createTime());
        buffer.append(UUIDUtil.create10UUidNum());
        return buffer.toString();
    }

    /**
     * 生成时间戳字符串，格式：yyyyMMddhhMMss
     *
     * @return
     */
    private static synchronized String createTimestamp() {
        Date date = new Date();
        date.setTime(System.currentTimeMillis());
        // 时间精确到秒
        String str = String.format("%1$tY%1$tm%1$td%1$tk%1$tM%1$tS", date);
        return str;
    }

    /**
     * 生成时间戳字符串，格式：yyyyMMdd
     *
     * @return
     */
    private static synchronized String createTime() {
        Date date = new Date();
        date.setTime(System.currentTimeMillis());
        // 时间精确到秒
        String str = String.format("%1$tY%1$tm%1$td", date);
        return str;
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 将map转换成Ascii码从小到大排序的keyvalue格式字符串
     * @Date 17:47 2020-03-21
     * @Param [map]
     * @return java.lang.String
     **/
    public static String getAsciiKeyValue(Map<String, String> map) {

        Set<String> keySet = map.keySet();
        String[] keyArray = (String[])keySet.toArray(new String[keySet.size()]);
        Arrays.sort(keyArray);

        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < keyArray.length; ++i) {
            String k = keyArray[i];
            if (StringUtils.isNotBlank(k) && !k.equals("sign") && ((String)map.get(k)).trim().length() > 0) {
                sb.append(k).append("=").append(((String)map.get(k)).trim()).append("&");
            }
        }

        return sb.toString();
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 对字符串进行UTF8编码
     * @Date 11:31 2020-05-07
     * @Param [map]
     * @return java.lang.String
     **/
    public static String strToUtf8(String str) {

        String utf8Str = null;
        try {
            byte[] utf8Bytes = str.getBytes("UTF-8");
            utf8Str = new String(utf8Bytes, "UTF-8");
        }catch (UnsupportedEncodingException e){
            logger.error("编码转换出错：{}",e.getMessage());
        }

        return utf8Str;
    }

}