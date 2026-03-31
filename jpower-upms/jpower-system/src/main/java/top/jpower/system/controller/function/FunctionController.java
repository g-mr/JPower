package top.jpower.system.controller.function;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.tree.Tree;
import com.github.xiaoymin.knife4j.annotations.Ignore;
import com.mybatisflex.core.util.UpdateEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.jpower.common.validated.group.Validation;
import top.jpower.core.auth.annotation.Function;
import top.jpower.core.auth.annotation.Menu;
import top.jpower.core.auth.utils.ShieldUtil;
import top.jpower.core.boot.argument.RequestSingleBody;
import top.jpower.core.boot.controller.BaseController;
import top.jpower.core.util.rsp.R;
import top.jpower.core.util.utils.Fc;
import top.jpower.system.dbs.entity.function.CoreFunction;
import top.jpower.system.service.role.CoreFunctionService;
import top.jpower.system.vo.FunctionSimpleVO;
import top.jpower.system.vo.FunctionVO;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static top.jpower.core.util.constants.JpowerConstants.TOP_CODE;
import static top.jpower.core.util.constants.JpowerConstants.TOP_CODE_LONG;

/**
 * 菜单控制器
 * 
 * @author mr.g
 */
@Tag(name = "菜单管理")
@Validated
@RestController
@RequestMapping("/core/function")
@RequiredArgsConstructor
public class FunctionController extends BaseController {

    private final CoreFunctionService coreFunctionService;

	@Operation(summary = "查询登录用户所有按钮接口资源（用于页面权限）", description = "用于页面权限判断，会把顶级按钮一起返回，顶级按钮代表所有菜单都可拥有权限")
	@GetMapping(value = "/but/code", produces = APPLICATION_JSON_VALUE)
	public R<List<String>> butCode() {
		return R.data(coreFunctionService.listBtnByRoleId(ShieldUtil.getUserRole()));
	}

	@Operation(summary = "页面菜单获取")
	@GetMapping(value = "/listMenuTree", produces = APPLICATION_JSON_VALUE)
	public R<List<Tree<Long>>> listMenuTree(@Parameter(description = "顶部菜单ID") Long topMenuId){
		return R.data(coreFunctionService.listMenuByRoleId(ShieldUtil.getUserRole(), ShieldUtil.getClientCode(), topMenuId, Boolean.TRUE));
	}

	@Function(value = "菜单列表",menus = {
			@Menu(client = "admin",menuCode = "SYSTEM_FUNCTION",code = "CHILD_FUNCTION",type = Menu.TYPE.INTERFACE)
	})
	@Operation(summary = "根据父节点查询子节点功能")
	@Parameters({
			@Parameter(name = "clientId_eq", description = "客户端ID", in = ParameterIn.QUERY,required = true),
			@Parameter(name = "parentId_eq", description = "父级节点", example = TOP_CODE,required = true, in = ParameterIn.QUERY),
			@Parameter(name = "alias", description = "别名", in = ParameterIn.QUERY),
			@Parameter(name = "code", description = "编码", in = ParameterIn.QUERY),
			@Parameter(name = "functionType_eq", description = "是否菜单 字典YN01", in = ParameterIn.QUERY),
			@Parameter(name = "functionName", description = "功能名称", in = ParameterIn.QUERY),
			@Parameter(name = "url", description = "功能URL", in = ParameterIn.QUERY),
			@Parameter(name = "menuId_eq", description = "顶级菜单ID", in = ParameterIn.QUERY)
	})
	@GetMapping(value = "/listByParent/{clientId}", produces = APPLICATION_JSON_VALUE)
	public R<List<FunctionVO>> list(@Parameter(description = "客户端ID",required = true) @PathVariable("clientId") Long clientId,
									@Ignore @RequestParam(required = false) Map<String,Object> map){
		map.put("clientId_eq", clientId);
		map.putIfAbsent("parentId_eq", TOP_CODE_LONG);
		return R.data(coreFunctionService.listFunction(map));
	}






















    @Function(value = "菜单按钮树形",menus = {
		@Menu(client = "admin",menuCode = "SYSTEM_FUNCTION",code = "SYSTEM_FUNCTION_MENUBTN",type = Menu.TYPE.INTERFACE)
    })
    @Operation(summary = "查询登录用户所有菜单按钮树形结构")
    @GetMapping(value = "/treeMenuTypeByClientId", produces = APPLICATION_JSON_VALUE)
    public R<List<Tree<Long>>> treeMenuTypeByClientId(@Parameter(description = "客户端ID") @RequestParam(required = false) Long clientId) {
        if (Fc.isNull(clientId)) {
            return R.data(ListUtil.empty());
        }
        return R.data(coreFunctionService.treeMenuTypeByClientId(ShieldUtil.isRoot() ? null : ShieldUtil.getUserRole(), clientId));
    }


