package com.qidiangk.smart.aster.controller.ivr;

import com.mybatisflex.core.update.UpdateWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.qidiangk.smart.common.validated.group.Validation;
import top.jpower.core.auth.annotation.Function;
import top.jpower.core.auth.annotation.Menu;
import top.jpower.core.boot.controller.BaseController;
import top.jpower.core.dbs.support.Wrappers;
import top.jpower.core.util.rsp.Pg;
import top.jpower.core.util.rsp.R;
import top.jpower.core.util.utils.Fc;
import com.qidiangk.smart.aster.dbs.entity.ivr.CallRouteDO;
import com.qidiangk.smart.aster.pojo.SelectVO;
import com.qidiangk.smart.aster.pojo.UserIntent;
import com.qidiangk.smart.aster.pojo.vo.ivr.RoutePageReqVO;
import com.qidiangk.smart.aster.pojo.vo.ivr.StatusVo;
import com.qidiangk.smart.aster.service.ICallRouteService;

import java.util.List;
import java.util.Map;


@Tag(name = "呼叫路由管理")
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/call-route")
public class CallRoutingController extends BaseController {

    private final ICallRouteService callRouteService;

    @Operation(summary = "呼叫路由列表")
    @GetMapping("/list")
    @Function(value = "路由列表", menus = {
            @Menu(client = "admin", menuCode = "CALL_ROUTER", code = "CALL_ROUTER_LIST", type = Menu.TYPE.INTERFACE)
    })
    public R<Pg<CallRouteDO>> list(RoutePageReqVO routePageReqVO) {
        return R.data(callRouteService.page(routePageReqVO));
    }

    @Operation(summary = "新增呼叫路由")
    @PostMapping("/create")
    @Function(value = "新增路由", menus = {
            @Menu(client = "admin", menuCode = "CALL_ROUTER", code = "CALL_ROUTER_CREATE", type = Menu.TYPE.BTN)
    })
    public R<Long> create(@Validated @RequestBody CallRouteDO callRouteDo) {
        callRouteDo.setStatus(true);
        callRouteService.save(callRouteDo);
        return R.data(callRouteDo.getId());
    }

    @Operation(summary = "修改呼叫路由")
    @PostMapping("/update")
    @Function(value = "修改路由", menus = {
            @Menu(client = "admin", menuCode = "CALL_ROUTER", code = "CALL_ROUTER_UPDATE", type = Menu.TYPE.BTN)
    })
    public R<Boolean> update(@Validated(Validation.Update.class) @RequestBody CallRouteDO callRouteDo) {
        return R.status(callRouteService.updateById(callRouteDo));
    }

    @Operation(summary = "删除呼叫路由")
    @DeleteMapping("/delete")
    @Function(value = "删除路由", menus = {
            @Menu(client = "admin", menuCode = "CALL_ROUTER", code = "CALL_ROUTER_DELETE", type = Menu.TYPE.BTN)
    })
    public R<Boolean> delete(@Parameter(description = "呼叫路由ID,多个逗号分割") String ids) {
        return R.status(callRouteService.removeByIds(Fc.toLongList(ids)));
    }

    @Operation(summary = "修改路由状态")
    @PostMapping("/status")
    @Function(value = "路由开关", menus = {
            @Menu(client = "admin", menuCode = "CALL_ROUTER", code = "CALL_ROUTER_SWITCH", type = Menu.TYPE.BTN)
    })
    public R<Boolean> status(@Valid @RequestBody StatusVo statusVo) {
        return R.status(callRouteService.update(UpdateWrapper.of(CallRouteDO.class)
                .set(CallRouteDO::getStatus, statusVo.getStatus()).toEntity(),
                Wrappers.getQueryWrapper()
                .eq(CallRouteDO::getId, statusVo.getId())));
    }

    @Operation(summary = "路由详情")
    @GetMapping("/detail/{id}")
    @Function(value = "路由详情", menus = {
            @Menu(client = "admin", menuCode = "CALL_ROUTER", btnCode = "CALL_ROUTER_CONFIG", code = "CALL_ROUTER_CONFIG_DETAIL", type = Menu.TYPE.INTERFACE)
    })
    public R<CallRouteDO> detail(@Parameter(description = "呼叫路由ID") @PathVariable("id") Long id) {
        return R.data(callRouteService.getById(id));
    }

//    @Operation(summary = "流程查看")
//    @GetMapping("/flow/{id}")
//    @Function(value = "流程查看", menus = {
//            @Menu(client = "admin", menuCode = "CALL_ROUTER", btnCode = "CALL_ROUTER_CONFIG", code = "CALL_ROUTER_CONFIG_VIEW", type = Menu.TYPE.INTERFACE)
//    })
//    public R<List<? extends UserIntent.Node>> viewFlow(@Parameter(description = "呼叫路由ID") @PathVariable("id") Long id) {
//        return R.data(callRouteService.flowView(id));
//    }

    @Operation(summary = "流程设置")
    @PostMapping("/flow/{id}")
    @Function(value = "流程设置", menus = {
            @Menu(client = "admin", menuCode = "CALL_ROUTER", code = "CALL_ROUTER_CONFIG", type = Menu.TYPE.BTN)
    })
    public R<Boolean> flow(@Parameter(description = "呼叫路由ID") @PathVariable("id") Long id,
                                 @Valid @Size(min = 1, message = "流程最少为一个") @RequestBody List<? extends UserIntent.Node> nodes) {
        return R.status(callRouteService.flow(id, nodes));
    }

    @Operation(summary = "下拉选择路由")
    @GetMapping("/select")
    @Function(value = "呼叫路由", menus = {
            @Menu(client = "admin", menuCode = "CALL_LINE", code = "CALL_LINE_ROUTE_SELECT", type = Menu.TYPE.INTERFACE),
            @Menu(client = "admin", menuCode = "CALL_TASK", code = "CALL_TASK_ROUTE_SELECT", type = Menu.TYPE.INTERFACE)
    })
    public R<List<SelectVO>> select(@RequestParam("process") @Parameter(description = "流程类型 （1：呼入 2：呼出）") Integer process) {
        return R.data(callRouteService.select(process));
    }

    @Operation(summary = "子流程列表")
    @GetMapping("/childList")
    @Function(value = "子流程列表", menus = {
            @Menu(client = "admin", menuCode = "CALL_ROUTER", btnCode = "CALL_ROUTER_CONFIG", code = "CALL_ROUTER_CONFIG_CHILD", type = Menu.TYPE.INTERFACE)
    })
    public R<List<CallRouteDO>> childList() {
        return R.data(callRouteService.childList());
    }

}
