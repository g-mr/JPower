package com.wlcb.jpower.module.common.support;

import cn.hutool.core.map.MapBuilder;
import cn.hutool.core.map.MapUtil;
import com.wlcb.jpower.module.common.utils.Fc;

import java.util.Map;

/**
 * 链式map
 *
 * @author mr.g
 */
public class ChainMap<K, V> extends MapBuilder<K, V> {

    private static final long serialVersionUID = 987087206329511311L;

    /**
     * 创建Builder，默认HashMap实现
     *
     * @param <K> Key类型
     * @param <V> Value类型
     * @return MapBuilder
     * @since 5.3.0
     */
    public static <K, V> ChainMap<K, V> create() {
        return create(false);
    }

    /**
     * 创建Builder
     *
     * @param <K>      Key类型
     * @param <V>      Value类型
     * @param isLinked true创建LinkedHashMap，false创建HashMap
     * @return MapBuilder
     * @since 5.3.0
     */
    public static <K, V> ChainMap<K, V> create(boolean isLinked) {
        return create(MapUtil.newHashMap(isLinked));
    }

    /**
     * 创建Builder
     *
     * @param <K> Key类型
     * @param <V> Value类型
     * @param map Map实体类
     * @return MapBuilder
     * @since 3.2.3
     */
    public static <K, V> ChainMap<K, V> create(Map<K, V> map) {
        return new ChainMap<>(map);
    }

    /**
     * 链式Map创建类
     *
     * @param map 要使用的Map实现类
     */
    public ChainMap(Map<K, V> map) {
        super(map);
    }

    /**
     * 链式Map创建
     *
     * @param k Key类型
     * @param v Value类型
     * @return 当前类
     */
    @Override
    public ChainMap<K, V> put(K k, V v) {
        super.put(k, v);
        return this;
    }

    /**
     * 返回int类型的值
     *
     * @author mr.g
     * @param k key
     * @return java.lang.Integer
     **/
    public Integer getInt(K k) {
        return Fc.toInt(map().get(k));
    }

    /**
     * 返回字符串类型的值
     *
     * @author mr.g
     * @param k key
     * @return java.lang.String
     **/
    public String getString(K k) {
        return Fc.toStr(map().get(k));
    }

    /**
     * 返回Boolean类型的值
     *
     * @author mr.g
     * @param k key
     * @return java.lang.String
     **/
    public Boolean getBool(K k) {
        return Fc.toBool(map().get(k));
    }

    /**
     * 返回值
     *
     * @author mr.g
     * @param k key
     * @return V
     **/
    public V get(K k) {
        return map().get(k);
    }
}
