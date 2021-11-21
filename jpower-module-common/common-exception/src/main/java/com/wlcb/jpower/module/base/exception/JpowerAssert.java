package com.wlcb.jpower.module.base.exception;

import com.wlcb.jpower.module.base.enums.JpowerError;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Map;

public class JpowerAssert {

    public static void createException(JpowerError err, Object... args) {
        String message = String.format(err.getMessage(), args);
        throw new JpowerException(err.getCode(), message);
    }

    /**
     * 如果表达式结果为false，则抛出异常
     *
     * @param expression 传入的表达式
     * @param err        错误信息枚举对象
     */
    public static void isTrue(boolean expression, JpowerError err) {
        if (!expression) {
            createException(err);
        }
    }

    /**
     * 如果表达式结果为false，则抛出异常
     *
     * @param expression 传入的表达式
     * @param err        错误信息枚举对象
     * @param args       消息参数
     */
    public static void isTrue(boolean expression, JpowerError err, Object... args) {
        if (!expression) {
            createException(err, args);
        }
    }

    /**
     * 如果表达式结果为true，则抛出异常
     *
     * @param expression 传入的表达式
     * @param err        错误信息枚举对象
     */
    public static void notTrue(boolean expression, JpowerError err) {
        if (expression) {
            createException(err);
        }
    }

    /**
     * 如果表达式结果为true，则抛出异常
     *
     * @param expression 传入的表达式
     * @param err        错误信息枚举对象
     * @param args       消息参数
     */
    public static void notTrue(boolean expression, JpowerError err, Object... args) {
        if (expression) {
            createException(err, args);
        }
    }

    /**
     * 如果对象不为空，则抛出异常
     *
     * @param object 要检查的对象
     * @param err    错误信息枚举对象
     * @param args   消息参数
     */
    public static void isNull(Object object, JpowerError err, Object... args) {
        if (object != null) {
            createException(err, args);
        }
    }

    /**
     * 如果对象不为空，则抛出异常
     *
     * @param object 要检查的对象
     * @param err    错误信息枚举对象
     */
    public static void isNull(Object object, JpowerError err) {
        if (object != null) {
            createException(err);
        }
    }

    /**
     * 如果对象为空，则抛出异常
     *
     * @param object 要检查的对象
     * @param err    错误信息枚举对象
     * @param args   消息参数
     */
    public static void notNull(Object object, JpowerError err, Object... args) {
        if (object == null) {
            createException(err, args);
        }
    }

    /**
     * 如果对象为空，则抛出异常
     *
     * @param object 要检查的对象
     * @param err    错误信息枚举对象
     */
    public static void notNull(Object object, JpowerError err) {
        if (object == null) {
            createException(err);
        }
    }

    /**
     * 如果字符串为空字符串，则抛出异常
     *
     * @param text 要判断的字符串
     * @param err  错误信息枚举对象
     */
    public static void notEmpty(String text, JpowerError err) {
        if (!StringUtils.hasLength(text)) {
            createException(err);
        }
    }

    /**
     * 如果字符串为空字符串，则抛出异常
     *
     * @param text 要判断的字符串
     * @param err  错误信息枚举对象
     * @param args 消息参数
     */
    public static void notEmpty(String text, JpowerError err, Object... args) {
        if (!StringUtils.hasLength(text)) {
            createException(err, args);
        }
    }

    /**
     * 如果字符串为非空字符串，则抛出异常
     *
     * @param text 要判断的字符串
     * @param err  错误信息枚举对象
     */
    public static void isEmpty(String text, JpowerError err) {
        if (StringUtils.hasLength(text)) {
            createException(err);
        }
    }

    /**
     * 如果字符串为非空字符串，则抛出异常
     *
     * @param text 要判断的字符串
     * @param err  错误信息枚举对象
     * @param args 消息参数
     */
    public static void isEmpty(String text, JpowerError err, Object... args) {
        if (StringUtils.hasLength(text)) {
            createException(err, args);
        }
    }

    /**
     * 如果字符串中包含子字符串，则抛出异常
     *
     * @param textToSearch
     * @param substring
     * @param err
     */
    public static void doesNotContain(String textToSearch, String substring, JpowerError err) {
        if (StringUtils.hasLength(textToSearch) && StringUtils.hasLength(substring)
                && textToSearch.indexOf(substring) != -1) {
            createException(err);
        }
    }

    /**
     * 如果字符串中包含子字符串，则抛出异常
     *
     * @param textToSearch
     * @param substring
     * @param err
     * @param args
     */
    public static void doesNotContain(String textToSearch, String substring, JpowerError err, Object... args) {
        if (StringUtils.hasLength(textToSearch) && StringUtils.hasLength(substring)
                && textToSearch.indexOf(substring) != -1) {
            createException(err, args);
        }
    }

