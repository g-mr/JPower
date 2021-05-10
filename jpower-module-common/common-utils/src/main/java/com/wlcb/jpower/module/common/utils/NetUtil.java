package com.wlcb.jpower.module.common.utils;

import org.springframework.util.StringUtils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.util.Enumeration;

/**
 * 服务器信息获取工具
 *
 * @Author mr.g
 * @Date 2021/5/1 0001 0:59
 */
public class NetUtil {

    public static final String LOCAL_HOST = "127.0.0.1";

    /**
     * 获取 服务器 hostname
     *
     * @return hostname
     */
    public static String getHostName(InetAddress address) {
        String hostname;
        try {
            // force a best effort reverse DNS lookup
            hostname = address.getHostName();
            if (!StringUtil.hasLength(hostname)) {
                hostname = address.toString();
            }
        } catch (Exception ignore) {
            hostname = LOCAL_HOST;
        }
        return hostname;
    }

    /**
     * 获取 服务器 HostIp
     *
     * @return HostIp
     */
    public static String getHostIp(InetAddress address) {
        String hostAddress;
        try {
            hostAddress = address.getHostAddress();
            if (!StringUtils.hasLength(hostAddress)) {
                hostAddress = address.toString();
            }
        } catch (Exception ignore) {
            hostAddress = LOCAL_HOST;
        }
        return hostAddress;
    }

}
