package com.wlcb.jpower.module.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import com.wlcb.jpower.module.common.utils.constants.CharPool;
import com.wlcb.jpower.module.common.utils.constants.StringPool;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

/**
 * 获取JSONObject得深结构数据
 *
 * @author mr.g
 */
public class DeepJson {


    /**
     * 查询指定的key值
     *
     * @author mr.g
     * @param jsonObject JSON
     * @param keys key eg:user.name
     * @return java.lang.Object
     **/
    public static Object find(JSONObject jsonObject,String keys) {
        JSON json = fd(jsonObject, keys);
        Object key = queryEndKey(keys);
        return Fc.isNull(json)?null:json instanceof JSONObject?((JSONObject) json).get(key):((JSONArray) json).get(Fc.toInt(key));
    }

    /**
     * 查询指定的key值,并转换为Integer
     *
     * @author mr.g
     * @param jsonObject JSON
     * @param keys key eg:user.name
     * @return java.lang.Integer
     **/
    public static Integer findInteger(JSONObject jsonObject, String keys) {
        JSON json = fd(jsonObject, keys);
        Object key = queryEndKey(keys);
        return Fc.isNull(json)?null:json instanceof JSONObject?((JSONObject) json).getInteger(Fc.toStr(key)):((JSONArray) json).getInteger(Fc.toInt(key));
    }

    /**
     * 查询指定的key值,并转换为int
     *
     * @author mr.g
     * @param jsonObject JSON
     * @param keys key eg:user.name
     * @return java.lang.Integer
     **/
    public static int findIntValue(JSONObject jsonObject, String keys) {
        JSON json = fd(jsonObject, keys);
        Object key = queryEndKey(keys);
        return Fc.isNull(json)?0:json instanceof JSONObject?((JSONObject) json).getIntValue(Fc.toStr(key)):((JSONArray) json).getIntValue(Fc.toInt(key));
    }

    /**
     * 查询指定的key值,并转换为Long
     *
     * @author mr.g
     * @param jsonObject JSON
     * @param keys key eg:user.name
     * @return java.lang.Integer
     **/
    public static Long findLong(JSONObject jsonObject, String keys) {
        JSON json = fd(jsonObject, keys);
        Object key = queryEndKey(keys);
        return Fc.isNull(json)?null:json instanceof JSONObject?((JSONObject) json).getLong(Fc.toStr(key)):((JSONArray) json).getLong(Fc.toInt(key));
    }

    /**
     * 查询指定的key值,并转换为long
     *
     * @author mr.g
     * @param jsonObject JSON
     * @param keys key eg:user.name
     * @return java.lang.Integer
     **/
    public static long findLongValue(JSONObject jsonObject, String keys) {
        JSON json = fd(jsonObject, keys);
        Object key = queryEndKey(keys);
        return Fc.isNull(json)?0L:json instanceof JSONObject?((JSONObject) json).getLongValue(Fc.toStr(key)):((JSONArray) json).getLongValue(Fc.toInt(key));
    }

    /**
     * 查询指定的key值,并转换为Float
     *
     * @author mr.g
     * @param jsonObject JSON
     * @param keys key eg:user.name
     * @return java.lang.Integer
     **/
    public static Float findFloat(JSONObject jsonObject, String keys) {
        JSON json = fd(jsonObject, keys);
        Object key = queryEndKey(keys);
        return Fc.isNull(json)?null:json instanceof JSONObject?((JSONObject) json).getFloat(Fc.toStr(key)):((JSONArray) json).getFloat(Fc.toInt(key));
    }

    /**
     * 查询指定的key值,并转换为float
     *
     * @author mr.g
     * @param jsonObject JSON
     * @param keys key eg:user.name
     * @return java.lang.Integer
     **/
    public static float findFloatValue(JSONObject jsonObject, String keys) {
        JSON json = fd(jsonObject, keys);
        Object key = queryEndKey(keys);
        return Fc.isNull(json)?0F:json instanceof JSONObject?((JSONObject) json).getFloatValue(Fc.toStr(key)):((JSONArray) json).getFloatValue(Fc.toInt(key));
    }

