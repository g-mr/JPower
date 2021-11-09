package com.wlcb.jpower.module.common.utils;

import cn.hutool.core.codec.Base64Decoder;
import cn.hutool.core.codec.Base64Encoder;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.EscapeUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.URLUtil;
import com.wlcb.jpower.module.common.enums.RandomType;
import com.wlcb.jpower.module.common.utils.constants.CharsetKit;
import com.wlcb.jpower.module.common.utils.constants.StringPool;
import org.apache.tomcat.util.http.ConcurrentDateFormat;
import org.springframework.beans.BeansException;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;

import java.io.Closeable;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.time.Duration;
import java.time.temporal.Temporal;
import java.util.*;

/**
 * 工具包集合，只做简单的调用，不删除原有工具类
 */
public class Fc {

    public static String trim(String str){
        return StringUtil.trim(str);
    }

    /**
     * 检查指定的对象引用不是 {@code null} 和
     * 如果是，则抛出自定义的 {@link NullPointerException}。
     * 这种方法主要用于在方法中进行参数验证和具有多个参数的构造函数
     * @param obj     the object reference to check for nullity
     * @param message detail message to be used in the event that a {@code
     *                NullPointerException} is thrown
     * @param <T>     the type of the reference
     * @return {@code obj} if not {@code null}
     * @throws NullPointerException if {@code obj} is {@code null}
     */
    public static <T> T requireNotNull(T obj, String message) {
        return Objects.requireNonNull(obj, message);
    }

    /**
     * 是否是空对象
     * @Author mr.g
     * @param obj
     * @return boolean
     **/
    public static boolean isNull(@Nullable Object obj) {
        return Objects.isNull(obj);
    }

    /**
     * 不是空对象
     * @Author mr.g
     * @param obj
     * @return boolean
     **/
    public static boolean notNull(@Nullable Object obj) {
        return Objects.nonNull(obj);
    }

    /**
     * 首字母变小写
     *
     * @param str 字符串
     * @return {String}
     */
    public static String lowerFirst(String str) {
        return StringUtil.lowerFirst(str);
    }

