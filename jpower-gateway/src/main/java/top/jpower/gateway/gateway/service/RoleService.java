package top.jpower.gateway.gateway.service;

import cn.hutool.core.collection.ListUtil;
import com.alibaba.fastjson2.JSON;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.retry.Retry;
import top.jpower.common.enums.YN01Enum;
import top.jpower.core.auth.utils.constant.ClientNameConstant;
import top.jpower.core.util.constants.StringPool;
import top.jpower.core.util.rsp.R;
import top.jpower.core.util.utils.ExceptionUtil;
import top.jpower.core.util.utils.Fc;
import top.jpower.core.util.utils.MapUtil;
import top.jpower.core.util.utils.StringUtil;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

import static top.jpower.core.util.constants.StringPool.NEWLINE;

/**
 * @author mr.g
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RoleService {

     private final WebClient webClient;

    /**
     * 根据角色ID查询功能
     *
     * @author mr.g
     * @param roleId 角色ID
     * @param clientCode
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     **/
    public Mono<List<String>> queryUrlByRole(Long roleId, String clientCode){
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .host(ClientNameConstant.getInstance().getJpowerSystem())
                        .path("/core/function/getUrlsByRoleIds")
                        .queryParam("roleIds", roleId)
                        .queryParam("clientCode", clientCode)
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<R<List<String>>>() {})
                .map(R::getData)
                .onErrorResume(e -> {
                    log.error("获取【{},{}】权限失败, error==>>{}{}", roleId, clientCode, NEWLINE,ExceptionUtil.getStackTraceAsString(e));
                    return Mono.just(ListUtil.empty());
                })
                .cache(Duration.ofMinutes(5));
    }


    /**
     * 根据菜单编码查询ID
     *
     * @author mr.g
     * @param code 菜单编码
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     **/
    public Mono<Optional<Long>> queryMenuIdByCode(String code){
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .host(ClientNameConstant.getInstance().getJpowerSystem())
                        .path("/core/menu/getIdByCode")
                        .queryParam("code", code)
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<R<Long>>() {})
                .onErrorResume(e -> {
                    log.error("获取【{}】菜单编码失败, error={}{}", code, NEWLINE, ExceptionUtil.getStackTraceAsString(e));
                    return Mono.empty();
                })
                .map(R::getData)
                .map(Optional::of)
                .defaultIfEmpty(Optional.empty())
                .cache(Duration.ofMinutes(5));
    }

    /**
     * 根据角色ID查询数据权限
     *
     * @author mr.g
     * @param roleIds 角色ID
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     **/
    public Mono<List<Map<String,Object>>> queryDataScopeByRole(List<Long> roleIds, String clientCode){

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .host(ClientNameConstant.getInstance().getJpowerSystem())
                        .path("/core/dataScope/getDataScopeByRole")
                        .queryParam("roleIds", StringUtil.join(roleIds))
                        .queryParam("clientCode", clientCode)
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<R<List<Map<String,Object>>>>() {})
                .map(R::getData)
                .onErrorResume(e -> {
                    log.error("获取【{},{}】数据权限失败, error={}{}", roleIds, clientCode, NEWLINE, ExceptionUtil.getStackTraceAsString(e));
                    return Mono.just(ListUtil.empty());
                })
                .defaultIfEmpty(Collections.emptyList())
                .cache(Duration.ofMinutes(5));

    }


    /**
     * 根据角色ID查询数据权限
     *
     * @author mr.g
     * @param roleIds 角色ID
     * @param clientCode 客户端编号
     * @return String
     **/
    public Mono<String> queryDataScopeByRoleAndMenu(List<Long> roleIds, String menuCode, String clientCode){
        if (Fc.isBlank(menuCode) || Fc.isEmpty(roleIds)){
            return Mono.just(StringPool.EMPTY);
        }

        // 并行查询
        Mono<List<Map<String, Object>>> dataMono = queryDataScopeByRole(roleIds, clientCode);
        Mono<Long> menuIdMono = queryMenuIdByCode(menuCode).flatMap(opt -> opt.map(Mono::just).orElse(Mono.empty()));

        return Mono.zip(dataMono, menuIdMono)
                .flatMap(tuple -> {

                    List<Map<String, Object>> list = tuple.getT1();
                    Long menuId = tuple.getT2();
                    if (Fc.hasEmpty(menuId, list)){
                        return Mono.just(StringPool.EMPTY);
                    }

                    return Mono.fromCallable(() -> {
                                List<Map<String, Object>> listScope = list.stream()
                                        .filter(m -> Fc.equalsValue(MapUtil.getLong(m, "menuId"), menuId))
                                        .sorted(Comparator.comparingInt(m -> MapUtil.getInt(m, "allRole")))
                                        .filter(m -> {
                                            if (Fc.equalsValue(MapUtil.getInt(m,"allRole"), YN01Enum.N.getValue())){
                                                return true;
                                            }
                                            return list.stream().noneMatch(lm-> Fc.equalsValue(MapUtil.getInt(lm,"allRole"), YN01Enum.N.getValue()) &&
                                                    Fc.equalsValue(MapUtil.getStr(lm,"scopeClass"), MapUtil.getStr(m,"scopeClass")));

                                        })
                                        .collect(Collectors.toList());

                                return JSON.toJSONString(listScope);
                            })
                            // 放到弹性线程池
                            .subscribeOn(Schedulers.boundedElastic());

                })
                .timeout(Duration.ofSeconds(5))
                .retryWhen(Retry.backoff(2, Duration.ofMillis(100)));

    }
}
