package com.wlcb.jpower.controller.monitor;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.wlcb.jpower.dbs.entity.TbLogMonitorSetting;
import com.wlcb.jpower.dbs.entity.TbLogMonitorSettingParam;
import com.wlcb.jpower.module.base.enums.JpowerError;
import com.wlcb.jpower.module.base.exception.JpowerAssert;
import com.wlcb.jpower.module.base.vo.ResponseData;
import com.wlcb.jpower.module.common.support.ChainMap;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.ReturnJsonUtil;
import com.wlcb.jpower.module.common.utils.constants.ConstantsEnum;
import com.wlcb.jpower.module.common.utils.constants.JpowerConstants;
import com.wlcb.jpower.properties.MonitorRestfulProperties;
import com.wlcb.jpower.service.MonitorSettingService;
import com.wlcb.jpower.service.TaskService;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

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

    @ApiOperationSupport(order = 1)
    @ApiOperation("服务列表")
    @GetMapping(value = "/servers",produces="application/json")
    public ResponseData<List<Map<String,Object>>> servers(){
        List<Map<String,Object>> list = new ArrayList<>();
        properties.getRoutes().forEach(route -> list.add(ChainMap.init().set("name",route.getName()).set("location",route.getLocation())));
        return ReturnJsonUtil.ok("获取成功",list);
    }

    @ApiOperationSupport(order = 2)
    @ApiOperation("分组列表")
    @GetMapping(value = "/tags",produces="application/json")
    public ResponseData<JSONArray> tags(@ApiParam(value = "服务名称",required = true) @RequestParam String name){
        JpowerAssert.notEmpty(name, JpowerError.Arg, "服务名称不可为空");

        MonitorRestfulProperties.Route routes = properties.getRoutes().stream().filter(route -> Fc.equals(route.getName(),name)).findFirst().get();
        JSONArray list = taskService.tagList(routes);
        return ReturnJsonUtil.ok("获取成功",list);
    }

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

    @ApiOperationSupport(order = 4)
    @ApiOperation("获取接口设置")
    @GetMapping(value = "/setup",produces="application/json")
    public ResponseData<TbLogMonitorSetting> setup(@ApiParam(value = "监控服务", required = true) String server, @ApiParam("所属分组") String tag, @ApiParam("监控地址") String path, @ApiParam("请求类型") String method){
        JpowerAssert.notEmpty(server,JpowerError.Arg,"监控服务不可为空");
        return ReturnJsonUtil.ok("获取成功",monitorSettingService.setting(server,tag,path,method));
    }

    @ApiOperationSupport(order = 5)
    @ApiOperation("保存接口设置")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id",value = "设置ID",paramType = "query",required = true),
        @ApiImplicitParam(name = "isMonitor",value = "是否监控",defaultValue = "1",paramType = "query",dataType = "int",required = true),
        @ApiImplicitParam(name = "code",value = "RESPOSE-STATUS,多个逗号分割",defaultValue = "200",paramType = "query",required = true),
        @ApiImplicitParam(name = "execJs",value = "JS代码",paramType = "query")
    })
    @PostMapping(value = "/save-setup",produces="application/json")
    public ResponseData<Boolean> saveSetup(@ApiIgnore TbLogMonitorSetting setting){
        JpowerAssert.notEmpty(setting.getId(),JpowerError.Arg,"设置ID不为空");
        setting.setIsMonitor(Fc.isNull(setting.getIsMonitor())? ConstantsEnum.YN01.Y.getValue() :setting.getIsMonitor());
        setting.setCode(Fc.isBlank(setting.getCode())?"200":setting.getCode());
        setting.setStatus(JpowerConstants.DB_STATUS_NORMAL);
        return ReturnJsonUtil.status(monitorSettingService.updateById(setting));
    }

    @ApiOperationSupport(order = 6)
    @ApiOperation("获取接口参数")
    @GetMapping(value = "/param",produces="application/json")
    public ResponseData<List<Map<String,Object>>> param(@ApiParam(value = "接口设置ID", required = true, type = "query") @RequestParam String settingId){
        JpowerAssert.notEmpty(settingId,JpowerError.Arg,"设置ID不为空");

        TbLogMonitorSetting setting = monitorSettingService.getById(settingId);
        JpowerAssert.notTrue(Fc.isNull(setting) || Fc.isBlank(setting.getName()) || Fc.isBlank(setting.getTag()) || Fc.isBlank(setting.getPath()) || Fc.isBlank(setting.getMethod()),JpowerError.Arg,"只有接口才可获取参数");

        List<Map<String,Object>> list = monitorSettingService.queryParam(settingId);
        if (Fc.isNull(list) || list.size() <= 0){
            MonitorRestfulProperties.Route route = properties.getRoutes().stream().filter(rt -> Fc.equals(rt.getName(),setting.getName())).findFirst().get();
            list = taskService.getParams(route,setting.getTag(),setting.getPath(),setting.getMethod());
        }
        return ReturnJsonUtil.ok("获取成功",list);
    }

    @ApiOperationSupport(order = 7)
    @ApiOperation("保存接口参数")
    @PostMapping(value = "/save-param/{settingId}",produces="application/json")
    public ResponseData<Boolean> saveParam(@ApiParam("设置ID") @PathVariable String settingId, @RequestBody List<TbLogMonitorSettingParam> settingParams){
        JpowerAssert.notEmpty(settingId,JpowerError.Arg,"设置ID不可为空");
        TbLogMonitorSetting setting = monitorSettingService.getById(settingId);
        JpowerAssert.notTrue(Fc.isNull(setting) || Fc.isBlank(setting.getName()) || Fc.isBlank(setting.getTag()) || Fc.isBlank(setting.getPath()) || Fc.isBlank(setting.getMethod()),JpowerError.Arg,"只有接口才可设置参数");

        return ReturnJsonUtil.status(monitorSettingService.saveParams(settingId,settingParams));
    }

}
