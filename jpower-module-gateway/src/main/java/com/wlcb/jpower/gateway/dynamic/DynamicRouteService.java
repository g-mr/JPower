package com.wlcb.jpower.gateway.dynamic;

import com.alibaba.nacos.common.utils.CollectionUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
     * @return
     */
    public String delete(String id) {
        try {
            log.info("gateway delete route id {}",id);
            this.routeDefinitionWriter.delete(Mono.just(id)).subscribe();
            list.remove(id);
            this.publisher.publishEvent(new RefreshRoutesEvent(this));
            return "delete success";
        } catch (Exception e) {
            return "delete fail";
        }
    }

    /**
     * 更新路由
     * @param definitions
     * @return
     */
    public String updateList(List<RouteDefinition> definitions) {
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
        definitions.forEach(definition -> {
            updateById(definition);
        });
        return "success";
    }

    /**
     * 更新路由
     * @param definition
     * @return
     */
    public String updateById(RouteDefinition definition) {
        try {
            log.info("gateway update route {}",definition);
            this.routeDefinitionWriter.delete(Mono.just(definition.getId()));
        } catch (Exception e) {
            return "update fail,not find route  routeId: "+definition.getId();
        }
        try {
            routeDefinitionWriter.save(Mono.just(definition)).subscribe();
            list.add(definition.getId());
            this.publisher.publishEvent(new RefreshRoutesEvent(this));
            return "success";
        } catch (Exception e) {
            return "update route fail";
        }
    }

    /**
     * 增加路由
     * @param definition
     * @return
     */
    public String add(RouteDefinition definition) {
        log.info("gateway add route {}",definition);
        routeDefinitionWriter.save(Mono.just(definition)).subscribe();
        list.add(definition.getId());
        /*
         TODO: 2021-05-12 当多次执行👇这行时会报错：java.util.ConcurrentModificationException: null
         TODO: 2021-05-12 但是目前测试不影响使用，可能是nacos+gateway问题，后续看看会不会修复
         */
        this.publisher.publishEvent(new RefreshRoutesEvent(this));
        return "success";
    }
}
