package com.wlcb.jpower.module.common.utils;

import cn.hutool.core.util.StrUtil;
import com.wlcb.jpower.module.common.support.StrFormatter;
import com.wlcb.jpower.module.common.support.StrSpliter;
import com.wlcb.jpower.module.common.utils.constants.CharPool;
import com.wlcb.jpower.module.common.utils.constants.StringPool;
import org.springframework.web.util.HtmlUtils;

import java.io.StringReader;
import java.io.StringWriter;
import java.text.MessageFormat;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 字符串工具类
 */
public class StringUtil extends StrUtil {

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


    public static final int INDEX_NOT_FOUND = -1;

    /**
     * 有 任意 一个 Blank
     *
     * @param css CharSequence
     * @return boolean
     */
    public static boolean isAnyBlank(final CharSequence... css) {
        if (ObjectUtil.isEmpty(css)) {
            return true;
        }
        return Stream.of(css).anyMatch(StringUtil::isBlank);
    }

    /**
     * 是否全非 Blank
     *
     * @param css CharSequence
     * @return boolean
     */
    public static boolean isNoneBlank(final CharSequence... css) {
        if (ObjectUtil.isEmpty(css)) {
            return false;
        }
        return Stream.of(css).allMatch(StringUtil::isNotBlank);
    }

    /**
     * 判断一个字符串是否是数字
     *
     * @param cs the CharSequence to check, may be null
     * @return {boolean}
     */
    public static boolean isNumeric(final CharSequence cs) {
        if (isBlank(cs)) {
            return false;
        }
        for (int i = cs.length(); --i >= 0; ) {
            int chr = cs.charAt(i);
            if (chr < 48 || chr > 57) {
                return false;
            }
        }
        return true;
    }

    /**
     * 将 {@code Collection} 转换为带分隔符(,)的 {@code String}
     */
    public static String join(Collection<?> coll) {
        return join(StringPool.COMMA, coll);
    }

    /**
     * 将 {@code Collection} 转换为带分隔符的 {@code String}
     */
    public static String join(Collection<?> coll, String delim) {
        return join(delim, coll);
    }

    /**
     * 将 {@code String} 数组转换为逗号分隔的数组
     */
    public static String join(Object[] arr) {
        return join(StringPool.COMMA, arr);
    }

    /**
     * 将 {@code String} 数组转换为带分隔符的 {@code String}
     */
    public static String join(Object[] arr, String delim) {
        return join(delim, arr);
    }

    /**
     * 转义HTML用于安全过滤
     *
     * @param html html
     * @return {String}
     */
    public static String escapeHtml(String html) {
        return StringUtil.isBlank(html) ? StringPool.EMPTY : HtmlUtils.htmlEscape(html);
    }

    /**
     * 清理字符串，清理出某些不可见字符
     *
     * @param txt 字符串
     * @return {String}
     */
    public static String cleanChars(String txt) {
        return txt.replaceAll("[ 　`·•�\\f\\t\\v\\s]", "");
    }

    /**
     * 格式化文本, {} 表示占位符<br>
     * 此方法只是简单将占位符 {} 按照顺序替换为参数<br>
     * 如果想输出 {} 使用 \\转义 { 即可，如果想输出 {} 之前的 \ 使用双转义符 \\\\ 即可<br>
     * 例：<br>
     * 通常使用：format("this is {} for {}", "a", "b") =》 this is a for b<br>
     * 转义{}： format("this is \\{} for {}", "a", "b") =》 this is \{} for a<br>
     * 转义\： format("this is \\\\{} for {}", "a", "b") =》 this is \a for b<br>
     *
     * @param template 文本模板，被替换的部分用 {} 表示
     * @param params   参数值
     * @return 格式化后的文本
     */
    public static String format(CharSequence template, Object... params) {
        if (null == template) {
            return null;
        }
        if (Fc.isEmpty(params) || isBlank(template)) {
            return template.toString();
        }
        return StrFormatter.format(template.toString(), params);
    }

    /**
     * 有序的格式化文本，使用{number}做为占位符<br>
     * 例：<br>
     * 通常使用：format("this is {0} for {1}", "a", "b") =》 this is a for b<br>
     *
     * @param pattern   文本格式
     * @param arguments 参数
     * @return 格式化后的文本
     */
    public static String indexedFormat(CharSequence pattern, Object... arguments) {
        return MessageFormat.format(pattern.toString(), arguments);
    }

