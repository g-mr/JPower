package com.wlcb.jpower.module.common.utils;

import com.wlcb.jpower.module.common.utils.constants.Fn;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.ClassUtils;
import org.springframework.web.method.HandlerMethod;

import java.lang.annotation.Annotation;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @ClassName ClassUtil
 * @Description TODO 类工具类
 * @Author 郭丁志
 * @Date 2020-07-23 16:36
 * @Version 1.0
 */
public class ClassUtil  extends cn.hutool.core.util.ClassUtil {

    /**
     * 获取Annotation
     *
     * @param method         Method
     * @param annotationType 注解类
     * @param <A>            泛型标记
     * @return {Annotation}
     */
    public static <A extends Annotation> A getAnnotation(Method method, Class<A> annotationType) {
        Method specificMethod = ClassUtils.getMostSpecificMethod(method, method.getDeclaringClass());
        specificMethod = BridgeMethodResolver.findBridgedMethod(specificMethod);
        // 先找方法，再找方法上的类
        A annotation = AnnotatedElementUtils.findMergedAnnotation(specificMethod, annotationType);
        ;
        if (Fc.isNull(annotation)) {
            annotation = AnnotatedElementUtils.findMergedAnnotation(specificMethod.getDeclaringClass(), annotationType);
        }
        return annotation;
    }

    /**
     * 获取Annotation
     *
     * @param handlerMethod  HandlerMethod
     * @param annotationType 注解类
     * @param <A>            泛型标记
     * @return {Annotation}
     */
    public static <A extends Annotation> A getAnnotation(HandlerMethod handlerMethod, Class<A> annotationType) {
        // 先找方法，再找方法上的类
        A annotation = handlerMethod.getMethodAnnotation(annotationType);
        if (Fc.isNull(annotation)) {
            // 获取类上面的Annotation，可能包含组合注解，故采用spring的工具类
            annotation = AnnotatedElementUtils.findMergedAnnotation(handlerMethod.getBeanType(), annotationType);
        }
        return annotation;
    }

    /**
     * 获取属性名称
     * @author mr.g
     * @param fn
     * @return java.lang.String
     */
    public static <T> String getFiledName(Fn<T, ?> fn) {
        // 从function取出序列化方法
        Method writeReplaceMethod;
        try {
            writeReplaceMethod = fn.getClass().getDeclaredMethod("writeReplace");
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        // 从序列化方法取出序列化的lambda信息
        boolean isAccessible = writeReplaceMethod.isAccessible();
        writeReplaceMethod.setAccessible(true);
        SerializedLambda serializedLambda;
        try {
            serializedLambda = (SerializedLambda) writeReplaceMethod.invoke(fn);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        writeReplaceMethod.setAccessible(isAccessible);

        // 从lambda信息取出method、field、class等
        String fieldName = serializedLambda.getImplMethodName().substring("get".length());
        fieldName = fieldName.replaceFirst(fieldName.charAt(0) + "", (fieldName.charAt(0) + "").toLowerCase());
        Field field;
        try {
            field = Class.forName(serializedLambda.getImplClass().replace("/", ".")).getDeclaredField(fieldName);
        } catch (ClassNotFoundException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }

        return fieldName;
    }


}
