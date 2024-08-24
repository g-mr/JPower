package top.jpower.jpower.controller.function;

import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import top.jpower.common.enums.DataScopeTypeEnum;
import top.jpower.common.enums.YN01Enum;
import top.jpower.core.exception.enums.JpowerError;
import top.jpower.core.exception.throwable.JpowerAssert;
import top.jpower.core.util.constants.StringPool;
import top.jpower.core.util.rsp.Pg;
import top.jpower.core.util.rsp.ResponseData;
import top.jpower.core.util.rsp.ReturnJsonUtil;
import top.jpower.core.util.utils.Fc;
import top.jpower.core.util.utils.MapUtil;
import top.jpower.jpower.dbs.entity.function.TbCoreDataScope;
import top.jpower.jpower.dbs.entity.role.TbCoreRoleData;
import top.jpower.jpower.module.annotation.Function;
import top.jpower.jpower.module.annotation.Menu;
import top.jpower.jpower.module.common.cache.CacheNames;
import top.jpower.jpower.module.common.utils.CacheUtil;
import top.jpower.jpower.module.mp.support.Condition;
import top.jpower.jpower.module.page.PaginationContext;
import top.jpower.jpower.service.role.CoreDataScopeService;
import top.jpower.jpower.service.role.CoreFunctionService;
import top.jpower.jpower.service.role.CoreRoleDataService;
import top.jpower.jpower.service.role.CoreRoleService;
import top.jpower.jpower.vo.DataFunctionVo;

import java.util.List;
import java.util.Map;

import static top.jpower.core.util.constants.JpowerConstants.TOP_CODE;

/**
 * @author ding
 * @description 数据权限入口
 * @date 2020-11-03 15:49
 */
@Api(tags = "数据权限管理")
@RestController
@RequestMapping("/core/dataScope")
@AllArgsConstructor
public class DataScopeController {

    private CoreDataScopeService dataScopeService;
    private CoreRoleService roleService;
    private CoreRoleDataService roleDataService;
    private CoreFunctionService coreFunctionService;

