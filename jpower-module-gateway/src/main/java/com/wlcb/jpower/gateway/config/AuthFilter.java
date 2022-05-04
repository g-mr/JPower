package com.wlcb.jpower.gateway.config;

import cn.hutool.core.collection.ListUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wlcb.jpower.gateway.utils.ExculdesUrl;
import com.wlcb.jpower.gateway.utils.IpUtil;
import com.wlcb.jpower.gateway.utils.TokenUtil;
import com.wlcb.jpower.module.common.cache.CacheNames;
import com.wlcb.jpower.module.common.redis.RedisUtil;
import com.wlcb.jpower.module.common.support.ChainMap;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.JwtUtil;
import com.wlcb.jpower.module.common.utils.constants.StringPool;
import com.wlcb.jpower.module.common.utils.constants.TokenConstant;
import com.wlcb.jpower.module.properties.AuthProperties;
import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.wlcb.jpower.module.common.auth.RoleConstant.ANONYMOUS;
import static com.wlcb.jpower.module.common.auth.RoleConstant.ROOT_ID;
import static com.wlcb.jpower.module.common.utils.constants.TokenConstant.HEADER_MENU;

/**
 * @ClassName AuthFilter
 * @Description TODO 鉴权
 * @Author 郭丁志
 * @Date 2020/8/29 0029 19:21
 * @Version 1.0
 */
@Component
@Slf4j
@RefreshScope
@AllArgsConstructor
@EnableConfigurationProperties({AuthProperties.class})
public class AuthFilter implements GlobalFilter, Ordered {

    private final RedisUtil redisUtil;
    private final ObjectMapper objectMapper;
    private final AuthProperties authProperties;
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        Route route = (Route) exchange.getAttributes().get(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
        String currentPath = exchange.getRequest().getURI().getPath();
        if (currentPath.startsWith(StringPool.SLASH+route.getId())){
            currentPath = currentPath.replace(StringPool.SLASH+route.getId(),StringPool.EMPTY);
        }

        //不鉴权得URL
        if (isSkip(currentPath)){
            return chain.filter(exchange);
        }

        String token = TokenUtil.getToken(exchange.getRequest());
        if (Fc.isNotBlank(token)) {

            Claims claims = JwtUtil.parseJWT(token);
            if (!redisUtil.exists(CacheNames.TOKEN_URL_KEY + token)){
                return proxyAuthenticationRequired(exchange.getResponse(), "令牌已过期，请重新登陆");
            }

            if (Fc.isNull(claims) || !isAuth(claims, token, currentPath)) {
                return unAuth(exchange.getResponse(), "请求未授权");
            }

            Object dataAuth = redisUtil.get(CacheNames.TOKEN_DATA_SCOPE_KEY + token);
            Map<String,String> map = Fc.isNull(dataAuth) ? ChainMap.newMap() : (Map<String, String>) dataAuth;
            return chain.filter(addHeader(exchange, StringPool.EMPTY, map.get(exchange.getRequest().getHeaders().getFirst(HEADER_MENU))));
        }else {
            //白名单
            String ip = IpUtil.getIP(exchange.getRequest());
            if (Fc.contains(authProperties.getWhileIp(),ip)){
                return chain.filter(addHeader(exchange,ip,StringPool.EMPTY));
            }

            //匿名用户
            if (getIsAnonymous(currentPath)){
                return chain.filter(addHeader(exchange,ANONYMOUS,StringPool.EMPTY));
            }
            return proxyAuthenticationRequired(exchange.getResponse(), "缺失令牌，鉴权失败");
        }
    }

    /**
     * 是否拥有权限
     * @Author mr.g
     * @param token TOKEN
     * @param currentPath 请求地址
     * @return boolean
     **/
    private boolean isAuth(Claims claims, String token,String currentPath){
        List roleIds = claims.get("roleIds",List.class);
        if (Fc.isNotEmpty(roleIds) && roleIds.contains(ROOT_ID)){
            return true;
        }

        Object o = redisUtil.get(CacheNames.TOKEN_URL_KEY + token);
        List<String> listUrl = Fc.isNull(o)? ListUtil.empty() :(List<String>) o;
        return listUrl.stream().anyMatch(pattern -> antPathMatcher.match(pattern, currentPath));
    }

    /**
     * 是否不过滤权限
     * @Author mr.g
     * @param path 请求地址
     * @return boolean
     **/
    private boolean isSkip(String path) {
        return ExculdesUrl.getExculudesUrl().stream().anyMatch(pattern -> antPathMatcher.match(pattern, path))
                || authProperties.getSkipUrl().stream().anyMatch(pattern -> antPathMatcher.match(pattern, path));
    }

    private boolean getIsAnonymous(String currentPath){
        Object o = redisUtil.get(CacheNames.TOKEN_URL_KEY+ ANONYMOUS);
        List<String> listUrl = Fc.isNull(o)?new ArrayList<>():(List<String>) o;
        return listUrl.stream().anyMatch(pattern -> antPathMatcher.match(pattern, currentPath));
    }

    private ServerWebExchange addHeader(ServerWebExchange exchange,String otherAuth, String dataScope) {
        ServerHttpRequest host = exchange.getRequest().mutate()
                .header(TokenConstant.PASS_HEADER_NAME,otherAuth)
                .header(TokenConstant.DATA_SCOPE_NAME,dataScope)
                .build();
        //将现在的request 变成 change对象
        return exchange.mutate().request(host).build();
    }

    private Mono<Void> proxyAuthenticationRequired(ServerHttpResponse resp, String msg) {
        String result = "";
        try {
            result = objectMapper.writeValueAsString(response(HttpStatus.PROXY_AUTHENTICATION_REQUIRED.value(),msg));
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
        }
        return sendMesg(resp,result);
    }

    private Mono<Void> unAuth(ServerHttpResponse resp, String msg) {
        String result = "";
        try {
            result = objectMapper.writeValueAsString(response(HttpStatus.UNAUTHORIZED.value(),msg));
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
        }
        return sendMesg(resp,result);
    }

    private Mono<Void> sendMesg(ServerHttpResponse resp, String result) {
        resp.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        resp.setStatusCode(HttpStatus.UNAUTHORIZED);
        DataBuffer buffer = resp.bufferFactory().wrap(result.getBytes(StandardCharsets.UTF_8));
        return resp.writeWith(Flux.just(buffer));
    }

    /**
     * 构建返回的JSON数据格式
     * @param status  状态码
     * @param message 信息
     * @return
     */
    public static Map<String, Object> response(int status, String message) {
        return ChainMap.init().set("code", status).set("message", message).set("status", false);
    }

    @Override
    public int getOrder() {
        return 888;
    }
}
