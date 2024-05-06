package top.jpower.jpower.controller.monitor;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import top.jpower.common.enums.YN01Enum;
import top.jpower.core.utils.utils.Fc;
import top.jpower.core.utils.utils.ReturnJsonUtil;
import top.jpower.jpower.dbs.entity.TbLogMonitorParam;
import top.jpower.jpower.dbs.entity.TbLogMonitorSetting;
import top.jpower.jpower.module.annotation.Function;
import top.jpower.jpower.module.annotation.Menu;
import top.jpower.jpower.module.base.enums.JpowerError;
import top.jpower.jpower.module.base.exception.JpowerAssert;
import top.jpower.jpower.module.base.vo.ResponseData;
import top.jpower.jpower.module.common.support.ChainMap;
import top.jpower.jpower.properties.MonitorRestfulProperties;
import top.jpower.jpower.service.MonitorSettingService;
import top.jpower.jpower.service.TaskService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author mr.g
 * @Date 2021/4/18 0018 1:16
 */
@Api(tags = "监控设置")
@RestController
@RequestMapping("/monitor/setting")
@AllArgsConstructor
public class SettingController {

    private final MonitorRestfulProperties properties;
    private final TaskService taskService;
    private final MonitorSettingService monitorSettingService;

    @Function(value = "服务列表",menus = {
            @Menu(client = "admin",menuCode = "MONITOR_RESULT",code = "MONITOR_SERVERS",type = Menu.TYPE.INTERFACE)
    })
    @ApiOperationSupport(order = 1)
    @ApiOperation("服务列表")
    @GetMapping(value = "/servers",produces="application/json")
    public ResponseData<List<Map<String,Object>>> servers(){
        List<Map<String,Object>> list = new ArrayList<>();
        properties.getRoutes().forEach(route -> list.add(ChainMap.<String,Object>create().put("name",route.getName()).put("location",route.getLocation()).build()));
        return ReturnJsonUtil.ok("获取成功",list);
    }

    @Function(value = "分组列表",menus = {
            @Menu(client = "admin",menuCode = "MONITOR_RESULT",code = "MONITOR_TAGS",type = Menu.TYPE.INTERFACE)
    })
    @ApiOperationSupport(order = 2)
    @ApiOperation("分组列表")
    @GetMapping(value = "/tags",produces="application/json")
    public ResponseData<JSONArray> tags(@ApiParam(value = "服务名称",required = true) @RequestParam String name){
        JpowerAssert.notEmpty(name, JpowerError.Arg, "服务名称不可为空");

        MonitorRestfulProperties.Route routes = properties.getRoutes().stream().filter(route -> Fc.equals(route.getName(),name)).findFirst().get();
        JSONArray list = taskService.tagList(routes);
        return ReturnJsonUtil.ok("获取成功",list);
    }

    @Function(value = "接口树形",menus = {
            @Menu(client = "admin",menuCode = "MONITOR_SETTING",code = "MONITOR_TREE",type = Menu.TYPE.INTERFACE)
    })
    @ApiOperationSupport(order = 3)
    @ApiOperation("接口树形列表")
    @GetMapping(value = "/monitors",produces="application/json")
    public ResponseData<JSONArray> monitors(){
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
        return ReturnJsonUtil.ok("获取成功",array);
    }

    @Function(value = "获取接口设置",menus = {
            @Menu(client = "admin",menuCode = "MONITOR_SETTING",code = "MONITOR_SETUP",type = Menu.TYPE.INTERFACE)
    })
    @ApiOperationSupport(order = 4)
    @ApiOperation("获取接口设置")
    @GetMapping(value = "/setup",produces="application/json")
    public ResponseData<TbLogMonitorSetting> setup(@ApiIgnore TbLogMonitorSetting setting){
        JpowerAssert.notEmpty(setting.getServer(),JpowerError.Arg,"监控服务不可为空");
        return ReturnJsonUtil.ok("获取成功",monitorSettingService.getOneSetting(setting));
    }

