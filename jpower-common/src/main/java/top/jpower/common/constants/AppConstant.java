package top.jpower.common.constants;

import lombok.Data;
import top.jpower.core.auth.utils.constant.ClientNameConstant;
import top.jpower.core.exception.enums.constants.LogConstant;

/**
 * 服务名常量
 *
 * @author mr.g
 **/
@Data
public class AppConstant implements LogConstant, ClientNameConstant {

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
     * 单体模块
     **/
    public static final String JPOWER_BOOT = JPOWER + "-boot";

    @Override
    public String getJpowerLog() {
        return JPOWER_LOG;
    }

    @Override
    public String getJpowerSystem() {
        return JPOWER_SYSTEM;
    }
}
