package com.wlcb.jpower.wrapper;

import com.wlcb.jpower.cache.dict.DictConfig;
import com.wlcb.jpower.module.base.annotation.Dict;
import com.wlcb.jpower.module.common.support.BeanProperty;
import com.wlcb.jpower.module.common.utils.BeanUtil;
import com.wlcb.jpower.module.common.utils.ExceptionsUtil;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.ReflectUtil;
import com.wlcb.jpower.module.mp.support.BaseEntityWrapper;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName DictWrapper
 * @Description TODO
 * @Author 郭丁志
 * @Date 2020-10-22 15:24
 * @Version 1.0
 */
@Slf4j
public abstract class DictWrapper<T, V> extends BaseEntityWrapper<T, V> {

    @Override
    public V entityVO(T entity){
        V v = conver(entity);
        dict(v);
        return v;
    }

    /**
     * @author 郭丁志
     * @Description //TODO 查询字典值
     * @date 0:27 2020/10/22 0022
     */
    private static <V> void dict(V bean){
        List<Field> list = BeanUtil.getFiledByAnnotation(bean.getClass(), Dict.class);
        for (Field field : list) {
            Dict dict = field.getAnnotation(Dict.class);
            String dictTypeCode = dict.name();
            String attributes = dict.attributes();
            attributes = Fc.isBlank(attributes) ? field.getName().concat("Str") : attributes;
            try {
                String code = Fc.toStr(field.get(bean));
                String value = DictConfig.getDictByTypeAndCode(dictTypeCode, code);
                if (!BeanUtil.isContainsField(list, attributes)) {
                    bean = (V) BeanUtil.generator(bean, new BeanProperty(attributes, String.class));
                }
                ReflectUtil.invokeSetter(bean, attributes, value);
            } catch (IllegalAccessException e) {
                log.error("设置字典值失败,filed={},e={}", field.getName(), ExceptionsUtil.getStackTraceAsString(e));
            }

        }

    }

    public static <T,V> V dict(T entity,Class<V> clz){
        V v = BeanUtil.copy(entity,clz);
        dict(v);
        return v;
    }

    public static <T,V> List<V> dict(List<T> list, Class<V> v){
        return list.stream().map(t -> dict(t,v)).collect(Collectors.toList());
    }

}
