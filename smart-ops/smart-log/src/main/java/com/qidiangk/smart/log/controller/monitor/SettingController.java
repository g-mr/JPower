package com.qidiangk.smart.log.controller.monitor;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.Ignore;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.qidiangk.smart.common.enums.YN01Enum;
import top.jpower.core.auth.annotation.Function;
import top.jpower.core.auth.annotation.Menu;
import top.jpower.core.exception.enums.JpowerError;
import top.jpower.core.exception.throwable.JpowerAssert;
import top.jpower.core.util.rsp.R;
import top.jpower.core.util.utils.ChainMap;
import top.jpower.core.util.utils.Fc;
import com.qidiangk.smart.log.dbs.entity.LogMonitorParam;
import com.qidiangk.smart.log.dbs.entity.LogMonitorSetting;
import com.qidiangk.smart.log.properties.MonitorRestfulProperties;
import com.qidiangk.smart.log.service.MonitorSettingService;
import com.qidiangk.smart.log.service.TaskService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author mr.g
 */
@Tag(name = "监控设置")
@RestController
@RequestMapping("/monitor/setting")
@RequiredArgsConstructor
public class SettingController {

    private final MonitorRestfulProperties properties;
    private final TaskService taskService;
    private final MonitorSettingService monitorSettingService;

    @Function(value = "服务列表",menus = {
		@Menu(client = "admin",menuCode = "MONITOR_RESULT",code = "MONITOR_SERVERS",type = Menu.TYPE.INTERFACE)
    })
    @ApiOperationSupport(order = 1)
    @Operation(description = "服务列表")
    @GetMapping(value = "/servers",produces="application/json")
    public R<List<Map<String,Object>>> servers(){
        List<Map<String,Object>> list = new ArrayList<>();
        properties.getRoutes().forEach(route -> list.add(ChainMap.<String,Object>create().put("name",route.getName()).put("location",route.getLocation()).build()));
        return R.data(list);
    }

    @Function(value = "分组列表",menus = {
            @Menu(client = "admin",menuCode = "MONITOR_RESULT",code = "MONITOR_TAGS",type = Menu.TYPE.INTERFACE)
    })
    @ApiOperationSupport(order = 2)
    @Operation(description = "分组列表")
    @GetMapping(value = "/tags",produces="application/json")
    public R<JSONArray> tags(@Parameter(description = "服务名称",required = true) @RequestParam String name){
        JpowerAssert.notEmpty(name, JpowerError.Arg, "服务名称不可为空");

        MonitorRestfulProperties.Route routes = properties.getRoutes().stream().filter(route -> Fc.equals(route.getName(),name)).findFirst().get();
        JSONArray list = taskService.tagList(routes);
        return R.data(list);
    }

    @Function(value = "接口树形",menus = {
            @Menu(client = "admin",menuCode = "MONITOR_SETTING",code = "MONITOR_TREE",type = Menu.TYPE.INTERFACE)
    })
    @ApiOperationSupport(order = 3)
    @Operation(description = "接口树形列表")
    @GetMapping(value = "/monitors",produces="application/json")
    public R<JSONArray> monitors(){
        JSONArray array = new JSONArray();
        properties.getRoutes().forEach(route -> {
            JSONObject json = new JSONObject();
            json.put("name",route.getName());
            json.put("location",route.getLocation());
            JSONArray jsonArray = taskService.tree(route);
            if (jsonArray.size() > 0){
                json.put("children",jsonArray);
            }
            array.add(json);
        });
        return R.data(array);
    }

    @Function(value = "获取接口设置",menus = {
		@Menu(client = "admin",menuCode = "MONITOR_SETTING",code = "MONITOR_SETUP",type = Menu.TYPE.INTERFACE)
    })
    @ApiOperationSupport(order = 4)
    @Operation(description = "获取接口设置")
    @GetMapping(value = "/setup",produces="application/json")
    public R<LogMonitorSetting> setup(@Ignore LogMonitorSetting setting){
        JpowerAssert.notEmpty(setting.getServer(),JpowerError.Arg,"监控服务不可为空");
        return R.data(monitorSettingService.getOneSetting(setting));
    }

