package com.qidiangk.smart.aster.handler.service;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qidiangk.smart.aster.dbs.dao.asterisk.EndpointsDao;
import com.qidiangk.smart.aster.dbs.entity.asterisk.EndpointsDO;
import com.qidiangk.smart.aster.pojo.UserIntent;
import com.qidiangk.smart.aster.pojo.dto.ActionDTO;
import com.qidiangk.smart.aster.service.ExteriorService;
import com.qidiangk.smart.aster.service.ICallRouteService;
import lombok.extern.slf4j.Slf4j;
import org.asteriskjava.fastagi.AgiChannel;
import org.asteriskjava.fastagi.AgiRequest;
import top.jpower.core.asterisk.agi.fastagi.support.AgiSupport;
import top.jpower.core.asterisk.audio.AsrClient;
import top.jpower.core.asterisk.audio.TtsClient;
import top.jpower.core.util.utils.Fc;
import top.jpower.core.util.utils.ReflectUtil;
import top.jpower.core.util.utils.StringUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.qidiangk.smart.aster.constants.VariableNameEnum.ROUTE_ID;

/**
 * Agi交互逻辑支持类
 *
 * @author mr.g
 */
@Slf4j
public class AgiSupportImpl extends AgiSupport {

    private final ExteriorService exteriorService;
    private final EndpointsDao endpointsDao;
    private UserIntent.Node.StartNode startNode;

    public AgiSupportImpl(AgiChannel channel, AgiRequest request, Thread thread) {
        super(channel, request, thread);
        exteriorService = SpringUtil.getBean(ExteriorService.class);
        endpointsDao = SpringUtil.getBean(EndpointsDao.class);
    }

    private UserIntent.Node.StartNode getStartNode() {
        if (startNode != null) {
            return startNode;
        }

        String routeId = super.getVariable(ROUTE_ID.getName());
        if (Fc.isNotBlank(routeId)){
            List<? extends UserIntent.Node> flowObj = SpringUtil.getBean(ICallRouteService.class).findCompleteFlow(Fc.toLong(routeId));
            if (Fc.isNotEmpty(flowObj)) {
                Optional<UserIntent.Node.StartNode> startNodeOptional = flowObj.stream()
                        .filter(node -> node instanceof UserIntent.Node.StartNode)
                        .map(node -> (UserIntent.Node.StartNode) node)
                        .findFirst();
                if (startNodeOptional.isPresent()) {
                    startNode = startNodeOptional.get();
                    return startNode;
                }
            }
        }
        return null;
    }

    @Override
    protected void realtimeData(Boolean isBot, String filePath, String content) {
        // 传递实时会话
        try {
            exteriorService.sendAction(ActionDTO.builder()
                    .type(isBot?1:0)
                    .callId(getUniqueId())
                    .content(content)
                    .recordBase64(Fc.isBlank(filePath) ? null : Base64.encode(FileUtil.readBytes(filePath)))
                    .build());
        } catch (Exception e){
            log.error("实时会话传递失败===>>{}", e.getMessage());
        }
    }

    /**
     * 初始化asr
     *
     * @return asrClient
     */
    @Override
    protected AsrClient initAsr() {
        if (getStartNode() == null) {
            throw new RuntimeException("未在流程中查找到开始节点，请检查流程配置，流程=="+super.getVariable(ROUTE_ID.getName()));
        }

        ObjectMapper objectMapper = SpringUtil.getBean(ObjectMapper.class);

        Class<? extends AsrClient> asrClass = startNode.getAsr().getAsrClientClass();

        if (Fc.isNotEmpty(startNode.getAsrOption())) {
            Class<?> parameterType = Arrays.stream(ReflectUtil.getConstructors(asrClass))
                    .filter(constructor -> constructor.getParameterTypes().length == 1)
                    .map(constructor -> constructor.getParameterTypes()[0])
                    .findFirst().orElse(null);

            return ReflectUtil.newInstance(asrClass, objectMapper.convertValue(startNode.getAsrOption(), parameterType));
        } else {
            return ReflectUtil.newInstance(asrClass);
        }

    }

    /**
     * 初始化tts
     *
     * @return ttsClient
     */
    @Override
    protected TtsClient initTts() {
        if (getStartNode() == null) {
            throw new RuntimeException("未在流程中查找到开始节点，请检查流程配置，流程=="+super.getVariable(ROUTE_ID.getName()));
        }

        ObjectMapper objectMapper = SpringUtil.getBean(ObjectMapper.class);

        Class<? extends TtsClient> ttsClass = startNode.getTts().getTtsClientClass();


        if (Fc.isNotEmpty(startNode.getTtsOption())) {
            Class<?> parameterType = Arrays.stream(ReflectUtil.getConstructors(ttsClass)).filter(constructor -> constructor.getParameterTypes().length == 1)
                    .map(constructor -> constructor.getParameterTypes()[0])
                    .findFirst().orElse(null);

            return ReflectUtil.newInstance(ttsClass, objectMapper.convertValue(startNode.getTtsOption(), parameterType));
        } else {
            return ReflectUtil.newInstance(ttsClass);
        }
    }

    public String getPhone() {
        String phone = super.getPhone();

        String endpointName = StrUtil.subBetween(super.channel().getName(), "/", "-");
        EndpointsDO endpoint = endpointsDao.getById(endpointName);
        if (endpoint != null) {
            if (endpoint.getAddZero()){
                phone = StringUtil.removePrefix(phone, "0");
            }

            if (Fc.isNotBlank(endpoint.getPrefix())){
                phone = StringUtil.removePrefix(phone, endpoint.getPrefix());
            }
        }
        return phone;
    }
}
