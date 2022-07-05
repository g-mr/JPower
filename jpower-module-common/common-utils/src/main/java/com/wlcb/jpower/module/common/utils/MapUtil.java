package com.wlcb.jpower.module.common.utils;

import cn.hutool.core.convert.Convert;

import java.util.Date;
import java.util.Map;

/**
 * Map工具类
 *
 * @author mr.g
 */
public class MapUtil extends cn.hutool.core.map.MapUtil {

    /**
     * 获取Map指定key的值，并转换为字符串后删除指定的Key
     *
     * @author mr.g
     * @param map
     * @param key
     * @return java.lang.String
     **/
    public static String getStrRemoveKey(Map<?, ?> map, Object key) {
        return getRemoveKey(map, key, String.class);
    }

    /**
     * 获取Map指定key的值，并转换为字符串后删除指定的Key
     *
     * @param map          Map
     * @param key          键
     * @param defaultValue 默认值
     * @return 值
     * @since 5.3.11
     */
    public static String getStrRemoveKey(Map<?, ?> map, Object key, String defaultValue) {
        return getRemoveKey(map, key, String.class, defaultValue);
    }

    /**
     * 获取Map指定key的值，并转换为Integer后删除指定的Key
     *
     * @param map Map
     * @param key 键
     * @return 值
     * @since 4.0.6
     */
    public static Integer getIntRemoveKey(Map<?, ?> map, Object key) {
        return getRemoveKey(map, key, Integer.class);
    }

    /**
     * 获取Map指定key的值，并转换为Integer后删除指定的Key
     *
     * @param map          Map
     * @param key          键
     * @param defaultValue 默认值
     * @return 值
     * @since 5.3.11
     */
    public static Integer getIntRemoveKey(Map<?, ?> map, Object key, Integer defaultValue) {
        return getRemoveKey(map, key, Integer.class, defaultValue);
    }

    /**
     * 获取Map指定key的值，并转换为Double后删除指定的Key
     *
     * @param map Map
     * @param key 键
     * @return 值
     * @since 4.0.6
     */
    public static Double getDoubleRemoveKey(Map<?, ?> map, Object key) {
        return get(map, key, Double.class);
    }

    /**
     * 获取Map指定key的值，并转换为Double后删除指定的Key
     *
     * @param map          Map
     * @param key          键
     * @param defaultValue 默认值
     * @return 值
     * @since 5.3.11
     */
    public static Double getDoubleRemoveKey(Map<?, ?> map, Object key, Double defaultValue) {
        return getRemoveKey(map, key, Double.class, defaultValue);
    }

    /**
     * 获取Map指定key的值，并转换为Float后删除指定的Key
     *
     * @param map Map
     * @param key 键
     * @return 值
     * @since 4.0.6
     */
    public static Float getFloatRemoveKey(Map<?, ?> map, Object key) {
        return getRemoveKey(map, key, Float.class);
    }

    /**
     * 获取Map指定key的值，并转换为Float后删除指定的Key
     *
     * @param map          Map
     * @param key          键
     * @param defaultValue 默认值
     * @return 值
     * @since 5.3.11
     */
    public static Float getFloatRemoveKey(Map<?, ?> map, Object key, Float defaultValue) {
        return getRemoveKey(map, key, Float.class, defaultValue);
    }

    /**
     * 获取Map指定key的值，并转换为Short后删除指定的Key
     *
     * @param map Map
     * @param key 键
     * @return 值
     * @since 4.0.6
     */
    public static Short getShortRemoveKey(Map<?, ?> map, Object key) {
        return getRemoveKey(map, key, Short.class);
    }

    /**
     * 获取Map指定key的值，并转换为Short后删除指定的Key
     *
     * @param map          Map
     * @param key          键
     * @param defaultValue 默认值
     * @return 值
     * @since 5.3.11
     */
    public static Short getShortRemoveKey(Map<?, ?> map, Object key, Short defaultValue) {
        return getRemoveKey(map, key, Short.class, defaultValue);
    }