    @Function(value = "保存接口设置",menus = {
            @Menu(client = "admin",menuCode = "MONITOR_SETTING",code = "MONITOR_SAVE_SETUP",type = Menu.TYPE.BTN)
    })
    @ApiOperationSupport(order = 5)
    @ApiOperation("保存接口设置")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "server",value = "服务名称",paramType = "query",required = true),
        @ApiImplicitParam(name = "tag",value = "所属分组",paramType = "query"),
        @ApiImplicitParam(name = "path",value = "监控地址",paramType = "query"),
        @ApiImplicitParam(name = "method",value = "请求方式",paramType = "query"),
        @ApiImplicitParam(name = "isMonitor",value = "是否监控",defaultValue = "1",paramType = "query",dataType = "int",required = true),
        @ApiImplicitParam(name = "code",value = "RESPOSE-STATUS,多个逗号分割",paramType = "query"),
        @ApiImplicitParam(name = "execJs",value = "JS代码",paramType = "query")
    })
    @PostMapping(value = "/save-setup",produces="application/json")
    public ResponseData<TbLogMonitorSetting> saveSetup(@ApiIgnore TbLogMonitorSetting setting){
        JpowerAssert.notEmpty(setting.getServer(),JpowerError.Arg,"服务名称不可为空");
        setting.setIsMonitor(Fc.isNull(setting.getIsMonitor())? YN01Enum.Y.getValue() :setting.getIsMonitor());
        if (monitorSettingService.save(setting)){
            return ReturnJsonUtil.ok("保存成功",setting);
        }
        return ReturnJsonUtil.fail("保存失败");
    }

    @Function(value = "删除接口设置",menus = {
            @Menu(client = "admin",menuCode = "MONITOR_SETTING",code = "MONITOR_DELETE_SETUP",type = Menu.TYPE.BTN)
    })
    @ApiOperationSupport(order = 6)
    @ApiOperation("删除接口设置")
    @DeleteMapping(value = "/delete-setup",produces="application/json")
    public ResponseData<Boolean> deleteSetup(@ApiParam("设置ID") @RequestParam Long id){
        JpowerAssert.notNull(id,JpowerError.Arg,"ID不可为空");
        return ReturnJsonUtil.status(monitorSettingService.removeRealById(id));
    }

    @Function(value = "获取接口参数",menus = {
            @Menu(client = "admin",menuCode = "MONITOR_SETTING",code = "MONITOR_PARAMS",type = Menu.TYPE.INTERFACE)
    })
    @ApiOperationSupport(order = 7)
    @ApiOperation("获取接口参数")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "server",value = "服务名称",paramType = "query",required = true),
            @ApiImplicitParam(name = "path",value = "监控地址",paramType = "query",required = true),
            @ApiImplicitParam(name = "method",value = "请求方式",paramType = "query",required = true),
    })
    @GetMapping(value = "/param",produces="application/json")
    public ResponseData<List<Map<String,Object>>> param(@ApiIgnore TbLogMonitorParam param){
        JpowerAssert.notEmpty(param.getServer(),JpowerError.Arg,"服务名称不为空");
        JpowerAssert.notEmpty(param.getPath(),JpowerError.Arg,"监控地址不为空");
        JpowerAssert.notEmpty(param.getMethod(),JpowerError.Arg,"请求方式不为空");

        MonitorRestfulProperties.Route route = properties.getRoutes().stream().filter(rt -> Fc.equals(rt.getName(),param.getServer())).findFirst().get();
        return ReturnJsonUtil.ok("获取成功",taskService.getParams(route,param.getPath(),param.getMethod()));
    }

    @Function(value = "保存接口参数",menus = {
            @Menu(client = "admin",menuCode = "MONITOR_SETTING",code = "MONITOR_SAVE_PARAMS",type = Menu.TYPE.BTN)
    })
    @ApiOperationSupport(order = 8)
    @ApiOperation("保存接口参数")
    @PostMapping(value = "/save-param",produces="application/json")
    public ResponseData<Boolean> saveParam(@ApiParam("服务名称") @RequestHeader(required = false) String server,
                                           @ApiParam("监控地址") @RequestHeader(required = false) String path,
                                           @ApiParam("请求方式") @RequestHeader(required = false) String method,
                                           @RequestBody List<TbLogMonitorParam> settingParams){
        JpowerAssert.notEmpty(server,JpowerError.Arg,"服务名称不为空");
        JpowerAssert.notEmpty(path,JpowerError.Arg,"监控地址不为空");
        JpowerAssert.notEmpty(method,JpowerError.Arg,"请求方式不为空");

        return ReturnJsonUtil.status(monitorSettingService.saveParams(server,path,method,settingParams));
    }

}
