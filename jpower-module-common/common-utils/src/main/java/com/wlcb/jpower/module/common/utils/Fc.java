/**
 * Copyright (c) 2018-2028, DreamLu 卢春梦 (qq596392912@gmail.com).
 * <p>
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE 3.0;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/lgpl.html
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wlcb.jpower.module.common.utils;

import cn.hutool.core.codec.Base64Decoder;
import cn.hutool.core.codec.Base64Encoder;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.URLUtil;
import com.wlcb.jpower.module.common.enums.RandomType;
import com.wlcb.jpower.module.common.utils.constants.CharsetKit;
import org.apache.tomcat.util.http.ConcurrentDateFormat;
import org.springframework.beans.BeansException;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;

import java.io.Closeable;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.time.Duration;
import java.time.temporal.Temporal;
import java.util.*;
import java.util.function.Supplier;

/**
 * 工具包集合，只做简单的调用，不删除原有工具类
 *
 * @author L.cm
 */
public class Fc {

    public static String trim(String str){
        return StringUtil.trim(str);
    }

    /**
     * Checks that the specified object reference is not {@code null}. This
     * method is designed primarily for doing parameter validation in methods
     * and constructors
     * @param obj the object reference to check for nullity
     * @param <T> the type of the reference
     * @return {@code obj} if not {@code null}
     * @throws NullPointerException if {@code obj} is {@code null}
     */
    public static <T> T requireNotNull(T obj) {
        return Objects.requireNonNull(obj);
    }

    /**
     * Checks that the specified object reference is not {@code null} and
     * throws a customized {@link NullPointerException} if it is. This method
     * is designed primarily for doing parameter validation in methods and
     * constructors with multiple parameters
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
     * Checks that the specified object reference is not {@code null} and
     * throws a customized {@link NullPointerException} if it is.
     *
     * @param obj             the object reference to check for nullity
     * @param messageSupplier supplier of the detail message to be
     *                        used in the event that a {@code NullPointerException} is thrown
     * @param <T>             the type of the reference
     * @return {@code obj} if not {@code null}
     * @throws NullPointerException if {@code obj} is {@code null}
     * @since 1.8
     */
    public static <T> T requireNotNull(T obj, Supplier<String> messageSupplier) {
        return Objects.requireNonNull(obj, messageSupplier);
    }

    public static boolean isNull(@Nullable Object obj) {
        return Objects.isNull(obj);
    }

    public static boolean notNull(@Nullable Object obj) {
        return Objects.nonNull(obj);
    }

    /**
     * 首字母变小写
     *
     * @param str 字符串
     * @return {String}
     */
    public static String firstCharToLower(String str) {
        return StringUtil.lowerFirst(str);
    }

    /**
     * 首字母变大写
     *
     * @param str 字符串
     * @return {String}
     */
    public static String firstCharToUpper(String str) {
        return StringUtil.upperFirst(str);
    }

    /**
     * Check whether the given {@code CharSequence} contains actual <em>text</em>.
     * <p>More specifically, this method returns {@code true} if the
     * {@code CharSequence} is not {@code null}, its length is greater than
     * 0, and it contains at least one non-whitespace character.
     * <pre class="code">
     * $.isBlank(null)		= true
     * $.isBlank("")		= true
     * $.isBlank(" ")		= true
     * $.isBlank("12345")	= false
     * $.isBlank(" 12345 ")	= false
     * </pre>
     *
     * @param cs the {@code CharSequence} to check (may be {@code null})
     * @return {@code true} if the {@code CharSequence} is not {@code null},
     * its length is greater than 0, and it does not contain whitespace only
     * @see Character#isWhitespace
     */
    public static boolean isBlank(@Nullable final CharSequence cs) {
        return StringUtil.isBlank(cs);
    }

    /**
     * <p>Checks if a CharSequence is not empty (""), not null and not whitespace only.</p>
     * <pre>
     * $.isNotBlank(null)	= false
     * $.isNotBlank("")		= false
     * $.isNotBlank(" ")	= false
     * $.isNotBlank("bob")	= true
     * $.isNotBlank("  bob  ") = true
     * </pre>
     *
     * @param cs the CharSequence to check, may be null
     * @return {@code true} if the CharSequence is
     * not empty and not null and not whitespace
     * @see Character#isWhitespace
     */
    public static boolean isNotBlank(@Nullable final CharSequence cs) {
        return StringUtil.isNotBlank(cs);
    }

    /**
     * 有 任意 一个 Blank
     *
     * @param css CharSequence
     * @return boolean
     */
    public static boolean isAnyBlank(final CharSequence... css) {
        return StringUtil.isAnyBlank(css);
    }

