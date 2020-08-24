package com.wlcb.jpower.module.common.nacos;

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
    String DATA_ID = "jpower".concat(StringPool.DOT).concat(FILE_EXTENSION);

    /** 通用开发环境配置文件 **/
    String DATA_ID_DEV = "jpower-dev".concat(StringPool.DOT).concat(FILE_EXTENSION);

    /** 通用测试环境配置文件 **/
    String DATA_ID_TEST = "jpower-test".concat(StringPool.DOT).concat(FILE_EXTENSION);

    /** 通用生产环境配置文件 **/
    String DATA_ID_PROD = "jpower-prod".concat(StringPool.DOT).concat(FILE_EXTENSION);

    /** 配置文件是否支持动态刷新 **/
    String CONFIG_REFRESH = "true";

    /** 分组 **/
    String CONFIG_GROUP = "DEFAULT_GROUP";

    /**
     * 动态获取nacos地址
     *
     * @param profile 环境变量
     * @return addr
     */
    static String nacosDataId(String profile) {
        switch (profile) {
            case (AppConstant.PROD_CODE):
                return DATA_ID_PROD;
            case (AppConstant.TEST_CODE):
                return DATA_ID_TEST;
            default:
                return DATA_ID_DEV;
        }
    }
}
