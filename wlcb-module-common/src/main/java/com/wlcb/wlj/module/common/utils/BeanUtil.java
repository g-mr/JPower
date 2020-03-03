package com.wlcb.wlj.module.common.utils;

import com.wlcb.wlj.module.base.vo.ResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.SimpleDateFormat;
import java.util.*;

public class BeanUtil {

    private static final Logger logger = LoggerFactory.getLogger(BeanUtil.class);

    private static final String fieldNullList = "serialVersionUID,id,createUser,createTime,updateUser,updateTime,createDate,updateDate,status";

    /**
     * @Author 郭丁志
     * @Description //TODO 判断类中属性是否都为空
     * @Date 18:56 2020-03-03
     * @Param [o]
     * @return com.wlcb.wlj.module.base.vo.ResponseData
     **/
    public static ResponseData allFieldIsNULL(Object o){
        try {
            for (Field field : getFieldList(o.getClass())) {
                field.setAccessible(true);

                Object object = field.get(o);
                String name = field.getName();
                if(!fieldNullList.contains(name)){
                    if (object instanceof CharSequence) {
                        if (org.springframework.util.ObjectUtils.isEmpty(object)) {
                            return ReturnJsonUtil.printJson(-1,name+"不可为空",false);
                        }
                    } else {
                        if (null == object) {
                            return ReturnJsonUtil.printJson(-1,name+"不可为空",false);
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("判断对象属性为空异常", e);

        }
        ResponseData responseData=new ResponseData();
        responseData.setCode(0);
        return responseData;
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 判断类中指定属性是否都为空
     * @Date 18:56 2020-03-03
     * @Param [o, fields]
     * @return com.wlcb.wlj.module.base.vo.ResponseData
     **/
    public static ResponseData allFieldIsNULL(Object o,String... fields){
        try {
            for (Field field : getFieldList(o.getClass())) {
                field.setAccessible(true);

                Object object = field.get(o);
                String name = field.getName();
                Arrays.sort(fields);
                if(Arrays.binarySearch(fields, name) >= 0){
                    if (object instanceof CharSequence) {
                        if (org.springframework.util.ObjectUtils.isEmpty(object)) {
                            return ReturnJsonUtil.printJson(-1,name+"不可为空",false);
                        }
                    } else {
                        if (null == object) {
                            return ReturnJsonUtil.printJson(-1,name+"不可为空",false);
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("判断对象属性为空异常", e);

        }
        ResponseData responseData=new ResponseData();
        responseData.setCode(0);
        return responseData;
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 获取属性列表
     * @Date 18:50 2020-03-03
     * @Param [clazz]
     * @return java.util.List<java.lang.reflect.Field>
     **/
    public static List<Field> getFieldList(Class<?> clazz){
        if(null == clazz){
            return null;
        }
        List<Field> fieldList = new LinkedList<Field>();
        Field[] fields = clazz.getDeclaredFields();
        for(Field field : fields){
            /** 过滤静态属性**/
            if(Modifier.isStatic(field.getModifiers())){
                continue;
            }
            /** 过滤transient 关键字修饰的属性**/
            if(Modifier.isTransient(field.getModifiers())){
                continue;
            }
            fieldList.add(field);
        }
        /** 处理父类字段**/
        Class<?> superClass = clazz.getSuperclass();
        if(superClass.equals(Object.class)) {
            return fieldList;
        }

        if(superClass.getName().equals("com.gdz.work.core.dbs.entity.BaseBean")){
            fieldList.addAll(getFieldList(superClass));
        }
        return fieldList;
    }


    /**
     * @Author 郭丁志
     * @Description //TODO set属性的值到Bean
     * @Date 18:56 2020-03-03
     * @Param [bean, valMap]
     * @return void
     **/
    public static void setFieldValue(Object bean, Map<String, String> valMap) {
        Class<?> cls = bean.getClass();
        // 取出bean里的所有方法
        Method[] methods = cls.getMethods();
        List<Field> fields = getFieldList(cls);

        for (Field field : fields) {
            try {

                String fieldSetName = parSetName(field.getName());
                if (!checkSetMet(methods, fieldSetName)) {
                    continue;
                }
                Method fieldSetMet = cls.getMethod(fieldSetName, field.getType());
                String value = valMap.get(field.getName());
                if (null != value && !"".equals(value)) {
                    String fieldType = field.getType().getSimpleName();
                    if ("String".equals(fieldType)) {
                        fieldSetMet.invoke(bean, value);
                    } else if ("Date".equals(fieldType)) {
                        Date temp = parseDate(value);
                        fieldSetMet.invoke(bean, temp);
                    } else if ("Integer".equals(fieldType)
                            || "int".equals(fieldType)) {
                        Integer intval = Integer.parseInt(value);
                        fieldSetMet.invoke(bean, intval);
                    } else if ("Long".equalsIgnoreCase(fieldType)) {
                        Long temp = Long.parseLong(value);
                        fieldSetMet.invoke(bean, temp);
                    } else if ("Double".equalsIgnoreCase(fieldType)) {
                        Double temp = Double.parseDouble(value);
                        fieldSetMet.invoke(bean, temp);
                    } else if ("Boolean".equalsIgnoreCase(fieldType)) {
                        Boolean temp = Boolean.parseBoolean(value);
                        fieldSetMet.invoke(bean, temp);
                    } else {
                        System.out.println("not supper type" + fieldType);
                    }
                }
            } catch (Exception e) {
                continue;
            }
        }

    }

    /**
     * @Author 郭丁志
     * @Description //TODO 格式化string为Date
     * @Date 18:56 2020-03-03
     * @Param [datestr]
     * @return java.util.Date
     **/
    public static Date parseDate(String datestr) {
        if (null == datestr || "".equals(datestr)) {
            return null;
        }
        try {
            String fmtstr = null;
            if (datestr.indexOf(':') > 0) {
                fmtstr = "yyyy-MM-dd HH:mm:ss";
            } else {

                fmtstr = "yyyy-MM-dd";
            }
            SimpleDateFormat sdf = new SimpleDateFormat(fmtstr, Locale.UK);
            return sdf.parse(datestr);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 取Bean的属性和值对应关系的MAP
     * @Date 18:56 2020-03-03
     * @Param [bean]
     * @return java.util.Map<java.lang.String,java.lang.String>
     **/
    public static Map<String, String> getFieldValueMap(Object bean) {
        Class<?> cls = bean.getClass();
        Map<String, String> valueMap = new HashMap<String, String>();
        // 取出bean里的所有方法
        Method[] methods = cls.getMethods();
        List<Field> fields = getFieldList(cls);

        for (Field field : fields) {
            try {
                String fieldType = field.getType().getSimpleName();
                String fieldGetName = parGetName(field.getName());
                if (!checkGetMet(methods, fieldGetName)) {
                    continue;
                }
                Method fieldGetMet = cls
                        .getMethod(fieldGetName, new Class[] {});
                Object fieldVal = fieldGetMet.invoke(bean, new Object[] {});
                String result = null;
                if ("Date".equals(fieldType)) {
                    result = fmtDate((Date) fieldVal);
                } else {
                    if (null != fieldVal) {
                        result = String.valueOf(fieldVal);
                    }
                }
                valueMap.put(field.getName(), result);
            } catch (Exception e) {
                continue;
            }
        }
        return valueMap;
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 判断是否存在某属性的 set方法
     * @Date 18:56 2020-03-03
     * @Param [methods, fieldSetMet]
     * @return boolean
     **/
    public static boolean checkSetMet(Method[] methods, String fieldSetMet) {
        for (Method met : methods) {
            if (fieldSetMet.equals(met.getName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 拼接在某属性的 set方法
     * @param fieldName
     * @return String
     */
    public static String parSetName(String fieldName) {
        if (null == fieldName || "".equals(fieldName)) {
            return null;
        }
        return "set" + fieldName.substring(0, 1).toUpperCase()
                + fieldName.substring(1);
    }

    /**
     * 拼接某属性的 get方法
     * @param fieldName
     * @return String
     */
    public static String parGetName(String fieldName) {
        if (null == fieldName || "".equals(fieldName)) {
            return null;
        }
        return "get" + fieldName.substring(0, 1).toUpperCase()
                + fieldName.substring(1);
    }

    /**
     * 判断是否存在某属性的 get方法
     * @param methods
     * @param fieldGetMet
     * @return boolean
     */
    public static boolean checkGetMet(Method[] methods, String fieldGetMet) {
        for (Method met : methods) {
            if (fieldGetMet.equals(met.getName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 日期转化为String
     * @param date
     * @return date string
     */
    public static String fmtDate(Date date) {
        if (null == date) {
            return null;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                    Locale.US);
            return sdf.format(date);
        } catch (Exception e) {
            return null;
        }
    }
}
