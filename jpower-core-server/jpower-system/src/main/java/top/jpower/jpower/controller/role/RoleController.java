package top.jpower.jpower.controller.role;

import cn.hutool.core.lang.tree.Tree;
import top.jpower.jpower.dbs.entity.role.TbCoreRole;
import top.jpower.jpower.module.annotation.Function;
import top.jpower.jpower.module.annotation.Menu;
import top.jpower.jpower.module.base.annotation.OperateLog;
import top.jpower.jpower.module.base.enums.JpowerError;
import top.jpower.jpower.module.base.exception.JpowerAssert;
import top.jpower.jpower.module.base.vo.ResponseData;
import top.jpower.jpower.module.common.cache.CacheNames;
import top.jpower.jpower.module.common.controller.BaseController;
import top.jpower.jpower.module.common.utils.CacheUtil;
import top.jpower.jpower.module.common.utils.Fc;
import top.jpower.jpower.module.common.utils.ReturnJsonUtil;
import top.jpower.jpower.module.common.utils.ShieldUtil;
import top.jpower.jpower.module.common.utils.constants.ConstantsEnum;
import top.jpower.jpower.module.common.utils.constants.StringPool;
import top.jpower.jpower.module.mp.support.Condition;
import top.jpower.jpower.service.role.CoreRoleService;
import top.jpower.jpower.service.role.CoreRolefunctionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static top.jpower.jpower.module.common.utils.constants.JpowerConstants.TOP_CODE;

@Api(tags = "角色管理")
@RestController
@AllArgsConstructor
@RequestMapping("/core/role")
public class RoleController extends BaseController {

    private CoreRoleService coreRoleService;
    private CoreRolefunctionService coreRolefunctionService;

