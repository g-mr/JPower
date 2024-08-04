package top.jpower.core.util.utils;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import top.jpower.core.util.constants.StringPool;

import java.lang.annotation.Annotation;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 类工具类
 *
 * @author mr.g
 */
public class ClassUtil  extends cn.hutool.core.util.ClassUtil {

    /**
     * 获取SpringBoot的启动类
     *
     * @author mr.g
     * @return java.lang.Class<?>
     **/
    public static Class<?> getMainClass(){
        Class<?> clz = null;

        List<String> top = ClassUtil.scanPackage().stream().map(c->{
            String pk = ClassUtil.getPackage(c);
            return StringUtil.split(pk, StringPool.DOT).get(0);
        }).distinct().collect(Collectors.toList());

        for (String pk : top) {
            if (StringUtil.equalsAny(pk, "org", "java", "io")){
                continue;
            }
            try {
                clz = queryMainClass(pk, SpringBootApplication.class);
            } catch (Exception ignored){}
            if (clz != null){
                break;
            }
        }
        return clz;
    }

    /**
     * 获取SpringBoot的启动类
     *
     * @author mr.g
     * @param application 查找得启动类
     * @return java.lang.Class<?>
     **/
    private static Class<?> queryMainClass(String pk, Class<? extends Annotation> application){
        // todo 这里会报错，拿不到类
        Set<Class<?>> set = ClassUtil.scanPackageByAnnotation(pk, application);
        Iterator<Class<?>> iterable = set.iterator();
        Class<?> cls = null;
        while (iterable.hasNext()){
            Class<?> clz = iterable.next();
            if (ClassUtil.isNormalClass(clz)){
                if (ReflectUtil.hasMainMethod(clz)) {
                    cls = clz;
                    break;
                }
            } else if (clz.isAnnotation()){
                clz = queryMainClass(pk, (Class<? extends Annotation>) clz);
                if (Fc.notNull(clz)){
                    cls = clz;
                    break;
                }
            }
        }

        return cls;
    }

}
