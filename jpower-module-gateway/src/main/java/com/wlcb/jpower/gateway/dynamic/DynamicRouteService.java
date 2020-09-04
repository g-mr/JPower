package com.wlcb.jpower.gateway.dynamic;

import com.wlcb.jpower.module.common.utils.ExceptionsUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @ClassName DynamicRouteService
 * @Description TODO
 * @Author 郭丁志
 * @Date 2020/8/26 0026 0:25
 * @Version 1.0
 */
@Slf4j
@Service
public class DynamicRouteService implements ApplicationEventPublisherAware {

    private final RouteDefinitionWriter routeDefinitionWriter;
    private ApplicationEventPublisher publisher;

    public DynamicRouteService(RouteDefinitionWriter routeDefinitionWriter) {
        this.routeDefinitionWriter = routeDefinitionWriter;
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }

    public void publish(RouteDefinition definition){
        try{
            this.update(definition);
            this.publishEvent();
        }catch (Exception e){
            log.error("更新路由失败={}", ExceptionsUtil.getStackTraceAsString(e));
        }
    }

    public void publishEvent(){
        this.publisher.publishEvent(new RefreshRoutesEvent(this));
    }

    public void update(RouteDefinition definition){
        this.routeDefinitionWriter.delete(Mono.just(definition.getId()));
        this.routeDefinitionWriter.save(Mono.just(definition)).subscribe();
    }

    public void publishList(List<RouteDefinition> definitions){
        definitions.forEach(this::publish);
    }
}