    /**
     * 查询指定的key值,并转换为Double
     *
     * @author mr.g
     * @param jsonObject JSON
     * @param keys key eg:user.name
     * @return java.lang.Integer
     **/
    public static Double findDouble(JSONObject jsonObject, String keys) {
        JSON json = fd(jsonObject, keys);
        Object key = queryEndKey(keys);
        return Fc.isNull(json)?null:json instanceof JSONObject?((JSONObject) json).getDouble(Fc.toStr(key)):((JSONArray) json).getDouble(Fc.toInt(key));
    }

    /**
     * 查询指定的key值,并转换为double
     *
     * @author mr.g
     * @param jsonObject JSON
     * @param keys key eg:user.name
     * @return java.lang.Integer
     **/
    public static double findDoubleValue(JSONObject jsonObject, String keys) {
        JSON json = fd(jsonObject, keys);
        Object key = queryEndKey(keys);
        return Fc.isNull(json)?0D:json instanceof JSONObject?((JSONObject) json).getDoubleValue(Fc.toStr(key)):((JSONArray) json).getDoubleValue(Fc.toInt(key));
    }

    /**
     * 查询指定的key值,并转换为BigDecimal
     *
     * @author mr.g
     * @param jsonObject JSON
     * @param keys key eg:user.name
     * @return java.lang.Integer
     **/
    public static BigDecimal findBigDecimal(JSONObject jsonObject, String keys) {
        JSON json = fd(jsonObject, keys);
        Object key = queryEndKey(keys);
        return Fc.isNull(json)?null:json instanceof JSONObject?((JSONObject) json).getBigDecimal(Fc.toStr(key)):((JSONArray) json).getBigDecimal(Fc.toInt(key));
    }

    /**
     * 查询指定的key值,并转换为BigInteger
     *
     * @author mr.g
     * @param jsonObject JSON
     * @param keys key eg:user.name
     * @return java.lang.Integer
     **/
    public static BigInteger findBigInteger(JSONObject jsonObject, String keys) {
        JSON json = fd(jsonObject, keys);
        Object key = queryEndKey(keys);
        return Fc.isNull(json)?null:json instanceof JSONObject?((JSONObject) json).getBigInteger(Fc.toStr(key)):((JSONArray) json).getBigInteger(Fc.toInt(key));
    }

    /**
     * 查询指定的key值,并转换为String
     *
     * @author mr.g
     * @param jsonObject JSON
     * @param keys key eg:user.name
     * @return java.lang.Integer
     **/
    public static String findString(JSONObject jsonObject, String keys) {
        JSON json = fd(jsonObject, keys);
        Object key = queryEndKey(keys);
        return Fc.isNull(json)?null:json instanceof JSONObject?((JSONObject) json).getString(Fc.toStr(key)):((JSONArray) json).getString(Fc.toInt(key));
    }

    /**
     * 查询指定的key值,并转换为Date
     *
     * @author mr.g
     * @param jsonObject JSON
     * @param keys key eg:user.name
     * @return java.lang.Integer
     **/
    public static Date findDate(JSONObject jsonObject, String keys) {
        JSON json = fd(jsonObject, keys);
        Object key = queryEndKey(keys);
        return Fc.isNull(json)?null:json instanceof JSONObject?((JSONObject) json).getDate(Fc.toStr(key)):((JSONArray) json).getDate(Fc.toInt(key));
    }

    /**
     * 查询指定的key值,并转换为java.sql.Date
     *
     * @author mr.g
     * @param jsonObject JSON
     * @param keys key eg:user.name
     * @return java.lang.Integer
     **/
    public static java.sql.Date findSqlDate(JSONObject jsonObject, String keys) {
        JSON json = fd(jsonObject, keys);
        Object key = queryEndKey(keys);
        return Fc.isNull(json)?null:json instanceof JSONObject?((JSONObject) json).getSqlDate(Fc.toStr(key)):((JSONArray) json).getSqlDate(Fc.toInt(key));
    }

    /**
     * 查询指定的key值,并转换为java.sql.Timestamp
     *
     * @author mr.g
     * @param jsonObject JSON
     * @param keys key eg:user.name
     * @return java.lang.Integer
     **/
    public static java.sql.Timestamp findTimestamp(JSONObject jsonObject, String keys) {
        JSON json = fd(jsonObject, keys);
        Object key = queryEndKey(keys);
        return Fc.isNull(json)?null:json instanceof JSONObject?((JSONObject) json).getTimestamp(Fc.toStr(key)):((JSONArray) json).getTimestamp(Fc.toInt(key));
    }

