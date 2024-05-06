package top.jpower.core.util.utils;

/**
 * Spring工具类
 *
 * @author mr.g
 */
public class SpringUtil extends cn.hutool.extra.spring.SpringUtil {

    /**
     * 是否存在bean
     *
     * @author mr.g
     * @param beanId bean名称
     * @return 是否存在
     **/
    public static boolean contains(String beanId) {
        return getBeanFactory().containsBean(beanId);
    }

}