    /**
     * 首字母变大写
     *
     * @param str 字符串
     * @return {String}
     */
    public static String upperFirst(String str) {
        return StringUtil.upperFirst(str);
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
     * @param str 被检测的字符串
     * @return 是否为非空
     */
    public static boolean isNotBlank(@Nullable final CharSequence str) {
        return StringUtil.isNotBlank(str);
    }

    /**
     * 有 任意 一个 Blank
     *
     * @param cs CharSequence
     * @return boolean
     */
    public static boolean isAnyBlank(final CharSequence... cs) {
        return StringUtil.isAnyBlank(cs);
    }

    /**
     * 是否全非 Blank
     *
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
     * @param obj 检查对象
     * @return 是否数组
     */
    public static boolean isArray(@Nullable Object obj) {
        return ObjectUtil.isArray(obj);
    }

    /**
     * 判断给定对象是否为空:
     * 即 {@code null} 或零长度.
     *
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
     * @param obj 检查对象
     * @return 是否不为空
     */
    public static boolean isNotEmpty(@Nullable Object obj) {
        return !ObjectUtil.isEmpty(obj);
    }

    /**
     * 判断给定数组是否为空：
     * 即 {@code null} 或零长度。
     *
     * @param array 检查对象
     * @return 数组是否为空
     */
    public static boolean isEmpty(@Nullable Object[] array) {
        return ObjectUtil.isEmpty(array);
    }

    /**
     * 判断数组不为空
     *
     * @param array 数组
     * @return 数组是否不为空
     */
    public static boolean isNotEmpty(@Nullable Object[] array) {
        return ObjectUtil.isNotEmpty(array);
    }

    /**
     * 对象组中是否存在 Empty Object
     *
     * @param os 对象组
     * @return boolean
     */
    public static boolean hasEmpty(Object... os) {
        for (Object o : os) {
            if (isEmpty(o)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 对象组中是否全是 Empty Object
     *
     * @param os 对象组
     * @return boolean
     */
    public static boolean allEmpty(Object... os) {
        for (Object o : os) {
            if (!isEmpty(o)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 比较两个对象是否相等。<br>
     * 相同的条件有两个，满足其一即可：<br>
     *
     * @param obj1 对象1
     * @param obj2 对象2
     * @return 是否相等
     */
    public static boolean equals(Object obj1, Object obj2) {
        return Objects.equals(obj1, obj2);
    }

    /**
     * 确定给定的对象是否相等，如果返回 {@code true}
     * 如果只有一个是 {@code null}，两者都是 {@code null} 或 {@code false}。
     * <p>将数组与 {@code Arrays.equals} 进行比较，执行相等
     * 基于数组元素而不是数组引用进行检查。
     *
     * @param o1 要比较的第一个对象
     * @param o2 要比较的第二个对象
     * @return 给定的对象是否相等
     * @see Object#equals(Object)
     * @see Arrays#equals
     */
    public static boolean equalsSafe(@Nullable Object o1, @Nullable Object o2) {
        return ObjectUtil.nullSafeEquals(o1, o2);
    }

    /**
     * 判断俩个类型的值是否相等
     * @param o1 要比较的第一个对象
     * @param o2 要比较的第二个对象
     * @return 给定的对象值是否相等
     **/
    public static boolean equalsValue(@Nullable Object o1, @Nullable Object o2) {
        return ObjectUtil.equalsValue(o1, o2);
    }

    /**
     * 判断给定的数组是否包含指定的元素
     *
     * @param array   要检查的数组
     * @param element 要查找的元素
     * @param <T>     通用标签
     * @return {@code true} if found, {@code false} else
     */
    public static <T> boolean contains(@Nullable T[] array, final T element) {
        return CollectionUtil.contains(array, element);
    }

    /**
     * 检查给定的迭代器是否包含给定的元素。
     *
     * @param iterator   要检查的集合
     * @param element 要查找的元素
     * @return {@code true} if found, {@code false} otherwise
     */
    public static boolean contains(@Nullable Iterator<?> iterator, Object element) {
        return CollectionUtil.contains(iterator, element);
    }

    /**
     * 检查给定的 Enumeration 是否包含给定的元素。
     *
     * @param enumeration 要检查的枚举
     * @param element     要查找的元素
     * @return {@code true} if found, {@code false} otherwise
     */
    public static boolean contains(@Nullable Enumeration<?> enumeration, Object element) {
        return CollectionUtil.contains(enumeration, element);
    }

    /**
     * 强转string
     *
     * @param str 字符串
     * @return String
     */
    public static String toStr(Object str) {
        return toStr(str, "");
    }

    /**
     * 强转string,并去掉多余空格
     *
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
     * @param str 要检查的 str，可能为 null
     * @return {boolean}
     */
    public static boolean isNumeric(final String str) {
        return NumberUtil.isInteger(str);
    }

    /**
     * 转换成数字
     */
    public static int toInt(final Object value) {
        return Convert.toInt(value,-1);
    }

    /**
     * 转换成数字
     */
    public static int toInt(final Object value, final int defaultValue) {
        return Convert.toInt(value,defaultValue);
    }

    /**
     * 转换成Long
     */
    public static long toLong(final Object value) {
        return Convert.toLong(value);
    }

    /**
     * 转换成Long
     */
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
            final Integer v = toInt(arr[i], 0);
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
     * @param coll the {@code Collection} to convert
     * @return the delimited {@code String}
     */
    public static String join(Collection<?> coll) {
        return StringUtil.join(coll);
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
     * 将 {@code String} 数组转换为逗号分隔的 {@code String}
     * <p>对 {@code toString()} 实现有用。
     *
     * @param arr the array to display
     * @return the delimited {@code String}
     */
    public static String join(Object[] arr) {
        return StringUtil.join(arr);
    }

    /**
     * 将 {@code String} 数组转换为带分隔符的 {@code String}（例如 CSV）。
     * <p>对 {@code toString()} 实现有用。
     *
     * @param arr   the array to display
     * @param delim the delimiter to use (typically a ",")
     * @return the delimited {@code String}
     */
    public static String join(Object[] arr, String delim) {
        return StringUtil.join(arr, delim);
    }

    /**
     * 生成uuid
     *
     * @return UUID
     */
    public static String randomUUID() {
        return UUIDUtil.getUUID();
    }

    /**
     * 生成雪花ID
     *
     * @return UUID
     */
    public static String randomSnowFlakeId() {
        return Fc.toStr(SnowFlakeIdUtil.getInstance().nextId());
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
     * 随机数生成
     *
     * @param count      字符长度
     * @param randomType 随机数类别
     * @return 随机数
     */
    public static String random(int count, RandomType randomType) {
        if (RandomType.INT == randomType) {
            return RandomUtil.randomNumbers(count);
        }else if (RandomType.STRING == randomType){
            return RandomUtil.randomString(count);
        }else {
            return RandomUtil.random(count);
        }
    }

    /**
     * 计算MD5摘要，并以32个字符的十六进制字符串形式返回值。
     *
     * @param data Data to digest
     * @return MD5 digest as a hexg string
     */
    public static String md5(final String data) {
        return MD5.parseStrToMd5L32(data);
    }

    /**
     * Return a hexadecimal string representation of the MD5 digest of the given bytes.
     *
     * @param bytes the bytes to calculate the digest over
     * @return a hexadecimal digest string
     */
    public static String md5(final byte[] bytes) {
        return MD5.md5Hex(bytes);
    }

    /**
     * 自定义加密 先MD5再SHA1
     *
     * @param data 字符串
     * @return String
     */
    public static String encrypt(String data) {
        return DigestUtil.encrypt(data);
    }

    /**
     * 编码
     *
     * @param value 字符串
     * @return {String}
     */
    public static String encodeBase64(String value) {
        return Base64Encoder.encode(value);
    }

    /**
     * 编码
     *
     * @param value   字符串
     * @param charset 字符集
     * @return {String}
     */
    public static String encodeBase64(String value, Charset charset) {
        return Base64Encoder.encode(value, charset);
    }

    /**
     * 编码URL安全
     *
     * @param value 字符串
     * @return {String}
     */
    public static String encodeBase64UrlSafe(String value) {
        return Base64Encoder.encodeUrlSafe(value);
    }

    /**
     * 编码URL安全
     *
     * @param value   字符串
     * @param charset 字符集
     * @return {String}
     */
    public static String encodeBase64UrlSafe(String value, Charset charset) {
        return Base64Encoder.encodeUrlSafe(value, charset);
    }

    /**
     * 解码
     *
     * @param value 字符串
     * @return {String}
     */
    public static String decodeBase64(String value) {
        return Base64Decoder.decodeStr(value);
    }

    /**
     * 解码
     *
     * @param value   字符串
     * @param charset 字符集
     * @return {String}
     */
    public static String decodeBase64(String value, Charset charset) {
        return Base64Decoder.decodeStr(value, charset);
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
     * 对所有非法或保留的字符进行编码
     * 意思是，在 URI 中的任何地方，如定义
     * <a href="https://tools.ietf.org/html/rfc3986">RFC 3986</a>.
     * 这有助于确保给定的字符串按原样保留
     * 并且不会对 URI 的结构或含义产生任何影响。
     *
     * @param source 要编码的字符串
     * @return 编码的字符串
     */
    public static String encode(String source) {
        return URLUtil.encode(source, CharsetKit.CHARSET_UTF_8);
    }

    /**
     * 对所有非法或保留的字符进行编码
     * 意思是，在 URI 中的任何地方，如定义
     * <a href="https://tools.ietf.org/html/rfc3986">RFC 3986</a>.
     * 这有助于确保给定的字符串按原样保留
     * 并且不会对 URI 的结构或含义产生任何影响。
     *
     * @param source  要编码的字符串
     * @param charset 要编码的字符编码
     * @return 编码的字符串
     */
    public static String encode(String source, Charset charset) {
        return URLUtil.encode(source, charset);
    }

    /**
     * 解码给定的编码 URI 组件。
     * <p>See {@link StringUtils#uriDecode(String, Charset)} for the decoding rules.
     *
     * @param source 编码的字符串
     * @return 解码后的值
     */
    public static String decode(String source) {
        return StringUtils.uriDecode(source, CharsetKit.CHARSET_UTF_8);
    }

    /**
     * 解码给定的编码 URI 组件。
     * <p>See {@link StringUtils#uriDecode(String, Charset)} for the decoding rules.
     *
     * @param source  编码的字符串
     * @param charset 要使用的字符编码
     * @return 解码后的值
     */
    public static String decode(String source, Charset charset) {
        return StringUtils.uriDecode(source, charset);
    }

    /**
     * 日期时间格式化
     *
     * @param date 时间
     * @return 格式化后的时间
     */
    public static String formatDateTime(Date date) {
        return DateUtil.getDate(date, DateUtil.PATTERN_DATETIME);
    }

    /**
     * 日期格式化
     *
     * @param date 时间
     * @return 格式化后的时间
     */
    public static String formatDate(Date date) {
        return DateUtil.getDate(date,DateUtil.PATTERN_DATE);
    }

    /**
     * 时间格式化
     *
     * @param date 时间
     * @return 格式化后的时间
     */
    public static String formatTime(Date date) {
        return DateUtil.getDate(date,DateUtil.PATTERN_TIME);
    }

    /**
     * 日期格式化
     *
     * @param date    时间
     * @param pattern 表达式
     * @return 格式化后的时间
     */
    public static String format(Date date, String pattern) {
        return DateUtil.getDate(date, pattern);
    }

    /**
     * 将字符串转换为时间
     *
     * @param dateStr 时间字符串
     * @return 时间
     */
    public static Date parseDate(String dateStr) {
        return DateUtil.parseDate(dateStr);
    }

    /**
     * 将字符串转换为时间
     *
     * @param dateStr 时间字符串
     * @param format  ConcurrentDateFormat
     * @return 时间
     */
    public static Date parse(String dateStr, ConcurrentDateFormat format) {
        try {
            return format.parse(dateStr);
        } catch (ParseException e) {
            throw ExceptionsUtil.unchecked(e);
        }
    }

    /**
     * 时间比较
     *
     * @param startInclusive the start instant, inclusive, not null
     * @param endExclusive   the end instant, exclusive, not null
     * @return a {@code Duration}, not null
     */
    public static Duration between(Temporal startInclusive, Temporal endExclusive) {
        return Duration.between(startInclusive, endExclusive);
    }

    /**
     * 获取Annotation
     *      Spring方法获取
     * @param annotatedElement AnnotatedElement
     * @param annotationType   注解类
     * @param <A>              泛型标记
     * @return {Annotation}
     */
    @Nullable
    public static <A extends Annotation> A getAnnotation(AnnotatedElement annotatedElement, Class<A> annotationType) {
        return AnnotatedElementUtils.findMergedAnnotation(annotatedElement, annotationType);
    }

    /**
     * 获取Annotation
     *      Spring方法获取
     * @param method         Method
     * @param annotationType 注解类
     * @param <A>            泛型标记
     * @return {Annotation}
     */
    @Nullable
    public static <A extends Annotation> A getAnnotation(Method method, Class<A> annotationType) {
        return ClassUtil.getAnnotation(method, annotationType);
    }

    /**
     * 获取Annotation
     *      Spring方法获取
     * @param handlerMethod  HandlerMethod
     * @param annotationType 注解类
     * @param <A>            泛型标记
     * @return {Annotation}
     */
    @Nullable
    public static <A extends Annotation> A getAnnotation(HandlerMethod handlerMethod, Class<A> annotationType) {
        return ClassUtil.getAnnotation(handlerMethod, annotationType);
    }

    /**
     * 实例化对象
     *
     * @param clazz 类
     * @param <T>   泛型标记
     * @return 对象
     */
    public static <T> T newInstance(Class<?> clazz) {
        return BeanUtil.newInstance(clazz);
    }

    /**
     * 实例化对象
     *
     * @param clazzStr 类名
     * @param <T>      泛型标记
     * @return 对象
     */
    public static <T> T newInstance(String clazzStr) {
        return BeanUtil.newInstance(clazzStr);
    }

    /**
     * 获取Bean的属性
     *
     * @param bean         bean
     * @param propertyName 属性名
     * @return 属性值
     */
    public static Object getProperty(Object bean, String propertyName) {
        return BeanUtil.getProperty(bean, propertyName);
    }

    /**
     * 设置Bean属性
     *
     * @param bean         bean
     * @param propertyName 属性名
     * @param value        属性值
     */
    public static void setProperty(Object bean, String propertyName, Object value) {
        BeanUtil.setProperty(bean, propertyName, value);
    }

    /**
     * 深复制
     * <p>
     * 注意：不支持链式Bean
     *
     * @param source 源对象
     * @param <T>    泛型标记
     * @return T
     */
    public static <T> T clone(T source) {
        return (T) BeanUtil.copyProperties(source, source.getClass());
    }

    /**
     * copy 对象属性到另一个对象，默认不使用Convert
     * <p>
     * 注意：不支持链式Bean，链式用 copyProperties
     *
     * @param source 源对象
     * @param clazz  类名
     * @param <T>    泛型标记
     * @return T
     */
    public static <T> T copy(Object source, Class<T> clazz) {
        return BeanUtil.copyProperties(source, clazz);
    }

    /**
     * 拷贝对象
     * <p>
     * 注意：不支持链式Bean，链式用 copyProperties
     *
     * @param source     源对象
     * @param targetBean 需要赋值的对象
     */
    public static void copy(Object source, Object targetBean) {
        BeanUtil.copyProperties(source, targetBean);
    }

    /**
     * 将给定源 bean 的属性值复制到目标类中。
     * <p>注意：源类和目标类不必匹配甚至派生
     * 彼此之间，只要属性匹配。任何 bean 属性
     * 源 bean 公开但目标 bean 不会被忽略。
     * <p>这只是一种方便的方法。对于更复杂的传输需求，
     *
     * @param source 源
     * @param target  目标 bean 类
     * @param <T>    泛型标记
     * @return T
     */
    public static <T> T copyProperties(Object source, Class<T> target) throws BeansException {
        T to = newInstance(target);
        BeanUtil.copyProperties(source, to);
        return to;
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
        T bean = BeanUtil.newInstance(valueType);
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
