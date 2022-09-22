package com.wlcb.jpower.module.common.utils.constants;

import com.wlcb.jpower.module.base.annotation.SystemName;

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

    /** 项目名称 **/
    @SystemName
    String JPOWER = "jpower";

    /** spring boot admin **/
    String JPOWER_ADMIN = JPOWER + "-admin";

    /** swagger聚合文档 **/
    String JPOWER_DOC = JPOWER + "-doc";

    /** 日志服务 **/
    String JPOWER_LOG = JPOWER + "-log";

    /** 网关模块名称 **/
    String JPOWER_GATEWAY = JPOWER + "-gateway";

    /** 系统模块名称 **/
    String JPOWER_SYSTEM = JPOWER + "-system";

    /** 鉴权模块 **/
    String JPOWER_AUTH = JPOWER + "-auth";

    /** 用户模块 **/
    String JPOWER_USER = JPOWER + "-user";

    /** 文件模块名称 **/
    String JPOWER_FILE = JPOWER + "-file";

}
