package com.wlcb.jpower.module.common.utils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @ClassName BeanToMapUtil
 * @Description TODO bean转map工具类
 * @Author 郭丁志
 * @Date 2020-04-17 16:23
 * @Version 1.0
 */
public class BeanToMapUtil {

    /**
     * 将一个 JavaBean 对象转化为一个 Map
     * @param bean
     * @return
     * @throws IntrospectionException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static Map<String, String> convertBean2Map(Object bean)
        throws IntrospectionException, IllegalAccessException, InvocationTargetException
    {
        Class<? extends Object> type = bean.getClass();
        Map<String, String> returnMap = new HashMap<>();
        BeanInfo beanInfo = Introspector.getBeanInfo(type);
        
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (int i = 0; i < propertyDescriptors.length; i++)
        {
            PropertyDescriptor descriptor = propertyDescriptors[i];
            String propertyName = descriptor.getName();
            if (!"class".equals(propertyName))
            {
                Method readMethod = descriptor.getReadMethod();
                String result = String.valueOf(readMethod.invoke(bean, new Object[0]));
                if (result != null)
                {
                    returnMap.put(propertyName, result);
                }
                else
                {
                    returnMap.put(propertyName, null);
                }
            }
        }
        return returnMap;
    }
    

    /**
      * 将 List<JavaBean>对象转化为List<Map>
      * @param beanList
      * @return
      * @throws Exception
      */
    public static <T> List<Map<String, String>> convertListBean2ListMap(List<T> beanList, Class<T> T)
        throws Exception
    {
        List<Map<String, String>> mapList = new ArrayList<>();
        for (int i = 0, n = beanList.size(); i < n; i++)
        {
            Object bean = beanList.get(i);
            Map<String, String> map = convertBean2Map(bean);
            mapList.add(map);
        }
        return mapList;
    }

}
