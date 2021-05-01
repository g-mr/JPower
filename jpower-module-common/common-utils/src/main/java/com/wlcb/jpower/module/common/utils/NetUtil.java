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
    public static String getHostName() {
        String hostname;
        try {
            InetAddress address = InetAddress.getLocalHost();
            // force a best effort reverse DNS lookup
            hostname = address.getHostName();
            if (StringUtils.isEmpty(hostname)) {
                hostname = address.toString();
            }
        } catch (UnknownHostException ignore) {
            hostname = LOCAL_HOST;
        }
        return hostname;
    }

    /**
     * 获取 服务器 HostIp
     *
     * @return HostIp
     */
    public static String getHostIp() {
        String hostAddress;
        try {
            InetAddress address = NetUtil.getLocalHostLANAddress();
            // force a best effort reverse DNS lookup
            hostAddress = address.getHostAddress();
            if (StringUtils.isEmpty(hostAddress)) {
                hostAddress = address.toString();
            }
        } catch (UnknownHostException ignore) {
            hostAddress = LOCAL_HOST;
        }
        return hostAddress;
    }

    private static InetAddress getLocalHostLANAddress() throws UnknownHostException {
        try {
            InetAddress candidateAddress = null;
            // Iterate all NICs (network interface cards)...
            for (Enumeration ifaces = NetworkInterface.getNetworkInterfaces(); ifaces.hasMoreElements(); ) {
                NetworkInterface iface = (NetworkInterface) ifaces.nextElement();
                // Iterate all IP addresses assigned to each card...
                for (Enumeration inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements(); ) {
                    InetAddress inetAddr = (InetAddress) inetAddrs.nextElement();
                    if (!inetAddr.isLoopbackAddress()) {

                        if (inetAddr.isSiteLocalAddress()) {
                            // Found non-loopback site-local address. Return it immediately...
                            return inetAddr;
                        } else if (candidateAddress == null) {
                            // Found non-loopback address, but not necessarily site-local.
                            // Store it as a candidate to be returned if site-local address is not subsequently found...
                            candidateAddress = inetAddr;
                            // Note that we don't repeatedly assign non-loopback non-site-local addresses as candidates,
                            // only the first. For subsequent iterations, candidate will be non-null.
                        }
                    }
                }
            }
            if (candidateAddress != null) {
                // We did not find a site-local address, but we found some other non-loopback address.
                // Server might have a non-site-local address assigned to its NIC (or it might be running
                // IPv6 which deprecates the "site-local" concept).
                // Return this non-loopback candidate address...
                return candidateAddress;
            }
            // At this point, we did not find a non-loopback address.
            // Fall back to returning whatever InetAddress.getLocalHost() returns...
            InetAddress jdkSuppliedAddress = InetAddress.getLocalHost();
            if (jdkSuppliedAddress == null) {
                throw new UnknownHostException("The JDK InetAddress.getLocalHost() method unexpectedly returned null.");
            }
            return jdkSuppliedAddress;
        } catch (Exception e) {
            UnknownHostException unknownHostException = new UnknownHostException("Failed to determine LAN address: " + e);
            unknownHostException.initCause(e);
            throw unknownHostException;
        }
    }

    /**
     * 尝试端口时候被占用
     *
     * @param port 端口号
     * @return 没有被占用：true,被占用：false
     */
    public static boolean tryPort(int port) {
        try (ServerSocket ignore = new ServerSocket(port)) {
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