    /**
     * 获取Map指定key的值，并转换为Bool后删除指定的Key
     *
     * @param map Map
     * @param key 键
     * @return 值
     * @since 4.0.6
     */
    public static Boolean getBoolRemoveKey(Map<?, ?> map, Object key) {
        return getRemoveKey(map, key, Boolean.class);
    }

    /**
     * 获取Map指定key的值，并转换为Bool后删除指定的Key
     *
     * @param map          Map
     * @param key          键
     * @param defaultValue 默认值
     * @return 值
     * @since 5.3.11
     */
    public static Boolean getBoolRemoveKey(Map<?, ?> map, Object key, Boolean defaultValue) {
        return getRemoveKey(map, key, Boolean.class, defaultValue);
    }

    /**
     * 获取Map指定key的值，并转换为Character后删除指定的Key
     *
     * @param map Map
     * @param key 键
     * @return 值
     * @since 4.0.6
     */
    public static Character getCharRemoveKey(Map<?, ?> map, Object key) {
        return getRemoveKey(map, key, Character.class);
    }

    /**
     * 获取Map指定key的值，并转换为Character后删除指定的Key
     *
     * @param map          Map
     * @param key          键
     * @param defaultValue 默认值
     * @return 值
     * @since 5.3.11
     */
    public static Character getCharRemoveKey(Map<?, ?> map, Object key, Character defaultValue) {
        return getRemoveKey(map, key, Character.class, defaultValue);
    }

    /**
     * 获取Map指定key的值，并转换为Long后删除指定的Key
     *
     * @param map Map
     * @param key 键
     * @return 值
     * @since 4.0.6
     */
    public static Long getLongRemoveKey(Map<?, ?> map, Object key) {
        return getRemoveKey(map, key, Long.class);
    }

    /**
     * 获取Map指定key的值，并转换为Long后删除指定的Key
     *
     * @param map          Map
     * @param key          键
     * @param defaultValue 默认值
     * @return 值
     * @since 5.3.11
     */
    public static Long getLongRemoveKey(Map<?, ?> map, Object key, Long defaultValue) {
        return getRemoveKey(map, key, Long.class, defaultValue);
    }

    /**
     * 获取Map指定key的值，并转换为{@link Date}后删除指定的Key
     *
     * @param map Map
     * @param key 键
     * @return 值
     * @since 4.1.2
     */
    public static Date getDateRemoveKey(Map<?, ?> map, Object key) {
        return getRemoveKey(map, key, Date.class);
    }

    /**
     * 获取Map指定key的值，并转换为{@link Date}后删除指定的Key
     *
     * @param map          Map
     * @param key          键
     * @param defaultValue 默认值
     * @return 值
     * @since 4.1.2
     */
    public static Date getDateRemoveKey(Map<?, ?> map, Object key, Date defaultValue) {
        return getRemoveKey(map, key, Date.class, defaultValue);
    }

    /**
     * 获取Map指定key的值，并转换为指定类型后删除指定的Key
     *
     * @param <T>  目标值类型
     * @param map  Map
     * @param key  键
     * @param type 值类型
     * @return 值
     * @since 4.0.6
     */
    public static <T> T getRemoveKey(Map<?, ?> map, Object key, Class<T> type) {
        return getRemoveKey(map, key, type, null);
    }

    /**
     * 获取Map指定key的值，并转换为指定类型后删除指定的Key
     *
     * @param <T>          目标值类型
     * @param map          Map
     * @param key          键
     * @param type         值类型
     * @param defaultValue 默认值
     * @return 值
     * @since 5.3.11
     */
    public static <T> T getRemoveKey(Map<?, ?> map, Object key, Class<T> type, T defaultValue) {
        if (null == map){
            return defaultValue;
        }
        return Convert.convert(type, map.remove(key), defaultValue);
    }

    /**
     * 对Map的ascii排序后返回url参数字符串（微信加密）
     *
     * @author mr.g
     * @param map MAP
     * @param otherParams 其它附加参数字符串
     * @return java.lang.String
     **/
    public static <K, V> String asciiSortJoin(Map<K, V> map, String... otherParams) {
        return joinIgnoreNull(sort(map),"&","=", otherParams);
    }

}
