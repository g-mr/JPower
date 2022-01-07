package com.wlcb.jpower.gateway.dynamic;

import com.alibaba.nacos.common.utils.CollectionUtils;
import com.wlcb.jpower.module.common.utils.Fc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @ClassName DynamicRouteService
 * @Description TODO
 * @Author mr.g
 * @Date 2020/8/26 0026 0:25
 * @Version 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DynamicRouteService implements ApplicationEventPublisherAware {

    private final RouteDefinitionWriter routeDefinitionWriter;
    private final RouteDefinitionLocator routeDefinitionLocator;
    /**
     * 发布事件
     */
    private ApplicationEventPublisher publisher;

    private final Set<String> list = new HashSet<>();

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }

    /**
     * 删除路由
     * @param id
     */
    public void delete(String id) {
        try {
            log.info("gateway delete route id {}",id);
            list.remove(id);
            //如果存在就删除
            if (isExist(id)){
                routeDefinitionWriter.delete(Mono.just(id)).subscribe();
                log.info("delete success");
            }
        } catch (Exception e) {
            log.warn("delete fail");
        }
    }

    /**
     * 更新路由
     * @param definitions
     */
    public void updateList(List<RouteDefinition> definitions) {
        log.info("gateway update route {}",definitions);
        // 删除缓存routerDefinition
        List<RouteDefinition> routeDefinitionsExits =  routeDefinitionLocator.getRouteDefinitions().buffer().blockFirst();
        if (!CollectionUtils.isEmpty(routeDefinitionsExits)) {
            routeDefinitionsExits.forEach(routeDefinition -> {
                log.info("delete routeDefinition:{}", routeDefinition);
                if (list.contains(routeDefinition.getId())){
                    delete(routeDefinition.getId());
                }
            });
        }
        definitions.forEach(this::updateById);
    }

    public boolean isExist(String id){
        return routeDefinitionLocator.getRouteDefinitions().any(r-> Fc.equalsValue(r.getId(),id)).blockOptional().orElse(false);
    }


    /**
     * 更新路由
     * @param definition
     */
    public void updateById(RouteDefinition definition) {
        log.info("gateway update route {}",definition);
        try {
            delete(definition.getId());
        } catch (Exception e) {
            log.warn("update fail,not find route  routeId: "+definition.getId());
        }
        try {
            routeDefinitionWriter.save(Mono.just(definition)).subscribe();
            list.add(definition.getId());
        } catch (Exception e) {
            log.warn("update route fail");
        }
    }

    /**
     * 增加路由
     * @param definition
     */
    public void add(RouteDefinition definition) {
        log.info("gateway add route {}",definition);
        list.add(definition.getId());
        routeDefinitionWriter.save(Mono.just(definition)).subscribe();
    }

    /**
     * 刷新路由
     */
    public void refresh() {
        this.publisher.publishEvent(new RefreshRoutesEvent(this));
    }
}
