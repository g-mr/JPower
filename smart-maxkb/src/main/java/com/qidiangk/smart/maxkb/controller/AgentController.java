package com.qidiangk.smart.maxkb.controller;

import com.qidiangk.smart.maxkb.pojo.vo.SelectVO;
import com.qidiangk.smart.maxkb.service.IAgentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.jpower.core.boot.controller.BaseController;
import top.jpower.core.util.rsp.R;

import java.util.List;

/**
 * 智能体控制器
 *
 * @author mr.g
 */
@Tag(name = "智能体")
@RestController
@RequestMapping("/agent")
@RequiredArgsConstructor
public class AgentController extends BaseController {

    private final IAgentService agentService;

    @GetMapping("select")
    @Operation(summary = "下拉列表")
    public R<List<SelectVO>> select() {
        return R.data(agentService.select());
    }

}
