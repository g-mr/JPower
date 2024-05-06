package top.jpower.core.util.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 注解工具类
 *
 * @author mr.g
 * @date 2023/6/29 9:38 PM
 */
public class AnnotationUtil extends cn.hutool.core.annotation.AnnotationUtil {

    /**
     * 获取指定类中包含的指定注解的方法
     * @author mr.g
     * @param clazz 要查找的类
     * @param annotation 指定的注解
     * @return 方法列表
     **/
    public static Set<Method> findAnnotatedMethods(Class<?> clazz, Class<? extends Annotation> annotation) {
        Set<Method> annotatedMethods = new HashSet<>();
        Method[] methods = ClassUtil.getPublicMethods(clazz);
        for (Method method : methods) {
            if (AnnotationUtil.hasAnnotation(method, annotation)) {
                annotatedMethods.add(method);
            }
        }
        return annotatedMethods;
    }

    public static Set<Field> findAnnotatedFields(Class<?> clazz, Class<? extends Annotation> annotation) {
        Set<Field> annotatedFields = new HashSet<>();
        List<Field> fields = BeanUtil.getFieldList(clazz);
        for (Field field : fields) {
            if (AnnotationUtil.hasAnnotation(field, annotation)) {
                annotatedFields.add(field);
            }
        }
        return annotatedFields;
    }
}