    /**
     * 格式化文本，使用 {varName} 占位<br>
     * map = {a: "aValue", b: "bValue"} format("{a} and {b}", map) ---=》 aValue and bValue
     *
     * @param template 文本模板，被替换的部分用 {key} 表示
     * @param map      参数值对
     * @return 格式化后的文本
     */
    public static String format(CharSequence template, Map<?, ?> map) {
        if (null == template) {
            return null;
        }
        if (null == map || map.isEmpty()) {
            return template.toString();
        }

        String template2 = template.toString();
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            template2 = template2.replace("{" + entry.getKey() + "}", Fc.toStr(entry.getValue()));
        }
        return template2;
    }

    /**
     * 切分字符串，不去除切分后每个元素两边的空白符，不去除空白项
     *
     * @param str       被切分的字符串
     * @param separator 分隔符字符
     * @param limit     限制分片数，-1不限制
     * @return 切分后的集合
     */
    public static List<String> split(CharSequence str, char separator, int limit) {
        return split(str, separator, limit, false, false);
    }

    /**
     * 切分字符串，去除切分后每个元素两边的空白符，去除空白项
     *
     * @param str       被切分的字符串
     * @param separator 分隔符字符
     * @return 切分后的集合
     * @since 3.1.2
     */
    public static List<String> splitTrim(CharSequence str, char separator) {
        return splitTrim(str, separator, -1);
    }

    /**
     * 切分字符串，去除切分后每个元素两边的空白符，去除空白项
     *
     * @param str       被切分的字符串
     * @param separator 分隔符字符
     * @return 切分后的集合
     * @since 3.2.0
     */
    public static List<String> splitTrim(CharSequence str, CharSequence separator) {
        return splitTrim(str, separator, -1);
    }

    /**
     * 切分字符串，去除切分后每个元素两边的空白符，去除空白项
     *
     * @param str       被切分的字符串
     * @param separator 分隔符字符
     * @param limit     限制分片数，-1不限制
     * @return 切分后的集合
     * @since 3.1.0
     */
    public static List<String> splitTrim(CharSequence str, char separator, int limit) {
        return split(str, separator, limit, true, true);
    }

    /**
     * 切分字符串，去除切分后每个元素两边的空白符，去除空白项
     *
     * @param str       被切分的字符串
     * @param separator 分隔符字符
     * @param limit     限制分片数，-1不限制
     * @return 切分后的集合
     * @since 3.2.0
     */
    public static List<String> splitTrim(CharSequence str, CharSequence separator, int limit) {
        return split(str, separator, limit, true, true);
    }

    /**
     * 切分字符串，不限制分片数量
     *
     * @param str         被切分的字符串
     * @param separator   分隔符字符
     * @param isTrim      是否去除切分字符串后每个元素两边的空格
     * @param ignoreEmpty 是否忽略空串
     * @return 切分后的集合
     * @since 3.0.8
     */
    public static List<String> split(CharSequence str, char separator, boolean isTrim, boolean ignoreEmpty) {
        return split(str, separator, 0, isTrim, ignoreEmpty);
    }

    /**
     * 切分字符串
     *
     * @param str         被切分的字符串
     * @param separator   分隔符字符
     * @param limit       限制分片数，-1不限制
     * @param isTrim      是否去除切分字符串后每个元素两边的空格
     * @param ignoreEmpty 是否忽略空串
     * @return 切分后的集合
     * @since 3.0.8
     */
    public static List<String> split(CharSequence str, char separator, int limit, boolean isTrim, boolean ignoreEmpty) {
        if (null == str) {
            return new ArrayList<>(0);
        }
        return StrSpliter.split(str.toString(), separator, limit, isTrim, ignoreEmpty);
    }

    /**
     * 切分字符串
     *
     * @param str         被切分的字符串
     * @param separator   分隔符字符
     * @param limit       限制分片数，-1不限制
     * @param isTrim      是否去除切分字符串后每个元素两边的空格
     * @param ignoreEmpty 是否忽略空串
     * @return 切分后的集合
     * @since 3.2.0
     */
    public static List<String> split(CharSequence str, CharSequence separator, int limit, boolean isTrim, boolean ignoreEmpty) {
        if (null == str) {
            return new ArrayList<>(0);
        }
        final String separatorStr = (null == separator) ? null : separator.toString();
        return StrSpliter.split(str.toString(), separatorStr, limit, isTrim, ignoreEmpty);
    }

    /**
     * 切分字符串
     *
     * @param str       被切分的字符串
     * @param separator 分隔符
     * @return 字符串
     */
    public static String[] split(CharSequence str, CharSequence separator) {
        if (str == null) {
            return new String[]{};
        }

        final String separatorStr = (null == separator) ? null : separator.toString();
        return StrSpliter.splitToArray(str.toString(), separatorStr, 0, false, false);
    }

    /**
     * 根据给定长度，将给定字符串截取为多个部分
     *
     * @param str 字符串
     * @param len 每一个小节的长度
     * @return 截取后的字符串数组
     * @see StrSpliter#splitByLength(String, int)
     */
    public static String[] split(CharSequence str, int len) {
        if (null == str) {
            return new String[]{};
        }
        return StrSpliter.splitByLength(str.toString(), len);
    }

    /**
     * 指定字符是否在字符串中出现过
     *
     * @param str        字符串
     * @param searchChar 被查找的字符
     * @return 是否包含
     * @since 3.1.2
     */
    public static boolean contains(CharSequence str, char searchChar) {
        return indexOf(str, searchChar) > -1;
    }

    /**
     * 查找指定字符串是否包含指定字符串列表中的任意一个字符串
     *
     * @param str      指定字符串
     * @param testStrs 需要检查的字符串数组
     * @return 是否包含任意一个字符串
     * @since 3.2.0
     */
    public static boolean containsAny(CharSequence str, CharSequence... testStrs) {
        return null != getContainsStr(str, testStrs);
    }

    /**
     * 查找指定字符串是否包含指定字符串列表中的任意一个字符串，如果包含返回找到的第一个字符串
     *
     * @param str      指定字符串
     * @param testStrs 需要检查的字符串数组
     * @return 被包含的第一个字符串
     * @since 3.2.0
     */
    public static String getContainsStr(CharSequence str, CharSequence... testStrs) {
        if (isEmpty(str) || Fc.isEmpty(testStrs)) {
            return null;
        }
        for (CharSequence checkStr : testStrs) {
            if (str.toString().contains(checkStr)) {
                return checkStr.toString();
            }
        }
        return null;
    }

    /**
     * 是否包含特定字符，忽略大小写，如果给定两个参数都为<code>null</code>，返回true
     *
     * @param str     被检测字符串
     * @param testStr 被测试是否包含的字符串
     * @return 是否包含
     */
    public static boolean containsIgnoreCase(CharSequence str, CharSequence testStr) {
        if (null == str) {
            // 如果被监测字符串和
            return null == testStr;
        }
        return str.toString().toLowerCase().contains(testStr.toString().toLowerCase());
    }

    /**
     * 查找指定字符串是否包含指定字符串列表中的任意一个字符串<br>
     * 忽略大小写
     *
     * @param str      指定字符串
     * @param testStrs 需要检查的字符串数组
     * @return 是否包含任意一个字符串
     * @since 3.2.0
     */
    public static boolean containsAnyIgnoreCase(CharSequence str, CharSequence... testStrs) {
        return null != getContainsStrIgnoreCase(str, testStrs);
    }

    /**
     * 查找指定字符串是否包含指定字符串列表中的任意一个字符串，如果包含返回找到的第一个字符串<br>
     * 忽略大小写
     *
     * @param str      指定字符串
     * @param testStrs 需要检查的字符串数组
     * @return 被包含的第一个字符串
     * @since 3.2.0
     */
    public static String getContainsStrIgnoreCase(CharSequence str, CharSequence... testStrs) {
        if (isEmpty(str) || Fc.isEmpty(testStrs)) {
            return null;
        }
        for (CharSequence testStr : testStrs) {
            if (containsIgnoreCase(str, testStr)) {
                return testStr.toString();
            }
        }
        return null;
    }

    /**
     * 改进JDK subString<br>
     * index从0开始计算，最后一个字符为-1<br>
     * 如果from和to位置一样，返回 "" <br>
     * 如果from或to为负数，则按照length从后向前数位置，如果绝对值大于字符串长度，则from归到0，to归到length<br>
     * 如果经过修正的index中from大于to，则互换from和to example: <br>
     * abcdefgh 2 3 =》 c <br>
     * abcdefgh 2 -3 =》 cde <br>
     *
     * @param str       String
     * @param fromIndex 开始的index（包括）
     * @param toIndex   结束的index（不包括）
     * @return 字串
     */
    public static String sub(CharSequence str, int fromIndex, int toIndex) {
        if (isEmpty(str)) {
            return StringPool.EMPTY;
        }
        int len = str.length();

        if (fromIndex < 0) {
            fromIndex = len + fromIndex;
            if (fromIndex < 0) {
                fromIndex = 0;
            }
        } else if (fromIndex > len) {
            fromIndex = len;
        }

        if (toIndex < 0) {
            toIndex = len + toIndex;
            if (toIndex < 0) {
                toIndex = len;
            }
        } else if (toIndex > len) {
            toIndex = len;
        }

        if (toIndex < fromIndex) {
            int tmp = fromIndex;
            fromIndex = toIndex;
            toIndex = tmp;
        }

        if (fromIndex == toIndex) {
            return StringPool.EMPTY;
        }

        return str.toString().substring(fromIndex, toIndex);
    }


    /**
     * 截取分隔字符串之前的字符串，不包括分隔字符串<br>
     * 如果给定的字符串为空串（null或""）或者分隔字符串为null，返回原字符串<br>
     * 如果分隔字符串为空串""，则返回空串，如果分隔字符串未找到，返回原字符串
     * <p>
     * 栗子：
     *
     * <pre>
     * StringUtil.subBefore(null, *)      = null
     * StringUtil.subBefore("", *)        = ""
     * StringUtil.subBefore("abc", "a")   = ""
     * StringUtil.subBefore("abcba", "b") = "a"
     * StringUtil.subBefore("abc", "c")   = "ab"
     * StringUtil.subBefore("abc", "d")   = "abc"
     * StringUtil.subBefore("abc", "")    = ""
     * StringUtil.subBefore("abc", null)  = "abc"
     * </pre>
     *
     * @param string          被查找的字符串
     * @param separator       分隔字符串（不包括）
     * @param isLastSeparator 是否查找最后一个分隔字符串（多次出现分隔字符串时选取最后一个），true为选取最后一个
     * @return 切割后的字符串
     * @since 3.1.1
     */
    public static String subBefore(CharSequence string, CharSequence separator, boolean isLastSeparator) {
        if (isEmpty(string) || separator == null) {
            return null == string ? null : string.toString();
        }

        final String str = string.toString();
        final String sep = separator.toString();
        if (sep.isEmpty()) {
            return StringPool.EMPTY;
        }
        final int pos = isLastSeparator ? str.lastIndexOf(sep) : str.indexOf(sep);
        if (pos == INDEX_NOT_FOUND) {
            return str;
        }
        return str.substring(0, pos);
    }

    /**
     * 截取分隔字符串之后的字符串，不包括分隔字符串<br>
     * 如果给定的字符串为空串（null或""），返回原字符串<br>
     * 如果分隔字符串为空串（null或""），则返回空串，如果分隔字符串未找到，返回空串
     * <p>
     * 栗子：
     *
     * <pre>
     * StringUtil.subAfter(null, *)      = null
     * StringUtil.subAfter("", *)        = ""
     * StringUtil.subAfter(*, null)      = ""
     * StringUtil.subAfter("abc", "a")   = "bc"
     * StringUtil.subAfter("abcba", "b") = "cba"
     * StringUtil.subAfter("abc", "c")   = ""
     * StringUtil.subAfter("abc", "d")   = ""
     * StringUtil.subAfter("abc", "")    = "abc"
     * </pre>
     *
     * @param string          被查找的字符串
     * @param separator       分隔字符串（不包括）
     * @param isLastSeparator 是否查找最后一个分隔字符串（多次出现分隔字符串时选取最后一个），true为选取最后一个
     * @return 切割后的字符串
     * @since 3.1.1
     */
    public static String subAfter(CharSequence string, CharSequence separator, boolean isLastSeparator) {
        if (isEmpty(string)) {
            return null == string ? null : string.toString();
        }
        if (separator == null) {
            return StringPool.EMPTY;
        }
        final String str = string.toString();
        final String sep = separator.toString();
        final int pos = isLastSeparator ? str.lastIndexOf(sep) : str.indexOf(sep);
        if (pos == INDEX_NOT_FOUND) {
            return StringPool.EMPTY;
        }
        return str.substring(pos + separator.length());
    }

    /**
     * 截取指定字符串中间部分，不包括标识字符串<br>
     * <p>
     * 栗子：
     *
     * <pre>
     * StringUtil.subBetween("wx[b]yz", "[", "]") = "b"
     * StringUtil.subBetween(null, *, *)          = null
     * StringUtil.subBetween(*, null, *)          = null
     * StringUtil.subBetween(*, *, null)          = null
     * StringUtil.subBetween("", "", "")          = ""
     * StringUtil.subBetween("", "", "]")         = null
     * StringUtil.subBetween("", "[", "]")        = null
     * StringUtil.subBetween("yabcz", "", "")     = ""
     * StringUtil.subBetween("yabcz", "y", "z")   = "abc"
     * StringUtil.subBetween("yabczyabcz", "y", "z")   = "abc"
     * </pre>
     *
     * @param str    被切割的字符串
     * @param before 截取开始的字符串标识
     * @param after  截取到的字符串标识
     * @return 截取后的字符串
     * @since 3.1.1
     */
    public static String subBetween(CharSequence str, CharSequence before, CharSequence after) {
        return subBetween(str,before,after,false);
    }

    /**
     * 截取指定字符串中间部分，不包括标识字符串
     * @param str    被切割的字符串
     * @param before 截取开始的字符串标识
     * @param after  截取到的字符串标识
     * @param isLastSeparator  是否查找最后一个分隔字符串（多次出现分隔字符串时选取最后一个），true为选取最后一个
     * @return 截取后的字符串
     * @since 3.1.1
     */
    public static String subBetween(CharSequence str, CharSequence before, CharSequence after, boolean isLastSeparator) {
        if (str == null || before == null || after == null) {
            return null;
        }

        final String str2 = str.toString();
        final String before2 = before.toString();
        final String after2 = after.toString();

        final int start = isLastSeparator ? str2.lastIndexOf(before2) : str2.indexOf(before2);
        if (start != INDEX_NOT_FOUND) {
            final int end = str2.indexOf(after2, start + before2.length());
            if (end != INDEX_NOT_FOUND) {
                return str2.substring(start + before2.length(), end);
            }
        }
        return null;
    }

    /**
     * 截取指定字符串中间部分，不包括标识字符串<br>
     * <p>
     * 栗子：
     *
     * <pre>
     * StringUtil.subBetween(null, *)            = null
     * StringUtil.subBetween("", "")             = ""
     * StringUtil.subBetween("", "tag")          = null
     * StringUtil.subBetween("tagabctag", null)  = null
     * StringUtil.subBetween("tagabctag", "")    = ""
     * StringUtil.subBetween("tagabctag", "tag") = "abc"
     * </pre>
     *
     * @param str            被切割的字符串
     * @param beforeAndAfter 截取开始和结束的字符串标识
     * @return 截取后的字符串
     * @since 3.1.1
     */
    public static String subBetween(CharSequence str, CharSequence beforeAndAfter) {
        return subBetween(str, beforeAndAfter, beforeAndAfter);
    }

    /**
     * 去掉指定前缀
     *
     * @param str    字符串
     * @param prefix 前缀
     * @return 切掉后的字符串，若前缀不是 preffix， 返回原字符串
     */
    public static String removePrefix(CharSequence str, CharSequence prefix) {
        if (isEmpty(str) || isEmpty(prefix)) {
            return StringPool.EMPTY;
        }

        final String str2 = str.toString();
        if (str2.startsWith(prefix.toString())) {
            return subSuf(str2, prefix.length());
        }
        return str2;
    }

    /**
     * 去掉指定的所有前缀
     * todo 生成的字符串只要是指定的前缀就一直去除
     * @param str    字符串
     * @param suffix 前缀
     * @return 切掉后的字符串，若前缀不是 suffix， 返回原字符串
     */
    public static String removeAllPrefix(CharSequence str, CharSequence suffix) {
        String newStr =  removePrefix(str,suffix);
        if(equals(newStr,str)){
            return newStr;
        }
        return removeAllPrefix(newStr,suffix);
    }

    /**
     * 忽略大小写去掉指定前缀
     *
     * @param str    字符串
     * @param prefix 前缀
     * @return 切掉后的字符串，若前缀不是 prefix， 返回原字符串
     */
    public static String removePrefixIgnoreCase(CharSequence str, CharSequence prefix) {
        if (isEmpty(str) || isEmpty(prefix)) {
            return StringPool.EMPTY;
        }

        final String str2 = str.toString();
        if (str2.toLowerCase().startsWith(prefix.toString().toLowerCase())) {
            return subSuf(str2, prefix.length());
        }
        return str2;
    }

    /**
     * 去掉指定的所有前缀后缀
     * todo 生成的字符串只要是指定的前缀后缀就一直去除
     * @param str    字符串
     * @param suffix 前缀后缀
     * @return 切掉后的字符串，若前缀后缀不是 suffix， 返回原字符串
     */
    public static String removeAllPrefixAndSuffix(CharSequence str, CharSequence suffix) {
        String newStr =  removeAllPrefix(str,suffix);
        return removeAllSuffix(newStr,suffix);
    }

    /**
     * 去掉指定的所有前缀后缀,并小写should字母
     * todo 生成的字符串只要是指定的前缀后缀就一直去除
     * @param str    字符串
     * @param suffix 前缀后缀
     * @return 切掉后的字符串，若前缀后缀不是 suffix， 返回原字符串
     */
    public static String removeAllPrefixAndSuffixLowerFirst(CharSequence str, CharSequence suffix) {
        String newStr =  removeAllPrefix(str,suffix);
        return lowerFirst(removeAllSuffix(newStr,suffix));
    }

    /**
     * 去掉指定后缀
     *
     * @param str    字符串
     * @param suffix 后缀
     * @return 切掉后的字符串，若后缀不是 suffix， 返回原字符串
     */
    public static String removeSuffix(CharSequence str, CharSequence suffix) {
        if (isEmpty(str) || isEmpty(suffix)) {
            return StringPool.EMPTY;
        }

        final String str2 = str.toString();
        if (str2.endsWith(suffix.toString())) {
            return subPre(str2, str2.length() - suffix.length());
        }
        return str2;
    }

    /**
     * 去掉指定的所有后缀
     * todo 生成的字符串只要是指定的后缀就一直去除
     * @param str    字符串
     * @param suffix 后缀
     * @return 切掉后的字符串，若后缀不是 suffix， 返回原字符串
     */
    public static String removeAllSuffix(CharSequence str, CharSequence suffix) {
        String newStr =  removeSuffix(str,suffix);
        if(equals(newStr,str)){
            return newStr;
        }
        return removeAllSuffix(newStr,suffix);
    }

    /**
     * 去掉指定后缀，并小写首字母
     *
     * @param str    字符串
     * @param suffix 后缀
     * @return 切掉后的字符串，若后缀不是 suffix， 返回原字符串
     */
    public static String removeSufAndLowerFirst(CharSequence str, CharSequence suffix) {
        return lowerFirst(removeSuffix(str, suffix));
    }

    /**
     * 忽略大小写去掉指定后缀
     *
     * @param str    字符串
     * @param suffix 后缀
     * @return 切掉后的字符串，若后缀不是 suffix， 返回原字符串
     */
    public static String removeSuffixIgnoreCase(CharSequence str, CharSequence suffix) {
        if (isEmpty(str) || isEmpty(suffix)) {
            return StringPool.EMPTY;
        }

        final String str2 = str.toString();
        if (str2.toLowerCase().endsWith(suffix.toString().toLowerCase())) {
            return subPre(str2, str2.length() - suffix.length());
        }
        return str2;
    }

    /**
     * 首字母变小写
     *
     * @param str 字符串
     * @return {String}
     */
    public static String lowerFirst(String str) {
        if (isEmpty(str)){
            return str;
        }

        char firstChar = str.charAt(0);
        if (firstChar >= CharPool.U_A && firstChar <= CharPool.U_Z) {
            char[] arr = str.toCharArray();
            arr[0] += (CharPool.L_A - CharPool.U_A);
            return new String(arr);
        }
        return str;
    }

    /**
     * 首字母变大写
     *
     * @param str 字符串
     * @return {String}
     */
    public static String upperFirst(String str) {
        char firstChar = str.charAt(0);
        if (firstChar >= CharPool.L_A && firstChar <= CharPool.L_Z) {
            char[] arr = str.toCharArray();
            arr[0] -= (CharPool.L_A - CharPool.U_A);
            return new String(arr);
        }
        return str;
    }

    /**
     * 切割指定位置之前部分的字符串
     *
     * @param string  字符串
     * @param toIndex 切割到的位置（不包括）
     * @return 切割后的剩余的前半部分字符串
     */
    public static String subPre(CharSequence string, int toIndex) {
        return sub(string, 0, toIndex);
    }

    /**
     * 切割指定位置之后部分的字符串
     *
     * @param string    字符串
     * @param fromIndex 切割开始的位置（包括）
     * @return 切割后后剩余的后半部分字符串
     */
    public static String subSuf(CharSequence string, int fromIndex) {
        if (isEmpty(string)) {
            return null;
        }
        return sub(string, fromIndex, string.length());
    }

    /**
     * 指定范围内查找指定字符
     *
     * @param str        字符串
     * @param searchChar 被查找的字符
     * @return 位置
     */
    public static int indexOf(final CharSequence str, char searchChar) {
        return indexOf(str, searchChar, 0);
    }

    /**
     * 指定范围内查找指定字符
     *
     * @param str        字符串
     * @param searchChar 被查找的字符
     * @param start      起始位置，如果小于0，从0开始查找
     * @return 位置
     */
    public static int indexOf(final CharSequence str, char searchChar, int start) {
        if (str instanceof String) {
            return ((String) str).indexOf(searchChar, start);
        } else {
            return indexOf(str, searchChar, start, -1);
        }
    }

    /**
     * 指定范围内查找指定字符
     *
     * @param str        字符串
     * @param searchChar 被查找的字符
     * @param start      起始位置，如果小于0，从0开始查找
     * @param end        终止位置，如果超过str.length()则默认查找到字符串末尾
     * @return 位置
     */
    public static int indexOf(final CharSequence str, char searchChar, int start, int end) {
        final int len = str.length();
        if (start < 0 || start > len) {
            start = 0;
        }
        if (end > len || end < 0) {
            end = len;
        }
        for (int i = start; i < end; i++) {
            if (str.charAt(i) == searchChar) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 指定范围内查找字符串，忽略大小写<br>
     *
     * <pre>
     * StringUtil.indexOfIgnoreCase(null, *, *)          = -1
     * StringUtil.indexOfIgnoreCase(*, null, *)          = -1
     * StringUtil.indexOfIgnoreCase("", "", 0)           = 0
     * StringUtil.indexOfIgnoreCase("aabaabaa", "A", 0)  = 0
     * StringUtil.indexOfIgnoreCase("aabaabaa", "B", 0)  = 2
     * StringUtil.indexOfIgnoreCase("aabaabaa", "AB", 0) = 1
     * StringUtil.indexOfIgnoreCase("aabaabaa", "B", 3)  = 5
     * StringUtil.indexOfIgnoreCase("aabaabaa", "B", 9)  = -1
     * StringUtil.indexOfIgnoreCase("aabaabaa", "B", -1) = 2
     * StringUtil.indexOfIgnoreCase("aabaabaa", "", 2)   = 2
     * StringUtil.indexOfIgnoreCase("abc", "", 9)        = -1
     * </pre>
     *
     * @param str       字符串
     * @param searchStr 需要查找位置的字符串
     * @return 位置
     * @since 3.2.1
     */
    public static int indexOfIgnoreCase(final CharSequence str, final CharSequence searchStr) {
        return indexOfIgnoreCase(str, searchStr, 0);
    }

    /**
     * 指定范围内查找字符串
     *
     * <pre>
     * StringUtil.indexOfIgnoreCase(null, *, *)          = -1
     * StringUtil.indexOfIgnoreCase(*, null, *)          = -1
     * StringUtil.indexOfIgnoreCase("", "", 0)           = 0
     * StringUtil.indexOfIgnoreCase("aabaabaa", "A", 0)  = 0
     * StringUtil.indexOfIgnoreCase("aabaabaa", "B", 0)  = 2
     * StringUtil.indexOfIgnoreCase("aabaabaa", "AB", 0) = 1
     * StringUtil.indexOfIgnoreCase("aabaabaa", "B", 3)  = 5
     * StringUtil.indexOfIgnoreCase("aabaabaa", "B", 9)  = -1
     * StringUtil.indexOfIgnoreCase("aabaabaa", "B", -1) = 2
     * StringUtil.indexOfIgnoreCase("aabaabaa", "", 2)   = 2
     * StringUtil.indexOfIgnoreCase("abc", "", 9)        = -1
     * </pre>
     *
     * @param str       字符串
     * @param searchStr 需要查找位置的字符串
     * @param fromIndex 起始位置
     * @return 位置
     * @since 3.2.1
     */
    public static int indexOfIgnoreCase(final CharSequence str, final CharSequence searchStr, int fromIndex) {
        return indexOf(str, searchStr, fromIndex, true);
    }

    /**
     * 指定范围内反向查找字符串
     *
     * @param str        字符串
     * @param searchStr  需要查找位置的字符串
     * @param fromIndex  起始位置
     * @param ignoreCase 是否忽略大小写
     * @return 位置
     * @since 3.2.1
     */
    public static int indexOf(final CharSequence str, CharSequence searchStr, int fromIndex, boolean ignoreCase) {
        if (str == null || searchStr == null) {
            return INDEX_NOT_FOUND;
        }
        if (fromIndex < 0) {
            fromIndex = 0;
        }

        final int endLimit = str.length() - searchStr.length() + 1;
        if (fromIndex > endLimit) {
            return INDEX_NOT_FOUND;
        }
        if (searchStr.length() == 0) {
            return fromIndex;
        }

        if (false == ignoreCase) {
            // 不忽略大小写调用JDK方法
            return str.toString().indexOf(searchStr.toString(), fromIndex);
        }

        for (int i = fromIndex; i < endLimit; i++) {
            if (isSubEquals(str, i, searchStr, 0, searchStr.length(), true)) {
                return i;
            }
        }
        return INDEX_NOT_FOUND;
    }

    /**
     * 指定范围内查找字符串，忽略大小写<br>
     *
     * @param str       字符串
     * @param searchStr 需要查找位置的字符串
     * @return 位置
     * @since 3.2.1
     */
    public static int lastIndexOfIgnoreCase(final CharSequence str, final CharSequence searchStr) {
        return lastIndexOfIgnoreCase(str, searchStr, str.length());
    }

    /**
     * 指定范围内查找字符串，忽略大小写<br>
     *
     * @param str       字符串
     * @param searchStr 需要查找位置的字符串
     * @param fromIndex 起始位置，从后往前计数
     * @return 位置
     * @since 3.2.1
     */
    public static int lastIndexOfIgnoreCase(final CharSequence str, final CharSequence searchStr, int fromIndex) {
        return lastIndexOf(str, searchStr, fromIndex, true);
    }

    /**
     * 指定范围内查找字符串<br>
     *
     * @param str        字符串
     * @param searchStr  需要查找位置的字符串
     * @param fromIndex  起始位置，从后往前计数
     * @param ignoreCase 是否忽略大小写
     * @return 位置
     * @since 3.2.1
     */
    public static int lastIndexOf(final CharSequence str, final CharSequence searchStr, int fromIndex, boolean ignoreCase) {
        if (str == null || searchStr == null) {
            return INDEX_NOT_FOUND;
        }
        if (fromIndex < 0) {
            fromIndex = 0;
        }
        fromIndex = Math.min(fromIndex, str.length());

        if (searchStr.length() == 0) {
            return fromIndex;
        }

        if (false == ignoreCase) {
            // 不忽略大小写调用JDK方法
            return str.toString().lastIndexOf(searchStr.toString(), fromIndex);
        }

        for (int i = fromIndex; i > 0; i--) {
            if (isSubEquals(str, i, searchStr, 0, searchStr.length(), true)) {
                return i;
            }
        }
        return INDEX_NOT_FOUND;
    }

    /**
     * 返回字符串 searchStr 在字符串 str 中第 ordinal 次出现的位置。<br>
     * 此方法来自：Apache-Commons-Lang
     * <p>
     * 栗子（*代表任意字符）：
     *
     * <pre>
     * StringUtil.ordinalIndexOf(null, *, *)          = -1
     * StringUtil.ordinalIndexOf(*, null, *)          = -1
     * StringUtil.ordinalIndexOf("", "", *)           = 0
     * StringUtil.ordinalIndexOf("aabaabaa", "a", 1)  = 0
     * StringUtil.ordinalIndexOf("aabaabaa", "a", 2)  = 1
     * StringUtil.ordinalIndexOf("aabaabaa", "b", 1)  = 2
     * StringUtil.ordinalIndexOf("aabaabaa", "b", 2)  = 5
     * StringUtil.ordinalIndexOf("aabaabaa", "ab", 1) = 1
     * StringUtil.ordinalIndexOf("aabaabaa", "ab", 2) = 4
     * StringUtil.ordinalIndexOf("aabaabaa", "", 1)   = 0
     * StringUtil.ordinalIndexOf("aabaabaa", "", 2)   = 0
     * </pre>
     *
     * @param str       被检查的字符串，可以为null
     * @param searchStr 被查找的字符串，可以为null
     * @param ordinal   第几次出现的位置
     * @return 查找到的位置
     * @since 3.2.3
     */
    public static int ordinalIndexOf(String str, String searchStr, int ordinal) {
        if (str == null || searchStr == null || ordinal <= 0) {
            return INDEX_NOT_FOUND;
        }
        if (searchStr.length() == 0) {
            return 0;
        }
        int found = 0;
        int index = INDEX_NOT_FOUND;
        do {
            index = str.indexOf(searchStr, index + 1);
            if (index < 0) {
                return index;
            }
            found++;
        } while (found < ordinal);
        return index;
    }

    /**
     * 截取两个字符串的不同部分（长度一致），判断截取的子串是否相同<br>
     * 任意一个字符串为null返回false
     *
     * @param str1       第一个字符串
     * @param start1     第一个字符串开始的位置
     * @param str2       第二个字符串
     * @param start2     第二个字符串开始的位置
     * @param length     截取长度
     * @param ignoreCase 是否忽略大小写
     * @return 子串是否相同
     * @since 3.2.1
     */
    public static boolean isSubEquals(CharSequence str1, int start1, CharSequence str2, int start2, int length, boolean ignoreCase) {
        if (null == str1 || null == str2) {
            return false;
        }

        return str1.toString().regionMatches(ignoreCase, start1, str2.toString(), start2, length);
    }

    /**
     * 比较两个字符串（大小写敏感）。
     *
     * <pre>
     * equalsIgnoreCase(null, null)   = true
     * equalsIgnoreCase(null, &quot;abc&quot;)  = false
     * equalsIgnoreCase(&quot;abc&quot;, null)  = false
     * equalsIgnoreCase(&quot;abc&quot;, &quot;abc&quot;) = true
     * equalsIgnoreCase(&quot;abc&quot;, &quot;ABC&quot;) = true
     * </pre>
     *
     * @param str1 要比较的字符串1
     * @param str2 要比较的字符串2
     * @return 如果两个字符串相同，或者都是<code>null</code>，则返回<code>true</code>
     */
    public static boolean equals(CharSequence str1, CharSequence str2) {
        return equals(str1, str2, false);
    }

    /**
     * 比较两个字符串（大小写不敏感）。
     *
     * <pre>
     * equalsIgnoreCase(null, null)   = true
     * equalsIgnoreCase(null, &quot;abc&quot;)  = false
     * equalsIgnoreCase(&quot;abc&quot;, null)  = false
     * equalsIgnoreCase(&quot;abc&quot;, &quot;abc&quot;) = true
     * equalsIgnoreCase(&quot;abc&quot;, &quot;ABC&quot;) = true
     * </pre>
     *
     * @param str1 要比较的字符串1
     * @param str2 要比较的字符串2
     * @return 如果两个字符串相同，或者都是<code>null</code>，则返回<code>true</code>
     */
    public static boolean equalsIgnoreCase(CharSequence str1, CharSequence str2) {
        return equals(str1, str2, true);
    }

    /**
     * 比较两个字符串是否相等。
     *
     * @param str1       要比较的字符串1
     * @param str2       要比较的字符串2
     * @param ignoreCase 是否忽略大小写
     * @return 如果两个字符串相同，或者都是<code>null</code>，则返回<code>true</code>
     * @since 3.2.0
     */
    public static boolean equals(CharSequence str1, CharSequence str2, boolean ignoreCase) {
        if (null == str1) {
            // 只有两个都为null才判断相等
            return str2 == null;
        }
        if (null == str2) {
            // 字符串2空，字符串1非空，直接false
            return false;
        }

        if (ignoreCase) {
            return str1.toString().equalsIgnoreCase(str2.toString());
        } else {
            return str1.equals(str2);
        }
    }

    /**
     * 创建StringBuilder对象
     *
     * @return StringBuilder对象
     */
    public static StringBuilder builder() {
        return new StringBuilder();
    }

    /**
     * 创建StringBuilder对象
     *
     * @param capacity 初始大小
     * @return StringBuilder对象
     */
    public static StringBuilder builder(int capacity) {
        return new StringBuilder(capacity);
    }

    /**
     * 创建StringBuilder对象
     *
     * @param strs 初始字符串列表
     * @return StringBuilder对象
     */
    public static StringBuilder builder(CharSequence... strs) {
        final StringBuilder sb = new StringBuilder();
        for (CharSequence str : strs) {
            sb.append(str);
        }
        return sb;
    }

    /**
     * 创建StringBuilder对象
     *
     * @param sb   初始StringBuilder
     * @param strs 初始字符串列表
     * @return StringBuilder对象
     */
    public static StringBuilder appendBuilder(StringBuilder sb, CharSequence... strs) {
        for (CharSequence str : strs) {
            sb.append(str);
        }
        return sb;
    }

    /**
     * 获得StringReader
     *
     * @param str 字符串
     * @return StringReader
     */
    public static StringReader getReader(CharSequence str) {
        if (null == str) {
            return null;
        }
        return new StringReader(str.toString());
    }

    /**
     * 获得StringWriter
     *
     * @return StringWriter
     */
    public static StringWriter getWriter() {
        return new StringWriter();
    }

    /**
     * 统计指定内容中包含指定字符串的数量<br>
     * 参数为 {@code null} 或者 "" 返回 {@code 0}.
     *
     * <pre>
     * StringUtil.count(null, *)       = 0
     * StringUtil.count("", *)         = 0
     * StringUtil.count("abba", null)  = 0
     * StringUtil.count("abba", "")    = 0
     * StringUtil.count("abba", "a")   = 2
     * StringUtil.count("abba", "ab")  = 1
     * StringUtil.count("abba", "xxx") = 0
     * </pre>
     *
     * @param content      被查找的字符串
     * @param strForSearch 需要查找的字符串
     * @return 查找到的个数
     */
    public static int count(CharSequence content, CharSequence strForSearch) {
        if (Fc.hasEmpty(content, strForSearch) || strForSearch.length() > content.length()) {
            return 0;
        }

        int count = 0;
        int idx = 0;
        final String content2 = content.toString();
        final String strForSearch2 = strForSearch.toString();
        while ((idx = content2.indexOf(strForSearch2, idx)) > -1) {
            count++;
            idx += strForSearch.length();
        }
        return count;
    }

    /**
     * 统计指定内容中包含指定字符的数量
     *
     * @param content       内容
     * @param charForSearch 被统计的字符
     * @return 包含数量
     */
    public static int count(CharSequence content, char charForSearch) {
        int count = 0;
        if (isEmpty(content)) {
            return 0;
        }
        int contentLength = content.length();
        for (int i = 0; i < contentLength; i++) {
            if (charForSearch == content.charAt(i)) {
                count++;
            }
        }
        return count;
    }

    /**
     * 下划线转驼峰
     *
     * @param para 字符串
     * @return String
     */
    public static String underlineToHump(String para) {
        StringBuilder result = new StringBuilder();
        String[] a = para.split("_");
        for (String s : a) {
            if (result.length() == 0) {
                result.append(s.toLowerCase());
            } else {
                result.append(s.substring(0, 1).toUpperCase());
                result.append(s.substring(1).toLowerCase());
            }
        }
        return result.toString();
    }

    /**
     * 驼峰转下划线
     *
     * @param para 字符串
     * @return String
     */
    public static String humpToUnderline(String para) {
        if (isBlank(para)){
            return para;
        }
        para = lowerFirst(para);
        StringBuilder sb = new StringBuilder(para);
        int temp = 0;
        for (int i = 0; i < para.length(); i++) {
            if (Character.isUpperCase(para.charAt(i))) {
                sb.insert(i + temp, "_");
                temp += 1;
            }
        }
        return sb.toString().toLowerCase();
    }

    /**
     * 横线转驼峰
     *
     * @param para 字符串
     * @return String
     */
    public static String lineToHump(String para) {
        if (isBlank(para)){
            return para;
        }
        StringBuilder result = new StringBuilder();
        String[] a = para.split("-");
        for (String s : a) {
            if (result.length() == 0) {
                result.append(s.toLowerCase());
            } else {
                result.append(s.substring(0, 1).toUpperCase());
                result.append(s.substring(1).toLowerCase());
            }
        }
        return result.toString();
    }

    /**
     * 驼峰转横线
     *
     * @param para 字符串
     * @return String
     */
    public static String humpToLine(String para) {
        para = lowerFirst(para);
        StringBuilder sb = new StringBuilder(para);
        int temp = 0;
        for (int i = 0; i < para.length(); i++) {
            if (Character.isUpperCase(para.charAt(i))) {
                sb.insert(i + temp, "-");
                temp += 1;
            }
        }
        return sb.toString().toLowerCase();
    }

    /**
     * 去除字符串前后的空格, 包括半角空格和全角空格(中文)等各种空格, java的string.trim()只能去英文半角空格
     * 以及去除字符串当中的空白字符
     * @param str
     */
    public static String trim(String str) {
        if (org.apache.commons.lang3.StringUtils.isBlank(str)) {
            return str;
        }

        str = org.apache.commons.lang3.StringUtils.trim(str.trim());

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
     * 字符串连接
     * @Author ding
     * @Date 00:21 2021-02-22
     * @param strs
     * @return java.lang.String
     **/
    public static String concat(String... strs) {
        List<String> list = Arrays.stream(strs).filter(StringUtil::isNotBlank).collect(Collectors.toList());
        return join(list,"");
    }

    /**
     * 通配符模式匹配
     * @param whitePath - 通配符
     * @param path - 匹配字符串
     */
    public static boolean wildcardEquals(String whitePath, String path) {
        String regPath = getRegPath(whitePath);
        return Pattern.compile(regPath).matcher(path).matches();
    }

    /**
     * @author 郭丁志
     * @Description // 把通配符转换成正则表达式
     * @param path - 通配符
     */
    public static String getRegPath(String path) {
        char[] chars = path.toCharArray();
        int len = chars.length;
        StringBuilder sb = new StringBuilder();
        boolean preX = false;
        for (int i = 0; i < len; i++) {
            if (chars[i] == '*') {//遇到*字符
                if (preX) {//如果是第二次遇到*，则将**替换成.*
                    sb.append(".*");
                    preX = false;
                } else if (i + 1 == len) {//如果是遇到单星，且单星是最后一个字符，则直接将*转成[^/]*
                    sb.append("[^/]*");
                } else {//否则单星后面还有字符，则不做任何动作，下一把再做动作
                    preX = true;
                    continue;
                }
            } else {//遇到非*字符
                if (preX) {//如果上一把是*，则先把上一把的*对应的[^/]*添进来
                    sb.append("[^/]*");
                    preX = false;
                }
                if (chars[i] == '?') {//接着判断当前字符是不是?，是的话替换成.
                    sb.append('.');
                } else {//不是?的话，则就是普通字符，直接添进来
                    sb.append(chars[i]);
                }
            }
        }
        return sb.toString();
    }

    public static String toLowerCase(String str){
        if (Fc.isBlank(str)){
            return StringPool.EMPTY;
        }
        return str.toLowerCase();
    }
}
