package top.jpower.system.controller.role;

import cn.hutool.core.lang.tree.Tree;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.jpower.common.validated.group.Validation;
import top.jpower.core.auth.annotation.Function;
import top.jpower.core.auth.annotation.Menu;
import top.jpower.core.boot.controller.BaseController;
import top.jpower.core.exception.annotation.OperateLog;
import top.jpower.core.util.rsp.R;
import top.jpower.core.util.utils.Fc;
import top.jpower.system.dbs.entity.role.CoreRole;
import top.jpower.system.service.role.CoreRoleFunctionService;
import top.jpower.system.service.role.CoreRoleService;
import top.jpower.system.vo.RoleFunctionSaveVO;
import top.jpower.system.vo.RoleFunctionVO;

import java.util.List;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * 角色管理
 *
 * @author mr.g
 */
@Tag(name = "角色管理")
@Validated
@RestController
@RequestMapping("/core/role")
@RequiredArgsConstructor
public class RoleController extends BaseController {

    private final CoreRoleService coreRoleService;
    private final CoreRoleFunctionService coreRoleFunctionService;

	@Function(value = "角色树形",menus = {
			@Menu(client = "admin",menuCode = "SYSTEM_USER",btnCode = "SYSTEM_USER_UPDATEROLE",code = "USER_ROLE_TREE",type = Menu.TYPE.INTERFACE)
	})
	@Operation(summary = "查询角色树结构")
	@GetMapping(value = "/tree",produces = APPLICATION_JSON_VALUE)
	public R<List<Tree<Long>>> tree(){
		return R.data(coreRoleService.treeSelect());
	}

    @Function(value = "树形角色列表",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_ROLE",code = "SYSTEM_ROLE_LIST_TREE",type = Menu.TYPE.INTERFACE)
    })
    @Operation(summary = "查询角色树结构列表")
    @GetMapping(value = "/listTree", produces = APPLICATION_JSON_VALUE)
    public R<List<Tree<Long>>> listTree(@RequestParam(required = false) Map<String, Object> params){
        return R.data(coreRoleService.listTree(params));
    }

    @Function(value = "新增",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_ROLE",code = "SYSTEM_ROLE_ADD",type = Menu.TYPE.BTN)
    })
    @Operation(summary = "新增角色")
    @PostMapping(value = "/add", produces = APPLICATION_JSON_VALUE)
    public R<Long> add(@Validated(Validation.Create.class) @RequestBody CoreRole coreRole){
        return R.status(coreRoleService.add(coreRole), coreRole.getId());
    }

    @Function(value = "修改",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_ROLE",code = "SYSTEM_ROLE_UPDATE",type = Menu.TYPE.BTN)
    })
    @Operation(summary = "修改角色信息")
    @PutMapping(value = "/update", produces = APPLICATION_JSON_VALUE)
    public R<Long> update(@Validated(Validation.Update.class) @RequestBody CoreRole coreRole){
        return R.status(coreRoleService.updateById(coreRole));
    }

    @Function(value = "删除",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_ROLE",code = "SYSTEM_ROLE_DELETE",type = Menu.TYPE.BTN)
    })
    @Operation(summary = "删除角色")
    @DeleteMapping(value = "/deleteStatus", produces = APPLICATION_JSON_VALUE)
    public R<Boolean> deleteStatus(@Parameter(description = "主键 多个逗号分割",required = true) @NotBlank(message = "ids不可为空") @RequestParam String ids){
        return R.status(coreRoleService.removeByIds(Fc.toLongList(ids)));
    }

    @Function(value = "顶部菜单ID",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_ROLE",btnCode = "SYSTEM_ROLE_SELECT_URL",code = "ROLE_TOPMENU_ID",type = Menu.TYPE.INTERFACE)
    })
    @Operation(summary = "角色关联的顶部菜单ID")
    @GetMapping(value = "/topMenuId/{roleId}",produces= APPLICATION_JSON_VALUE)
    public R<List<Long>> topMenuId(@Parameter(description = "角色ID", required = true) @PathVariable("roleId") Long roleId){
        return R.data(coreRoleService.queryMenuIdByRoleId(roleId));
    }

    @Function(value = "设置权限",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_ROLE",code = "SYSTEM_ROLE_UPDATEFUNCTION",type = Menu.TYPE.BTN)
    })
    @Operation(summary = "重新给角色赋权")
    @OperateLog(title = "重新给角色赋权")
    @PostMapping(value = "/addFunction",produces= APPLICATION_JSON_VALUE)
    public R<Boolean> addFunction(@Valid @RequestBody RoleFunctionSaveVO roleFunctionSaveVO){
        return R.status(coreRoleFunctionService.addRoleFunctions(roleFunctionSaveVO));
    }
































    @Operation(summary = "查询角色的权限")
    @GetMapping(value = "/roleFunction/{roleId}", produces = APPLICATION_JSON_VALUE)
    public R<List<RoleFunctionVO>> roleFunction(@Parameter(description = "角色主键",required = true) @PathVariable("roleId") Long roleId){
        return R.data(coreRoleFunctionService.selectRoleFunctionByRoleId(roleId));
    }

}
