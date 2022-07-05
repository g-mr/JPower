package com.wlcb.jpower.module.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

/**
 * JSON工具类
 *
 * @author mr.g
 */
@Slf4j
public class JsonUtil {

    /**
     * 将对象序列化成json字符串
     *
     * @param value javaBean
     * @param <T>   T 泛型标记
     * @return json字符串
     */
    public static <T> String toJson(T value) {
        return JSON.toJSONString(value);
    }

    /**
     * 将json反序列化成对象
     *
     * @param content   content
     * @param valueType class
     * @param <T>       T 泛型标记
     * @return Bean
     */
    public static <T> T parseObject(String content, Class<T> valueType) {
        return JSON.parseObject(content,valueType);
    }

    /**
     * 将json反序列化成List对象
     *
     * @param content   content
     * @param valueType class
     * @param <T>       T 泛型标记
     * @return Bean
     */
    public static <T> List<T> parseArray(String content, Class<T> valueType) {
        return JSON.parseArray(content,valueType);
    }

    /**
     * 将json byte 数组反序列化成对象
     *
     * @param bytes     json bytes
     * @param valueType class
     * @param <T>       T 泛型标记
     * @return Bean
     */
    public static <T> T parseObject(byte[] bytes, Class<T> valueType) {
        return JSON.parseObject(bytes,valueType);
    }

    /**
     * JSON字符串转MAP
     *
     * @author mr.g
     * @param content json字符串
     * @return java.util.Map<java.lang.String,java.lang.Object>
     **/
    public static Map<String, Object> toMap(String content) {
        return JSON.parseObject(content,Map.class);
    }

    /**
     * 判断是否是JSON字符串
     *
     * @author mr.g
     * @param json json字符串
     * @return 是否是json字符串
     */
    public static boolean isJsonValid(String json ) {
        try {
            JSON.parse(json);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    /**
     * 判断是否是JsonObject
     *
     * @author mr.g
     * @param json json字符串
     * @return 是否是json字符串
     */
    public static boolean isJsonObject(String json) {
        try {
            JSONObject.parseObject(json);
        } catch (JSONException ex) {
            return false;
        }
        return true;
    }

    /**
     * 判断是否是JsonArray
     *
     * @author mr.g
     * @param json json字符串
     * @return 是否是json字符串
     */
    public static boolean isJsonArray(String json) {
        try {
            JSONObject.parseArray(json);
        } catch (JSONException ex) {
            return false;
        }
        return true;
    }

    /**
     * JSON转XML
     *
     * @author mr.g
     * @param json JSON字符串
     * @return xml字符串
     */
    public static String json2xml(String json) {
        JSONObject jsonObj = JSON.parseObject(json);
        StringBuffer buff = new StringBuffer();
        String tempObj;
        JSONArray tempArr;
        for (String temp : jsonObj.keySet()) {
            buff.append("<").append(temp.trim()).append(">");
            if (jsonObj.get(temp) instanceof JSONObject) {
                tempObj = jsonObj.getString(temp);
                buff.append(json2xml(tempObj));
            } else if (jsonObj.get(temp) instanceof JSONArray) {
                tempArr = (JSONArray) jsonObj.get(temp);
                if (tempArr.size() > 0) {
                    tempArr.stream().map(Fc::toStr).forEach(str -> buff.append(json2xml(str)));
                }
            } else {
                String tempStr = jsonObj.get(temp).toString();
                buff.append(tempStr.trim());
            }
            buff.append("</").append(temp.trim()).append(">");
        }
        return buff.toString();
    }

}
