package com.wlcb.jpower.module.common.utils;

import org.springframework.lang.Nullable;

/**
 * @ClassName ObjectUtil
 * @Description TODO 工具类
 * @Author 郭丁志
 * @Date 2020-07-23 15:15
 * @Version 1.0
 */
public class ObjectUtil extends org.springframework.util.ObjectUtils {

    /**
     * @Author 郭丁志
     * 判断元素不为空
     * @param obj object
     * @return boolean
     */
    public static boolean isNotEmpty(@Nullable Object obj) {
        return !ObjectUtil.isEmpty(obj);
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 判断俩个类型的值是否相等
     * @Date 17:50 2020-08-02
     * @Param [o1, o2]
     * @return boolean
     **/
    public static boolean equalsValue(@Nullable Object o1, @Nullable Object o2) {
        String v1 = String.valueOf(o1);
        String v2 = String.valueOf(o2);
        return StringUtil.equals(v1,v2);
    }

}
