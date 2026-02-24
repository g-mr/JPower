package top.jpower.system.controller.role;

import cn.hutool.core.lang.tree.Tree;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import top.jpower.common.enums.YN01Enum;
import top.jpower.core.boot.controller.BaseController;
import top.jpower.core.exception.annotation.OperateLog;
import top.jpower.core.exception.enums.JpowerError;
import top.jpower.core.exception.throwable.JpowerAssert;
import top.jpower.core.util.constants.StringPool;
import top.jpower.core.util.rsp.ResponseData;
import top.jpower.core.util.rsp.ReturnJsonUtil;
import top.jpower.core.util.utils.Fc;
import top.jpower.system.dbs.entity.role.TbCoreRole;
import top.jpower.core.auth.annotation.Function;
import top.jpower.core.auth.annotation.Menu;
import top.jpower.common.constants.CacheNames;
import top.jpower.core.redis.cache.CacheUtil;
import top.jpower.core.auth.utils.ShieldUtil;
import top.jpower.core.dbs.mp.support.Condition;
import top.jpower.system.service.role.CoreRoleFunctionService;
import top.jpower.system.service.role.CoreRoleService;

import java.util.List;
import java.util.Map;

import static top.jpower.core.util.constants.JpowerConstants.TOP_CODE;

@Api(tags = "角色管理")
@RestController
@AllArgsConstructor
@RequestMapping("/core/role")
public class RoleController extends BaseController {

    private CoreRoleService coreRoleService;
    private CoreRoleFunctionService coreRoleFunctionService;

