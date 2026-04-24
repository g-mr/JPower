package top.jpower.common.constants;

import lombok.Data;
import top.jpower.core.auth.utils.constant.ClientNameConstant;
import top.jpower.core.exception.enums.constants.LogConstant;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

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

    /**
     * BOOT模式下服务前缀与包名的映射关系。
     * key: 包名前缀（用于匹配Controller所在包）
     * value: 对应的URL路径前缀（服务名称）
     */
    public static final Map<String, String> SERVICE_PREFIX_MAP;

    static {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("top.jpower.system", JPOWER_SYSTEM);
        map.put("top.jpower.user", JPOWER_USER);
        map.put("top.jpower.resource", JPOWER_RESOURCE);
        map.put("top.jpower.log", JPOWER_LOG);
        map.put("top.jpower.auth", JPOWER_AUTH);
        SERVICE_PREFIX_MAP = Collections.unmodifiableMap(map);
    }

    /**
     * 从请求路径中去除BOOT模式下的服务前缀。
     * 用于BOOT模式下将实际请求路径（如 /jpower-system/core/xxx）
     * 转换为权限匹配路径（如 /core/xxx），以便与数据库中存储的权限URL匹配。
     *
     * @param path 原始请求路径
     * @return 去除服务前缀后的路径
     */
    public static String stripServicePrefix(String path) {
        if (path == null || path.isEmpty()) {
            return path;
        }
        for (String prefix : SERVICE_PREFIX_MAP.values()) {
            String prefixWithSlash = "/" + prefix;
            if (path.startsWith(prefixWithSlash)) {
                return path.substring(prefixWithSlash.length());
            }
        }
        return path;
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
