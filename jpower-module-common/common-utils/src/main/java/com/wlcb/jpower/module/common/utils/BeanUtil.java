package com.wlcb.jpower.module.common.utils;

import cn.hutool.core.util.ReflectUtil;
import com.wlcb.jpower.module.common.support.BeanProperty;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.cglib.beans.BeanGenerator;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * 实体工具类
 *
 * @author mr.g
 */
public class BeanUtil  extends cn.hutool.core.bean.BeanUtil {

    /**
     * 实例化对象
     * @param clazz 类
     * @param <T> 泛型标记
     * @return 对象
     */
    public static <T> T newInstance(Class<?> clazz) {
        return (T) ReflectUtil.newInstance(clazz);
    }

    /**
     * 实例化对象
     * @param clazzStr 类名
     * @param <T> 泛型标记
     * @return 对象
     */
    public static <T> T newInstance(String clazzStr) {
        return ReflectUtil.newInstance(clazzStr);
    }

    /**
     * 给一个Bean添加字段
     * @param superBean 父级Bean
     * @param props 新增属性
     * @return  {Object}
     */
    public static Object generator(Object superBean, BeanProperty... props) {
        Class<?> superclass = superBean.getClass();
        Object genBean = generator(superclass, props);
        copyProperties(superBean, genBean);
        return genBean;
    }

    /**
     * 给一个class添加字段
     * @param superclass 父级
     * @param props 新增属性
     * @return {Object}
     */
    public static Object generator(Class<?> superclass, BeanProperty... props) {
        BeanGenerator generator = new BeanGenerator();
        generator.setSuperclass(superclass);
        generator.setUseCache(true);
        for (BeanProperty prop : props) {
            generator.addProperty(prop.getName(), prop.getType());
        }
        return generator.create();
    }

    /**
     * 使用 {@link BeanUtils#copyProperties(Object, Object, String[])} 拷贝Bean
     * <p>Note: 比hutool的 {@link #copyProperties(Object, Object, String[])} 方法好处是可以把动态增加的属性也可以拷贝过去
     * @author mr.g
     * @param source           源Bean对象
     * @param clz              目标Bean对象
     * @param ignoreProperties 不拷贝的的属性列表
     * @return 目标对象
     */
    public static <T> T copy(Object source, Class<T> clz, String... ignoreProperties) {
        T target = ReflectUtil.newInstanceIfPossible(clz);
        copy(source, target, ignoreProperties);
        return target;
    }

    /**
     * 使用 {@link BeanUtils#copyProperties(Object, Object, Class)} 拷贝Bean
     * <p>Note: 比hutool的 {@link #copyProperties(Object, Object, String[])} 方法好处是可以把动态增加的属性也可以拷贝过去
     * @author mr.g
     * @param source           源Bean对象
     * @param clz              目标Bean对象
     * @param editable         要将属性设置限制为的类(或接口)
     * @return 目标对象
     */
    public static <T> T copy(Object source, Class<T> clz, Class<?> editable) {
        T target = ReflectUtil.newInstanceIfPossible(clz);
        copy(source, target, editable);
        return target;
    }

    /**
     * 使用 {@link BeanUtils#copyProperties(Object, Object, String[])} 拷贝Bean
     * <p>Note: 比hutool的 {@link #copyProperties(Object, Object, String[])} 方法好处是可以把动态增加的属性也可以拷贝过去
     * @author mr.g
     * @param source           源Bean对象
     * @param target           目标Bean对象
     * @param ignoreProperties 不拷贝的的属性列表
     */
    public static void copy(Object source, Object target, String... ignoreProperties) {
        BeanUtils.copyProperties(source, target, ignoreProperties);
    }

    /**
     * 使用 {@link BeanUtils#copyProperties(Object, Object, Class)} 拷贝Bean
     * <p>Note: 比hutool的 {@link #copyProperties(Object, Object, String[])} 方法好处是可以把动态增加的属性也可以拷贝过去
     * @author mr.g
     * @param source           源Bean对象
     * @param target           目标Bean对象
     * @param editable         要将属性设置限制为的类(或接口)
     */
    public static void copy(Object source, Object target, Class<?> editable) {
        BeanUtils.copyProperties(source, target, editable);
    }

