package com.wlcb.jpower.module.common.deploy.server;

import com.wlcb.jpower.module.common.utils.NetUtil;
import lombok.Getter;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 获取服务器信息
 *
 * @Author mr.g
 * @Date 2021/5/1 0001 0:54
 */
@Getter
@Configuration(proxyBeanMethods = false)
public class ServerInfo implements SmartInitializingSingleton {

    private final ServerProperties serverProperties;
    private String hostName;
    private String ip;
    private Integer port;
    private String ipWithPort;

    @Autowired(required = false)
    public ServerInfo(ServerProperties serverProperties) {
        this.serverProperties = serverProperties;
    }

    @Override
    public void afterSingletonsInstantiated() {
        this.hostName = NetUtil.getHostName();
        this.ip = NetUtil.getHostIp();
        this.port = serverProperties.getPort();
        this.ipWithPort = String.format("%s:%d", ip, port);
    }
}
