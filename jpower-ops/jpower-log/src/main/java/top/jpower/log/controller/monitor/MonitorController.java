package top.jpower.log.controller.monitor;

import com.github.xiaoymin.knife4j.annotations.Ignore;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.jpower.core.auth.annotation.Function;
import top.jpower.core.auth.annotation.Menu;
import top.jpower.core.boot.controller.BaseController;
import top.jpower.core.dbs.support.WrapperKeyword;
import top.jpower.core.dbs.support.Wrappers;
import top.jpower.core.util.constants.ImportExportConstants;
import top.jpower.core.util.rsp.Pg;
import top.jpower.core.util.rsp.R;
import top.jpower.core.util.support.excel.BeanExcelUtil;
import top.jpower.core.util.utils.DateUtil;
import top.jpower.core.util.utils.FileUtil;
import top.jpower.log.dbs.entity.LogMonitorResult;
import top.jpower.log.service.MonitorResultService;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author mr.g
 * @Date 2021/4/15 0015 21:06
 */
@Tag(name = "接口监控")
@RestController
@RequestMapping("/monitor/log")
@AllArgsConstructor
public class MonitorController extends BaseController {

    private final MonitorResultService monitorResultService;

    @Function(value = "监控结果",menus = {
            @Menu(client = "admin",menuCode = "MONITOR_RESULT",code = "MONITOR_RESULTS_LIST",type = Menu.TYPE.INTERFACE)
    })
    @Operation(summary = "监控结果列表", description = "默认查询最近一个月得")
    @Parameters({
		@Parameter(name = "pageNum", description = "第几页", example = "1", schema = @Schema(defaultValue = "1", type = "integer"), in = ParameterIn.QUERY, required = true),
		@Parameter(name = "pageSize", description = "每页长度", example = "10", schema = @Schema(defaultValue = "10", type = "integer"), in = ParameterIn.QUERY, required = true),
        @Parameter(name = "name_eq", description = "服务名称", in = ParameterIn.QUERY),
        @Parameter(name = "path", description = "接口地址", in = ParameterIn.QUERY),
        @Parameter(name = "createTime_dategt", description = "开始时间", in = ParameterIn.QUERY),
        @Parameter(name = "createTime_datelt", description = "结束时间", in = ParameterIn.QUERY)
    })
    @GetMapping(value = "/list",produces="application/json")
    public R<Pg<LogMonitorResult>> list(@Ignore @RequestParam(required = false) Map<String,Object> map){
        return R.data(monitorResultService.pageList(initMap(map)));
    }

    private Map<String,Object> initMap(Map<String,Object> map){
        if (!map.containsKey("createTime" + WrapperKeyword.DATE_GT.getSuffixKeyword())){
            map.put("createTime" + WrapperKeyword.DATE_GT.getSuffixKeyword(), DateUtil.formatDateTime(DateUtil.offsetDay(new Date(), -30)));
        }

        if (!map.containsKey("createTime" + WrapperKeyword.DATE_LT.getSuffixKeyword())){
            map.put("createTime" + WrapperKeyword.DATE_LT.getSuffixKeyword(),DateUtil.now());
        }
        return map;
    }

    @Function(value = "导出监控结果",menus = {
		@Menu(client = "admin",menuCode = "MONITOR_RESULT",code = "MONITOR_RESULTS_EXPORT",type = Menu.TYPE.BTN)
    })
    @Operation(summary = "导出结果列表")
    @Parameters({
		@Parameter(name = "pageNum", description = "第几页", example = "1", schema = @Schema(defaultValue = "1", type = "integer"), in = ParameterIn.QUERY, required = true),
		@Parameter(name = "pageSize", description = "每页长度", example = "10", schema = @Schema(defaultValue = "10", type = "integer"), in = ParameterIn.QUERY, required = true),
        @Parameter(name = "name", description = "服务名称", in = ParameterIn.QUERY),
        @Parameter(name = "path", description = "接口地址", in = ParameterIn.QUERY),
        @Parameter(name = "createTime_dategt", description = "开始时间", in = ParameterIn.QUERY),
        @Parameter(name = "createTime_datelt", description = "结束时间", in = ParameterIn.QUERY)
    })
    @GetMapping(value = "/export")
    public void export(@Ignore @RequestParam(required = false) Map<String,Object> map) throws IOException {
        List<LogMonitorResult> list = monitorResultService.list(Wrappers.getQueryWrapper(initMap(map)).orderBy(LogMonitorResult::getCreateTime).desc());

        BeanExcelUtil<LogMonitorResult> beanExcelUtil = new BeanExcelUtil<>(LogMonitorResult.class, ImportExportConstants.EXPORT_PATH);
		String str = beanExcelUtil.exportExcel(list, "监控结果");
        File file = new File(ImportExportConstants.EXPORT_PATH + str);
        FileUtil.download(file, getResponse(), "接口监控.xlsx");
    }
}
