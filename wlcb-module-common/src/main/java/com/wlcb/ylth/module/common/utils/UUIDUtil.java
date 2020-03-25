package com.wlcb.ylth.module.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @ClassName: UUIDUtil
 * @Description: UUID工具类
 * @author: jiangyuanzhong
 * @date: 2016年5月30日 上午11:20:58
 */
public class UUIDUtil {
    /**
     * 初始化 62 进制数据，索引位置代表字符的数值，比如 A代表10，z代表61等
     */
    private static String chars = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static int scale = 62;

    final static char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8',
            '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l',
            'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y',
            'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
            'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y',
            'Z' };
    final static Map<Character, Integer> digitMap = new HashMap<Character, Integer>();

    static {
        for (int i = 0; i < digits.length; i++) {
            digitMap.put(digits[i], (int) i);
        }
    }

    /**
     * 支持的最大进制数
     */
    public static final int MAX_RADIX = digits.length;

    /**
     * 支持的最小进制数
     */
    public static final int MIN_RADIX = 2;

    /**
     * 将长整型数值转换为指定的进制数（最大支持62进制，字母数字已经用尽）
     *
     * @param i
     * @param radix
     * @return
     */
    public static String toString(long i, int radix) {
        if (radix < MIN_RADIX || radix > MAX_RADIX) {
            radix = 10;
        }
        if (radix == 10) {
            return Long.toString(i);
        }
        final int size = 65;
        int charPos = 64;

        char[] buf = new char[size];
        boolean negative = (i < 0);

        if (!negative) {
            i = -i;
        }

        while (i <= -radix) {
            buf[charPos--] = digits[(int) (-(i % radix))];
            i = i / radix;
        }
        buf[charPos] = digits[(int) (-i)];

        if (negative) {
            buf[--charPos] = '-';
        }

        return new String(buf, charPos, (size - charPos));
    }
    static NumberFormatException forInputString(String s) {
        return new NumberFormatException("For input string: \"" + s + "\"");
    }
    /**
     * 将字符串转换为长整型数字
     *
     * @param s
     *            数字字符串
     * @param radix
     *            进制数
     * @return
     */
    public static long toNumber(String s, int radix) {
        if (s == null) {
            throw new NumberFormatException("null");
        }

        if (radix < MIN_RADIX) {
            throw new NumberFormatException("radix " + radix
                    + " less than Numbers.MIN_RADIX");
        }
        if (radix > MAX_RADIX) {
            throw new NumberFormatException("radix " + radix
                    + " greater than Numbers.MAX_RADIX");
        }

        long result = 0;
        boolean negative = false;
        int i = 0, len = s.length();
        long limit = -Long.MAX_VALUE;
        long multmin;
        Integer digit;

        if (len > 0) {
            char firstChar = s.charAt(0);
            if (firstChar < '0') {
                if (firstChar == '-') {
                    negative = true;
                    limit = Long.MIN_VALUE;
                } else if (firstChar != '+') {
                    throw forInputString(s);
                }
                if (len == 1) {
                    throw forInputString(s);
                }
                i++;
            }
            multmin = limit / radix;
            while (i < len) {
                digit = digitMap.get(s.charAt(i++));
                if (digit == null) {
                    throw forInputString(s);
                }
                if (digit < 0) {
                    throw forInputString(s);
                }
                if (result < multmin) {
                    throw forInputString(s);
                }
                result *= radix;
                if (result < limit + digit) {
                    throw forInputString(s);
                }
                result -= digit;
            }
        } else {
            throw forInputString(s);
        }
        return negative ? result : -result;
    }


    private static String digits(long val, int digits) {
        long hi = 1L << (digits * 4);
        return toString(hi | (val & (hi - 1)), MAX_RADIX).substring(1);
    }

    /**
     * 将数字转为62进制(6位)
     * @param num    Long 型数字
     */
    public static String encode(long num) {
        StringBuilder sb = new StringBuilder();
        int remainder = 0;

        while (num > scale - 1) {
            /**
             * 对 scale 进行求余，然后将余数追加至 sb 中，由于是从末位开始追加的，因此最后需要反转（reverse）字符串
             */
            remainder = Long.valueOf(num % scale).intValue();
            sb.append(chars.charAt(remainder));

            num = num / scale;
        }
        sb.append(chars.charAt(Long.valueOf(num).intValue()));
        String value = sb.reverse().toString();
        return StringUtils.leftPad(value, 6, '0');
    }

    /**
     * 以62进制（字母加数字）生成19位UUID，最短的UUID
     * @author: wangliuyang
     * @return String
     */
    public static String get19UUID() {
        UUID uuid = UUID.randomUUID();
        StringBuilder sb = new StringBuilder();
        sb.append(digits(uuid.getMostSignificantBits() >> 32, 8));
        sb.append(digits(uuid.getMostSignificantBits() >> 16, 4));
        sb.append(digits(uuid.getMostSignificantBits(), 4));
        sb.append(digits(uuid.getLeastSignificantBits() >> 48, 4));
        sb.append(digits(uuid.getLeastSignificantBits(), 12));
        return sb.toString();
    }
    /**
     * 生成6位62进制秒级时间戳(时间戳为10位)
     *
     */
    public static String getUUID62Decimal(){
        return encode(System.currentTimeMillis());
    }

	/**
	 * @Title: getUUID
	 * @Description: 获取无中划线的uuid
	 * @author: jiangyuanzhong
	 * @return
	 * @return: String
	 */
	public static String getUUID(){
		String randomUUID = UUID.randomUUID().toString();
		String id = randomUUID.replace("-", "");
		return id;
	}

    /**
     * 生成指定长度UUID字符串（去除下划线）
     *
     * @param num
     * @return
     */
    public static synchronized String createUuid(int num) {
        String s = UUID.randomUUID().toString();
        String sub = s.substring(0, 8) + s.substring(9, 13) + s.substring(14, 18)
                + s.substring(19, 23) + s.substring(24);
        if (num > 0) {
            sub = sub.substring(0, num);
        }
        return sub;
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 生成10位数字型UUId
     * @Date 16:20 2020-03-22
     * @Param []
     * @return java.lang.String
     **/
    public static String create10UUidNum() {
        int hashCodeV = UUID.randomUUID().toString().hashCode();
        if(hashCodeV < 0) {//有可能是负数
            hashCodeV = - hashCodeV;
        }
        // 0 代表前面补充0
        // 4 代表长度为4
        // d 代表参数为正数型
        return String.format("%010d", hashCodeV);
    }

	public static void main(String[] args) {
        System.out.println(getUUID());
	}
}