    /**
     * 如果数组为空，则抛出异常
     *
     * @param array
     * @param err
     */
    public static void notEmpty(Object[] array, JpowerError err) {
        if (ObjectUtils.isEmpty(array)) {
            createException(err);
        }
    }

    /**
     * 如果数组为空，则抛出异常
     *
     * @param array
     * @param err
     * @param args
     */
    public static void notEmpty(Object[] array, JpowerError err, Object... args) {
        if (ObjectUtils.isEmpty(array)) {
            createException(err, args);
        }
    }

    /**
     * 如果对象数组中存在空对象，则抛出异常
     *
     * @param array
     * @param err
     */
    public static void noNullElements(Object[] array, JpowerError err) {
        if (array != null) {
            for (int i = 0; i < array.length; i++) {
                if (array[i] == null) {
                    createException(err);
                }
            }
        }
    }

    /**
     * 如果对象数组中存在空对象，则抛出异常
     *
     * @param array
     * @param err
     * @param args
     */
    public static void noNullElements(Object[] array, JpowerError err, Object... args) {
        if (array != null) {
            for (int i = 0; i < array.length; i++) {
                if (array[i] == null) {
                    createException(err, args);
                }
            }
        }
    }

    /**
     * 如果集合为空，则抛出异常
     *
     * @param collection
     * @param err
     */
    public static void notEmpty(Collection<?> collection, JpowerError err) {
        if (CollectionUtils.isEmpty(collection)) {
            createException(err);
        }
    }

    /**
     * 如果集合为空，则抛出异常
     *
     * @param collection
     * @param err
     * @param args
     */
    public static void notEmpty(Collection<?> collection, JpowerError err, Object... args) {
        if (CollectionUtils.isEmpty(collection)) {
            createException(err, args);
        }
    }

    /**
     * 如果集合不为空，则抛出异常
     *
     * @param collection
     * @param err
     */
    public static void isEmpty(Collection<?> collection, JpowerError err) {
        if (!CollectionUtils.isEmpty(collection)) {
            createException(err);
        }
    }

    /**
     * 如果集合不为空，则抛出异常
     *
     * @param collection
     * @param err
     * @param args
     */
    public static void isEmpty(Collection<?> collection, JpowerError err, Object... args) {
        if (!CollectionUtils.isEmpty(collection)) {
            createException(err, args);
        }
    }

    /**
     * 如果map为空，则抛出异常
     *
     * @param map
     * @param err
     */
    public static void notEmpty(Map<?, ?> map, JpowerError err) {
        if (CollectionUtils.isEmpty(map)) {
            createException(err);
        }
    }

    /**
     * 如果map为空，则抛出异常
     *
     * @param map
     * @param err
     * @param args
     */
    public static void notEmpty(Map<?, ?> map, JpowerError err, Object... args) {
        if (CollectionUtils.isEmpty(map)) {
            createException(err, args);
        }
    }

    /**
     * 如果对象不是指定class的实例，则抛出异常
     *
     * @param clazz
     * @param obj
     * @param err
     */
    public static void isInstanceOf(Class<?> clazz, Object obj, JpowerError err) {
        notNull(clazz, err);
        if (!clazz.isInstance(obj)) {
            createException(err);
        }
    }

    /**
     * 如果对象不是指定class的实例，则抛出异常
     *
     * @param clazz
     * @param obj
     * @param err
     * @param args
     */
    public static void isInstanceOf(Class<?> clazz, Object obj, JpowerError err, Object... args) {
        notNull(clazz, err, args);
        if (!clazz.isInstance(obj)) {
            createException(err, args);
        }
    }

    /**
     * 如果不全是数字，则抛出异常
     *
     * @param text
     * @param err
     */
    public static void isDigit(String text, JpowerError err) {
        notEmpty(text, err);
        String reg_digit = "[0-9]*";
        if (!text.matches(reg_digit)) {
            createException(err);
        }
    }

    /**
     * 如果数字大于0，则抛出异常
     *
     * @param i
     * @param err
     */
    public static void geZero(long i, JpowerError err, Object... args) {
        if (i > 0) {
            createException(err,args);
        }
    }

    /**
     * 如果数字不大于0，则抛出异常
     *
     * @param i
     * @param err
     */
    public static void notGeZero(long i, JpowerError err, Object... args) {
        if (i <= 0) {
            createException(err,args);
        }
    }


}
