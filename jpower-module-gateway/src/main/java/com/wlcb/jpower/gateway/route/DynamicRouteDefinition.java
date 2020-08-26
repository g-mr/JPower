package com.wlcb.jpower.gateway.route;

import lombok.Data;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.validation.annotation.Validated;

/**
 * @ClassName DynamicRouteDefinition
 * @Description TODO 动态路由信息
 * @Author 郭丁志
 * @Date 2020-08-26 11:22
 * @Version 1.0
 */
@Validated
@Data
public class DynamicRouteDefinition extends RouteDefinition {

    private String name;

}
