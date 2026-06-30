package com.qidiangk.smart.system.controller.params;

import com.github.xiaoymin.knife4j.annotations.Ignore;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.qidiangk.smart.common.validated.group.Validation;
import top.jpower.core.auth.annotation.Function;
import top.jpower.core.auth.annotation.Menu;
import top.jpower.core.boot.controller.BaseController;
import top.jpower.core.util.rsp.Pg;
import top.jpower.core.util.rsp.R;
import top.jpower.core.util.utils.Fc;
import com.qidiangk.smart.system.dbs.entity.params.CoreParam;
import com.qidiangk.smart.system.service.params.CoreParamService;

import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * 系统参数管理
 *
 * @author mr.g
 */
@Tag(name = "系统参数管理")
@Validated
@RestController
@RequestMapping("/core/param")
@AllArgsConstructor
public class ParamsController extends BaseController {

    private CoreParamService paramService;

    @Function(value = "列表",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_PARAMS",code = "PARAM_LIST",type = Menu.TYPE.INTERFACE)
    })
    @Operation(summary = "系统参数分页列表")
    @Parameters({
		@Parameter(name = "pageNum", description = "第几页", example = "1", schema = @Schema(defaultValue = "1", type = "integer"), in = ParameterIn.QUERY, required = true),
		@Parameter(name = "pageSize", description = "每页长度", example = "10", schema = @Schema(defaultValue = "10", type = "integer"), in = ParameterIn.QUERY, required = true),
		@Parameter(name = "code", description = "编码", in = ParameterIn.QUERY),
		@Parameter(name = "name", description = "名称", in = ParameterIn.QUERY),
		@Parameter(name = "value", description = "值", in = ParameterIn.QUERY)
    })
    @GetMapping(value = "/list" , produces = APPLICATION_JSON_VALUE)
    public R<Pg<CoreParam>> list(@Ignore @RequestParam(required = false) Map<String,Object> map){
        return R.data(paramService.pageByMap(map));
    }

    @Function(value = "新增",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_PARAMS",code = "SYSTEM_PARAMS_ADD",type = Menu.TYPE.BTN)
    })
    @Operation(summary = "新增系统参数")
    @PostMapping(value = "/add", produces = APPLICATION_JSON_VALUE)
    public R<Long> add(@Validated(Validation.Create.class) @RequestBody CoreParam coreParam){
        return R.data(paramService.create(coreParam));
    }

    @Function(value = "编辑",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_PARAMS",code = "SYSTEM_PARAMS_UPDATE",type = Menu.TYPE.BTN)
    })
    @Operation(summary = "修改系统参数")
    @PutMapping(value = "/update", produces = APPLICATION_JSON_VALUE)
    public R<Boolean> update(@Validated(Validation.Create.class) @RequestBody CoreParam coreParam){
        return R.status(paramService.updateById(coreParam));
    }

    @Function(value = "删除",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_PARAMS",code = "SYSTEM_PARAMS_DELETE",type = Menu.TYPE.BTN)
    })
    @Operation(summary = "删除系统参数")
    @DeleteMapping(value = "/delete", produces = APPLICATION_JSON_VALUE)
    public R<Boolean> delete(@Parameter(description = "主键",required = true) @NotBlank(message = "主键不可为空") @RequestParam String ids){
        return R.status(paramService.removeByIds(Fc.toLongList(ids)));
    }

















    @Function(value = "详情",menus = {
		@Menu(client = "admin", menuCode = "SYSTEM_PARAMS",code = "SYSTEM_PARAMS_DETAIL",type = Menu.TYPE.BTN)
    })
    @Operation(summary = "通过Id获取参数详情")
    @GetMapping(value = "/queryById/{id}", produces = APPLICATION_JSON_VALUE)
    public R<CoreParam> queryById(@Parameter(description = "主键ID") @NotNull(message = "ID不可为空") @PathVariable("id") Long id){
        return R.data(paramService.getById(id));
    }

}
