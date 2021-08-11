package com.wlcb.jpower.module.common.utils;

import cn.hutool.core.util.StrUtil;
import com.wlcb.jpower.module.common.utils.constants.StringPool;

import java.util.Collection;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * 字符串工具类
 * @author mr.g
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
     * 清理字符串，清理出某些不可见字符
     *
     * @param txt 字符串
     * @return {String}
     */
    public static String cleanChars(String txt) {
        return txt.replaceAll("[ 　`·•�\\f\\t\\v\\s]", "");
    }

    /**
     * 截取指定字符串中间部分，不包括标识字符串
     * @param str    被切割的字符串
     * @param before 截取开始的字符串标识
     * @param after  截取到的字符串标识
     * @param isLastSeparator  是否查找最后一个分隔字符串（多次出现分隔字符串时选取最后一个），true为选取最后一个
     * @return 截取后的字符串
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
     * 去掉指定的所有前缀
     * 生成的字符串只要是指定的前缀就一直去除
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
     * 去掉指定的所有前缀后缀,并小写首字母
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
     * StringBuilder append
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
     * 下划线转驼峰
     *
     * @param para 字符串
     * @return String
     */
    public static String underlineToHump(String para) {
        return toCamelCase(para);
    }

    /**
     * 驼峰转下划线
     *
     * @param para 字符串
     * @return String
     */
    public static String humpToUnderline(String para) {
        return toUnderlineCase(para);
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
        String[] a = para.split(StringPool.DASH);
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
                sb.insert(i + temp, StringPool.DASH);
                temp += 1;
            }
        }
        return sb.toString().toLowerCase();
    }

    /**
     * 是否是空格字符
     * @Author mr.g
     * @param aChar
     * @return boolean
     **/
    public static boolean isSpace(char aChar) {
        return aChar == SPACE_32 || aChar == SPACE_12288 || aChar == SPACE_160 || aChar == SPACE_8194
                || aChar == SPACE_8195 || aChar == SPACE_8197 || aChar == SPACE_8201;
    }

    /**
     * 字符串连接
     * @Author mr.g
     * @Date 00:21 2021-02-22
     * @param strs
     * @return java.lang.String
     **/
    public static String concat(String... strs) {
        return concat(true,strs);
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
     * @author mr.g
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

    /**
     * 小写字符串
     * @Author mr.g
     * @param str
     * @return java.lang.String
     **/
    public static String toLowerCase(String str){
        if (Fc.isBlank(str)){
            return StringPool.EMPTY;
        }
        return str.toLowerCase();
    }
}
