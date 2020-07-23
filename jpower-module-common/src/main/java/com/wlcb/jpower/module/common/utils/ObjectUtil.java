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
     * 判断元素不为空
     * @param obj object
     * @return boolean
     */
    public static boolean isNotEmpty(@Nullable Object obj) {
        return !ObjectUtil.isEmpty(obj);
    }

}
