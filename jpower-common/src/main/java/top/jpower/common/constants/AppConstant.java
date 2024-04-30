package top.jpower.common.constants;

import lombok.Data;
import top.jpower.core.utils.constants.JpowerConstants;
import top.jpower.core.utils.constants.StringPool;
import top.jpower.core.utils.utils.Fc;
import top.jpower.jpower.module.base.constants.LogConstant;
import top.jpower.jpower.module.common.constants.ClientNameConstant;
import top.jpower.jpower.module.common.nacos.NacosConstants;

/**
 * 服务名常量
 *
 * @author mr.g
 **/
@Data
public class AppConstant implements NacosConstants, LogConstant, ClientNameConstant {

    /** 项目名称 **/
    public static final String JPOWER = "jpower";

    /** spring boot admin **/
    public static final String JPOWER_ADMIN = JPOWER + "-admin";

    /** swagger聚合文档 **/
    public static final String JPOWER_DOC = JPOWER + "-doc";

    /** 日志服务 **/
    public static final String JPOWER_LOG = JPOWER + "-log";

    /** 网关模块名称 **/
    public static final String JPOWER_GATEWAY = JPOWER + "-gateway";

    /** 系统模块名称 **/
    public static final String JPOWER_SYSTEM = JPOWER + "-system";

    /** 鉴权模块 **/
    public static final String JPOWER_AUTH = JPOWER + "-auth";

    /** 用户模块 **/
    public static final String JPOWER_USER = JPOWER + "-user";

    /** 文件模块名称 **/
    public static final String JPOWER_RESOURCE = JPOWER + "-resource";

    /**
     * 动态获取公共nacos地址
     *
     * @param profile 环境变量
     * @return addr
     */
    @Override
    public String nacosProfileDataId(String profile) {
        if (Fc.isBlank(profile)){
            profile = JpowerConstants.DEV_CODE;
        }

        return JPOWER.concat(StringPool.DASH).concat(profile).concat(StringPool.DOT).concat(FILE_EXTENSION);
    }

    /**
     * 动态获取公共nacos地址
     *
     * @return addr
     */
    @Override
    public String nacosDataId() {
        return JPOWER.concat(StringPool.DOT).concat(FILE_EXTENSION);
    }

    @Override
    public String getJpowerLog() {
        return JPOWER_LOG;
    }

    @Override
    public String getJpowerSystem() {
        return JPOWER_SYSTEM;
    }
}
