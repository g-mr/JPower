package top.jpower.core.util.utils;

import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.ReflectUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
     * 获取属性列表
	 *
	 * @author mr.g
     * @param clazz  类
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
     * 获取所有拥有指定注解得属性
	 *
	 * @author mr.g
     * @param cls BEAN类
     * @param annotationType 指定得注解
     * @return java.util.List<java.lang.reflect.Field>
     */
    public static List<Field> getFiledByAnnotation(Class<?> cls, Class<? extends Annotation> annotationType) {
        List<Field> fieldList = new ArrayList<>();

        List<Field> fields = BeanUtil.getFieldList(cls);
        for (Field field : fields) {
            Annotation annotation = field.getAnnotation(annotationType);
            if (annotation != null){
                fieldList.add(field);
            }
        }
        return fieldList;
    }

    /**
     * 合并俩个Bean值
     *
     * @author mr.g
     * @param source 源对象
     * @param target 目标对象
     * @return java.lang.Object
     **/
    public static Object merge(Object source, Object target){
        copyProperties(source, target, CopyOptions.create().setIgnoreNullValue(Boolean.TRUE));
        return target;
    }

    /**
     * bean转换成map 父类字段自动去除
     *
     * @author mr.g
     * @param bean bean对象
     * @param names 父类需要保留的字段
     * @return java.util.Map<java.lang.String,java.lang.Object>
     **/
    public static Map<String,Object> beanToMapRemoveSuper(Object bean, String... names){
        Field[] declaredFields = bean.getClass().getDeclaredFields();

        return beanToMap(bean, MapUtil.newHashMap(true), CopyOptions.create().setPropertiesFilter((filed, value)->
                Fc.contains(names,filed.getName()) || Fc.contains(declaredFields,filed)));
    }

    /**
     * list<bean>转换成list<map> 父类字段自动去除
     *
     * @author mr.g
     * @param collection bean对象
     * @param names 父类需要保留的字段
     * @return java.util.Map<java.lang.String,java.lang.Object>
     **/
    public static List<Map<String,Object>> listBeanToMapRemoveSuper(Collection<?> collection, String... names){
        if (null == collection) {
            return null;
        }
        if (collection.isEmpty()) {
            return new ArrayList<>(0);
        }

        return collection.stream().map((source) -> beanToMapRemoveSuper(source,names)).collect(Collectors.toList());
    }

}
