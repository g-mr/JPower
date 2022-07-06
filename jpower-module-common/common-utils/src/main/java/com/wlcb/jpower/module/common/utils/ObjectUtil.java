package com.wlcb.jpower.module.common.utils;

import lombok.NonNull;

/**
 * 对象工具类
 *
 * @author mr.g
 **/
public class ObjectUtil extends cn.hutool.core.util.ObjectUtil {

    /**
     * 判断俩个类型的值是否相等
     *
     * @author mr.g
     * @param o1 对象1
     * @param o2 对象2
     * @return 是否相等
     **/
    public static boolean equalsValue(@NonNull Object o1, @NonNull Object o2) {
        String v1 = String.valueOf(o1);
        String v2 = String.valueOf(o2);
        return StringUtil.equals(v1,v2);
    }

}
