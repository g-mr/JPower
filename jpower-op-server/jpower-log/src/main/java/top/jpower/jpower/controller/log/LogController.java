package top.jpower.jpower.controller.log;

import top.jpower.jpower.dbs.entity.TbLogError;
import top.jpower.jpower.dbs.entity.TbLogOperate;
import top.jpower.jpower.module.annotation.Function;
import top.jpower.jpower.module.annotation.Menu;
import top.jpower.jpower.module.base.vo.Pg;
import top.jpower.jpower.module.base.vo.ResponseData;
import top.jpower.jpower.module.common.controller.BaseController;
import top.jpower.jpower.module.common.page.PaginationContext;
import top.jpower.jpower.module.common.utils.ReturnJsonUtil;
import top.jpower.jpower.module.mp.support.Condition;
import top.jpower.jpower.service.ErrorLogService;
import top.jpower.jpower.service.OperateLogService;
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

    @Function(value = "操作日志",menus = {
            @Menu(client = "admin",menuCode = "OPERATE_LOG",code = "OPERATE_LOG_LIST",type = Menu.TYPE.INTERFACE)
    })
    @ApiOperation("操作日志列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum",value = "第几页",defaultValue = "1",paramType = "query",dataType = "int",required = true),
            @ApiImplicitParam(name = "pageSize",value = "每页长度",defaultValue = "10",paramType = "query",dataType = "int",required = true),
            @ApiImplicitParam(name = "title",value = "操作标题",paramType = "query"),
            @ApiImplicitParam(name = "businessType_eq",value = "业务类型",paramType = "query"),
            @ApiImplicitParam(name = "serverName",value = "服务名称",paramType = "query"),
            @ApiImplicitParam(name = "clientCode_eq",value = "客户端编码",paramType = "query"),
            @ApiImplicitParam(name = "operName",value = "操作人员",paramType = "query"),
            @ApiImplicitParam(name = "createTime_dategt",value = "开始时间",paramType = "query", dataTypeClass = Date.class),
            @ApiImplicitParam(name = "createTime_datelt",value = "结束时间",paramType = "query", dataTypeClass = Date.class)
    })
    @GetMapping("/operate/list")
    public ResponseData<Pg<TbLogOperate>> listOperateLog(@ApiIgnore @RequestParam Map<String,Object> operateLog){
        return ReturnJsonUtil.ok("请求成功", operateLogService.page(PaginationContext.getMpPage(),
                Condition.getQueryWrapper(operateLog,TbLogOperate.class).lambda().orderByDesc(TbLogOperate::getCreateTime)));
    }

    @Function(value = "错误日志",menus = {
            @Menu(client = "admin",menuCode = "ERROR_LOG",code = "ERROR_LOG_LIST",type = Menu.TYPE.INTERFACE)
    })
    @ApiOperation("错误日志列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum",value = "第几页",defaultValue = "1",paramType = "query",dataType = "int",required = true),
            @ApiImplicitParam(name = "pageSize",value = "每页长度",defaultValue = "10",paramType = "query",dataType = "int",required = true),
            @ApiImplicitParam(name = "serverName",value = "服务名称",paramType = "query"),
            @ApiImplicitParam(name = "exceptionName",value = "异常名称",paramType = "query"),
            @ApiImplicitParam(name = "clientCode_eq",value = "客户端编码",paramType = "query"),
            @ApiImplicitParam(name = "operName",value = "操作人员",paramType = "query"),
            @ApiImplicitParam(name = "createTime_dategt",value = "开始时间",paramType = "query", dataTypeClass = Date.class),
            @ApiImplicitParam(name = "createTime_datelt",value = "结束时间",paramType = "query", dataTypeClass = Date.class)
    })
    @GetMapping("/error/list")
    public ResponseData<Pg<TbLogError>> listErrorLog(@ApiIgnore @RequestParam Map<String,Object> errorLog){
        return ReturnJsonUtil.ok("请求成功",errorLogService.page(PaginationContext.getMpPage(),
                Condition.getQueryWrapper(errorLog,TbLogError.class).lambda().orderByDesc(TbLogError::getCreateTime)));
    }
}
