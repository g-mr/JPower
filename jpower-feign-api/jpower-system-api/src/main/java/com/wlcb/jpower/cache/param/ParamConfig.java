package com.wlcb.jpower.cache.param;

import com.wlcb.jpower.feign.ParamsClient;
import com.wlcb.jpower.module.common.cache.CacheNames;
import com.wlcb.jpower.module.common.utils.CacheUtil;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.SpringUtil;
import org.apache.commons.lang3.StringUtils;

/**
 * 系统参数缓存
 *
 * @author mr.g
 **/
public class ParamConfig {

    private static ParamsClient paramsClient;

    static {
        paramsClient = SpringUtil.getBean(ParamsClient.class);
    }

    /**
     * 获取string类型参数
     *
     * @author mr.g
     * @param code CODE
     * @return 值
     **/
    public static String getString(String code){
        return CacheUtil.get(CacheNames.PARAM_KEY,CacheNames.PARAM_CODE_KEY,code,() -> paramsClient.queryByCode(code).getData(), Boolean.FALSE);
    }

    /**
     * 获取string类型参数,并给一个默认值
     *
     * @author mr.g
     * @param code CODE
     * @param defaultValue 默认值
     * @return 值
     **/
    public static String getString(String code,String defaultValue){
        String value = getString(code);
        return StringUtils.isBlank(value)?defaultValue:value;
    }

    /**
     * 获取Int类型参数值
     *
     * @author mr.g
     * @param code CODE
     * @return 值
     **/
    public static Integer getInt(String code){
        String p = getString(code);
        Integer value = null;
        if (StringUtils.isNotBlank(p)){
            value = Fc.toInt(p);
        }
        return value;
    }

    /**
     * 获取int类型参数,并给一个默认值
     *
     * @author mr.g
     * @param code CODE
     * @param defaultValue 默认值
     * @return 值
     **/
    public static Integer getInt(String code,Integer defaultValue){
        Integer value = getInt(code);
        return value==null?defaultValue:value;
    }

    /**
     * 获取Double类型参数
     *
     * @author mr.g
     * @param code CODE
     * @return 值
     **/
    public static Double getDouble(String code){
        return Fc.toDouble(getString(code));
    }

    /**
     * 获取double类型参数,并给一个默认值
     *
     * @author mr.g
     * @param code CODE
     * @param defaultValue 默认值
     * @return 值
     **/
    public static Double getDouble(String code,Double defaultValue){
        Double value = getDouble(code);
        return value==null?defaultValue:value;
    }

    /**
     * 获取Long类型参数
     *
     * @author mr.g
     * @param code CODE
     * @return 值
     **/
    public static Long getLong(String code){
        return Fc.toLong(getString(code));
    }

    /**
     * 获取Long类型参数,并给一个默认值
     *
     * @author mr.g
     * @param code CODE
     * @param defaultValue 默认值
     * @return 值
     **/
    public static Long getLong(String code,Long defaultValue){
        Long value = getLong(code);
        return value==null?defaultValue:value;
    }

    /**
     * 获取Boolean类型参数
     *
     * @author mr.g
     * @param code CODE
     * @return 值
     **/
    public static Boolean getBoolean(String code){
        return Fc.toBool(getString(code));
    }

    /**
     * 获取Boolean类型参数,并给一个默认值
     *
     * @author mr.g
     * @param code CODE
     * @param defaultValue 默认值
     * @return 值
     **/
    public static Boolean getBoolean(String code, boolean defaultValue){
        Boolean value = getBoolean(code);
        return value==null?defaultValue:value;
    }

}
