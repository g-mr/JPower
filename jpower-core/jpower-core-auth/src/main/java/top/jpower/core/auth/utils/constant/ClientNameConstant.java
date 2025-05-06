package top.jpower.core.auth.utils.constant;

import cn.hutool.core.exceptions.UtilException;
import top.jpower.core.util.constants.StringPool;
import top.jpower.core.util.utils.ClassUtil;
import top.jpower.core.util.utils.Fc;
import top.jpower.core.util.utils.ReflectUtil;

import java.util.Set;

/**
 * @author mr.g
 * @date 2024/4/30 5:03 PM
 */
public interface ClientNameConstant {

    String JPOWER_SYSTEM = "jpower-system";

    static ClientNameConstant getInstance(){
        Set<Class<?>> set = ClassUtil.scanPackageBySuper(StringPool.EMPTY, ClientNameConstant.class);
        if (Fc.isEmpty(set)){
            return new ClientNameConstant() {};
        }

        return set.stream().map(clz->{
            try {
                return (ClientNameConstant) ReflectUtil.newInstance(clz);
            } catch (UtilException e){
                return null;
            }
        }).filter(Fc::notNull).findFirst().get();
    }

    default String getJpowerSystem() {
        return JPOWER_SYSTEM;
    }

}
