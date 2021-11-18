package com.wlcb.jpower.controller.log;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wlcb.jpower.dbs.entity.TbLogError;
import com.wlcb.jpower.dbs.entity.TbLogOperate;
import com.wlcb.jpower.module.base.vo.ResponseData;
import com.wlcb.jpower.module.common.controller.BaseController;
import com.wlcb.jpower.module.common.page.PaginationContext;
import com.wlcb.jpower.module.common.utils.ReturnJsonUtil;
import com.wlcb.jpower.module.mp.support.Condition;
import com.wlcb.jpower.module.mp.support.SqlKeyword;
import com.wlcb.jpower.service.ErrorLogService;
import com.wlcb.jpower.service.OperateLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Date;
import java.util.Map;

/**
 * @Author mr.g
 * @Date 2021/5/1 0001 19:32
 */
@Api(tags = "系统日志")
@RestController
@RequestMapping("/log")
@RequiredArgsConstructor
public class LogController extends BaseController {

    private final ErrorLogService errorLogService;
    private final OperateLogService operateLogService;

    @ApiOperation("操作日志列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum",value = "第几页",defaultValue = "1",paramType = "query",dataType = "int",required = true),
            @ApiImplicitParam(name = "pageSize",value = "每页长度",defaultValue = "10",paramType = "query",dataType = "int",required = true),
            @ApiImplicitParam(name = "title",value = "操作标题",paramType = "query"),
            @ApiImplicitParam(name = "businessType_eq",value = "业务类型",paramType = "query"),
            @ApiImplicitParam(name = "serverName",value = "服务名称",paramType = "query"),
            @ApiImplicitParam(name = "clientCode_eq",value = "客户端编码",paramType = "query"),
            @ApiImplicitParam(name = "operName",value = "操作人员",paramType = "query"),
            @ApiImplicitParam(name = "createTime" + SqlKeyword.DATE_GT,value = "开始时间",paramType = "query", dataTypeClass = Date.class),
            @ApiImplicitParam(name = "createTime" + SqlKeyword.DATE_LT,value = "结束时间",paramType = "query", dataTypeClass = Date.class)
    })
    @GetMapping("/operate/list")
    public ResponseData<Page<TbLogOperate>> listOperateLog(@ApiIgnore @RequestParam Map<String,Object> operateLog){
        Page<TbLogOperate> operate = operateLogService.page(PaginationContext.getMpPage(),
                Condition.getQueryWrapper(operateLog,TbLogOperate.class).lambda().orderByDesc(TbLogOperate::getCreateTime));
        return ReturnJsonUtil.ok("请求成功", operate);
    }

    @ApiOperation("错误日志列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum",value = "第几页",defaultValue = "1",paramType = "query",dataType = "int",required = true),
            @ApiImplicitParam(name = "pageSize",value = "每页长度",defaultValue = "10",paramType = "query",dataType = "int",required = true),
            @ApiImplicitParam(name = "serverName",value = "服务名称",paramType = "query"),
            @ApiImplicitParam(name = "exceptionName",value = "异常名称",paramType = "query"),
            @ApiImplicitParam(name = "clientCode_eq",value = "客户端编码",paramType = "query"),
            @ApiImplicitParam(name = "operName",value = "操作人员",paramType = "query"),
            @ApiImplicitParam(name = "createTime" + SqlKeyword.DATE_GT,value = "开始时间",paramType = "query", dataTypeClass = Date.class),
            @ApiImplicitParam(name = "createTime" + SqlKeyword.DATE_LT,value = "结束时间",paramType = "query", dataTypeClass = Date.class)
    })
    @GetMapping("/error/list")
    public ResponseData<Page<TbLogError>> listErrorLog(@ApiIgnore @RequestParam Map<String,Object> errorLog){
        Page<TbLogError> errorPage = errorLogService.page(PaginationContext.getMpPage(),
                Condition.getQueryWrapper(errorLog,TbLogError.class).lambda().orderByDesc(TbLogError::getCreateTime));
        return ReturnJsonUtil.ok("请求成功",errorPage);
    }
}
