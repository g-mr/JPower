package com.wlcb.jpower.controller.monitor;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wlcb.jpower.module.base.vo.ResponseData;
import com.wlcb.jpower.module.common.support.ChainMap;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.ReturnJsonUtil;
import com.wlcb.jpower.properties.MonitorRestfulProperties;
import com.wlcb.jpower.properties.MonitorRestfulProperties.Route;
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
 * @Date 2021/4/15 0015 21:06
 */
@Api(tags = "接口监控")
@RestController
@RequestMapping("/monitor")
@AllArgsConstructor
public class MonitorController {

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

        Route routes = properties.getRoutes().stream().filter(route -> Fc.equals(route.getName(),name)).findFirst().get();
        JSONArray list = Fc.isNull(routes)?new JSONArray():taskService.tagList(routes);
        return ReturnJsonUtil.ok("获取成功",list);
    }

}
