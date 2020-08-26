package com.wlcb.jpower.gateway.dynamic;

import com.alibaba.cloud.nacos.NacosConfigProperties;
import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.wlcb.jpower.gateway.utils.NacosUtils;
import com.wlcb.jpower.module.common.utils.ExceptionsUtil;
import com.wlcb.jpower.module.common.utils.Fc;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executor;

/**
 * @ClassName NacosRouteDefinitionRepository
 * @Description TODO 动态路由
 * @Author 郭丁志
 * @Date 2020/8/25 0025 23:05
 * @Version 1.0
 */
@Order
@Slf4j
@Component
public class NacosDynamicRouteListener {

    private NacosConfigProperties nacosConfigProperties;
    private DynamicRouteService dynamicRouteService;

    public NacosDynamicRouteListener(DynamicRouteService dynamicRouteService,NacosConfigProperties nacosConfigProperties){
        this.dynamicRouteService = dynamicRouteService;
        this.nacosConfigProperties = nacosConfigProperties;
        addListener();
    }

    /**
     * 添加Nacos监听
     */
    private void addListener() {
        try {
            Properties properties = new Properties();
            properties.setProperty(PropertyKeyConst.SERVER_ADDR,nacosConfigProperties.getServerAddr());
            properties.setProperty(PropertyKeyConst.NAMESPACE,nacosConfigProperties.getNamespace());
            ConfigService configService= NacosFactory.createConfigService(properties);
            configService.addListener(NacosUtils.getRouteDataId(), nacosConfigProperties.getGroup(), new Listener() {
                @Override
                public Executor getExecutor() {
                    return null;
                }

                @Override
                public void receiveConfigInfo(String configInfo) {
                    List<RouteDefinition> list = JSON.parseArray(configInfo,RouteDefinition.class);
                    dynamicRouteService.updateList(list);
                }
            });

            String configInfo = configService.getConfig(NacosUtils.getRouteDataId(),nacosConfigProperties.getGroup(),5000);
            if (Fc.isNotBlank(configInfo)){
                List<RouteDefinition> list = JSON.parseArray(configInfo,RouteDefinition.class);
                dynamicRouteService.updateList(list);
            }

        } catch (Exception e) {
            log.error("nacos-addListener-error", ExceptionsUtil.getStackTraceAsString(e));
        }
    }

}
