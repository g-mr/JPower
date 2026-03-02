package top.jpower.system.controller.function;

import com.github.xiaoymin.knife4j.annotations.Ignore;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.jpower.common.validated.group.Validation;
import top.jpower.core.auth.annotation.Function;
import top.jpower.core.auth.annotation.Menu;
import top.jpower.core.boot.argument.RequestSingleBody;
import top.jpower.core.boot.controller.BaseController;
import top.jpower.core.util.rsp.Pg;
import top.jpower.core.util.rsp.R;
import top.jpower.core.util.utils.Fc;
import top.jpower.system.dbs.entity.function.CoreTopMenu;
import top.jpower.system.service.role.CoreFunctionService;
import top.jpower.system.service.role.CoreMenuService;
import top.jpower.system.vo.MenuClientVO;
import top.jpower.system.vo.MenuSelectVO;
import top.jpower.system.vo.MenuVO;
import top.jpower.system.vo.SelectIdNameVO;

import java.util.List;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * 顶级菜单接口
 * 
 * @author mr.g
 */
@Tag(name = "顶部菜单管理")
@Validated
@RestController
@RequestMapping("/core/menu")
@RequiredArgsConstructor
public class TopMenuController extends BaseController {

    private final CoreMenuService menuService;
    private final CoreFunctionService functionService;

    @Function(value = "新增菜单",menus = {
		@Menu(client = "admin",menuCode = "SYSTEM_TOPMENU",code = "TOPMENU_ADD",type = Menu.TYPE.BTN)
    })
    @Operation(summary = "新增菜单")
    @PostMapping(value = "/add", produces = APPLICATION_JSON_VALUE)
    public R<Long> add(@Validated(Validation.Create.class) @RequestBody CoreTopMenu topMenu){
        return R.data(menuService.create(topMenu));
    }

    @Function(value = "更新菜单",menus = {
		@Menu(client = "admin",menuCode = "SYSTEM_TOPMENU",code = "TOPMENU_UPDATE",type = Menu.TYPE.BTN)
    })
    @Operation(summary = "更新菜单")
    @PutMapping(value = "/update", produces = APPLICATION_JSON_VALUE)
    public R<Boolean> update(@Validated(Validation.Update.class) @RequestBody CoreTopMenu topMenu){
        return R.status(menuService.editById(topMenu));
    }

    @Function(value = "菜单开关",menus = {
		@Menu(client = "admin",menuCode = "SYSTEM_TOPMENU",code = "TOPMENU_SWITCH",type = Menu.TYPE.BTN)
    })
    @Operation(summary = "菜单开关")
    @PutMapping(value = "/switch/{id}", produces = APPLICATION_JSON_VALUE)
    public R<Boolean> statusSwitch(@Parameter(description = "主键",required = true) @PathVariable("id") Long id,
								   @Parameter(description = "开关状态",required = true) @NotNull(message = "开关状态不可为空") @RequestSingleBody Boolean status){
        return R.status(menuService.updateStatusById(id, status));
    }

    @Function(value = "删除菜单",menus = {
		@Menu(client = "admin",menuCode = "SYSTEM_TOPMENU",code = "TOPMENU_DELETE",type = Menu.TYPE.BTN)
    })
    @Operation(summary = "删除菜单")
    @DeleteMapping(value = "/delete", produces = APPLICATION_JSON_VALUE)
    public R<Boolean> delete(@Parameter(description = "主键", required = true) @NotBlank(message = "ids不可为空") @RequestParam String ids){
        return R.status(menuService.removeByIds(Fc.toLongList(ids)));
    }

    @Function(value = "菜单列表",menus = {
		@Menu(client = "admin",menuCode = "SYSTEM_TOPMENU",code = "TOPMENU_LIST",type = Menu.TYPE.INTERFACE)
    })
    @Parameters({
		@Parameter(name = "pageNum", description = "第几页", example = "1", schema = @Schema(defaultValue = "1", type = "integer"), in = ParameterIn.QUERY, required = true),
		@Parameter(name = "pageSize", description = "每页长度", example = "10", schema = @Schema(defaultValue = "10", type = "integer"), in = ParameterIn.QUERY, required = true),
		@Parameter(name = "code", description = "菜单编号", in = ParameterIn.QUERY),
		@Parameter(name = "name", description = "菜单名称", in = ParameterIn.QUERY),
		@Parameter(name = "status_eq", description = "是否启用", in = ParameterIn.QUERY)
    })
    @Operation(summary = "菜单列表")
    @GetMapping(value = "/list", produces = APPLICATION_JSON_VALUE)
    public R<Pg<CoreTopMenu>> list(@Ignore @RequestParam(required = false) Map<String,Object> map){
        return R.data(menuService.pg(map));
    }

