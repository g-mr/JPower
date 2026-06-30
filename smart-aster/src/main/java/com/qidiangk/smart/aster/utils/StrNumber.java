package com.qidiangk.smart.aster.utils;

import cn.hutool.core.convert.NumberChineseFormatter;
import cn.hutool.core.util.ReUtil;

public class StrNumber {

    public static String coverChinese(String str, Boolean isSingle){
        // 使用正则匹配所有数字（包括小数）
        return ReUtil.replaceAll(str, "\\d+\\.?\\d*", match -> {
            String numStr = match.group();
            try {
                if (numStr.contains(".")) {
                    // 处理小数
                    return NumberChineseFormatter.format(Double.parseDouble(numStr), false);
                } else {
                    if (isSingle){
                        StringBuilder buffer = new StringBuilder();
                        for (char c : numStr.toCharArray()) {
                            buffer.append(NumberChineseFormatter.numberCharToChinese(c, false));
                        }
                        return buffer.toString();
                    } else {
                        return NumberChineseFormatter.format(Long.parseLong(numStr), false);
                    }

                }
            } catch (NumberFormatException e) {
                return numStr; // 转换失败返回原字符串
            }
        });
    }

}
