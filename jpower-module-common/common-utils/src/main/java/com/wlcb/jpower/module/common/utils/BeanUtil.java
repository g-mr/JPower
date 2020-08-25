package com.wlcb.jpower.module.common.utils;

import com.wlcb.jpower.module.common.support.BaseBeanCopier;
import com.wlcb.jpower.module.common.support.BeanProperty;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.cglib.beans.BeanGenerator;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.cglib.core.CodeGenerationException;
import org.springframework.util.Assert;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

public class BeanUtil  extends org.springframework.beans.BeanUtils {

    private static final Logger logger = LoggerFactory.getLogger(BeanUtil.class);

    private static final String fieldNullList = "serialVersionUID,id,createUser,createTime,updateUser,updateTime,createDate,updateDate,status,createTimeStr,updateTimeStr";

    /**
     * 实例化对象
     * @param clazz 类
     * @param <T> 泛型标记
     * @return 对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T newInstance(Class<?> clazz) {
        return (T) instantiateClass(clazz);
    }

    /**
     * 实例化对象
     * @param clazzStr 类名
     * @param <T> 泛型标记
     * @return 对象
     */
    public static <T> T newInstance(String clazzStr) {
        try {
            Class<?> clazz = Class.forName(clazzStr);
            return newInstance(clazz);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取Bean的属性
     * @param bean bean
     * @param propertyName 属性名
     * @return 属性值
     */
    public static Object getProperty(Object bean, String propertyName) {
        Assert.notNull(bean, "bean Could not null");
        return BeanMap.create(bean).get(propertyName);
    }

    /**
     * 设置Bean属性
     * @param bean bean
     * @param propertyName 属性名
     * @param value 属性值
     */
    public static void setProperty(Object bean, String propertyName, Object value) {
        Assert.notNull(bean, "bean Could not null");
        BeanMap.create(bean).put(propertyName, value);
    }

    /**
     * copy 对象属性到另一个对象，默认不使用Convert
     *
     * 注意：不支持链式Bean，链式用 copyProperties
     *
     * @param source 源对象
     * @param clazz 类名
     * @param <T> 泛型标记
     * @return T
     */
    public static <T> T copy(Object source, Class<T> clazz) {
        BaseBeanCopier copier = BaseBeanCopier.create(source.getClass(), clazz, false);

        T to = newInstance(clazz);
        copier.copy(source, to, null);
        return to;
    }

    /**
     * 拷贝对象
     *
     * 注意：不支持链式Bean，链式用 copyProperties
     *
     * @param source 源对象
     * @param targetBean 需要赋值的对象
     */
    public static void copy(Object source, Object targetBean) {
        BaseBeanCopier copier = BaseBeanCopier
                .create(source.getClass(), targetBean.getClass(), false);

        copier.copy(source, targetBean, null);
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
        BeanUtil.copy(superBean, genBean);
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
     * 获取 Bean 的所有 get方法
     * @param type 类
     * @return PropertyDescriptor数组
     */
    public static PropertyDescriptor[] getBeanGetters(Class type) {
        return getPropertiesHelper(type, true, false);
    }

    /**
     * 获取 Bean 的所有 set方法
     * @param type 类
     * @return PropertyDescriptor数组
     */
    public static PropertyDescriptor[] getBeanSetters(Class type) {
        return getPropertiesHelper(type, false, true);
    }

    private static PropertyDescriptor[] getPropertiesHelper(Class type, boolean read, boolean write) {
        try {
            PropertyDescriptor[] all = BeanUtil.getPropertyDescriptors(type);
            if (read && write) {
                return all;
            } else {
                List<PropertyDescriptor> properties = new ArrayList<>(all.length);
                for (PropertyDescriptor pd : all) {
                    if (read && pd.getReadMethod() != null) {
                        properties.add(pd);
                    } else if (write && pd.getWriteMethod() != null) {
                        properties.add(pd);
                    }
                }
                return properties.toArray(new PropertyDescriptor[0]);
            }
        } catch (BeansException ex) {
            throw new CodeGenerationException(ex);
        }
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 判断类中属性是否都为空
     * @Date 18:56 2020-03-03
     * @Param [o]
     * @return ResponseData
     **/
    public static void allFieldIsNULL(Object o){
        try {
            for (Field field : getFieldList(o.getClass())) {
                field.setAccessible(true);

                Object object = field.get(o);
                String name = field.getName();
                if(!fieldNullList.contains(name)){
                    Assert.notNull(object,name+"不可为空");
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 判断类中指定属性是否都为空
     * @Date 18:56 2020-03-03
     * @Param [o, fields]
     * @return ResponseData
     **/
    public static void allFieldIsNULL(Object o,String... fields){
        try {
            for (Field field : getFieldList(o.getClass())) {
                field.setAccessible(true);

                Object object = field.get(o);
                String name = field.getName();
                Arrays.sort(fields);
                if(Arrays.binarySearch(fields, name) >= 0){
                    Assert.notNull(object,name+"不可为空");
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 获取属性列表 去除继承的属性
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
     * @Description //TODO set属性的值到Bean
     * @Date 18:56 2020-03-03
     * @Param [bean, valMap]
     * @return void
     **/
    public static void setFieldValue(Object bean, Map<String, String> valMap) {
        Class<?> cls = bean.getClass();
        // 取出bean里的所有方法
        Method[] methods = cls.getMethods();
        List<Field> fields = getFieldList(cls);

        for (Field field : fields) {
            try {

                String fieldSetName = parSetName(field.getName());
                if (!checkSetMet(methods, fieldSetName)) {
                    continue;
                }
                Method fieldSetMet = cls.getMethod(fieldSetName, field.getType());
                String value = valMap.get(field.getName());
                if (null != value && !"".equals(value)) {
                    String fieldType = field.getType().getSimpleName();
                    if ("String".equals(fieldType)) {
                        fieldSetMet.invoke(bean, value);
                    } else if ("Date".equals(fieldType)) {
                        Date temp = Fc.parseDate(value);
                        fieldSetMet.invoke(bean, temp);
                    } else if ("Integer".equals(fieldType)
                            || "int".equals(fieldType)) {
                        Integer intval = Integer.parseInt(value);
                        fieldSetMet.invoke(bean, intval);
                    } else if ("Long".equalsIgnoreCase(fieldType)) {
                        Long temp = Long.parseLong(value);
                        fieldSetMet.invoke(bean, temp);
                    } else if ("Double".equalsIgnoreCase(fieldType)) {
                        Double temp = Double.parseDouble(value);
                        fieldSetMet.invoke(bean, temp);
                    } else if ("Boolean".equalsIgnoreCase(fieldType)) {
                        Boolean temp = Boolean.parseBoolean(value);
                        fieldSetMet.invoke(bean, temp);
                    } else {
                        System.out.println("not supper type" + fieldType);
                    }
                }
            } catch (Exception e) {
                continue;
            }
        }

    }

    /**
     * @Author 郭丁志
     * @Description //TODO 取Bean的属性和值对应关系的MAP
     * @Date 18:56 2020-03-03
     * @Param [bean]
     * @return java.util.Map<java.lang.String,java.lang.String>
     **/
    public static Map<String, String> getFieldValueMap(Object bean) {
        Class<?> cls = bean.getClass();
        Map<String, String> valueMap = new HashMap<String, String>();
        // 取出bean里的所有方法
        Method[] methods = cls.getMethods();
        List<Field> fields = getFieldList(cls);

        for (Field field : fields) {
            try {
                String fieldType = field.getType().getSimpleName();
                String fieldGetName = parGetName(field.getName());
                if (!checkGetMet(methods, fieldGetName)) {
                    continue;
                }
                Method fieldGetMet = cls
                        .getMethod(fieldGetName, new Class[] {});
                Object fieldVal = fieldGetMet.invoke(bean, new Object[] {});
                String result = null;
                if ("Date".equals(fieldType)) {
                    result = Fc.formatDateTime((Date) fieldVal);
                } else {
                    if (null != fieldVal) {
                        result = Fc.toStr(fieldVal);
                    }
                }
                valueMap.put(field.getName(), result);
            } catch (Exception e) {
                continue;
            }
        }
        return valueMap;
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 判断是否存在某属性的 set方法
     * @Date 18:56 2020-03-03
     * @Param [methods, fieldSetMet]
     * @return boolean
     **/
    public static boolean checkSetMet(Method[] methods, String fieldSetMet) {
        for (Method met : methods) {
            if (fieldSetMet.equals(met.getName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 拼接在某属性的 set方法
     * @param fieldName
     * @return String
     */
    public static String parSetName(String fieldName) {
        if (null == fieldName || "".equals(fieldName)) {
            return null;
        }
        return "set" + fieldName.substring(0, 1).toUpperCase()
                + fieldName.substring(1);
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
    public static List<Field> getFiledHaveAnnotation(Class<?> cls, Class<? extends Annotation> annotationType) {
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