    @Function(value = "保存接口设置",menus = {
		@Menu(client = "admin",menuCode = "MONITOR_SETTING",code = "MONITOR_SAVE_SETUP",type = Menu.TYPE.BTN)
    })
    @ApiOperationSupport(order = 5)
    @Operation(description = "保存接口设置")
    @Parameters({
        @Parameter(name = "server", description = "服务名称", in = ParameterIn.QUERY,required = true),
        @Parameter(name = "tag",description = "所属分组", in = ParameterIn.QUERY),
        @Parameter(name = "path",description = "监控地址", in = ParameterIn.QUERY),
        @Parameter(name = "method",description = "请求方式", in = ParameterIn.QUERY),
        @Parameter(name = "isMonitor",description = "是否监控", in = ParameterIn.QUERY,required = true),
        @Parameter(name = "code",description = "RESPOSE-STATUS,多个逗号分割", in = ParameterIn.QUERY),
        @Parameter(name = "execJs",description = "JS代码", in = ParameterIn.QUERY)
    })
    @PostMapping(value = "/save-setup",produces="application/json")
    public R<LogMonitorSetting> saveSetup(@Ignore LogMonitorSetting setting){
        JpowerAssert.notEmpty(setting.getServer(),JpowerError.Arg,"服务名称不可为空");
        setting.setIsMonitor(Fc.isNull(setting.getIsMonitor())? YN01Enum.Y.getValue() :setting.getIsMonitor());
        if (monitorSettingService.save(setting)){
            return R.data(setting);
        }
        return R.fail();
    }

    @Function(value = "删除接口设置",menus = {
            @Menu(client = "admin",menuCode = "MONITOR_SETTING",code = "MONITOR_DELETE_SETUP",type = Menu.TYPE.BTN)
    })
    @ApiOperationSupport(order = 6)
    @Operation(description = "删除接口设置")
    @DeleteMapping(value = "/delete-setup",produces="application/json")
    public R<Boolean> deleteSetup(@Parameter(description = "设置ID") @NotBlank(message = "ID不可为空") @RequestParam Long id){
        return R.status(monitorSettingService.removeRealById(id));
    }

    @Function(value = "获取接口参数",menus = {
            @Menu(client = "admin",menuCode = "MONITOR_SETTING",code = "MONITOR_PARAMS",type = Menu.TYPE.INTERFACE)
    })
    @ApiOperationSupport(order = 7)
    @Operation(description = "获取接口参数")
    @Parameters({
		@Parameter(name = "server", description = "服务名称", in = ParameterIn.QUERY,required = true),
		@Parameter(name = "path", description = "监控地址", in = ParameterIn.QUERY,required = true),
		@Parameter(name = "method", description = "请求方式", in = ParameterIn.QUERY,required = true),
    })
    @GetMapping(value = "/param",produces="application/json")
    public R<List<Map<String,Object>>> param(@Ignore LogMonitorParam param){
        JpowerAssert.notEmpty(param.getServer(),JpowerError.Arg,"服务名称不为空");
        JpowerAssert.notEmpty(param.getPath(),JpowerError.Arg,"监控地址不为空");
        JpowerAssert.notEmpty(param.getMethod(),JpowerError.Arg,"请求方式不为空");

        MonitorRestfulProperties.Route route = properties.getRoutes().stream().filter(rt -> Fc.equals(rt.getName(),param.getServer())).findFirst().get();
        return R.data(taskService.getParams(route,param.getPath(),param.getMethod()));
    }

    @Function(value = "保存接口参数",menus = {
            @Menu(client = "admin",menuCode = "MONITOR_SETTING",code = "MONITOR_SAVE_PARAMS",type = Menu.TYPE.BTN)
    })
    @ApiOperationSupport(order = 8)
    @Operation(description = "保存接口参数")
    @PostMapping(value = "/save-param",produces="application/json")
    public R<Boolean> saveParam(@Parameter(description = "服务名称") @RequestHeader(required = false) String server,
							    @Parameter(description = "监控地址") @RequestHeader(required = false) String path,
							    @Parameter(description = "请求方式") @RequestHeader(required = false) String method,
							    @RequestBody List<LogMonitorParam> settingParams){
        JpowerAssert.notEmpty(server,JpowerError.Arg,"服务名称不为空");
        JpowerAssert.notEmpty(path,JpowerError.Arg,"监控地址不为空");
        JpowerAssert.notEmpty(method,JpowerError.Arg,"请求方式不为空");

        return R.status(monitorSettingService.saveParams(server,path,method,settingParams));
    }

}