    /**
     * 查询指定的key值,并转换为Boolean
     *
     * @author mr.g
     * @param jsonObject JSON
     * @param keys key eg:user.name
     * @return java.lang.Integer
     **/
    public static Boolean findBoolean(JSONObject jsonObject, String keys) {
        JSON json = fd(jsonObject, keys);
        Object key = queryEndKey(keys);
        return Fc.isNull(json)?null:json instanceof JSONObject?((JSONObject) json).getBoolean(Fc.toStr(key)):((JSONArray) json).getBoolean(Fc.toInt(key));
    }

    /**
     * 查询指定的key值,并转换为boolean
     *
     * @author mr.g
     * @param jsonObject JSON
     * @param keys key eg:user.name
     * @return java.lang.Integer
     **/
    public static boolean findBooleanValue(JSONObject jsonObject, String keys) {
        JSON json = fd(jsonObject, keys);
        Object key = queryEndKey(keys);
        return !Fc.isNull(json) && (json instanceof JSONObject ? ((JSONObject) json).getBooleanValue(Fc.toStr(key)) : ((JSONArray) json).getBooleanValue(Fc.toInt(key)));
    }

    /**
     * 查询指定的key值,并转换为Byte
     *
     * @author mr.g
     * @param jsonObject JSON
     * @param keys key eg:user.name
     * @return java.lang.Integer
     **/
    public static Byte findByte(JSONObject jsonObject, String keys) {
        JSON json = fd(jsonObject, keys);
        Object key = queryEndKey(keys);
        return Fc.isNull(json) ? null : json instanceof JSONObject ? ((JSONObject) json).getByte(Fc.toStr(key)) : ((JSONArray) json).getByte(Fc.toInt(key));
    }

    /**
     * 查询指定的key值,并转换为byte
     *
     * @author mr.g
     * @param jsonObject JSON
     * @param keys key eg:user.name
     * @return java.lang.Integer
     **/
    public static byte findByteValue(JSONObject jsonObject, String keys) {
        JSON json = fd(jsonObject, keys);
        Object key = queryEndKey(keys);
        return Fc.isNull(json) ? 0 : json instanceof JSONObject ? ((JSONObject) json).getByteValue(Fc.toStr(key)) : ((JSONArray) json).getByteValue(Fc.toInt(key));
    }

    /**
     * 查询指定的key值,并转换为Short
     *
     * @author mr.g
     * @param jsonObject JSON
     * @param keys key eg:user.name
     * @return java.lang.Integer
     **/
    public static Short findShort(JSONObject jsonObject, String keys) {
        JSON json = fd(jsonObject, keys);
        Object key = queryEndKey(keys);
        return Fc.isNull(json) ? null : json instanceof JSONObject ? ((JSONObject) json).getShort(Fc.toStr(key)) : ((JSONArray) json).getShort(Fc.toInt(key));
    }

    /**
     * 查询指定的key值,并转换为short
     *
     * @author mr.g
     * @param jsonObject JSON
     * @param keys key eg:user.name
     * @return java.lang.Integer
     **/
    public static short findShortValue(JSONObject jsonObject, String keys) {
        JSON json = fd(jsonObject, keys);
        Object key = queryEndKey(keys);
        return Fc.isNull(json) ? 0 : json instanceof JSONObject ? ((JSONObject) json).getShortValue(Fc.toStr(key)) : ((JSONArray) json).getShortValue(Fc.toInt(key));
    }

    /**
     * 查询指定的key值,并转换为JSONObject
     *
     * @author mr.g
     * @param jsonObject JSON
     * @param keys key eg:user.name
     * @return java.lang.Integer
     **/
    public static JSONObject findJsonObject(JSONObject jsonObject, String keys) {
        JSON json = fd(jsonObject, keys);
        Object key = queryEndKey(keys);
        return Fc.isNull(json)?null:json instanceof JSONObject?((JSONObject) json).getJSONObject(Fc.toStr(key)):((JSONArray) json).getJSONObject(Fc.toInt(key));
    }