    /**
     * 是否全非 Blank
     *
     * @param css CharSequence
     * @return boolean
     */
    public static boolean isNoneBlank(final CharSequence... css) {
        return StringUtil.isNoneBlank(css);
    }

    /**
     * Determine whether the given object is an array:
     * either an Object array or a primitive array.
     *
     * @param obj the object to check
     * @return 是否数组
     */
    public static boolean isArray(@Nullable Object obj) {
        return ObjectUtil.isArray(obj);
    }

    /**
     * Determine whether the given object is empty:
     * i.e. {@code null} or of zero length.
     *
     * @param obj the object to check
     * @return 数组是否为空
     */
    public static boolean isEmpty(@Nullable Object obj) {
        return ObjectUtil.isEmpty(obj);
    }

    /**
     * Determine whether the given object is not empty:
     * i.e. {@code null} or of zero length.
     *
     * @param obj the object to check
     * @return 是否不为空
     */
    public static boolean isNotEmpty(@Nullable Object obj) {
        return !ObjectUtil.isEmpty(obj);
    }

    /**
     * Determine whether the given array is empty:
     * i.e. {@code null} or of zero length.
     *
     * @param array the array to check
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
     * Determine if the given objects are equal, returning {@code true} if
     * both are {@code null} or {@code false} if only one is {@code null}.
     * <p>Compares arrays with {@code Arrays.equals}, performing an equality
     * check based on the array elements rather than the array reference.
     *
     * @param o1 first Object to compare
     * @param o2 second Object to compare
     * @return whether the given objects are equal
     * @see Object#equals(Object)
     * @see Arrays#equals
     */
    public static boolean equalsSafe(@Nullable Object o1, @Nullable Object o2) {
        return ObjectUtil.nullSafeEquals(o1, o2);
    }

    public static boolean equalsValue(@Nullable Object o1, @Nullable Object o2) {
        return ObjectUtil.equalsValue(o1, o2);
    }

    /**
     * Check whether the given Array contains the given element.
     *
     * @param array   the Array to check
     * @param element the element to look for
     * @param <T>     The generic tag
     * @return {@code true} if found, {@code false} else
     */
    public static <T> boolean contains(@Nullable T[] array, final T element) {
        return CollectionUtil.contains(array, element);
    }

    /**
     * Check whether the given Iterator contains the given element.
     *
     * @param iterator the Iterator to check
     * @param element  the element to look for
     * @return {@code true} if found, {@code false} otherwise
     */
    public static boolean contains(@Nullable Iterator<?> iterator, Object element) {
        return CollectionUtil.contains(iterator, element);
    }

    /**
     * Check whether the given Enumeration contains the given element.
     *
     * @param enumeration the Enumeration to check
     * @param element     the element to look for
     * @return {@code true} if found, {@code false} otherwise
     */
    public static boolean contains(@Nullable Enumeration<?> enumeration, Object element) {
        return CollectionUtil.contains(enumeration, element);
    }

    /**
     * 强转string,并去掉多余空格
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
     * @param cs the CharSequence to check, may be null
     * @return {boolean}
     */
    public static boolean isNumeric(final CharSequence cs) {
        return StringUtil.isNumeric(cs);
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
        return toLongArray(",", str);
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
        return Arrays.asList(toLongArray(str));
    }

    /**
     * 转换为Long集合<br>
     *
     * @param split 分隔符
     * @param str   被转换的值
     * @return 结果
     */
    public static List<Long> toLongList(String split, String str) {
        return Arrays.asList(toLongArray(split, str));
    }

    /**
     * 转换为String数组<br>
     *
     * @param str 被转换的值
     * @return 结果
     */
    public static String[] toStrArray(String str) {
        return toStrArray(",", str);
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
        return Arrays.asList(toStrArray(str));
    }