    @Function(value = "树形按钮",menus = {
		@Menu(client = "admin",menuCode = "SYSTEM_ROLE",btnCode = "SYSTEM_ROLE_SELECT_URL",code = "SYSTEM_ROLE_BUT_TREE",type = Menu.TYPE.INTERFACE)
    })
    @Operation(summary = "登录用户树形按钮接口", description = "当不传菜单ID时，会查出顶级按钮接口；单独查一个菜单时，不会把顶级按钮接口返回")
    @GetMapping(value = "/treeButByMenu", produces = APPLICATION_JSON_VALUE)
    public R<List<Tree<Long>>> treeButByMenu(@Parameter(description = "菜单Id",required = true) @RequestParam(required = false, defaultValue = TOP_CODE) Long id,
											   @Parameter(description = "客户端ID",required = true) @NotNull(message = "客户端ID不可为空") @RequestParam(required = false) Long clientId){
        return R.data(coreFunctionService.treeButByMenu(ShieldUtil.getUserRole(), id, clientId));
    }

    @Function(value = "接口资源",menus = {
		@Menu(client = "admin",menuCode = "SYSTEM_ROLE", btnCode = "SYSTEM_ROLE_SELECT_URL",code = "ROLE_INTERFACE_LIST",type = Menu.TYPE.INTERFACE)
    })
    @Operation(summary = "接口资源")
    @GetMapping(value = "/listInterface", produces = APPLICATION_JSON_VALUE)
    public R<List<FunctionSimpleVO>> listInterface(@Parameter(description = "客户端ID",required = true) @NotNull(message = "客户端ID不可为空") @RequestParam(required = false) Long clientId){
        return R.data(coreFunctionService.listInterface(ShieldUtil.getUserRole(), clientId));
    }

    @Function(value = "新增",menus = {
		@Menu(client = "admin",menuCode = "SYSTEM_FUNCTION",code = "SYSTEM_FUNCTION_ADD",type = Menu.TYPE.BTN)
    })
    @Operation(summary = "新增")
    @PostMapping(value = "/add", produces = APPLICATION_JSON_VALUE)
    public R<Long> add(@Validated(Validation.Create.class) @RequestBody CoreFunction coreFunction){
		return R.data(coreFunctionService.create(coreFunction));
    }

    @Function(value = "删除",menus = {
		@Menu(client = "admin",menuCode = "SYSTEM_FUNCTION",code = "SYSTEM_FUNCTION_DELETE",type = Menu.TYPE.BTN)
    })
    @Operation(summary = "删除")
    @DeleteMapping(value = "/delete", produces = APPLICATION_JSON_VALUE)
    public R<Boolean> delete(@Parameter(description = "主键 多个逗号分割",required = true) @NotBlank(message = "ids不可为空") @RequestParam String ids){
		return R.status(coreFunctionService.delete(Fc.toLongList(ids)));
    }

    @Function(value = "修改",menus = {
		@Menu(client = "admin",menuCode = "SYSTEM_FUNCTION",code = "SYSTEM_FUNCTION_UPDATE",type = Menu.TYPE.BTN)
    })
    @Operation(summary = "修改")
    @PutMapping(value = "/update", produces = APPLICATION_JSON_VALUE)
    public R<Boolean> update(@Validated(Validation.Update.class) @RequestBody CoreFunction coreFunction){
		return R.status(coreFunctionService.update(coreFunction));
    }

    @Function(value = "设置层级",menus = {
		@Menu(client = "admin",menuCode = "SYSTEM_FUNCTION",code = "SYSTEM_FUNCTION_HIERARCHY",type = Menu.TYPE.BTN)
    })
    @Operation(summary = "保存层级")
    @PostMapping(value = "/saveHierarchy", produces = APPLICATION_JSON_VALUE)
    public R<Boolean> saveHierarchy(@Parameter(description = "上级ID",required = true) @RequestSingleBody(defaultValue = TOP_CODE) Long parentId,
						   			@Parameter(description = "主键，多个逗号分割",required = true) @NotBlank(message = "ids不可为空") @RequestSingleBody List<Long> ids){
		return R.status(coreFunctionService.saveHierarchy(parentId, ids));
    }

