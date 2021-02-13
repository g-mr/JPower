package com.wlcb.jpower.wrapper;

import com.wlcb.jpower.cache.dict.DictCache;
import com.wlcb.jpower.module.base.annotation.Dict;
import com.wlcb.jpower.module.common.support.BeanProperty;
import com.wlcb.jpower.module.common.utils.BeanUtil;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.ReflectUtil;
import com.wlcb.jpower.module.mp.support.BaseWrapper;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName DictWrapper
 * @Description TODO 查询字典
 * @Author 郭丁志
 * @Date 2020-10-22 15:24
 * @Version 1.0
 */
@Slf4j
public abstract class BaseDictWrapper<T, V> extends BaseWrapper<T, V> {

    private static final String DICT_ATTRIBUTES_SUFFIX = "Str";


    public static <T, V> BaseDictWrapper<T, V> builder(){
        return new BaseDictWrapper<T, V>() {
            @Override
            protected Object conver(Object entity) {
                return entity;
            }
        };
    }

    @Override
    public V entityVO(T entity){
        return dict(conver(entity));
    }

    /**
     * @author 郭丁志
     * @Description //TODO 查询字典值
     * @date 0:27 2020/10/22 0022
     */
    public static <V> V dict(V bean){
        List<Field> list = BeanUtil.getFiledByAnnotation(bean.getClass(), Dict.class);
        for (Field field : list) {
            Dict dict = field.getAnnotation(Dict.class);
            String dictTypeCode = dict.name();
            String attributes = dict.attributes();
            attributes = Fc.isBlank(attributes) ? field.getName().concat(DICT_ATTRIBUTES_SUFFIX) : attributes;
            String code = Fc.toStr(ReflectUtil.invokeGetter(bean,field.getName()));
            String value = DictCache.getDictByTypeAndCode(dictTypeCode, code);
            if (!BeanUtil.isContainsField(BeanUtil.getFieldList(bean.getClass()), attributes)) {
                bean = (V) BeanUtil.generator(bean, new BeanProperty(attributes, String.class));
            }
            ReflectUtil.invokeSetter(bean, attributes, value);
        }
        return bean;
    }

    public V dict(T entity,Class<V> clz){
        return dict(BeanUtil.copy(entity,clz));
    }

    public List<V> dict(List<T> list, Class<V> v){
        return list.stream().map(t -> dict(t,v)).collect(Collectors.toList());
    }

    public static <V> List<V> dict(List<V> list){
        return list.stream().map(BaseDictWrapper::dict).collect(Collectors.toList());
    }

}
