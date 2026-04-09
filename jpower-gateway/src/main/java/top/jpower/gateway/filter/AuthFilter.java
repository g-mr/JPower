package top.jpower.gateway.filter;

import cn.hutool.core.collection.ListUtil;
import com.alibaba.fastjson2.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import top.jpower.common.constants.CacheNames;
import top.jpower.core.auth.properties.AuthProperties;
import top.jpower.core.auth.utils.JwtUtil;
import top.jpower.core.redis.cache.RedisService;
import top.jpower.core.util.constants.StringPool;
import top.jpower.core.util.constants.TokenConstant;
import top.jpower.core.util.utils.ChainMap;
import top.jpower.core.util.utils.CollectionUtil;
import top.jpower.core.util.utils.Fc;
import top.jpower.gateway.gateway.utils.ExculdesUrl;
import top.jpower.gateway.gateway.utils.IpUtil;
import top.jpower.gateway.gateway.utils.TokenUtil;
import top.jpower.gateway.gateway.service.RoleService;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static top.jpower.core.auth.utils.constant.RoleConstant.*;
import static top.jpower.core.util.constants.JpowerConstants.HEADER_MENU;

/**
 * 鉴权
 *
 * @author mr.g
 */
@Component
@Slf4j
@RefreshScope
@AllArgsConstructor
@EnableConfigurationProperties({AuthProperties.class})
public class AuthFilter implements GlobalFilter, Ordered {

    private final RedisService redisService;
    private final RoleService roleClient;
    private final ObjectMapper objectMapper;
    private final AuthProperties authProperties;
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        Route route = (Route) exchange.getAttributes().get(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
        String path = exchange.getRequest().getURI().getPath();
        String currentPath = path.startsWith(StringPool.SLASH+route.getId()) ?
                path.replace(StringPool.SLASH+route.getId(),StringPool.EMPTY) :
                path;

        String token = TokenUtil.getToken(exchange.getRequest());
        if (Fc.isNotBlank(token)) {

            Claims claims = JwtUtil.parseJwt(token);
            if (!redisService.exist(CacheNames.TOKEN_URL_KEY + token)){
                return proxyAuthenticationRequired(exchange.getResponse(), "令牌已过期，请重新登录");
            }

            // 不是忽略权限得需要校验
            if (!isSkip(currentPath)){
                if (Fc.isNull(claims) || !isAuth(claims, token, currentPath)) {
                    return unAuth(exchange.getResponse(), "请求未授权");
                }
            }

            Object dataAuth = redisService.valueOps().get(CacheNames.TOKEN_DATA_SCOPE_KEY + token);
            Map<String,List> map = Fc.isNull(dataAuth) ? ChainMap.<String,List>create().build() : (Map<String, List>) dataAuth;
            return chain.filter(addHeader(exchange, StringPool.EMPTY, JSON.toJSONString(map.getOrDefault(exchange.getRequest().getHeaders().getFirst(HEADER_MENU),ListUtil.empty()))));
        }else {
            //白名单
            String ip = IpUtil.getIP(exchange.getRequest());
            if (Fc.contains(authProperties.getWhileIp(),ip)){
                return chain.filter(addHeader(exchange,ip,StringPool.EMPTY));
            }

            return getIsAnonymous(currentPath, exchange.getRequest())
                    .flatMap(isAnonymous -> {
                        if (isAnonymous) {

                            if (ExculdesUrl.getExculudesUrl().stream().anyMatch(pattern -> antPathMatcher.match(pattern, currentPath))) {
                                return chain.filter(addHeader(exchange, ANONYMOUS, StringPool.EMPTY));
                            }

                            return roleClient.queryDataScopeByRoleAndMenu(Collections.singletonList(ANONYMOUS_ID),
                                            exchange.getRequest().getHeaders().getFirst(HEADER_MENU),
                                            TokenUtil.getClientCodeFromHeader(exchange.getRequest()))
                                    .flatMap(dataAuth -> chain.filter(addHeader(exchange, ANONYMOUS, dataAuth)));
                        } else {
                            return proxyAuthenticationRequired(exchange.getResponse(), "缺失令牌，鉴权失败");
                        }
                    });
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
        List<Long> roleIds = claims.get("roleIds",List.class);
        if (Fc.isNotEmpty(roleIds) && CollectionUtil.containsValue(roleIds, ROOT_ID)){
            return true;
        }

        Object o = redisService.valueOps().get(CacheNames.TOKEN_URL_KEY + token);
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

    private Mono<Boolean> getIsAnonymous(String currentPath, ServerHttpRequest request){

        if (isSkip(currentPath)){
            return Mono.just(true);
        }

        String clientCode = TokenUtil.getClientCodeFromHeader(request);

        //获取匿名用户的权限
        return roleClient.queryUrlByRole(ANONYMOUS_ID, clientCode)
                .flatMapIterable(Function.identity())
                .any(pattern -> antPathMatcher.match(pattern, currentPath))
                .defaultIfEmpty(false)
                .cache(Duration.ofMinutes(5));
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
        return sendMesg(HttpStatus.UNAUTHORIZED, resp,result);
    }

    private Mono<Void> unAuth(ServerHttpResponse resp, String msg) {
        String result = "";
        try {
            result = objectMapper.writeValueAsString(response(HttpStatus.FORBIDDEN.value(),msg));
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
        }
        return sendMesg(HttpStatus.FORBIDDEN, resp,result);
    }

    private Mono<Void> sendMesg(HttpStatus status, ServerHttpResponse resp, String result) {
        resp.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        resp.setStatusCode(status);
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
        return ChainMap.<String, Object>create().put("code", status).put("message", message).put("status", false).build();
    }

    @Override
    public int getOrder() {
        return 888;
    }
}