    @Function(value = "树形角色列表",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_ROLE",code = "SYSTEM_ROLE_LIST_TREE",type = Menu.TYPE.INTERFACE)
    })
    @ApiOperation("查询角色树结构列表")
    @RequestMapping(value = "/listTree",method = {RequestMethod.GET,RequestMethod.POST},produces="application/json")
    public ResponseData<List<Tree<Long>>> listTree(TbCoreRole coreRole){
        List<Tree<Long>> list = coreRoleService.tree(Condition.getLambdaTreeWrapper(coreRole,TbCoreRole::getId,TbCoreRole::getParentId)
                        .orderByAsc(TbCoreRole::getCreateTime));
        return ReturnJsonUtil.data(list);
    }

    @Function(value = "角色树形",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_USER",btnCode = "SYSTEM_USER_UPDATEROLE",code = "USER_ROLE_TREE",type = Menu.TYPE.INTERFACE)
    })
    @ApiOperation("查询角色树结构")
    @GetMapping(value = "/tree",produces="application/json")
    public ResponseData<List<Tree<Long>>> tree(TbCoreRole coreRole){
        List<Tree<Long>> list = coreRoleService.tree(Condition.getLambdaTreeWrapper(coreRole,TbCoreRole::getId,TbCoreRole::getParentId)
                .select(TbCoreRole::getAlias,TbCoreRole::getName)
                .func(q->{
                    if (!ShieldUtil.isRoot()){
                        List<Long> roleId = ShieldUtil.getUserRole();
                        q.apply("ancestor_id regexp '"+Fc.join(roleId, StringPool.SPILT)+"'")
                                .or()
                                .in(TbCoreRole::getId,roleId);
                    }
                })
                .orderByAsc(TbCoreRole::getCreateTime));
        return ReturnJsonUtil.data(list);
    }

    @Function(value = "新增",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_ROLE",code = "SYSTEM_ROLE_ADD",type = Menu.TYPE.BTN)
    })
    @ApiOperation("新增角色")
    @RequestMapping(value = "/add",method = {RequestMethod.POST},produces="application/json")
    public ResponseData add(TbCoreRole coreRole){
        JpowerAssert.notEmpty(coreRole.getName(),JpowerError.Arg,"名称不可为空");

        if (Fc.isNull(coreRole.getParentId())){
            coreRole.setParentId(Fc.toLong(TOP_CODE));
        }

        if (Fc.isNull(coreRole.getIsSysRole())){
            coreRole.setIsSysRole(YN01Enum.N.getValue());
        }

        String ancestorId = TOP_CODE;
        if (!Fc.equalsValue(coreRole.getParentId(),TOP_CODE)){
            ancestorId = coreRoleService.getObj(Condition.<TbCoreRole>getQueryWrapper().lambda().select(TbCoreRole::getAncestorId).eq(TbCoreRole::getId,coreRole.getParentId()),Fc::toStr);
            ancestorId = ancestorId + StringPool.COMMA + coreRole.getParentId();
        }
        coreRole.setAncestorId(ancestorId);

        CacheUtil.clear(CacheNames.ROLE_KEY);
        return ReturnJsonUtil.status(coreRoleService.add(coreRole),coreRole.getId());
    }

    @Function(value = "删除",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_ROLE",code = "SYSTEM_ROLE_DELETE",type = Menu.TYPE.BTN)
    })
    @ApiOperation("删除角色")
    @RequestMapping(value = "/deleteStatus",method = {RequestMethod.DELETE},produces="application/json")
    public ResponseData deleteStatus(@ApiParam(value = "主键 多个逗号分割",required = true) @RequestParam String ids){

        JpowerAssert.notEmpty(ids, JpowerError.Arg,"ids不可为空");

        long c = coreRoleService.listByPids(Fc.toLongList(ids));
        JpowerAssert.geZero(c, JpowerError.Business, "该角色存在下级角色，请先删除下级角色");

        CacheUtil.clear(CacheNames.ROLE_KEY);
        CacheUtil.clear(CacheNames.FUNCTION_KEY);
        CacheUtil.clear(CacheNames.DATASCOPE_KEY);
        CacheUtil.clear(CacheNames.USER_KEY);
        return ReturnJsonUtil.status(coreRoleService.remove(Condition.<TbCoreRole>getQueryWrapper().lambda()
                .in(TbCoreRole::getId,Fc.toLongList(ids))
                .eq(TbCoreRole::getIsSysRole, YN01Enum.N.getValue())));
    }

    @Function(value = "修改",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_ROLE",code = "SYSTEM_ROLE_UPDATE",type = Menu.TYPE.BTN)
    })
    @ApiOperation(value = "修改角色信息",notes = "主键不用传")
    @RequestMapping(value = "/update",method = {RequestMethod.PUT},produces="application/json")
    public ResponseData update(TbCoreRole coreRole){

        JpowerAssert.notNull(coreRole.getId(), JpowerError.Arg,"id不可为空");

        if (Fc.notNull(coreRole.getParentId())){
            String ancestorId = TOP_CODE;
            if (!Fc.equalsValue(coreRole.getParentId(),TOP_CODE)){
                ancestorId = coreRoleService.getObj(Condition.<TbCoreRole>getQueryWrapper().lambda().select(TbCoreRole::getAncestorId).eq(TbCoreRole::getId,coreRole.getParentId()),Fc::toStr);
                ancestorId = ancestorId + StringPool.COMMA + coreRole.getParentId();
            }
            coreRole.setAncestorId(ancestorId);
        }

        CacheUtil.clear(CacheNames.ROLE_KEY);
        return ReturnJsonUtil.status(coreRoleService.updateById(coreRole));
    }

    @ApiOperation("查询角色的权限")
    @GetMapping(value = "/roleFunction",produces="application/json")
    public ResponseData<List<Map<String,Object>>> roleFunction(@ApiParam(value = "角色主键",required = true) @RequestParam Long roleId){

        JpowerAssert.notNull(roleId, JpowerError.Arg,"角色id不可为空");

        List<Map<String,Object>> roleFunction = coreRoleFunctionService.selectRoleFunctionByRoleId(roleId);
        return ReturnJsonUtil.ok("查询成功", roleFunction);
    }

    @Function(value = "设置权限",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_ROLE",code = "SYSTEM_ROLE_UPDATEFUNCTION",type = Menu.TYPE.BTN)
    })
    @ApiOperation("重新给角色赋权")
    @OperateLog(title = "重新给角色赋权",isSaveLog = true)
    @PostMapping(value = "/addFunction",produces="application/json")
    public ResponseData addFunction(@ApiParam(value = "角色主键",required = true) @RequestParam Long roleId,
                                    @ApiParam(value = "功能主键 多个逗号分割") @RequestParam(required = false) String functionIds,
                                    @ApiParam(value = "顶级菜单主键 多个逗号分割") @RequestParam(required = false) String topMenuIds,
                                    @ApiParam(value = "是否自动保存接口权限", defaultValue = "false") @RequestParam(required = false, defaultValue = "true") Boolean isAutoSaveInterface){

        JpowerAssert.notNull(roleId, JpowerError.Arg,"角色id不可为空");
        JpowerAssert.notNull(coreRoleService.getById(roleId),JpowerError.Business,"该角色不存在");

        //保存功能权限和顶部菜单权限
        if (coreRoleFunctionService.addRoleFunctions(roleId, Fc.toLongList(functionIds), isAutoSaveInterface) && coreRoleService.saveTopMenu(roleId,Fc.toLongList(topMenuIds))){
            CacheUtil.clear(CacheNames.ROLE_KEY);
            CacheUtil.clear(CacheNames.FUNCTION_KEY);
            return ReturnJsonUtil.ok("设置成功");
        }else {
            return ReturnJsonUtil.fail("设置失败");
        }
    }

    @Function(value = "顶部菜单ID",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_ROLE",btnCode = "SYSTEM_ROLE_SELECT_URL",code = "ROLE_TOPMENU_ID",type = Menu.TYPE.INTERFACE)
    })
    @ApiOperation("角色关联的顶部菜单ID")
    @GetMapping(value = "/topMenuId",produces="application/json")
    public ResponseData<List<Long>> topMenuId(@ApiParam(value = "角色ID") Long roleId){
        JpowerAssert.notNull(roleId,JpowerError.Arg,"角色ID不可为空");

        return ReturnJsonUtil.data(coreRoleService.topMenuId(roleId));
    }

}
