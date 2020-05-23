package com.wlcb.jpower.module.common.utils.param;

import com.wlcb.jpower.module.common.service.core.params.CoreParamService;
import com.wlcb.jpower.module.common.service.redis.RedisUtils;
import com.wlcb.jpower.module.common.utils.constants.ConstantsUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @ClassName ParamConfig
 * @Description TODO 获取参数
 * @Author 郭丁志
 * @Date 2020-05-06 14:55
 * @Version 1.0
 */
@Component
public class ParamConfig {

    @Autowired
    private RedisUtils utils;
    @Autowired
    private CoreParamService service;

    private static RedisUtils redisUtils;
    private static CoreParamService paramService;

    @PostConstruct
    public void init() {
        redisUtils = utils;
        paramService = service;
    }

    private static final String prefix = ConstantsUtils.PROPERTIES_PREFIX;

    /**
     * @Author 郭丁志
     * @Description //TODO 获取string类型参数
     * @Date 15:47 2020-05-06
     * @Param [key]
     * @return java.lang.String
     **/
    public static String getString(String code){
        String vlaue = (String) redisUtils.get(prefix+code);
        if (StringUtils.isBlank(vlaue)){
            vlaue = paramService.selectByCode(code);

            if (StringUtils.isNotBlank(vlaue)){
                redisUtils.set(prefix+code,vlaue);
            }
        }
        return vlaue;
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
        Integer vlaue = Integer.parseInt(getString(code));
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
