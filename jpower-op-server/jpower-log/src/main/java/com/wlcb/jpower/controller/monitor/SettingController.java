package com.wlcb.jpower.controller.monitor;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wlcb.jpower.module.base.enums.JpowerError;
import com.wlcb.jpower.module.base.exception.JpowerAssert;
import com.wlcb.jpower.module.base.vo.ResponseData;
import com.wlcb.jpower.module.common.support.ChainMap;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.ReturnJsonUtil;
import com.wlcb.jpower.properties.MonitorRestfulProperties;
import com.wlcb.jpower.service.TaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @ApiOperation("服务列表")
    @GetMapping(value = "/servers",produces="application/json")
    public ResponseData<List<Map<String,Object>>> servers(){
        List<Map<String,Object>> list = new ArrayList<>();
        properties.getRoutes().forEach(route -> list.add(ChainMap.init().set("name",route.getName()).set("location",route.getLocation())));
        return ReturnJsonUtil.ok("获取成功",list);
    }

    @ApiOperation("分组列表")
    @GetMapping(value = "/tags",produces="application/json")
    public ResponseData<JSONArray> tags(@ApiParam(value = "服务名称",required = true) @RequestParam String name){
        JpowerAssert.notEmpty(name, JpowerError.Arg, "服务名称不可为空");

        MonitorRestfulProperties.Route routes = properties.getRoutes().stream().filter(route -> Fc.equals(route.getName(),name)).findFirst().get();
        JSONArray list = taskService.tagList(routes);
        return ReturnJsonUtil.ok("获取成功",list);
    }

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

    @ApiOperation("获取接口设置")
    @GetMapping(value = "/setup",produces="application/json")
    public ResponseData<JSONObject> setup(@ApiParam(value = "监控服务", required = true) String server,@ApiParam("所属分组") String tag,@ApiParam("监控地址") String path,@ApiParam("请求类型") String method){
        JSONObject json = new JSONObject();
        return ReturnJsonUtil.ok("获取成功",json);
    }

    @ApiOperation("获取接口设置")
    @GetMapping(value = "/param",produces="application/json")
    public ResponseData<JSONObject> param(@ApiParam(value = "接口设置ID", required = true) String settingId){
        JSONObject json = new JSONObject();
        return ReturnJsonUtil.ok("获取成功",json);
    }

}
