package com.qidiangk.smart.aster.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.asteriskjava.live.AsteriskChannel;
import org.asteriskjava.live.AsteriskServer;
import org.springframework.web.bind.annotation.*;
import top.jpower.core.boot.controller.BaseController;
import top.jpower.core.util.rsp.R;
import com.qidiangk.smart.aster.constants.VariableNameEnum;
import com.qidiangk.smart.aster.pojo.vo.ivr.CallVO;
import com.qidiangk.smart.aster.service.CallService;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Tag(name = "呼叫接口")
@RestController
@RequiredArgsConstructor
@RequestMapping("/call")
public class CallController extends BaseController {

    private final CallService callService;
    private final AsteriskServer asteriskServer;


    @Operation(summary = "呼叫")
    @GetMapping("/{attendId}")
    public R<String> call(@Parameter(description="坐席ID") @PathVariable("attendId") String attendId,
                          @Parameter(description="线路ID") @RequestParam("lineId") String lineId,
                          @Parameter(description="电话号", required = true) @RequestParam(name = "phone") String phone) {
        CallVO callVO = callService.call(attendId, phone, lineId);
        return R.data(callVO.getLinkedId());
    }

    @Operation(summary = "挂断")
    @GetMapping("/hangup/{attendId}")
    public R<String> hangup(@Parameter(description="坐席ID") @PathVariable("attendId") String attendId) {
        Optional<AsteriskChannel> asteriskChannelOpt = asteriskServer.getChannels().stream()
                .filter(channel -> {
                    String name = channel.getName();
                    // 通道名格式: PJSIP/{endpointId}-XXXX 或 PJSIP/{phone}@{lineId}-XXXX
                    String endpointPart = StrUtil.subBetween(name, "/", "-");
                    return StrUtil.equals(endpointPart, attendId);
                }).findFirst();

        if (asteriskChannelOpt.isPresent()) {
            asteriskChannelOpt.get().hangup();
            return R.ok();
        } else {
            return R.fail(404,"未找到通道");
        }
    }

    @Operation(summary = "呼叫")
    @GetMapping("/robot/{lineId}")
    public R<String> call(@Parameter(description="线路ID") @PathVariable("lineId") String lineId,
                                     @Parameter(description="电话号", required = true) @RequestParam(name = "phone") String phone,
                                     @Parameter(description="路由ID", required = true) @RequestParam(name = "routeId") String routeId,
                                     @Parameter(description="参数") @RequestParam(name = "params", required = false) String paramsStr) {
        JSONObject json = JSONUtil.parseObj(StrUtil.blankToDefault(paramsStr, "{}"));
        json.set(VariableNameEnum.ROUTE_ID.getName(), routeId);
        Map<String, Object> params = new HashMap<>(json.size());
        params.putAll(json);
        CallVO callVO = callService.call(phone, lineId, params, true, false);
        return R.data(callVO.getLinkedId());
    }

}
