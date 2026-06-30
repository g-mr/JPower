package com.qidiangk.smart.aster.tripartite.dianxin;

import jakarta.websocket.ClientEndpointConfig;
import lombok.RequiredArgsConstructor;
import com.qidiangk.smart.aster.tripartite.property.DianxinProperty;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 电信鉴权
 */
@RequiredArgsConstructor
public class HeaderAuthConfigurator extends ClientEndpointConfig.Configurator {
    private final DianxinProperty dianxinProperty;
    private final URI uri;
    @Override
    public void beforeRequest(Map<String, List<String>> headers) {

        String auth = SignUtils.get(dianxinProperty.getAppId(), dianxinProperty.getAppKey(), uri.getPath());

        // 设置自定义请求头
        headers.put("Content-Type", Collections.singletonList("application/json"));
        headers.put("X-APP-ID", Collections.singletonList(dianxinProperty.getAppId()));
        headers.put("Authorization", Collections.singletonList(auth));
    }
}