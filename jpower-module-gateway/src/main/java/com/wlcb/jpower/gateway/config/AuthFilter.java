package com.wlcb.jpower.gateway.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wlcb.jpower.gateway.feign.SystemClient;
import com.wlcb.jpower.gateway.utils.ExculdesUrl;
import com.wlcb.jpower.gateway.utils.IpUtil;
import com.wlcb.jpower.gateway.utils.TokenUtil;
import com.wlcb.jpower.module.base.vo.ResponseData;
import com.wlcb.jpower.module.common.cache.CacheNames;
import com.wlcb.jpower.module.common.redis.RedisUtil;
import com.wlcb.jpower.module.common.support.ChainMap;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.JwtUtil;
import com.wlcb.jpower.module.common.utils.constants.StringPool;
import com.wlcb.jpower.module.common.utils.constants.TokenConstant;
import com.wlcb.jpower.module.properties.AuthProperties;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

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
@EnableConfigurationProperties({AuthProperties.class})
public class AuthFilter implements GlobalFilter, Ordered {

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private ObjectMapper objectMapper;
    @Resource
    private SystemClient systemClient;

    @Resource
    private AuthProperties authProperties;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        Route route = (Route) exchange.getAttributes().get(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
        String currentPath = exchange.getRequest().getURI().getPath();
        if (currentPath.startsWith("/"+route.getId())){
            currentPath = currentPath.replace("/"+route.getId(),"");
        }

        //不鉴权得URL
        if (ExculdesUrl.getExculudesUrl().stream().map(url -> url.replace(ExculdesUrl.TARGET, ExculdesUrl.REPLACEMENT)).anyMatch(currentPath::contains)){
            return chain.filter(exchange);
        }

        String token = TokenUtil.getToken(exchange.getRequest());
        if (Fc.isNotBlank(token)) {

            Claims claims = JwtUtil.parseJWT(token);
            List<String> listUrl = (List<String>) redisUtil.get(CacheNames.TOKEN_URL_KEY + token);
            if (Fc.isNull(claims) || !Fc.contains(listUrl.iterator(), currentPath)) {
                return unAuth(exchange.getResponse(), "请求未授权");
            }

            return chain.filter(addHeader(exchange, StringPool.EMPTY));
        }else {
            //白名单
            String ip = IpUtil.getIP(exchange.getRequest());
            if (Fc.contains(authProperties.getWhileIp().iterator(),ip)){
                return chain.filter(addHeader(exchange,ip));
            }

            //匿名用户
            ResponseData<Boolean> responseData = systemClient.queryRoleByUrl(currentPath);
            if (responseData.getCode() == HttpStatus.OK.value() && responseData.getData() != null && responseData.getData()){
                return chain.filter(addHeader(exchange,"anonymous"));
            }
            return unAuth(exchange.getResponse(), "缺失令牌，鉴权失败");
        }
    }

    private ServerWebExchange addHeader(ServerWebExchange exchange,String value) {
        ServerHttpRequest host = exchange.getRequest().mutate().header(TokenConstant.PASS_HEADER_NAME,value).build();
        //将现在的request 变成 change对象
        return exchange.mutate().request(host).build();
    }

    private Mono<Void> unAuth(ServerHttpResponse resp, String msg) {
        resp.setStatusCode(HttpStatus.UNAUTHORIZED);
        resp.getHeaders().add("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE);
        String result = "";
        try {
            result = objectMapper.writeValueAsString(response(HttpStatus.UNAUTHORIZED.value(),msg));
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
        }
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
        return 999;
    }
}