    @Function(value = "客户端顶部菜单树",menus = {
		@Menu(client = "admin",menuCode = "SYSTEM_ROLE",btnCode = "SYSTEM_DATASCOPE_LIST",code = "ROLE_CLIENT_TOPMENU",type = Menu.TYPE.INTERFACE)
    })
    @Operation(summary = "客户端顶部菜单树")
    @GetMapping(value = "/listName", produces = APPLICATION_JSON_VALUE)
    public R<List<MenuSelectVO>> listName(){
        return R.data(menuService.clientMenu());
    }

    @Function(value = "关联一级菜单ID",menus = {
		@Menu(client = "admin",menuCode = "SYSTEM_TOPMENU",code = "TOPMENU_FUNCTION_ID",type = Menu.TYPE.INTERFACE)
    })
    @Operation(summary = "顶部菜单关联的左侧第一级菜单ID")
    @GetMapping(value = "/listFunctionId/{menuId}", produces = APPLICATION_JSON_VALUE)
    public R<List<Long>> listFunctionId(@Parameter(description = "顶部菜单ID",required = true) @PathVariable("menuId") Long menuId){
        return R.data(menuService.listFunctionId(menuId));
    }

    @Function(value = "关联菜单",menus = {
		@Menu(client = "admin",menuCode = "SYSTEM_TOPMENU",code = "TOPMENU_FUNCTION",type = Menu.TYPE.BTN)
    })
    @Operation(summary = "一级菜单列表")
    @GetMapping(value = "/listFunction/{clientId}", produces = APPLICATION_JSON_VALUE)
    public R<List<SelectIdNameVO>> listFunction(@Parameter(description = "客户端ID",required = true) @PathVariable("clientId") Long clientId){
        return R.data(functionService.selectByClientId(clientId));
    }

    @Function(value = "保存一级菜单",menus = {
		@Menu(client = "admin",menuCode = "SYSTEM_TOPMENU",code = "TOPMENU_FUNCTION_SAVE",type = Menu.TYPE.BTN)
    })
    @Operation(summary = "设置顶部菜单关联的一级菜单")
    @PostMapping(value = "/saveFunction/{menuId}", produces = APPLICATION_JSON_VALUE)
    public R<Boolean> saveFunction(@Parameter(description = "顶部菜单ID",required = true) @PathVariable("menuId") Long menuId,
								   @Parameter(description = "功能ID") @RequestSingleBody List<Long> functionIds){
        return R.status(menuService.saveFunction(menuId, functionIds));
    }

    @Operation(summary = "获取当前登录用户的顶级菜单")
    @GetMapping(value = "/roleMenu", produces = APPLICATION_JSON_VALUE)
    public R<List<MenuVO>> roleMenu(){
        return R.data(menuService.roleMenu());
    }

    @Function(value = "顶级菜单选项",menus = {
		@Menu(client = "admin",menuCode = "SYSTEM_FUNCTION",code = "FUNCTION_TOPMENU_SELECT",type = Menu.TYPE.INTERFACE),
		@Menu(client = "admin",menuCode = "SYSTEM_DATASCOPE",code = "DATASCOPE_TOPMENU_SELECT",type = Menu.TYPE.INTERFACE),
		@Menu(client = "admin",menuCode = "SYSTEM_ROLE",btnCode = "SYSTEM_ROLE_SELECT_URL",code = "ROLE_TOPMENU",type = Menu.TYPE.INTERFACE)
    })
    @Operation(summary = "获取顶级菜单下拉框", description = "只获取当前用户的权限")
    @GetMapping(value = "/select/{clientId}", produces = APPLICATION_JSON_VALUE)
    public R<List<MenuClientVO>> select(@Parameter(description = "客户端ID",required = true) @PathVariable("clientId") Long clientId){
        return R.data(menuService.selectList(clientId));
    }
}