    /**
     * 查询指定的key值,并转换为JSONArray
     *
     * @author mr.g
     * @param jsonObject JSON
     * @param keys key eg:user.name
     * @return java.lang.Integer
     **/
    public static JSONArray findJsonArray(JSONObject jsonObject, String keys) {
        JSON json = fd(jsonObject, keys);
        Object key = queryEndKey(keys);
        return Fc.isNull(json)?null:json instanceof JSONObject?((JSONObject) json).getJSONArray(Fc.toStr(key)):((JSONArray) json).getJSONArray(Fc.toInt(key));
    }

    /**
     * 查询指定的key值,并转换为指定的类型
     *
     * @author mr.g
     * @param jsonObject JSON
     * @param keys key eg:user.name
     * @param clazz 类型
     * @return java.lang.Integer
     **/
    public static <T> T findObject(JSONObject jsonObject, String keys, Class<T> clazz) {
        JSON json = fd(jsonObject, keys);
        Object key = queryEndKey(keys);
        return Fc.isNull(json)?null:json instanceof JSONObject?((JSONObject) json).getObject(Fc.toStr(key), clazz):((JSONArray) json).getObject(Fc.toInt(key), clazz);
    }

    /**
     * 查询指定的key值,并转换为指定的类型
     *
     * @author mr.g
     * @param jsonObject JSON
     * @param keys key eg:user.name
     * @param type 类型
     * @return java.lang.Integer
     **/
    public static <T> T findObject(JSONObject jsonObject, String keys, Type type) {
        JSON json = fd(jsonObject, keys);
        Object key = queryEndKey(keys);
        return Fc.isNull(json)?null:json instanceof JSONObject?((JSONObject) json).getObject(Fc.toStr(key), type):((JSONArray) json).getObject(Fc.toInt(key), type);
    }

    /**
     * 查找最后一个key
     *
     * @author mr.g
     * @param keys 最后的key
     * @return java.lang.Object
     */
    private static Object queryEndKey(String keys) {
        String key = CollectionUtils.lastElement(Splitter.on(StringPool.DOT).omitEmptyStrings().trimResults().splitToList(keys));
        if (CharMatcher.inRange(CharPool.NUMBER_0, CharPool.NUMBER_9).removeFrom(key).endsWith(StringPool.LEFT_SQ_BRACKET+StringPool.RIGHT_SQ_BRACKET)){
            return Fc.toInt(StringUtil.subBetween(key,StringPool.LEFT_SQ_BRACKET,StringPool.RIGHT_SQ_BRACKET,true));
        }
        return key;
    }

    /**
     * 深结构查询值
     *
     * @author mr.g
     * @param jsonObject JSON
     * @param keys 查找的key
     * @return com.alibaba.fastjson.JSON
     */
    private static JSON fd(JSONObject jsonObject, String keys) {
        if (Fc.isNull(jsonObject) || Fc.isBlank(keys)){
            return null;
        }

        List<String> keyList = Splitter.on(StringPool.DOT).omitEmptyStrings().trimResults().splitToList(keys);
        for (int i = 0; i < keyList.size(); i++) {
            String key = keyList.get(i);

            JSONArray json = null;
            int index = 0;
            if (CharMatcher.inRange(CharPool.NUMBER_0, CharPool.NUMBER_9).removeFrom(key).endsWith(StringPool.LEFT_SQ_BRACKET+StringPool.RIGHT_SQ_BRACKET)){
                index = Fc.toInt(StringUtil.subBetween(key,StringPool.LEFT_SQ_BRACKET,StringPool.RIGHT_SQ_BRACKET,true));
                String arrayKey = StringUtil.removeSuffix(key,StringPool.LEFT_SQ_BRACKET+index+StringPool.RIGHT_SQ_BRACKET);
                json = jsonObject.getJSONArray(arrayKey);
            }

            if (i == keyList.size()-1){
                return Fc.isNull(json)?jsonObject:json;
            }else {
                return fd(Fc.isNull(json)?jsonObject.getJSONObject(key):json.getJSONObject(index),StringUtil.removePrefix(keys,key+StringPool.DOT));
            }
        }
        return null;
    }

}
