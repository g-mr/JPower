package com.wlcb.jpower.module.common.utils;

import cn.hutool.core.util.ReflectUtil;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * 实体工具类
 *
 * @author mr.g
 */
public class BeanUtil extends cn.hutool.core.bean.BeanUtil {

    /**
     * 实例化对象
     * @param clazz 类
     * @param <T> 泛型标记
     * @return 对象
     */
    public static <T> T newBean(Class<?> clazz) {
        return (T) ReflectUtil.newInstance(clazz);
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 获取属性列表
     * @Date 18:50 2020-03-03
     * @Param [clazz]
     * @return java.util.List<java.lang.reflect.Field>
     **/
    public static List<Field> getFieldList(Class<?> clazz){
        return CollectionUtil.toList(ReflectUtil.getFields(clazz, field -> {
            // 过滤static 关键字修饰的属性
            if(Modifier.isStatic(field.getModifiers())){
                return false;
            }
            // 过滤transient 关键字修饰的属性
            if(Modifier.isTransient(field.getModifiers())){
                return false;
            }

            return true;
        }));
    }

    /**
     * @author 郭丁志
     * @Description //TODO 获取所有拥有指定注解得属性
     * @date 22:23 2020/8/9 0009
     * @param cls BEAN类
     * @param annotationType 指定得注解
     * @return java.util.List<java.lang.reflect.Field>
     */
    public static List<Field> getFiledByAnnotation(Class<?> cls, Class<? extends Annotation> annotationType) {
        List<Field> fieldList = new ArrayList<>();

        List<Field> fields = FieldUtils.getAllFieldsList(cls);
        for (Field field : fields) {
            Annotation annotation = field.getAnnotation(annotationType);
            if (annotation != null){
                fieldList.add(field);
            }
        }
        return fieldList;
    }

}
