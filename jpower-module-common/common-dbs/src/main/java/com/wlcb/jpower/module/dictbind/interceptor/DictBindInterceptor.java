package com.wlcb.jpower.module.dictbind.interceptor;


import cn.hutool.core.util.ArrayUtil;
import com.wlcb.jpower.module.base.annotation.Dict;
import com.wlcb.jpower.module.common.support.BeanProperty;
import com.wlcb.jpower.module.common.utils.BeanUtil;
import com.wlcb.jpower.module.common.utils.ClassUtil;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.ReflectUtil;
import com.wlcb.jpower.module.config.interceptor.chain.MybatisInterceptor;
import com.wlcb.jpower.module.dbs.entity.base.BaseEntity;
import com.wlcb.jpower.module.dictbind.handler.IDictBindHandler;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author mr.g
 * @date 2021-05-20 17:00
 */
@Slf4j
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DictBindInterceptor implements MybatisInterceptor {

    private static final String DICT_ATTRIBUTES_SUFFIX = "Str";

    private IDictBindHandler dictBindHandler;

    @Override
    public Object result(Object result, ResultSetHandler resultSetHandler, Statement statement){

        if (result instanceof List){
            List list = (List) result;
            if (list.size()>0){
                Object object = list.get(0);
                if (ClassUtil.isAssignable(BaseEntity.class, object.getClass())){
                    List newList = new ArrayList(list.size());
                    list.forEach(bean -> {
                        bean = createField(bean);
                        MetaObject metaObject = MetaObject.forObject(bean,new DefaultObjectFactory(),new DefaultObjectWrapperFactory(),new DefaultReflectorFactory());
                        BeanUtil.getFiledByAnnotation(bean.getClass(), Dict.class).forEach(field -> {
                            Dict dict = field.getAnnotation(Dict.class);
                            if (Fc.isBlank(dict.attributes())){
                                setAttributesDefaultValue(dict,field.getName());
                            }
                            dictBindHandler.setMetaObject(dict, metaObject.getValue(field.getName()), metaObject);
                        });
                        newList.add(metaObject.getOriginalObject());
                    });

                    return newList;
                }
            }
        }

        return result;
    }

    /**
     * 创建字段
     * @author mr.g
     * @return 新生成得bean
     */
    private Object createField(final Object bean) {
        List<Field> fields = BeanUtil.getFiledByAnnotation(bean.getClass(), Dict.class);
        List<String> listAttr = fields.stream().map(field -> {
            Dict dict = field.getAnnotation(Dict.class);
            return Fc.isBlank(dict.attributes()) ? field.getName().concat(DICT_ATTRIBUTES_SUFFIX) : dict.attributes();
        }).filter(attribute -> !ReflectUtil.hasField(bean.getClass(), attribute)).collect(Collectors.toList());
        if (listAttr.size() > 0){
            List<BeanProperty> properties = new ArrayList<>();
            listAttr.forEach(a-> properties.add(new BeanProperty(a,String.class)));
            return BeanUtil.generator(bean, ArrayUtil.toArray(properties,BeanProperty.class));
        }

        return bean;
    }

    /**
     * 设置默认值
     * @author mr.g
     * @return void
     */
    private void setAttributesDefaultValue(Dict dict,String name){
        try{
            InvocationHandler invocationHandler = Proxy.getInvocationHandler(dict);
            Field f = invocationHandler.getClass().getDeclaredField("memberValues");
            f.setAccessible(true);
            Map<String, Object> memberValues = (Map<String, Object>) f.get(invocationHandler);
            memberValues.put("attributes", name.concat(DICT_ATTRIBUTES_SUFFIX));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
