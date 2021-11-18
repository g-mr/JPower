package com.wlcb.jpower.controller.monitor;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wlcb.jpower.dbs.entity.TbLogMonitorResult;
import com.wlcb.jpower.module.base.vo.ResponseData;
import com.wlcb.jpower.module.common.controller.BaseController;
import com.wlcb.jpower.module.common.support.BeanExcelUtil;
import com.wlcb.jpower.module.common.utils.DateUtil;
import com.wlcb.jpower.module.common.utils.ReturnJsonUtil;
import com.wlcb.jpower.module.common.utils.constants.ImportExportConstants;
import com.wlcb.jpower.module.mp.support.Condition;
import com.wlcb.jpower.module.mp.support.SqlKeyword;
import com.wlcb.jpower.service.MonitorResultService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author mr.g
 * @Date 2021/4/15 0015 21:06
 */
@Api(tags = "接口监控")
@RestController
@RequestMapping("/monitor/log")
@AllArgsConstructor
public class MonitorController extends BaseController {

    private final MonitorResultService monitorResultService;

    @ApiOperation(value = "监控结果列表",notes = "默认查询最近一个月得")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "pageNum",value = "第几页",defaultValue = "1",paramType = "query",dataType = "int",required = true),
        @ApiImplicitParam(name = "pageSize",value = "每页长度",defaultValue = "10",paramType = "query",dataType = "int",required = true),
        @ApiImplicitParam(name = "name_eq",value = "服务名称",paramType = "query"),
        @ApiImplicitParam(name = "path",value = "接口地址",paramType = "query"),
        @ApiImplicitParam(name = "createTime" + SqlKeyword.DATE_GT,value = "开始时间",paramType = "query", dataTypeClass = Date.class),
        @ApiImplicitParam(name = "createTime" + SqlKeyword.DATE_LT,value = "结束时间",paramType = "query", dataTypeClass = Date.class)
    })
    @GetMapping(value = "/list",produces="application/json")
    public ResponseData<Page<TbLogMonitorResult>> list(@ApiIgnore @RequestParam Map<String,Object> map){
        return ReturnJsonUtil.ok("获取成功",monitorResultService.pageList(initMap(map)));
    }

    private Map<String,Object> initMap(Map<String,Object> map){
        if (!map.containsKey("createTime" + SqlKeyword.DATE_GT)){
            map.put("createTime" + SqlKeyword.DATE_GT,DateUtil.getDate(DateUtil.getDate(new Date(), -30),DateUtil.PATTERN_DATETIME));
        }

        if (!map.containsKey("createTime" + SqlKeyword.DATE_LT)){
            map.put("createTime" + SqlKeyword.DATE_LT,DateUtil.getDate(DateUtil.PATTERN_DATETIME));
        }
        return map;
    };

    @ApiOperation(value = "导出结果列表")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "pageNum",value = "第几页",defaultValue = "1",paramType = "query",dataType = "int",required = true),
        @ApiImplicitParam(name = "pageSize",value = "每页长度",defaultValue = "10",paramType = "query",dataType = "int",required = true),
        @ApiImplicitParam(name = "name",value = "服务名称",paramType = "query"),
        @ApiImplicitParam(name = "path",value = "接口地址",paramType = "query"),
        @ApiImplicitParam(name = "createTime" + SqlKeyword.DATE_GT,value = "开始时间",paramType = "query", dataTypeClass = Date.class),
        @ApiImplicitParam(name = "createTime" + SqlKeyword.DATE_LT,value = "结束时间",paramType = "query", dataTypeClass = Date.class)
    })
    @GetMapping(value = "/export")
    public void export(@ApiIgnore @RequestParam Map<String,Object> map){
        List<TbLogMonitorResult> list = monitorResultService.list(Condition.getQueryWrapper(initMap(map),TbLogMonitorResult.class).lambda().orderByDesc(TbLogMonitorResult::getCreateTime));

        BeanExcelUtil<TbLogMonitorResult> beanExcelUtil = new BeanExcelUtil<>(TbLogMonitorResult.class, ImportExportConstants.EXPORT_PATH);
        ResponseData<String> responseData = beanExcelUtil.exportExcel(list, "监控结果");
        download(responseData,"接口监控.xlsx");
    }
}
