package com.wlcb.jpower.module.common.utils;

import com.wlcb.jpower.module.common.utils.constants.StringPool;
import org.apache.commons.lang3.StringUtils;

/**
 * 反射工具类
 * @author mr.g
 */
public class ReflectUtil extends cn.hutool.core.util.ReflectUtil{
    private static final String SETTER_PREFIX = "set";

    private static final String GETTER_PREFIX = "get";

    /**
     * 调用Getter方法.
     * 支持多级，如：对象名.对象名.方法
     */
    public static <E> E invokeGetter(Object obj, String propertyName)
    {
        Object object = obj;
        for (String name : StringUtils.split(propertyName, StringPool.DOT)) {
            String getterMethodName = GETTER_PREFIX + StringUtil.upperFirst(name);
            object = invoke(object, getterMethodName);
        }
        return (E) object;
    }

    /**
     * 调用Setter方法, 仅匹配方法名。
     * 支持多级，如：对象名.对象名.方法
     */
    public static <E> void invokeSetter(Object obj, String propertyName, E value) {
        Object object = obj;
        String[] names = StringUtils.split(propertyName, StringPool.DOT);
        for (int i = 0; i < names.length; i++) {
            if (i < names.length - 1) {
                String getterMethodName = GETTER_PREFIX + StringUtil.upperFirst(names[i]);
                object = invoke(object, getterMethodName);
            } else {
                String setterMethodName = SETTER_PREFIX + StringUtil.upperFirst(names[i]);
                invoke(object, setterMethodName, value);
            }
        }
    }

}
