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
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
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

    @ApiOperation("接口列表")
    @GetMapping(value = "/monitors",produces="application/json")
    public ResponseData<JSONArray> monitors(@ApiParam(value = "服务名称",required = true) @RequestParam String name,@ApiParam(value = "分组名称") @RequestParam(required = false) String tag){
        MonitorRestfulProperties.Route routes = properties.getRoutes().stream().filter(route -> Fc.equals(route.getName(),name)).findFirst().get();
        JSONArray list = taskService.tagList(routes);
        return ReturnJsonUtil.ok("获取成功",list);
    }


    @SneakyThrows
    public static void main(String[] args) {
        ScriptEngineManager m = new ScriptEngineManager();
        ScriptEngine engine = m.getEngineByName("JavaScript");
        String js = "function a(jsonstr){ var json = JSON.parse(jsonstr); return json.code == 200 }";

        if (js.contains("java")){
            throw new IllegalArgumentException("非法JS表达式");
        }

        engine.eval(js);

        Invocable inv = (Invocable) engine;
        JSONObject json = new JSONObject();
        json.put("code","200");
        Boolean res = (Boolean) inv.invokeFunction("a",json.toJSONString());

        System.out.println(res);
    }
}