    @Function(value = "功能权限",menus = {
		@Menu(client = "admin",menuCode = "SYSTEM_ROLE",code = "SYSTEM_ROLE_SELECT_URL",type = Menu.TYPE.BTN)
    })
    @Operation(summary = "根据角色ID查询所有的权限ID")
    @GetMapping(value = "/queryUrlIdByRole", produces = APPLICATION_JSON_VALUE)
    public R<Set<Long>> queryUrlIdByRole(@Parameter(description = "角色ID 多个逗号分割",required = true) @NotBlank(message = "roleIds不可为空") @RequestParam String roleIds){
        return R.data(coreFunctionService.queryUrlIdByRole(Fc.toLongList(roleIds)));
    }

    @Operation(summary = "懒加载登录用户所有功能树形结构")
    @GetMapping(value = "/lazyTree", produces = APPLICATION_JSON_VALUE)
    public R<List<Tree<Long>>> lazyTree(@Parameter(description = "父级编码", example = TOP_CODE,required = true) @RequestParam(defaultValue = TOP_CODE) Long parentId){
        return R.data(coreFunctionService.lazyTreeByRole(parentId, ShieldUtil.getUserRole()));
    }

    @Operation(summary = "查询登录用户所有功能的树形列表")
    @GetMapping(value = "/listTree", produces = APPLICATION_JSON_VALUE)
    public R<List<Tree<Long>>> listTree(){
        return R.data(coreFunctionService.listTreeByRoleId(ShieldUtil.getUserRole()));
    }

    @Function(value = "菜单树形",menus = {
		@Menu(client = "admin",menuCode = "SYSTEM_ROLE",btnCode = "SYSTEM_ROLE_SELECT_URL",code = "ROLE_MENU_TREE",type = Menu.TYPE.INTERFACE),
		@Menu(client = "admin",menuCode = "SYSTEM_FUNCTION",code = "SYSTEM_FUNCTION_MENU",type = Menu.TYPE.INTERFACE)
    })
    @Operation(summary = "查询登录用户所有菜单树形结构")
    @GetMapping(value = "/menuTree", produces = APPLICATION_JSON_VALUE)
    public R<List<Tree<Long>>> menuTree(@Parameter(description = "客户端ID") @RequestParam(required = false) Long clientId,
										@Parameter(description = "顶部菜单ID") @RequestParam(required = false) Long topMenuId){
        if (Fc.isNull(clientId)){
            return R.data(ListUtil.empty());
        }
        return R.data(coreFunctionService.menuTreeByRoleIds(ShieldUtil.getUserRole(), clientId, topMenuId));
    }

    @Function(value = "客户端功能树",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_TENANT",code = "CLIENT_MENU_TREE",type = Menu.TYPE.INTERFACE)
    })
    @Operation(summary = "查询登录用户所有菜单树形结构并根据客户端区分")
    @GetMapping(value = "/clientMenuTree", produces = APPLICATION_JSON_VALUE)
    public R<List<Tree<Long>>> clientMenuTree(){
        return R.data(coreFunctionService.treeClientMenu(ShieldUtil.getUserRole()));
    }

    @Function(value = "功能点同步",alias = "同步", menus = {
		@Menu(client = "admin",menuCode = "SYSTEM_FUNCTION", code = "SYSTEM_FUNCTION_GENERATE",type = Menu.TYPE.BTN)
    })
    @Operation(summary = "生成功能点")
    @PostMapping(value = "/generate", produces = APPLICATION_JSON_VALUE)
    public R<Boolean> generate(){
        return R.status(coreFunctionService.generateFunction());
    }

    @Function(value = "菜单开关",alias = "同步", menus = {
		@Menu(client = "admin",menuCode = "SYSTEM_FUNCTION", code = "SYSTEM_FUNCTION_HIDE",type = Menu.TYPE.BTN)
    })
    @Operation(summary = "菜单开关")
    @PostMapping(value = "/hide/{id}", produces = APPLICATION_JSON_VALUE)
    public R<Boolean> hide(@Parameter(description = "主键",required = true) @PathVariable("id") Long id,
						   @Parameter(description = "是否隐藏",required = true) @NotNull(message = "hide不可为空") @RequestSingleBody Boolean hide){
        return R.status(coreFunctionService.updateById(UpdateEntity.of(CoreFunction.class).setId(id).setIsHide(hide)));
    }
}
