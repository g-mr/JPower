package com.wlcb.jpower.module.dictbind.interceptor;


import com.wlcb.jpower.module.base.annotation.Dict;
import com.wlcb.jpower.module.common.utils.BeanUtil;
import com.wlcb.jpower.module.common.utils.ClassUtil;
import com.wlcb.jpower.module.config.interceptor.chain.MybatisInterceptor;
import com.wlcb.jpower.module.dbs.entity.base.BaseEntity;
import com.wlcb.jpower.module.dictbind.handler.DictBindHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;

import java.lang.reflect.Field;
import java.sql.Statement;
import java.util.List;

/**
 * @author mr.g
 * @date 2021-05-20 17:00
 */
@Slf4j
@RequiredArgsConstructor
public class DictBindInterceptor implements MybatisInterceptor {

    private final DictBindHandler dictBindHandler;

    @Override
    public Object result(Object result, ResultSetHandler resultSetHandler, Statement statement){

        if (result instanceof List){
            List list = (List) result;
            if (list.size()>0){
                Object object = list.get(0);
                if (ClassUtil.isAssignable(BaseEntity.class, object.getClass())){
                    list.forEach(bean -> {
                        MetaObject metaObject = MetaObject.forObject(bean,new DefaultObjectFactory(),new DefaultObjectWrapperFactory(),new DefaultReflectorFactory());
                        List<Field> fields = BeanUtil.getFiledByAnnotation(bean.getClass(), Dict.class);
                        fields.forEach(field -> dictBindHandler.setMetaObject(field.getAnnotation(Dict.class), metaObject.getValue(field.getName()), metaObject));
                    });
                }
            }
        }

        return result;
    }

}