    /**
     * 使用 {@link BeanUtils#copyProperties(Object, Object, Class)} 拷贝集合中的Bean属性<br>
     * 此方法遍历集合中每个Bean，复制其属性后加入一个新的{@link List}中。
     * <p>Note: 比hutool的 {@link #copyProperties(Object, Object, String[])} 方法好处是可以把动态增加的属性也可以拷贝过去.
     * @param collection 原Bean集合
     * @param targetType 目标Bean类型
     * @param <T> Bean类型
     * @return 复制后的List
     * @since 5.6.4
     */
    public static <T> List<T> copyToList(Collection<?> collection, Class<T> targetType){
        return collection.stream().map((source)->{
            final T target = ReflectUtil.newInstanceIfPossible(targetType);
            copy(source, target);
            return target;
        }).collect(Collectors.toList());
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 获取属性列表
     * @Date 18:50 2020-03-03
     * @Param [clazz]
     * @return java.util.List<java.lang.reflect.Field>
     **/
    public static List<Field> getFieldList(Class<?> clazz){
        if(null == clazz){
            return null;
        }
        List<Field> fieldList = new LinkedList<Field>();
        Field[] fields = clazz.getDeclaredFields();
        for(Field field : fields){
            /** 过滤静态属性**/
            if(Modifier.isStatic(field.getModifiers())){
                continue;
            }
            /** 过滤transient 关键字修饰的属性**/
            if(Modifier.isTransient(field.getModifiers())){
                continue;
            }
            fieldList.add(field);
        }
        /** 处理父类字段**/
        Class<?> superClass = clazz.getSuperclass();
        if(superClass.equals(Object.class)) {
            return fieldList;
        }

        if(!superClass.getName().equals(Serializable.class.getName()) && superClass instanceof Serializable){
            fieldList.addAll(getFieldList(superClass));
        }
        return fieldList;
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 取Bean的属性和值对应关系的MAP
     * @Date 18:56 2020-03-03
     * @Param [bean]
     * @return java.util.Map<java.lang.String,java.lang.String>
     **/
    public static Map<String, Object> getFieldValueMap(Object bean) {
        Class<?> cls = bean.getClass();
        Map<String, Object> valueMap = new HashMap<String, Object>();
        // 取出bean里的所有方法
        Method[] methods = cls.getMethods();
        List<Field> fields = getFieldList(cls);

        for (Field field : fields) {
            try {
                String fieldGetName = parGetName(field.getName());
                if (!checkGetMet(methods, fieldGetName)) {
                    continue;
                }
                Method fieldGetMet = cls
                        .getMethod(fieldGetName, new Class[] {});
                Object fieldVal = fieldGetMet.invoke(bean, new Object[] {});
                valueMap.put(field.getName(), fieldVal);
            } catch (Exception e) {
                continue;
            }
        }
        return valueMap;
    }

    /**
     * 拼接某属性的 get方法
     * @param fieldName
     * @return String
     */
    public static String parGetName(String fieldName) {
        if (null == fieldName || "".equals(fieldName)) {
            return null;
        }
        return "get" + fieldName.substring(0, 1).toUpperCase()
                + fieldName.substring(1);
    }

    /**
     * 判断是否存在某属性的 get方法
     * @param methods
     * @param fieldGetMet
     * @return boolean
     */
    public static boolean checkGetMet(Method[] methods, String fieldGetMet) {
        for (Method met : methods) {
            if (fieldGetMet.equals(met.getName())) {
                return true;
            }
        }
        return false;
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

    /**
     * 是否存在一个字段
     * @Author mr.g
     * @param list
     * @param fieldName
     * @return boolean
     **/
    public static boolean isContainsField(List<Field> list,String fieldName) {

        AtomicBoolean isExist = new AtomicBoolean(false);

        list.forEach(field -> {
            if (Fc.equals(field.getName(),fieldName)){
                isExist.set(true);
            }
        });
        return isExist.get();
    }

}
