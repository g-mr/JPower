package com.qidiangk.smart.log.controller.log;

import com.github.xiaoymin.knife4j.annotations.Ignore;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.jpower.core.auth.annotation.Function;
import top.jpower.core.auth.annotation.Menu;
import top.jpower.core.boot.controller.BaseController;
import top.jpower.core.dbs.support.Wrappers;
import top.jpower.core.util.rsp.Pg;
import top.jpower.core.util.rsp.R;
import com.qidiangk.smart.log.dbs.entity.LogError;
import com.qidiangk.smart.log.dbs.entity.LogOperate;
import com.qidiangk.smart.log.service.ErrorLogService;
import com.qidiangk.smart.log.service.OperateLogService;

import java.util.Map;

/**
 * @author mr.g
 */
@Tag(name = "系统日志")
@RestController
@RequestMapping("/log")
@RequiredArgsConstructor
public class LogController extends BaseController {

    private final ErrorLogService errorLogService;
    private final OperateLogService operateLogService;

    @Function(value = "操作日志",menus = {
		@Menu(client = "admin",menuCode = "OPERATE_LOG",code = "OPERATE_LOG_LIST",type = Menu.TYPE.INTERFACE)
    })
    @Operation(summary = "操作日志列表")
    @Parameters({
		@Parameter(name = "pageNum", description = "第几页", example = "1", in = ParameterIn.QUERY,required = true),
		@Parameter(name = "pageSize", description = "每页长度", example = "10", in = ParameterIn.QUERY,required = true),
		@Parameter(name = "title", description = "操作标题", in = ParameterIn.QUERY),
		@Parameter(name = "businessType_eq", description = "业务类型", in = ParameterIn.QUERY),
		@Parameter(name = "serverName", description = "服务名称", in = ParameterIn.QUERY),
		@Parameter(name = "clientCode_eq", description = "客户端编码", in = ParameterIn.QUERY),
		@Parameter(name = "operName", description = "操作人员", in = ParameterIn.QUERY),
		@Parameter(name = "createTime_dategt", description = "开始时间", in = ParameterIn.QUERY),
		@Parameter(name = "createTime_datelt", description = "结束时间", in = ParameterIn.QUERY)
    })
    @GetMapping("/operate/list")
    public R<Pg<LogOperate>> listOperateLog(@Ignore @RequestParam(required = false) Map<String,Object> operateLog){
        return R.data(operateLogService.pg(Wrappers.getQueryWrapper(operateLog).orderBy(LogOperate::getCreateTime).desc()));
    }

    @Function(value = "错误日志",menus = {
		@Menu(client = "admin",menuCode = "ERROR_LOG",code = "ERROR_LOG_LIST",type = Menu.TYPE.INTERFACE)
    })
    @Operation(summary = "错误日志列表")
    @Parameters({
            @Parameter(name = "pageNum", description = "第几页",example = "1", in = ParameterIn.QUERY,required = true),
            @Parameter(name = "pageSize", description = "每页长度",example = "10", in = ParameterIn.QUERY,required = true),
            @Parameter(name = "serverName", description = "服务名称", in = ParameterIn.QUERY),
            @Parameter(name = "exceptionName", description = "异常名称", in = ParameterIn.QUERY),
            @Parameter(name = "clientCode_eq", description = "客户端编码", in = ParameterIn.QUERY),
            @Parameter(name = "operName", description = "操作人员", in = ParameterIn.QUERY),
            @Parameter(name = "createTime_dategt", description = "开始时间", in = ParameterIn.QUERY),
            @Parameter(name = "createTime_datelt", description = "结束时间", in = ParameterIn.QUERY)
    })
    @GetMapping("/error/list")
    public R<Pg<LogError>> listErrorLog(@Ignore @RequestParam(required = false) Map<String,Object> errorLog){
        return R.data(errorLogService.pg(Wrappers.getQueryWrapper(errorLog).orderBy(LogError::getCreateTime).desc()));
    }
}
