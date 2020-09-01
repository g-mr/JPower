package com.wlcb.jpower.config.param;

import com.wlcb.jpower.module.common.cache.CacheNames;
import com.wlcb.jpower.module.common.service.core.params.CoreParamService;
import com.wlcb.jpower.module.common.utils.CacheUtil;
import com.wlcb.jpower.module.common.utils.SpringUtil;
import org.apache.commons.lang3.StringUtils;

/**
 * @ClassName ParamConfig
 * @Description TODO 获取系统参数
 * @Author 郭丁志
 * @Date 2020-05-06 14:55
 * @Version 1.0
 */
public class ParamConfig {

    private static CoreParamService paramService;

    static {
        paramService = SpringUtil.getBean(CoreParamService.class);
    }

    private static final String PREFIX = CacheNames.PARAMS_REDIS_CODE_KEY;

    /**
     * @Author 郭丁志
     * @Description //TODO 获取string类型参数
     * @Date 15:47 2020-05-06
     **/
    public static String getString(String code){
        return CacheUtil.get(CacheNames.PARAMS_REDIS_CACHE,PREFIX,code,() -> paramService.selectByCode(code));
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 获取string类型参数,并给一个默认值
     * @Date 16:26 2020-05-06
     * @Param [code, defaultValue]
     * @return java.lang.String
     **/
    public static String getString(String code,String defaultValue){
        String vlaue = getString(code);
        return StringUtils.isBlank(vlaue)?defaultValue:vlaue;
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 获取Int类型参数值
     * @Date 16:27 2020-05-06
     * @Param [code]
     * @return java.lang.Integer
     **/
    public static Integer getInt(String code){
        String p = getString(code);
        Integer vlaue = null;
        if (StringUtils.isNotBlank(p)){
            vlaue = Integer.parseInt(p);
        }
        return vlaue;
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 获取Int类型参数值，并给一个默认值
     * @Date 16:29 2020-05-06
     * @Param [code, defaultValue]
     * @return java.lang.Integer
     **/
    public static Integer getInt(String code,Integer defaultValue){
        Integer vlaue = getInt(code);
        return vlaue==null?defaultValue:vlaue;
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 获取Double类型参数值
     * @Date 16:27 2020-05-06
     * @Param [code]
     * @return java.lang.Integer
     **/
    public static Double getDouble(String code){
        Double vlaue = Double.parseDouble(getString(code));
        return vlaue;
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 获取Double类型参数值，并给一个默认值
     * @Date 16:29 2020-05-06
     * @Param [code, defaultValue]
     * @return java.lang.Integer
     **/
    public static Double getInt(String code,Double defaultValue){
        Double vlaue = getDouble(code);
        return vlaue==null?defaultValue:vlaue;
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 获取Long类型参数值
     * @Date 16:27 2020-05-06
     * @Param [code]
     * @return java.lang.Integer
     **/
    public static Long getLong(String code){
        Long vlaue = Long.parseLong(getString(code));
        return vlaue;
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 获取Long类型参数值，并给一个默认值
     * @Date 16:29 2020-05-06
     * @Param [code, defaultValue]
     * @return java.lang.Integer
     **/
    public static Long getLong(String code,Long defaultValue){
        Long vlaue = getLong(code);
        return vlaue==null?defaultValue:vlaue;
    }
}
