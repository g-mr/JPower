package com.wlcb.jpower.module.common.nacos;

import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.constants.AppConstant;
import com.wlcb.jpower.module.common.utils.constants.StringPool;

/**
 * @ClassName NacosConstants
 * @Description TODO
 * @Author 郭丁志
 * @Date 2020-08-19 16:03
 * @Version 1.0
 */
public interface NacosConstants {

    /** 配置文件类型 **/
    String FILE_EXTENSION = "yaml";

    /** 通用配置文件 **/
    String DATA_ID = AppConstant.JPOWER.concat(StringPool.DOT).concat(FILE_EXTENSION);

    /** 配置文件是否支持动态刷新 **/
    String CONFIG_REFRESH = "true";

    /** 分组 **/
    String CONFIG_GROUP = "DEFAULT_GROUP";

    /**
     * 动态获取公共nacos地址
     *
     * @param profile 环境变量
     * @return addr
     */
    static String nacosProfileDataId(String profile) {
        if (Fc.isBlank(profile)){
            profile = AppConstant.DEV_CODE;
        }
        return AppConstant.JPOWER.concat(StringPool.DASH).concat(profile).concat(StringPool.DOT).concat(FILE_EXTENSION);
    }

}
