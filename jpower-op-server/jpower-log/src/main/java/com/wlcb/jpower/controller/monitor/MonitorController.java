package com.wlcb.jpower.controller.monitor;

import com.wlcb.jpower.module.base.vo.ResponseData;
import com.wlcb.jpower.module.common.utils.ReturnJsonUtil;
import com.wlcb.jpower.properties.MonitorRestfulProperties;
import com.wlcb.jpower.service.TaskService;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author mr.g
 * @Date 2021/4/15 0015 21:06
 */
@Api(tags = "接口监控")
@RestController
@RequestMapping("/")
@AllArgsConstructor
public class MonitorController {

    private final MonitorRestfulProperties properties;
    private final TaskService taskService;

    @GetMapping(value = "/test",produces="application/json")
    public ResponseData test(){
        properties.getRoutes().forEach(taskService::process);
        return ReturnJsonUtil.status(true);
    }

}
