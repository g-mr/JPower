package com.wlcb.jpower.module.common.utils;

import cn.hutool.core.bean.NullWrapperBean;
import cn.hutool.core.util.TypeUtil;
import com.wlcb.jpower.module.common.utils.constants.StringPool;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * 反射工具类
 *
 * @author mr.g
 */
@Slf4j
public class ReflectUtil extends cn.hutool.core.util.ReflectUtil{

    /**
     * SET方法前缀
     **/
    private static final String SETTER_PREFIX = "set";

    /**
     * GET方法前缀
     **/
    private static final String GETTER_PREFIX = "get";

    /**
     * 调用字段的Getter方法<br/>
     * 支持多级，如：对象名.对象名.方法
     *
     * @author mr.g
     * @param obj 对象
     * @param propertyName 字段名
     * @return 执行结果
     **/
    @SuppressWarnings("unchecked")
    public static <E> E invokeGetter(Object obj, String propertyName) {
        Object object = obj;
        for (String name : StringUtils.split(propertyName, StringPool.DOT)) {
            String getterMethodName = GETTER_PREFIX + StringUtil.upperFirst(name);
            object = invoke(object, getterMethodName);
        }
        return (E) object;
    }

    /**
     * 调用字段的Setter方法<br/>
     * 如果需要传递的参数为null,请使用NullWrapperBean来传递,不然会丢失类型信息<br/>
     * 支持多级，如：对象名.对象名.方法
     *
     * @see NullWrapperBean
     * @author mr.g
     * @param obj 对象
     * @param propertyName 字段名
     * @param value 值
     **/
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

    /**
     * 通过反射, 获得Class定义中声明的泛型参数的类型, 注意泛型必须定义在父类处<br/>
     * 如无法找到, 返回null.
     *
     * @author mr.g
     * @param clazz Class类
     * @return 泛型类型CLASS
     **/
    public static Class<?> getClassGenricType(final Class<?> clazz) {
        return getClassGenricType(clazz, 0);
    }

    /**
     * 通过反射, 获得Class定义中声明的父类的泛型参数的类型<br/>
     * 如无法找到, 返回null.
     *
     * @author mr.g
     * @param clazz Class类
     * @param clazz 泛型类型的索引号，即第几个泛型类型
     * @return 泛型类型CLASS
     */
    public static Class<?> getClassGenricType(final Class<?> clazz, final int index) {
        return TypeUtil.getClass(TypeUtil.getTypeArgument(clazz,1));
    }
}
