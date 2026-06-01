package top.jpower.core.dbs.dictbind.interceptor;


import com.mybatisflex.core.util.EnumWrapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import top.jpower.core.dbs.config.interceptor.chain.MybatisInterceptor;
import top.jpower.core.dbs.dictbind.annotation.Dict;
import top.jpower.core.dbs.dictbind.handler.IDictBindHandler;
import top.jpower.core.util.utils.BeanUtil;
import top.jpower.core.util.utils.ClassUtil;
import top.jpower.core.util.utils.Fc;
import top.jpower.core.util.utils.ReflectUtil;

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

    private final static String DICT_PARAMS = "params";
    private IDictBindHandler dictBindHandler;

    @Override
    public Object result(Object result, ResultSetHandler resultSetHandler, Statement statement){

        if (result instanceof List){
            List list = (List) result;
            if (list.size()>0){
                Object object = list.get(0);
                if (Fc.notNull(object) && !ClassUtil.isSimpleValueType(object.getClass())){
                    List newList = new ArrayList(list.size());
                    list.forEach(bean -> {
                        MetaObject metaObject = MetaObject.forObject(bean,new DefaultObjectFactory(),new DefaultObjectWrapperFactory(),new DefaultReflectorFactory());
                        BeanUtil.getFiledByAnnotation(bean.getClass(), Dict.class).forEach(field -> {
                            Dict dict = field.getAnnotation(Dict.class);
                            if (Fc.isNotBlank(dict.name())){
                                Object val = metaObject.getValue(field.getName());
                                if (Fc.notNull(val)) {
                                    if (ClassUtil.isEnum(field.getType())) {
                                        val = EnumWrapper.of(field.getType()).getEnumValue(val);
                                    }
                                    //判断需要赋值的字段是否存在于bean todo 回头这里需要优化，简化IDictBindHandler的实现
                                    if ((Fc.isNotBlank(dict.attributes()) && ReflectUtil.hasField(bean.getClass(), dict.attributes()))
                                            || (Fc.isBlank(dict.attributes()) && ReflectUtil.hasField(bean.getClass(), dict.attributes()))) {
                                        dictBindHandler.setMetaObject(dict, field.getName() , val, metaObject);
                                    }
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