    /**
     * 转换为String集合<br>
     *
     * @param split 分隔符
     * @param str   被转换的值
     * @return 结果
     */
    public static List<String> toStrList(String split, String str) {
        return Arrays.asList(toStrArray(split, str));
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
     * Convert a {@code Collection} into a delimited {@code String} (e.g., CSV).
     * <p>Useful for {@code toString()} implementations.
     *
     * @param coll the {@code Collection} to convert
     * @return the delimited {@code String}
     */
    public static String join(Collection<?> coll) {
        return StringUtil.join(coll);
    }

    /**
     * Convert a {@code Collection} into a delimited {@code String} (e.g. CSV).
     * <p>Useful for {@code toString()} implementations.
     *
     * @param coll  the {@code Collection} to convert
     * @param delim the delimiter to use (typically a ",")
     * @return the delimited {@code String}
     */
    public static String join(Collection<?> coll, String delim) {
        return StringUtil.join(coll, delim);
    }

    /**
     * Convert a {@code String} array into a comma delimited {@code String}
     * (i.e., CSV).
     * <p>Useful for {@code toString()} implementations.
     *
     * @param arr the array to display
     * @return the delimited {@code String}
     */
    public static String join(Object[] arr) {
        return StringUtil.join(arr);
    }

    /**
     * Convert a {@code String} array into a delimited {@code String} (e.g. CSV).
     * <p>Useful for {@code toString()} implementations.
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
        return StringUtil.escapeHtml(html);
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
     * InputStream to String utf-8
     *
     * @param input the <code>InputStream</code> to read from
     * @return the requested String
     * @throws NullPointerException if the input is null
     */
    public static String toString(InputStream input) {
        return cn.hutool.core.io.IoUtil.readUtf8(input);
    }

    /**
     * InputStream to String
     *
     * @param input   the <code>InputStream</code> to read from
     * @param charset the <code>Charsets</code>
     * @return the requested String
     * @throws NullPointerException if the input is null
     */
    public static String toString(@Nullable InputStream input, Charset charset) {
        return cn.hutool.core.io.IoUtil.read(input, charset);
    }

    public static byte[] toByteArray(@Nullable InputStream input) {
        return cn.hutool.core.io.IoUtil.readBytes(input);
    }


    /**
     * Encode all characters that are either illegal, or have any reserved
     * meaning, anywhere within a URI, as defined in
     * <a href="https://tools.ietf.org/html/rfc3986">RFC 3986</a>.
     * This is useful to ensure that the given String will be preserved as-is
     * and will not have any o impact on the structure or meaning of the URI.
     *
     * @param source the String to be encoded
     * @return the encoded String
     */
    public static String encode(String source) {
        return URLUtil.encode(source, CharsetKit.CHARSET_UTF_8);
    }

    /**
     * Encode all characters that are either illegal, or have any reserved
     * meaning, anywhere within a URI, as defined in
     * <a href="https://tools.ietf.org/html/rfc3986">RFC 3986</a>.
     * This is useful to ensure that the given String will be preserved as-is
     * and will not have any o impact on the structure or meaning of the URI.
     *
     * @param source  the String to be encoded
     * @param charset the character encoding to encode to
     * @return the encoded String
     */
    public static String encode(String source, Charset charset) {
        return URLUtil.encode(source, charset);
    }

    /**
     * Decode the given encoded URI component.
     * <p>See {@link StringUtils#uriDecode(String, Charset)} for the decoding rules.
     *
     * @param source the encoded String
     * @return the decoded value
     * @throws IllegalArgumentException when the given source contains invalid encoded sequences
     * @see StringUtils#uriDecode(String, Charset)
     * @see java.net.URLDecoder#decode(String, String)
     */
    public static String decode(String source) {
        return StringUtils.uriDecode(source, CharsetKit.CHARSET_UTF_8);
    }

    /**
     * Decode the given encoded URI component.
     * <p>See {@link StringUtils#uriDecode(String, Charset)} for the decoding rules.
     *
     * @param source  the encoded String
     * @param charset the character encoding to use
     * @return the decoded value
     * @throws IllegalArgumentException when the given source contains invalid encoded sequences
     * @see StringUtils#uriDecode(String, Charset)
     * @see java.net.URLDecoder#decode(String, String)
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
     * 获取方法参数信息
     *
     * @param constructor    构造器
     * @param parameterIndex 参数序号
     * @return {MethodParameter}
     */
    public static MethodParameter getMethodParameter(Constructor<?> constructor, int parameterIndex) {
        return ClassUtil.getMethodParameter(constructor, parameterIndex);
    }

    /**
     * 获取方法参数信息
     *
     * @param method         方法
     * @param parameterIndex 参数序号
     * @return {MethodParameter}
     */
    public static MethodParameter getMethodParameter(Method method, int parameterIndex) {
        return ClassUtil.getMethodParameter(method, parameterIndex);
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
     * Copy the property values of the given source bean into the target class.
     * <p>Note: The source and target classes do not have to match or even be derived
     * from each other, as long as the properties match. Any bean properties that the
     * source bean exposes but the target bean does not will silently be ignored.
     * <p>This is just a convenience method. For more complex transfer needs,
     *
     * @param source the source bean
     * @param target  the target bean class
     * @param <T>    泛型标记
     * @return T
     * @throws BeansException if the copying failed
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
