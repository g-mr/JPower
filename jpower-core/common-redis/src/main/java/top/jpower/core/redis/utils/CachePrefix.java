package top.jpower.core.redis.utils;

import org.slf4j.MDC;
import top.jpower.core.util.utils.Fc;

/**
 * @author mr.g
 * @date 2024-10-24 23:31
 * @description
 */
public class CachePrefix {

    private final static String REMOVE_PREFIX = "removePrefix";

    /**
     * 清除缓存前缀
     *
     * @author mr.g
     **/
    public static void clear(){
        MDC.put(REMOVE_PREFIX, "true");
    }

    /**
     * 是否清除缓存前缀
     *
     * @author mr.g
     **/
    public static boolean isClear(){
        return Fc.toBoolean(MDC.get(REMOVE_PREFIX), false);
    }

    /**
     * 关闭清除缓前缀
     *
     * @author mr.g
     **/
    public static void close(){
        MDC.remove(REMOVE_PREFIX);
    }

}
