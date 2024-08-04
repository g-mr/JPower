package top.jpower.core.exception.enums.constants;

import cn.hutool.core.exceptions.UtilException;
import top.jpower.core.util.constants.StringPool;
import top.jpower.core.util.utils.ClassUtil;
import top.jpower.core.util.utils.Fc;
import top.jpower.core.util.utils.ReflectUtil;

import java.util.Set;

/**
 * @author mr.g
 * @date 2024/4/30 4:38 PM
 */
public interface LogConstant {

    String JPOWER_LOG = "jpower-log";

    static LogConstant getInstance(){
        Set<Class<?>> set = ClassUtil.scanPackageBySuper(StringPool.EMPTY, LogConstant.class);
        if (Fc.isEmpty(set)){
            return new LogConstant() {};
        }

        return set.stream().map(clz->{
            try {
                return (LogConstant) ReflectUtil.newInstance(clz);
            } catch (UtilException e){
                return null;
            }
        }).filter(Fc::notNull).findFirst().get();
    }

    default String getJpowerLog() {
        return JPOWER_LOG;
    }
}
