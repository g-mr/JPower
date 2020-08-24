package com.wlcb.jpower.module.common.support;

import com.wlcb.jpower.module.common.utils.Fc;
import org.springframework.util.LinkedCaseInsensitiveMap;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;

/**
 * @ClassName ChainMap
 * @Description TODO
 * @Author 郭丁志
 * @Date 2020-07-23 15:05
 * @Version 1.0
 */
public class ChainMap extends LinkedCaseInsensitiveMap<Object> {

    private ChainMap() {

    }

    /**
     * 创建ChainMap
     *
     * @return ChainMap
     */
    public static ChainMap init() {
        return new ChainMap();
    }

    public static HashMap newMap() {
        return new HashMap(16);
    }

    /**
     * 设置列
     *
     * @param attr  属性
     * @param value 值
     * @return 本身
     */
    public ChainMap set(String attr, Object value) {
        this.put(attr, value);
        return this;
    }

    /**
     * 设置列，当键或值为null时忽略
     *
     * @param attr  属性
     * @param value 值
     * @return 本身
     */
    public ChainMap setIgnoreNull(String attr, Object value) {
        if (null != attr && null != value) {
            set(attr, value);
        }
        return this;
    }

    public Object getObj(String key) {
        return super.get(key);
    }

    /**
     * 获得特定类型值
     *
     * @param <T>          值类型
     * @param attr         字段名
     * @param defaultValue 默认值
     * @return 字段值
     */
    public <T> T get(String attr, T defaultValue) {
        final Object result = get(attr);
        return (T) (result != null ? result : defaultValue);
    }

    /**
     * 获得特定类型值
     *
     * @param attr 字段名
     * @return 字段值
     */
    public String getStr(String attr) {
        return Fc.toStr(get(attr), null);
    }

    /**
     * 获得特定类型值
     *
     * @param attr 字段名
     * @return 字段值
     */
    public Integer getInt(String attr) {
        return Fc.toInt(get(attr), -1);
    }

    /**
     * 获得特定类型值
     *
     * @param attr 字段名
     * @return 字段值
     */
    public Long getLong(String attr) {
        return Fc.toLong(get(attr), -1L);
    }

    /**
     * 获得特定类型值
     *
     * @param attr 字段名
     * @return 字段值
     */
    public Float getFloat(String attr) {
        return Fc.toFloat(get(attr), null);
    }

    public Double getDouble(String attr) {
        return Fc.toDouble(get(attr), null);
    }


    /**
     * 获得特定类型值
     *
     * @param attr 字段名
     * @return 字段值
     */
    public Boolean getBool(String attr) {
        return Fc.toBoolean(get(attr), null);
    }

    /**
     * 获得特定类型值
     *
     * @param attr 字段名
     * @return 字段值
     */
    public byte[] getBytes(String attr) {
        return get(attr, null);
    }

    /**
     * 获得特定类型值
     *
     * @param attr 字段名
     * @return 字段值
     */
    public Date getDate(String attr) {
        return get(attr, null);
    }

    /**
     * 获得特定类型值
     *
     * @param attr 字段名
     * @return 字段值
     */
    public Time getTime(String attr) {
        return get(attr, null);
    }

    /**
     * 获得特定类型值
     *
     * @param attr 字段名
     * @return 字段值
     */
    public Timestamp getTimestamp(String attr) {
        return get(attr, null);
    }

    /**
     * 获得特定类型值
     *
     * @param attr 字段名
     * @return 字段值
     */
    public Number getNumber(String attr) {
        return get(attr, null);
    }

    @Override
    public ChainMap clone() {
        return (ChainMap) super.clone();
    }

}
