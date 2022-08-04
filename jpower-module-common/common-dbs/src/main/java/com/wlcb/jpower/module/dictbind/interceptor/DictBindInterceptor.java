package com.wlcb.jpower.module.dictbind.interceptor;


import com.wlcb.jpower.module.base.annotation.Dict;
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

import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * 字典拦截器
 *
 * @author mr.g
 * @date 2021-05-20 17:00
 */
@Slf4j
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DictBindInterceptor implements MybatisInterceptor {

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
                        MetaObject metaObject = MetaObject.forObject(bean,new DefaultObjectFactory(),new DefaultObjectWrapperFactory(),new DefaultReflectorFactory());
                        BeanUtil.getFiledByAnnotation(bean.getClass(), Dict.class).forEach(field -> {
                            Dict dict = field.getAnnotation(Dict.class);
                            if (Fc.isNotBlank(dict.name())){
                                //判断需要赋值的字段是否存在于bean
                                if (Fc.isNotBlank(dict.attributes()) && ReflectUtil.hasField(bean.getClass(), dict.attributes())) {
                                    dictBindHandler.setMetaObject(dict, field.getName() ,metaObject.getValue(field.getName()), metaObject);
                                } else if (Fc.isBlank(dict.attributes())) {
                                    dictBindHandler.setMetaObject(dict, field.getName() ,metaObject.getValue(field.getName()), metaObject);
                                }
                            }

                        });
                        newList.add(metaObject.getOriginalObject());
                    });

                    return newList;
                }
            }
        }

        return result;
    }
}
