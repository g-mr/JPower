package com.wlcb.jpower.module.base.utils;

/**
 * @ClassName AppConstant
 * @Description TODO 环境常量
 * @Author 郭丁志
 * @Date 2020-08-02 18:12
 * @Version 1.0
 */
public interface AppConstant {

    /**
     * 开发环境
     */
    String DEV_CODE = "dev";
    /**
     * 生产环境
     */
    String PROD_CODE = "prod";
    /**
     * 测试环境
     */
    String TEST_CODE = "test";

    /**
     * 代码部署于 linux 上，工作默认为 mac 和 Windows
     */
    String OS_NAME_LINUX = "LINUX";

}
