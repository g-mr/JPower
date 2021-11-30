package com.wlcb.jpower.module.common.utils;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.Nullable;

import java.util.*;

/**
 * @ClassName CollectionUtil
 * @Description TODO list工具类
 * @Author 郭丁志
 * @Date 2020-07-23 15:15
 * @Version 1.0
 */
public class CollectionUtil extends CollUtil {

    /**
     * 新建一个HashSet
     *
     * @param <T>        集合元素类型
     * @param collection 集合
     * @return HashSet对象
     */
    public static <T> HashSet<T> newHashSet(Collection<T> collection) {
        return isEmpty(collection) ? newHashSet() : newHashSet(false, collection);
    }

    /**
     * 判断给定的数组是否包含指定的元素
     */
    public static <T> boolean contains(@Nullable T[] array, final T element) {
        if (array == null) {
            return false;
        }
        return Arrays.stream(array).anyMatch(x -> ObjectUtil.nullSafeEquals(x, element));
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
        String[] strings = new String[arrays.length-1];
        System.arraycopy(arrays, 1, strings, 0, strings.length);
        return strings;
    }

}