    @Function(value = "数据权限菜单列表",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_DATASCOPE",code = "SYSTEM_DATASCOPE_MENU",type = Menu.TYPE.INTERFACE)
    })
    @ApiOperation("数据权限菜单列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "clientId_eq",value = "客户端ID",paramType = "query",required = true),
            @ApiImplicitParam(name = "parentId_eq",value = "父级节点",defaultValue = TOP_CODE,required = true,paramType = "query"),
            @ApiImplicitParam(name = "alias",value = "别名",paramType = "query"),
            @ApiImplicitParam(name = "code",value = "编码",paramType = "query"),
            @ApiImplicitParam(name = "functionType_eq",value = "是否菜单 字典YN01",paramType = "query"),
            @ApiImplicitParam(name = "functionName",value = "功能名称",paramType = "query"),
            @ApiImplicitParam(name = "url",value = "功能URL",paramType = "query"),
            @ApiImplicitParam(name = "menuId_eq",value = "顶级菜单ID",paramType = "query")
    })
    @RequestMapping(value = "/listDataByParent",method = {RequestMethod.GET,RequestMethod.POST},produces="application/json")
    public ResponseData<List<DataFunctionVo>> listDataByParent(@ApiIgnore @RequestParam Map<String,Object> coreFunction){
        JpowerAssert.notNull(MapUtil.getLong(coreFunction,"clientId_eq"),JpowerError.Arg,"客户端ID不可为空");
        coreFunction.put("parentId_eq", Fc.toLong(coreFunction.get("parentId_eq"),Fc.toLong(TOP_CODE)));

        List<DataFunctionVo> list = coreFunctionService.listDataFunction(coreFunction);
        return ReturnJsonUtil.ok("获取成功", list);
    }

    @Function(value = "复制",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_DATASCOPE", btnCode = "SYSTEM_DATASCOPE_LISTPAGE",code = "SYSTEM_DATASCOPE_COPY",type = Menu.TYPE.BTN)
    })
    @ApiOperation(value = "复制")
    @PostMapping(value = "/copy",produces="application/json")
    public ResponseData copy(@ApiParam("主建") String id){

        JpowerAssert.notEmpty(id, JpowerError.Arg,"ID 不可为空");

        TbCoreDataScope dataScope = dataScopeService.getById(id);
        JpowerAssert.notNull(dataScope, JpowerError.NotFind, "数据权限");

        dataScope.setId(null);
        CacheUtil.clear(CacheNames.DATASCOPE_KEY);
        return ReturnJsonUtil.status(dataScopeService.save(dataScope));
    }

    @Function(value = "新增",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_DATASCOPE", btnCode = "SYSTEM_DATASCOPE_LISTPAGE",code = "SYSTEM_DATASCOPE_ADD",type = Menu.TYPE.BTN)
    })
    @ApiOperation(value = "新增",notes = "主键ID不可传")
    @PostMapping(value = "/add",produces="application/json")
    public ResponseData add(TbCoreDataScope dataScope){

        JpowerAssert.notNull(dataScope.getMenuId(), JpowerError.Arg,"菜单ID不可为空");
        JpowerAssert.notEmpty(dataScope.getScopeCode(), JpowerError.Arg,"编号不可为空");
        JpowerAssert.notEmpty(dataScope.getScopeName(), JpowerError.Arg,"名称不可为空");
        JpowerAssert.notEmpty(dataScope.getScopeClass(), JpowerError.Arg,"权限类名不可为空");
        if (Fc.isBlank(dataScope.getScopeColumn())){
            dataScope.setScopeColumn(StringPool.ASTERISK);
        }
        if(Fc.isEmpty(dataScope.getScopeType())){
            dataScope.setScopeType(DataScopeTypeEnum.ALL.getValue());
        }
        if(Fc.isEmpty(dataScope.getAllRole())){
            dataScope.setScopeType(YN01Enum.N.getValue());
        }
        if (Fc.equals(dataScope.getScopeType(),DataScopeTypeEnum.CUSTOM.getValue())){
            JpowerAssert.notEmpty(dataScope.getScopeValue(), JpowerError.Arg,"数据权限值域不可为空");
        }

        CacheUtil.clear(CacheNames.DATASCOPE_KEY);
        return ReturnJsonUtil.status(dataScopeService.save(dataScope));
    }

    @Function(value = "修改",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_DATASCOPE", btnCode = "SYSTEM_DATASCOPE_LISTPAGE",code = "SYSTEM_DATASCOPE_UPDATE",type = Menu.TYPE.BTN)
    })
    @ApiOperation("修改")
    @PutMapping(value = "/update",produces="application/json")
    public ResponseData update(TbCoreDataScope dataScope){
        JpowerAssert.notNull(dataScope.getId(), JpowerError.Arg,"主键不可为空");

        CacheUtil.clear(CacheNames.DATASCOPE_KEY);
        return ReturnJsonUtil.status(dataScopeService.updateById(dataScope));
    }

    @Function(value = "删除",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_DATASCOPE", btnCode = "SYSTEM_DATASCOPE_LISTPAGE",code = "SYSTEM_DATASCOPE_DELETE",type = Menu.TYPE.BTN)
    })
    @ApiOperation("删除")
    @DeleteMapping(value = "/delete",produces="application/json")
    public ResponseData delete(@ApiParam(value = "主键",required = true) @RequestParam Long id){
        JpowerAssert.notNull(id, JpowerError.Arg,"主键不可为空");
        CacheUtil.clear(CacheNames.DATASCOPE_KEY);
        return ReturnJsonUtil.status(dataScopeService.removeRealById(id));
    }

    @Function(value = "详情",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_DATASCOPE", btnCode = "SYSTEM_DATASCOPE_LISTPAGE",code = "SYSTEM_DATASCOPE_DETAIL",type = Menu.TYPE.BTN)
    })
    @ApiOperation("详情")
    @GetMapping(value = "/queryById",produces="application/json")
    public ResponseData<TbCoreDataScope> queryById(@ApiParam(value = "主键",required = true) @RequestParam Long id){
        JpowerAssert.notNull(id, JpowerError.Arg,"主键不可为空");
        return ReturnJsonUtil.ok("查询成功",dataScopeService.getById(id));
    }

    @Function(value = "列表",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_DATASCOPE",code = "SYSTEM_DATASCOPE_LISTPAGE",type = Menu.TYPE.BTN)
    })
    @ApiOperation("分页列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "menuId_eq",value = "菜单ID",required = true,paramType = "query"),
            @ApiImplicitParam(name = "scopeCode",value = "权限编号",paramType = "query"),
            @ApiImplicitParam(name = "scopeName",value = "权限名称",paramType = "query"),
            @ApiImplicitParam(name = "scopeType_eq",value = "权限类型 字典DATA_SCOPE_TYPE",dataType="integer",paramType = "query"),
            @ApiImplicitParam(name = "allRole_eq",value = "是否所有角色都执行",dataType="integer",paramType = "query")
    })
    @GetMapping(value = "/listPage",produces="application/json")
    public ResponseData<Pg<TbCoreDataScope>> listPage(@ApiIgnore @RequestParam Map<String,Object> map){
        JpowerAssert.notNull(Fc.toLong(map.get("menuId_eq")), JpowerError.Arg,"菜单ID不可为空");
        return ReturnJsonUtil.ok("查询成功",dataScopeService.page(PaginationContext.getMpPage(),Condition.getQueryWrapper(map,TbCoreDataScope.class)));
    }

    @Function(value = "数据权限",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_ROLE",code = "SYSTEM_DATASCOPE_LIST",type = Menu.TYPE.BTN)
    })
    @ApiOperation("通过菜单ID查询列表")
    @GetMapping(value = "/listByMenuId",produces="application/json")
    public ResponseData<List<TbCoreDataScope>> listByMenuId(@ApiParam(value = "菜单ID",required = true) @RequestParam Long menuId){
        JpowerAssert.notNull(menuId, JpowerError.Arg,"菜单ID不可为空");
        return ReturnJsonUtil.ok("查询成功",dataScopeService.list(Condition.<TbCoreDataScope>getQueryWrapper().lambda()
                .eq(TbCoreDataScope::getMenuId,menuId)));
    }

    @Function(value = "数据权限ID",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_ROLE",btnCode = "SYSTEM_DATASCOPE_LIST",code = "SYSTEM_DATASCOPE_LISTID",type = Menu.TYPE.INTERFACE)
    })
    @ApiOperation("通过角色ID查询所有数据权限ID")
    @GetMapping(value = "/listIdByRoleId",produces="application/json")
    public ResponseData<List<Long>> listIdByRoleId(@ApiParam(value = "角色ID 多个逗号分割",required = true) @RequestParam String roleIds){
        JpowerAssert.notEmpty(roleIds, JpowerError.Arg,"角色ID不可为空");
        return ReturnJsonUtil.ok("查询成功",roleDataService.listObjs(Condition.<TbCoreRoleData>getQueryWrapper().lambda()
                .select(TbCoreRoleData::getDataId)
                .in(TbCoreRoleData::getRoleId,Fc.toLongList(roleIds)),Fc::toLong));
    }

    @Function(value = "数据赋权",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_ROLE",btnCode = "SYSTEM_DATASCOPE_LIST",code = "SYSTEM_DATASCOPE_ROLE",type = Menu.TYPE.BTN)
    })
    @ApiOperation("角色赋权")
    @PostMapping(value = "/roleDataScope",produces="application/json")
    public ResponseData roleDataScope(@ApiParam(value = "角色主键",required = true) @RequestParam Long roleId,
                                      @ApiParam("数据权限主键,多个逗号分割") @RequestParam(required = false) String dataIds){
        JpowerAssert.notNull(roleId, JpowerError.Arg,"角色主键不可为空");
        JpowerAssert.notNull(roleService.getById(roleId), JpowerError.NotFind,"角色");

        CacheUtil.clear(CacheNames.DATASCOPE_KEY);
        CacheUtil.clear(CacheNames.ROLE_KEY);
        return ReturnJsonUtil.status(dataScopeService.roleDataScope(roleId,Fc.toLongList(dataIds)));
    }
}
