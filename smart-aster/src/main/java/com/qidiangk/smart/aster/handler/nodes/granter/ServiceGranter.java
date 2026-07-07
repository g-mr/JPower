package com.qidiangk.smart.aster.handler.nodes.granter;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.qidiangk.smart.aster.handler.nodes.NodeContext;
import com.qidiangk.smart.aster.handler.nodes.NodeGranter;
import com.qidiangk.smart.aster.handler.nodes.NodeResult;
import com.qidiangk.smart.aster.pojo.UserIntent;
import jakarta.validation.constraints.NotNull;
import jodd.util.StringPool;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.StringSubstitutor;
import org.asteriskjava.fastagi.AgiException;
import org.springframework.stereotype.Component;
import top.jpower.core.util.utils.Fc;

import java.util.List;
import java.util.Map;

import static com.qidiangk.smart.aster.constants.ConstantUtil.MUSIC;

/**
 * 服务调用
 */
@Slf4j
@Component(ServiceGranter.GRANT_TYPE)
public class ServiceGranter implements NodeGranter<UserIntent.Node.ServiceNode> {
    public static final String GRANT_TYPE = "service";


    /**
     * 不支持嵌套，例如 ${var${name}}
     * @param param
     * @param map
     * @return
     */
    private String formatParams(String param, Map<String, Object> map){
        List<String> matches = ReUtil.findAll("\\$\\{([^}]+)\\}", param, 1);
        matches.forEach(match -> {
            if (!StrUtil.contains(match, ".") && !map.containsKey(match)) {
                map.put(match, "");
            }
        });
        return StringSubstitutor.replace(param, map);
    }

    @Override
    public @NotNull NodeResult grant(NodeContext nodeContext, Object lastResult, UserIntent.Node.ServiceNode node) {
        String params = "";
        if (Fc.notNull(node.getBody())){
            if (StrUtil.equalsAnyIgnoreCase(node.getMethod(), "PUT", "POST")) {
                params = node.getBody().toString();
                params =  formatParams(params, nodeContext.getParams());
            } else {
                JSONObject json;
                if (node.getBody().isArray()){
                    json = JSONUtil.parseObj(formatParams(node.getBody().get(0).toString(), nodeContext.getParams()));
                } else {
                    json = JSONUtil.parseObj(formatParams(node.getBody().toString(), nodeContext.getParams()));
                }
                params = HttpUtil.toParams(json);
            }
        }

        if (node.getPlayMusic()){
            try {
                nodeContext.getSupport().streamFile("请稍等,现在为您查询", false);
                nodeContext.getSupport().playMusicOnHold(MUSIC);
            } catch (AgiException e) {
                log.error("播放音乐失败=={}", ExceptionUtil.stacktraceToString(e));
            }
        }

        String url = formatParams(node.getUrl(), nodeContext.getParams());
        log.info("开始调用接口=>{}, 参数={}", url, params);
        try {
            String body = HttpRequest.of(url).method(Method.valueOf(node.getMethod()))
                    .body(params).headerMap(node.getHeaders(), true)
                    .execute().body();
            log.info("接口返回结果=>{}", body);

            JSONObject json = JSONUtil.parseObj(body, JSONConfig.create()
                    .setIgnoreNullValue(false)
                    .setStripTrailingZeros(false)
                    .setWriteLongAsString(false)
                    .setIgnoreCase(false));
            Object result = json.getByPath(node.getResult());

            return NodeResult.builder()
                    .nextId(node.getNextNode())
                    .result(ObjectUtil.defaultIfNull(result, StringPool.EMPTY))
                    .build();
        } catch (Exception e){
            log.error("接口请求异常=={}", ExceptionUtil.stacktraceToString(e));
            nodeContext.getSupport().streamFile("接口请求异常", false);
            return NodeResult.builder().nextId("end").build();
        }
    }

}
