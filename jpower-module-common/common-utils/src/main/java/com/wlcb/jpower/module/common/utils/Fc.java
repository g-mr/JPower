package com.wlcb.jpower.module.common.utils;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.EscapeUtil;
import cn.hutool.core.util.NumberUtil;
import com.wlcb.jpower.module.common.utils.constants.StringPool;
import lombok.NonNull;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.lang.Nullable;

import java.io.Closeable;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.util.*;

/**
 * 工具包集合
 *
 * @author mr.g
 */
public class Fc {

    /**
     * 除去字符串头尾部的空白
     *
     * @author mr.g
     * @param str 字符串
     * @return java.lang.String
     **/
    public static String trim(String str){
        return StringUtil.trim(str);
    }

    /**
     * 检查指定的对象引用不是 {@code null} 和
     * 如果是，则抛出自定义的 {@link NullPointerException}。
     * 这种方法主要用于在方法中进行参数验证和具有多个参数的构造函数
     *
     * @author mr.g
     * @param obj     对象
     * @param message 为空时的错误信息
     * @return {@code obj} if not {@code null}
     */
    public static <T> T requireNotNull(T obj, String message) {
        return Objects.requireNonNull(obj, message);
    }

    /**
     * 是否是空对象
     *
     * @author mr.g
     * @param obj 对象
     * @return boolean
     **/
    public static boolean isNull(@Nullable Object obj) {
        return ObjectUtil.isNull(obj);
    }

    /**
     * 不是空对象
     *
     * @author mr.g
     * @param obj
     * @return boolean
     **/
    public static boolean notNull(@Nullable Object obj) {
        return ObjectUtil.isNotNull(obj);
    }

    /**
     * 字符串是否为空白，空白的定义如下：
     *
     *
     * <li>{@code StrUtil.isBlank(null)     // true}</li>
     * <li>{@code StrUtil.isBlank("")       // true}</li>
     * <li>{@code StrUtil.isBlank(" \t\n")  // true}</li>
     * <li>{@code StrUtil.isBlank("abc")    // false}</li>
     *
     * @author mr.g
     * @param str 被检测的字符串
     * @return 若为空白，则返回 true
     */
    public static boolean isBlank(@Nullable final CharSequence str) {
        return StringUtil.isBlank(str);
    }

    /**
     * <p>字符串是否为非空白，非空白的定义如下
     * <pre>
     * $.isNotBlank(null)	= false
     * $.isNotBlank("")		= false
     * $.isNotBlank(" ")	= false
     * $.isNotBlank("bob")	= true
     * $.isNotBlank("  bob  ") = true
     * </pre>
     *
     * @author mr.g
     * @param str 被检测的字符串
     * @return 是否为非空
     */
    public static boolean isNotBlank(@Nullable final CharSequence str) {
        return StringUtil.isNotBlank(str);
    }

    /**
     * 有 任意 一个 Blank
     *
     * @author mr.g
     * @param cs CharSequence
     * @return boolean
     */
    public static boolean isAnyBlank(final CharSequence... cs) {
        return StringUtil.isAnyBlank(cs);
    }

    /**
     * 是否全非 Blank
     *
     * @author mr.g
     * @param cs CharSequence
     * @return boolean
     */
    public static boolean isNoneBlank(final CharSequence... cs) {
        return StringUtil.isAllNotBlank(cs);
    }

    /**
     * 判断给定对象是否为数组:
     * 对象数组或原始数组.
     *
     * @author mr.g
     * @param obj 检查对象
     * @return 是否数组
     */
    public static boolean isArray(@Nullable Object obj) {
        return ArrayUtil.isArray(obj);
    }

    /**
     * 判断给定对象是否为空:
     * 即 {@code null} 或零长度.
     *
     * @author mr.g
     * @param obj 检查对象
     * @return 数组是否为空
     */
    public static boolean isEmpty(@Nullable Object obj) {
        return ObjectUtil.isEmpty(obj);
    }

    /**
     * 判断给定的对象是否不为空：
     * 即 {@code null} 或零长度。
     *
     * @author mr.g
     * @param obj 检查对象
     * @return 是否不为空
     */
    public static boolean isNotEmpty(@Nullable Object obj) {
        return ObjectUtil.isNotEmpty(obj);
    }

    /**
     * 判断给定数组是否为空：
     * 即 {@code null} 或零长度。
     *
     * @author mr.g
     * @param array 检查对象
     * @return 数组是否为空
     */
    public static boolean isEmpty(@Nullable Object[] array) {
        return ObjectUtil.isEmpty(array);
    }

