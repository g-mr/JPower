package com.wlcb.jpower.module.common.utils;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * @ClassName CollectionUtil
 * @Description TODO list工具类
 * @Author 郭丁志
 * @Date 2020-07-23 15:15
 * @Version 1.0
 */
public class CollectionUtil extends CollectionUtils{

    /**
     * Check whether the given Array contains the given element.
     *
     * @param array   the Array to check
     * @param element the element to look for
     * @param <T>     The generic tag
     * @return {@code true} if found, {@code false} else
     */
    public static <T> boolean contains(@Nullable T[] array, final T element) {
        if (array == null) {
            return false;
        }
        return Arrays.stream(array).anyMatch(x -> ObjectUtil.nullSafeEquals(x, element));
    }

    /**
     * 对象是否为数组对象
     *
     * @param obj 对象
     * @return 是否为数组对象，如果为{@code null} 返回false
     */
    public static boolean isArray(Object obj) {
        if (null == obj) {
            return false;
        }
        return obj.getClass().isArray();
    }

    /**
     * Determine whether the given Collection is not empty:
     * i.e. {@code null} or of zero length.
     *
     * @param coll the Collection to check
     * @return boolean
     */
    public static boolean isNotEmpty(@Nullable Collection<?> coll) {
        return !CollectionUtils.isEmpty(coll);
    }

    /**
     * Determine whether the given Map is not empty:
     * i.e. {@code null} or of zero length.
     *
     * @param map the Map to check
     * @return boolean
     */
    public static boolean isNotEmpty(@Nullable Map<?, ?> map) {
        return !CollectionUtils.isEmpty(map);
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 分割list
     * @Date 14:51 2020-07-24
     * @Param [list, size]
     * @return java.util.List<java.util.List<T>>
     **/
    public static <T> List<List<T>> split(List<T> list, int size){
        return Lists.partition(list,size);
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 将map转换成Ascii码从小到大排序的keyvalue格式字符串
     * @Date 17:47 2020-03-21
     * @Param [map]
     * @return java.lang.String
     **/
    public static String getAsciiKeyValue(Map<String, String> map) {

        Set<String> keySet = map.keySet();
        String[] keyArray = (String[])keySet.toArray(new String[keySet.size()]);
        Arrays.sort(keyArray);

        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < keyArray.length; ++i) {
            String k = keyArray[i];
            if (StringUtils.isNotBlank(k) && !k.equals("sign") && ((String)map.get(k)).trim().length() > 0) {
                sb.append(k).append("=").append(((String)map.get(k)).trim()).append("&");
            }
        }

        return sb.toString();
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 删除数组第一个元素
     * @Date 16:11 2020-07-02
     * @Param [ips]
     * @return java.lang.String
     **/
    public static String[] removeStart(String[] arrays){
        String[] ss = new String[arrays.length-1];
        System.arraycopy(arrays, 1, ss, 0, ss.length);
        return ss;
    }

}