    @Function(value = "树形角色列表",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_ROLE",code = "SYSTEM_ROLE_LIST_TREE",type = Menu.TYPE.INTERFACE)
    })
    @ApiOperation("查询角色树结构列表")
    @RequestMapping(value = "/listTree",method = {RequestMethod.GET,RequestMethod.POST},produces="application/json")
    public ResponseData<List<Tree<String>>> listTree(TbCoreRole coreRole){
        List<Tree<String>> list = coreRoleService.tree(Condition.getLambdaTreeWrapper(coreRole,TbCoreRole::getId,TbCoreRole::getParentId)
                        .orderByAsc(TbCoreRole::getCreateTime));
        return ReturnJsonUtil.ok("获取成功", list);
    }

    @Function(value = "角色树形",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_USER",code = "USER_ROLE_TREE",type = Menu.TYPE.INTERFACE)
    })
    @ApiOperation("查询角色树结构")
    @GetMapping(value = "/tree",produces="application/json")
    public ResponseData<List<Tree<String>>> tree(TbCoreRole coreRole){
        List<Tree<String>> list = coreRoleService.tree(Condition.getLambdaTreeWrapper(coreRole,TbCoreRole::getId,TbCoreRole::getParentId)
                .select(TbCoreRole::getAlias,TbCoreRole::getName)
                .func(q->{
                    if (!ShieldUtil.isRoot()){
                        List<String> roleId = ShieldUtil.getUserRole();
                        q.apply("ancestor_id regexp '"+Fc.join(roleId, StringPool.SPILT)+"'")
                                .or()
                                .in(TbCoreRole::getId,roleId);
                    }
                })
                .orderByAsc(TbCoreRole::getCreateTime));
        return ReturnJsonUtil.ok("获取成功", list);
    }

    @Function(value = "新增",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_ROLE",code = "SYSTEM_ROLE_ADD",type = Menu.TYPE.BTN)
    })
    @ApiOperation("新增角色")
    @RequestMapping(value = "/add",method = {RequestMethod.POST},produces="application/json")
    public ResponseData add(TbCoreRole coreRole){
        JpowerAssert.notEmpty(coreRole.getName(),JpowerError.Arg,"名称不可为空");

        if (StringUtils.isBlank(coreRole.getParentId())){
            coreRole.setParentId(TOP_CODE);
        }

        if (Fc.isNull(coreRole.getIsSysRole())){
            coreRole.setIsSysRole(ConstantsEnum.YN01.N.getValue());
        }

        String ancestorId = TOP_CODE;
        if (!Fc.equalsValue(coreRole.getParentId(),TOP_CODE)){
            ancestorId = coreRoleService.getObj(Condition.<TbCoreRole>getQueryWrapper().lambda().select(TbCoreRole::getAncestorId).eq(TbCoreRole::getId,coreRole.getParentId()),Fc::toStr);
            ancestorId = ancestorId + StringPool.COMMA + coreRole.getParentId();
        }
        coreRole.setAncestorId(ancestorId);

        CacheUtil.clear(CacheNames.ROLE_KEY,coreRole.getTenantCode());
        return ReturnJsonUtil.status(coreRoleService.add(coreRole),coreRole.getId());
    }

    @Function(value = "删除",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_ROLE",code = "SYSTEM_ROLE_DELETE",type = Menu.TYPE.BTN)
    })
    @ApiOperation("删除角色")
    @RequestMapping(value = "/deleteStatus",method = {RequestMethod.DELETE},produces="application/json")
    public ResponseData deleteStatus(@ApiParam(value = "主键 多个逗号分割",required = true) @RequestParam String ids){

        JpowerAssert.notEmpty(ids, JpowerError.Arg,"ids不可为空");

        List<String> tenants = coreRoleService.listObjs(Condition.<TbCoreRole>getQueryWrapper().lambda().select(TbCoreRole::getTenantCode).in(TbCoreRole::getId,Fc.toStrList(ids)),Fc::toStr);
        long c = coreRoleService.listByPids(ids);
        if (c > 0){
            return ReturnJsonUtil.busFail("该角色存在下级角色，请先删除下级角色");
        }

        CacheUtil.clear(CacheNames.ROLE_KEY,tenants.toArray(new String[tenants.size()]));
        CacheUtil.clear(CacheNames.FUNCTION_KEY);
        CacheUtil.clear(CacheNames.DATASCOPE_KEY);
        CacheUtil.clear(CacheNames.USER_KEY);
        return ReturnJsonUtil.status(coreRoleService.remove(Condition.<TbCoreRole>getQueryWrapper().lambda()
                .in(TbCoreRole::getId,Fc.toStrList(ids))
                .eq(TbCoreRole::getIsSysRole,ConstantsEnum.YN01.N.getValue())));
    }

    @Function(value = "修改",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_ROLE",code = "SYSTEM_ROLE_UPDATE",type = Menu.TYPE.BTN)
    })
    @ApiOperation(value = "修改角色信息",notes = "主键不用传")
    @RequestMapping(value = "/update",method = {RequestMethod.PUT},produces="application/json")
    public ResponseData update(TbCoreRole coreRole){

        JpowerAssert.notEmpty(coreRole.getId(), JpowerError.Arg,"id不可为空");

        if (Fc.isNotBlank(coreRole.getParentId())){
            String ancestorId = TOP_CODE;
            if (!Fc.equalsValue(coreRole.getParentId(),TOP_CODE)){
                ancestorId = coreRoleService.getObj(Condition.<TbCoreRole>getQueryWrapper().lambda().select(TbCoreRole::getAncestorId).eq(TbCoreRole::getId,coreRole.getParentId()),Fc::toStr);
                ancestorId = ancestorId + StringPool.COMMA + coreRole.getParentId();
            }
            coreRole.setAncestorId(ancestorId);
        }

        CacheUtil.clear(CacheNames.ROLE_KEY,coreRole.getTenantCode());
        return ReturnJsonUtil.status(coreRoleService.update(coreRole));
    }

    @ApiOperation("查询角色的权限")
    @GetMapping(value = "/roleFunction",produces="application/json")
    public ResponseData<List<Map<String,Object>>> roleFunction(@ApiParam(value = "角色主键",required = true) @RequestParam String roleId){

        JpowerAssert.notEmpty(roleId, JpowerError.Arg,"角色id不可为空");

        List<Map<String,Object>> roleFunction = coreRolefunctionService.selectRoleFunctionByRoleId(roleId);
        return ReturnJsonUtil.ok("查询成功", roleFunction);
    }

    @Function(value = "设置权限",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_ROLE",code = "SYSTEM_ROLE_UPDATEFUNCTION",type = Menu.TYPE.BTN)
    })
    @ApiOperation("重新给角色赋权")
    @OperateLog(title = "重新给角色赋权",isSaveLog = true)
    @RequestMapping(value = "/addFunction",method = {RequestMethod.POST},produces="application/json")
    public ResponseData addFunction(@ApiParam(value = "角色主键",required = true) @RequestParam String roleId,
                                    @ApiParam(value = "功能主键 多个逗号分割") @RequestParam(required = false) String functionIds,
                                    @ApiParam(value = "顶级菜单主键 多个逗号分割") @RequestParam(required = false) String topMenuIds){

        JpowerAssert.notEmpty(roleId, JpowerError.Arg,"角色id不可为空");
        JpowerAssert.notNull(coreRoleService.getById(roleId),JpowerError.Business,"该角色不存在");

        //保存功能权限和顶部菜单权限
        if (coreRolefunctionService.addRolefunctions(roleId,functionIds) && coreRoleService.saveTopMenu(roleId,Fc.toStrList(topMenuIds))){
            TbCoreRole role = coreRoleService.getById(roleId);
            CacheUtil.clear(CacheNames.ROLE_KEY,role.getTenantCode());
            CacheUtil.clear(CacheNames.FUNCTION_KEY,role.getTenantCode());
            return ReturnJsonUtil.ok("设置成功");
        }else {
            return ReturnJsonUtil.fail("设置失败");
        }
    }

    @Function(value = "顶部菜单ID",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_ROLE",code = "ROLE_TOPMENU_ID",type = Menu.TYPE.INTERFACE)
    })
    @ApiOperation("角色关联的顶部菜单ID")
    @GetMapping(value = "/topMenuId",produces="application/json")
    public ResponseData<List<String>> topMenuId(@ApiParam(value = "角色ID") String roleId){
        JpowerAssert.notEmpty(roleId,JpowerError.Arg,"角色ID不可为空");

        return ReturnJsonUtil.data(coreRoleService.topMenuId(roleId));
    }
}