    /**
     * 判断数组不为空
     *
     * @author mr.g
     * @param array 数组
     * @return 数组是否不为空
     */
    public static boolean isNotEmpty(@Nullable Object[] array) {
        return ObjectUtil.isNotEmpty(array);
    }

    /**
     * 对象组中是否存在 Empty Object
     *
     * @author mr.g
     * @param os 对象组
     * @return boolean
     */
    public static boolean hasEmpty(Object... os) {
        for (Object o : os) {
            if (isEmpty(o)) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    /**
     * 对象组中是否全是 Empty Object
     *
     * @author mr.g
     * @param os 对象组
     * @return boolean
     */
    public static boolean allEmpty(Object... os) {
        for (Object o : os) {
            if (!isEmpty(o)) {
                return Boolean.FALSE;
            }
        }
        return Boolean.TRUE;
    }

    /**
     * 比较两个对象是否相等。<br>
     * 相同的条件有两个，满足其一即可：<br>
     *
     * @author mr.g
     * @param obj1 对象1
     * @param obj2 对象2
     * @return 是否相等
     */
    public static boolean equals(Object obj1, Object obj2) {
        return ObjectUtil.equals(obj1, obj2);
    }

    /**
     * 判断俩个类型的值是否相等
     *
     * @author mr.g
     * @param o1 要比较的第一个对象
     * @param o2 要比较的第二个对象
     * @return 给定的对象值是否相等
     **/
    public static boolean equalsValue(@NonNull Object o1, @NonNull Object o2) {
        return ObjectUtil.equalsValue(o1, o2);
    }

    /**
     * 判断俩个类型的值是否相等
     *
     * @author mr.g
     * @param o1 要比较的第一个对象
     * @param o2 要比较的第二个对象
     * @return 给定的对象值是否相等
     **/
    public static boolean equalsValue(@Nullable CharSequence o1, @Nullable CharSequence o2) {
        return StringUtil.equals(o1, o2);
    }

    /**
     * 判断给定的数组是否包含指定的元素
     *
     * @author mr.g
     * @param array   要检查的数组
     * @param element 要查找的元素
     * @param <T>     通用标签
     * @return {@code true} if found, {@code false} else
     */
    public static <T> boolean contains(@Nullable T[] array, final T element) {
        return ArrayUtil.contains(array, element);
    }

    /**
     * 检查给定的迭代器是否包含给定的元素。
     *
     * @author mr.g
     * @param iterator   要检查的集合
     * @param element 要查找的元素
     * @return {@code true} if found, {@code false} otherwise
     */
    public static boolean contains(@Nullable Collection<?> iterator, Object element) {
        return CollectionUtil.contains(iterator, element);
    }

    /**
     * 强转string
     *
     * @author mr.g
     * @param str 字符串
     * @return String
     */
    public static String toStr(Object str) {
        return toStr(str, "");
    }

    /**
     * 强转string,并去掉多余空格
     *
     * @author mr.g
     * @param str          字符串
     * @param defaultValue 默认值
     * @return String
     */
    public static String toStr(Object str, String defaultValue) {
        if (null == str) {
            return defaultValue;
        }
        return String.valueOf(str);
    }

    /**
     * 判断一个字符串是否是数字
     *
     * @author mr.g
     * @param str 要检查的 str，可能为 null
     * @return {boolean}
     */
    public static boolean isNumeric(final String str) {
        return NumberUtil.isInteger(str);
    }

    /**
     * 转换成数字
     *
     * @author mr.g
     * @param value 对象
     * @return int
     **/
    public static int toInt(final Object value) {
        return Convert.toInt(value,-1);
    }

    /**
     * 转换成数字
     *
     * @author mr.g
     * @param value 对象
     * @param defaultValue 默认值
     * @return int
     **/
    public static int toInt(final Object value, final int defaultValue) {
        return Convert.toInt(value,defaultValue);
    }

    /**
     * 转换成Long
     *
     * @author mr.g
     * @param value 对象
     * @return long
     **/
    public static long toLong(final Object value) {
        return Convert.toLong(value);
    }

    /**
     * 转换成Long
     *
     * @author mr.g
     * @param value 对象
     * @param defaultValue 默认值
     * @return long
     **/
    public static long toLong(final Object value, final long defaultValue) {
        return Convert.toLong(value, defaultValue);
    }

    /**
     * 转换成Double
     */
    public static Double toDouble(Object value) {
        return Convert.toDouble(value);
    }

    /**
     * 转换成Double
     */
    public static Double toDouble(Object value, Double defaultValue) {
        return Convert.toDouble(value, defaultValue);
    }

    /**
     * 转换成Float
     */
    public static Float toFloat(Object value) {
        return Convert.toFloat(value);
    }

    /**
     * 转换成Float
     */
    public static Float toFloat(Object value, Float defaultValue) {
        return Convert.toFloat(value, defaultValue);
    }

    /**
     * 转换成Boolean
     */
    public static Boolean toBoolean(Object value) {
        return toBoolean(value, null);
    }

    /**
     * 转换成Boolean
     */
    public static Boolean toBoolean(Object value, Boolean defaultValue) {
        if (value != null) {
            String val = String.valueOf(value);
            val = val.toLowerCase().trim();
            return Boolean.parseBoolean(val);
        }
        return defaultValue;
    }

    /**
     * 转换为boolean<br>
     * String支持的值为：true、false、yes、ok、no，1,0 如果给定的值为空，或者转换失败，返回默认值<br>
     * 转换失败不会报错
     */
    public static Boolean toBool(Object value, Boolean defaultValue) {
        return Convert.toBool(value, defaultValue);
    }

    /**
     * 转换为boolean<br>
     * 如果给定的值为空，或者转换失败，返回默认值<code>null</code><br>
     * 转换失败不会报错
     */
    public static Boolean toBool(Object value) {
        return toBool(value, null);
    }

    /**
     * 转换为Integer数组<br>
     *
     * @param str 被转换的值
     * @return 结果
     */
    public static Integer[] toIntArray(String str) {
        return toIntArray(",", str);
    }

    /**
     * 转换为Integer数组<br>
     *
     * @param split 分隔符
     * @param str   被转换的值
     * @return 结果
     */
    public static Integer[] toIntArray(String split, String str) {
        if (StringUtil.isEmpty(str)) {
            return new Integer[]{};
        }
        String[] arr = str.split(split);
        final Integer[] ints = new Integer[arr.length];
        for (int i = 0; i < arr.length; i++) {
            final int v = toInt(arr[i], 0);
            ints[i] = v;
        }
        return ints;
    }

    /**
     * 转换为Integer集合<br>
     *
     * @param str 结果被转换的值
     * @return 结果
     */
    public static List<Integer> toIntList(String str) {
        return new ArrayList<>(Arrays.asList(toIntArray(str)));
    }

    /**
     * 转换为Integer集合<br>
     *
     * @param split 分隔符
     * @param str   被转换的值
     * @return 结果
     */
    public static List<Integer> toIntList(String split, String str) {
        return new ArrayList<>(Arrays.asList(toIntArray(split, str)));
    }

    /**
     * 转换为Long数组<br>
     *
     * @param str 被转换的值
     * @return 结果
     */
    public static Long[] toLongArray(String str) {
        return toLongArray(StringPool.COMMA, str);
    }

    /**
     * 转换为Long数组<br>
     *
     * @param split 分隔符
     * @param str   被转换的值
     * @return 结果
     */
    public static Long[] toLongArray(String split, String str) {
        if (StringUtil.isEmpty(str)) {
            return new Long[]{};
        }
        String[] arr = str.split(split);
        final Long[] longs = new Long[arr.length];
        for (int i = 0; i < arr.length; i++) {
            final Long v = toLong(arr[i], 0);
            longs[i] = v;
        }
        return longs;
    }

    /**
     * 转换为Long集合<br>
     *
     * @param str 结果被转换的值
     * @return 结果
     */
    public static List<Long> toLongList(String str) {
        return new ArrayList<>(Arrays.asList(toLongArray(str)));
    }

    /**
     * 转换为Long集合<br>
     *
     * @param split 分隔符
     * @param str   被转换的值
     * @return 结果
     */
    public static List<Long> toLongList(String split, String str) {
        return new ArrayList<>(Arrays.asList(toLongArray(split, str)));
    }

    /**
     * 转换为String数组<br>
     *
     * @param str 被转换的值
     * @return 结果
     */
    public static String[] toStrArray(String str) {
        return toStrArray(StringPool.COMMA, str);
    }

    /**
     * 转换为String数组<br>
     *
     * @param split 分隔符
     * @param str   被转换的值
     * @return 结果
     */
    public static String[] toStrArray(String split, String str) {
        if (isBlank(str)) {
            return new String[]{};
        }
        return str.split(split);
    }

    /**
     * 转换为String集合<br>
     *
     * @param str 结果被转换的值
     * @return 结果
     */
    public static List<String> toStrList(String str) {
        return new ArrayList<>(Arrays.asList(toStrArray(str)));
    }

    /**
     * 转换为String集合<br>
     *
     * @param split 分隔符
     * @param str   被转换的值
     * @return 结果
     */
    public static List<String> toStrList(String split, String str) {
        return new ArrayList<>(Arrays.asList(toStrArray(split, str)));
    }



    /**
     * 转换为BigInteger<br>
     * 如果给定的值为空，或者转换失败，返回默认值<br>
     * 转换失败不会报错
     *
     * @param value 被转换的值
     * @param defaultValue 转换错误时的默认值
     * @return 结果
     */
    public static BigInteger toBigInteger(Object value, BigInteger defaultValue) {
        return Convert.toBigInteger(value,defaultValue);
    }

    /**
     * 转换为BigInteger<br>
     * 如果给定的值为空，或者转换失败，返回默认值<code>null</code><br>
     * 转换失败不会报错
     *
     * @param value 被转换的值
     * @return 结果
     */
    public static BigInteger toBigInteger(Object value){
        return Convert.toBigInteger(value);
    }

    /**
     * 转换为BigDecimal<br>
     * 如果给定的值为空，或者转换失败，返回默认值<br>
     * 转换失败不会报错
     *
     * @param value 被转换的值
     * @param defaultValue 转换错误时的默认值
     * @return 结果
     */
    public static BigDecimal toBigDecimal(Object value, BigDecimal defaultValue) {
        return Convert.toBigDecimal(value,defaultValue);
    }

    /**
     * 转换为BigDecimal<br>
     * 如果给定的值为空，或者转换失败，返回默认值<br>
     * 转换失败不会报错
     *
     * @param value 被转换的值
     * @return 结果
     */
    public static BigDecimal toBigDecimal(Object value) {
        return Convert.toBigDecimal(value);
    }


    /**
     * 将 {@code Collection} 转换为带分隔符的 {@code String}（例如 CSV）。
     * <p>对 {@code toString()} 实现有用。
     *
     * @param coll  the {@code Collection} to convert
     * @param delim the delimiter to use (typically a ",")
     * @return the delimited {@code String}
     */
    public static String join(Collection<?> coll, String delim) {
        return StringUtil.join(coll, delim);
    }

    /**
     * 生成uuid
     *
     * @return UUID
     */
    public static String randomUUID() {
        return UuidUtil.fastUUID().toString(true);
    }

    /**
     * 生成雪花ID
     *
     * @return UUID
     */
    public static long randomSnowFlakeId() {
        return SnowFlakeIdUtil.nextId();
    }

    /**
     * 转义HTML用于安全过滤
     *
     * @param html html
     * @return {String}
     */
    public static String escapeHtml(String html) {
        return EscapeUtil.escapeHtml4(html);
    }

    /**
     * 随机数生成
     *
     * @param count 字符长度
     * @return 随机数
     */
    public static String random(int count) {
        return RandomUtil.random(count);
    }

    /**
     * closeQuietly
     *
     * @param closeable 自动关闭
     */
    public static void closeQuietly(@Nullable Closeable closeable) {
        cn.hutool.core.io.IoUtil.close(closeable);
    }

    /**
     * InputStream 到字符串 utf-8
     *
     * @param input 要从中读取的 <code>InputStream</code>
     * @return 请求的字符串
     */
    public static String toString(InputStream input) {
        return cn.hutool.core.io.IoUtil.readUtf8(input);
    }

    /**
     * 输入流转字符串
     *
     * @param input   要从中读取的 <code>InputStream</code>
     * @param charset <code>字符集</code>
     * @return 请求的字符串
     */
    public static String toString(@Nullable InputStream input, Charset charset) {
        return cn.hutool.core.io.IoUtil.read(input, charset);
    }

    /**
     * 输入流转byte
     *
     * @param input
     * @return byte[]
     **/
    public static byte[] toByteArray(@Nullable InputStream input) {
        return cn.hutool.core.io.IoUtil.readBytes(input);
    }

    /**
     * 将对象装成map形式
     *
     * @param bean 源对象
     * @return {Map}
     */
    public static Map<String, Object> toMap(Object bean) {
        return BeanMap.create(bean);
    }

    /**
     * 将map 转为 bean
     *
     * @param beanMap   map
     * @param valueType 对象类型
     * @param <T>       泛型标记
     * @return {T}
     */
    public static <T> T toBean(Map<String, Object> beanMap, Class<T> valueType) {
        T bean = BeanUtil.newBean(valueType);
        BeanMap.create(bean).putAll(beanMap);
        return bean;
    }

    /**
     * 将List对象装成List<map>形式
     *
     * @param beanList 源对象
     * @return {Map}
     */
    public static <T> List<Map<String, Object>> toListMap(List<? extends Serializable> beanList){
        List<Map<String, Object>> mapList = new ArrayList<>();
        for (int i = 0, n = beanList.size(); i < n; i++){
            Object bean = beanList.get(i);
            Map<String, Object> map = toMap(bean);
            mapList.add(map);
        }
        return mapList;
    }

}
