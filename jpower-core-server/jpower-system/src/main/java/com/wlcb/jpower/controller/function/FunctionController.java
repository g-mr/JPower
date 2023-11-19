package com.wlcb.jpower.controller.function;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.tree.Tree;
import com.wlcb.jpower.dbs.entity.client.TbCoreClient;
import com.wlcb.jpower.dbs.entity.function.TbCoreFunction;
import com.wlcb.jpower.module.annotation.Function;
import com.wlcb.jpower.module.annotation.Menu;
import com.wlcb.jpower.module.base.enums.JpowerError;
import com.wlcb.jpower.module.base.exception.JpowerAssert;
import com.wlcb.jpower.module.base.vo.ResponseData;
import com.wlcb.jpower.module.common.cache.CacheNames;
import com.wlcb.jpower.module.common.controller.BaseController;
import com.wlcb.jpower.module.common.node.ForestNodeMerger;
import com.wlcb.jpower.module.common.utils.*;
import com.wlcb.jpower.module.common.utils.constants.ConstantsReturn;
import com.wlcb.jpower.module.mp.support.Condition;
import com.wlcb.jpower.service.client.CoreClientService;
import com.wlcb.jpower.service.role.CoreFunctionService;
import com.wlcb.jpower.vo.FunctionVo;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.wlcb.jpower.module.common.utils.constants.JpowerConstants.TOP_CODE;

/**
 * @author mr.gmac
 */
@Api(tags = "菜单管理")
@RestController
@RequestMapping("/core/function")
@AllArgsConstructor
public class FunctionController extends BaseController {

    private CoreFunctionService coreFunctionService;
    private CoreClientService clientService;

    @Function(value = "菜单列表",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_FUNCTION",code = "CHILD_FUNCTION",type = Menu.TYPE.INTERFACE)
    })
    @ApiOperation("根据父节点查询子节点功能")
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
    @RequestMapping(value = "/listByParent",method = {RequestMethod.GET,RequestMethod.POST},produces="application/json")
    public ResponseData<List<FunctionVo>> list(@ApiIgnore @RequestParam Map<String,Object> coreFunction){
        JpowerAssert.notEmpty(MapUtil.getStr(coreFunction,"clientId_eq"),JpowerError.Arg,"客户端ID不可为空");

        coreFunction.remove("clientId");
        coreFunction.remove("parentId");
        coreFunction.remove("functionType");
        coreFunction.remove("menuId");

        if(StringUtils.isBlank(Fc.toStr(coreFunction.get("parentId_eq")))){
            coreFunction.put("parentId_eq", TOP_CODE);
        }

        List<FunctionVo> list = coreFunctionService.listFunction(coreFunction);
        return ReturnJsonUtil.ok("获取成功", list);
    }

    @Function(value = "新增",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_FUNCTION",code = "SYSTEM_FUNCTION_ADD",type = Menu.TYPE.BTN)
    })
    @ApiOperation("新增")
    @PostMapping(value = "/add", produces="application/json")
    public ResponseData add(TbCoreFunction coreFunction){
        JpowerAssert.notEmpty(coreFunction.getFunctionName(),JpowerError.Arg,"名称不可为空");
        JpowerAssert.notEmpty(coreFunction.getCode(),JpowerError.Arg,"编码不可为空");
        JpowerAssert.notEmpty(coreFunction.getUrl(),JpowerError.Arg,"URL不可为空");
        JpowerAssert.notEmpty(coreFunction.getClientId(),JpowerError.Arg,"客户端ID不可为空");
        JpowerAssert.notNull(coreFunction.getFunctionType(),JpowerError.Arg,"功能类型不可为空");

        if(StringUtils.isBlank(coreFunction.getParentId())){
            coreFunction.setParentId("-1");
        }
        if(Fc.isEmpty(coreFunction.getIsHide())){
            coreFunction.setIsHide(Boolean.FALSE);
        }

        TbCoreFunction function = coreFunctionService.selectFunctionByCode(coreFunction.getCode());
        if (function != null){
            return ReturnJsonUtil.print(ConstantsReturn.RECODE_BUSINESS,"该菜单已存在", false);
        }

        Boolean is = coreFunctionService.add(coreFunction);

        if (is){
            CacheUtil.clear(CacheNames.FUNCTION_KEY);
            return ReturnJsonUtil.ok("新增成功",coreFunction.getId());
        }else {
            return ReturnJsonUtil.fail("新增失败");
        }
    }

    @Function(value = "删除",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_FUNCTION",code = "SYSTEM_FUNCTION_DELETE",type = Menu.TYPE.BTN)
    })
    @ApiOperation("删除")
    @RequestMapping(value = "/delete",method = {RequestMethod.DELETE},produces="application/json")
    public ResponseData delete(@ApiParam(value = "主键 多个逗号分割",required = true) @RequestParam String ids){

        JpowerAssert.notEmpty(ids, JpowerError.Arg, "ids不可为空");

        long c = coreFunctionService.listByPids(ids);
        if (c > 0){
            return ReturnJsonUtil.print(ConstantsReturn.RECODE_BUSINESS,"该菜单存在下级菜单，请先删除下级菜单", false);
        }

        Boolean is = coreFunctionService.delete(ids);

        if (is){
            CacheUtil.clear(CacheNames.FUNCTION_KEY);
            return ReturnJsonUtil.ok("删除成功");
        }else {
            return ReturnJsonUtil.fail("删除失败");
        }
    }

    @Function(value = "修改",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_FUNCTION",code = "SYSTEM_FUNCTION_UPDATE",type = Menu.TYPE.BTN)
    })
    @ApiOperation("修改")
    @RequestMapping(value = "/update",method = {RequestMethod.PUT},produces="application/json")
    public ResponseData update(TbCoreFunction coreFunction){

        JpowerAssert.notEmpty(coreFunction.getId(), JpowerError.Arg, "id不可为空");

        if (StringUtils.isNotBlank(coreFunction.getCode())){
            TbCoreFunction function = coreFunctionService.selectFunctionByCode(coreFunction.getCode());
            if (function != null && !StringUtils.equals(function.getId(),function.getId())){
                return ReturnJsonUtil.print(ConstantsReturn.RECODE_BUSINESS,"该菜单已存在", false);
            }
        }

        Boolean is = coreFunctionService.update(coreFunction);

        if (is){
            CacheUtil.clear(CacheNames.FUNCTION_KEY);
            return ReturnJsonUtil.ok("修改成功");
        }else {
            return ReturnJsonUtil.fail("修改失败");
        }
    }

    @Function(value = "设置层级",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_FUNCTION",code = "SYSTEM_FUNCTION_HIERARCHY",type = Menu.TYPE.BTN)
    })
    @ApiOperation("保存层级")
    @PostMapping(value = "/saveHierarchy", produces="application/json")
    public ResponseData saveHierarchy(@ApiParam(value = "上级ID",required = true) String parentId, @ApiParam(value = "主键，多个逗号分割",required = true) String ids){

        JpowerAssert.notEmpty(ids, JpowerError.Arg, "ids不可为空");

        boolean is = coreFunctionService.saveHierarchy(parentId, Fc.toStrList(Fc.toStr(ids,TOP_CODE)));

        if (is){
            CacheUtil.clear(CacheNames.FUNCTION_KEY);
            return ReturnJsonUtil.ok("设置成功");
        }else {
            return ReturnJsonUtil.fail("设置失败");
        }
    }

    @Function(value = "角色权限",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_ROLE",code = "SYSTEM_ROLE_SELECT_URL",type = Menu.TYPE.BTN)
    })
    @ApiOperation("根据角色ID查询所有的权限ID")
    @RequestMapping(value = "/queryUrlIdByRole",method = {RequestMethod.GET},produces="application/json")
    public ResponseData<List<String>> queryUrlIdByRole(@ApiParam(value = "角色ID 多个逗号分割",required = true) @RequestParam String roleIds){
        List<String> list = coreFunctionService.queryUrlIdByRole(roleIds);
        return ReturnJsonUtil.ok("查询成功",list);
    }

    @ApiOperation("懒加载登录用户所有功能树形结构")
    @RequestMapping(value = "/lazyTree",method = {RequestMethod.GET},produces="application/json")
    public ResponseData<List<Tree<String>>> lazyTree(@ApiParam(value = "父级编码",defaultValue = TOP_CODE,required = true) @RequestParam(defaultValue = TOP_CODE) String parentId){
        List<String> roleIds = ShieldUtil.getUserRole();
        List<Tree<String>> list = ShieldUtil.isRoot()?coreFunctionService.tree(Condition.getLambdaTreeWrapper(TbCoreFunction.class,TbCoreFunction::getId,TbCoreFunction::getParentId)
                .lazy(parentId)
                .select(TbCoreFunction::getFunctionName,TbCoreFunction::getUrl)
                .orderByAsc(TbCoreFunction::getSort)):
                coreFunctionService.lazyTreeByRole(parentId,roleIds);
        return ReturnJsonUtil.ok("查询成功",list);
    }

    @ApiOperation("页面菜单获取")
    @GetMapping(value = "/listMenuTree", produces="application/json")
    public ResponseData<List<Tree<String>>> listMenuTree(@ApiParam("顶部菜单ID") Long topMenuId){
        List<Long> roleIds = ShieldUtil.getUserRole();
        return ReturnJsonUtil.data(ForestNodeMerger.mergeTree(BeanUtil.copyToList(coreFunctionService.listMenuByRoleId(roleIds,ShieldUtil.getClientCode(),topMenuId, Boolean.TRUE),FunctionVo.class)));
    }

    @ApiOperation(value = "查询登录用户所有按钮接口资源（用于页面权限）", notes = "用于页面权限判断，会把顶级按钮一起返回，顶级按钮代表所有菜单都可拥有权限")
    @GetMapping(value = "/listBut", produces="application/json")
    public ResponseData<List<String>> listBut(@ApiParam("顶部菜单ID") String topMenuId){
        List<String> list = coreFunctionService.listBtnByRoleId(ShieldUtil.getUserRole(),topMenuId);
        return ReturnJsonUtil.ok("查询成功", list);
    }

    @ApiOperation("查询登录用户所有功能的树形列表")
    @GetMapping(value = "/listTree", produces="application/json")
    public ResponseData<List<Tree<String>>> listTree(){
        List<Tree<String>> list = ShieldUtil.isRoot()?
                coreFunctionService.tree(Condition.getLambdaTreeWrapper(TbCoreFunction.class,TbCoreFunction::getId,TbCoreFunction::getParentId)
                        .eq(TbCoreFunction::getClientId,clientService.queryIdByCode(ShieldUtil.getClientCode()))):
                coreFunctionService.listTreeByRoleId(ShieldUtil.getUserRole());
        return ReturnJsonUtil.ok("查询成功", list);
    }

    @Function(value = "菜单树形",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_ROLE",code = "ROLE_MENU_TREE",type = Menu.TYPE.INTERFACE),
            @Menu(client = "admin",menuCode = "SYSTEM_FUNCTION",code = "SYSTEM_FUNCTION_MENU",type = Menu.TYPE.INTERFACE)
    })
    @ApiOperation("查询登录用户所有菜单树形结构")
    @GetMapping(value = "/menuTree", produces="application/json")
    public ResponseData<List<Tree<String>>> menuTree(@ApiParam("客户端ID") String clientId,@ApiParam("顶部菜单ID") String topMenuId){
        if (Fc.isBlank(clientId)){
            return ReturnJsonUtil.data(ListUtil.empty());
        }
        return ReturnJsonUtil.data(coreFunctionService.menuTreeByRoleIds(ShieldUtil.getUserRole(),clientId,topMenuId));
    }

    @Function(value = "客户端功能树",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_TENANT",code = "CLIENT_MENU_TREE",type = Menu.TYPE.INTERFACE)
    })
    @ApiOperation("查询登录用户所有菜单树形结构并根据客户端区分")
    @GetMapping(value = "/clientMenuTree", produces="application/json")
    public ResponseData<List<Tree<String>>> clientMenuTree(){

        List<TbCoreFunction> list = coreFunctionService.menuByRoleIds(ShieldUtil.getUserRole());

        List<TbCoreClient> clients = clientService.list(Condition.<TbCoreClient>getQueryWrapper().lambda().orderByAsc(TbCoreClient::getSortNum));

        List<Map<String,Object>> listMap = new ArrayList<>(clients.size()+list.size());

        listMap.addAll(clients.stream().map(client-> {

            Map<String,Object> map = MapUtil.newHashMap(3);
            map.put("disabled",Boolean.TRUE);

            list.forEach(f->{
                if (Fc.equalsValue(f.getParentId(),TOP_CODE) && Fc.equalsValue(f.getClientId(),client.getId())){
                    f.setParentId(client.getId());
                    map.put("disabled",Boolean.FALSE);
                }
            });

            map.put("name",client.getName());
            map.put("id",client.getId());
            map.put("parentId",TOP_CODE);
            return map;
        }).collect(Collectors.toList()));

        listMap.addAll(list.stream().map(function->{
            Map<String,Object> map = MapUtil.newHashMap(4);
            map.put("name",function.getFunctionName());
            map.put("id",function.getId());
            map.put("parentId",function.getParentId());
            map.put("code",function.getCode());
            return map;
        }).collect(Collectors.toList()));

        return ReturnJsonUtil.data(ForestNodeMerger.mergeTree(listMap));
    }

    @Function(value = "菜单资源",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_ROLE",code = "SYSTEM_ROLE_BUT",type = Menu.TYPE.INTERFACE)
    })
    @ApiOperation(value = "查询登录用户一个菜单下的所有按钮接口资源", notes = "当不传菜单ID时，会查出顶级资源；单独查一个菜单时，不会把顶级按钮返回")
    @GetMapping(value = "/listButByMenu", produces="application/json")
    public ResponseData<List<TbCoreFunction>> listButByMenu(@ApiParam(value = "菜单Id",required = true) @RequestParam(required = false,defaultValue = TOP_CODE) String id,
                                                            @ApiParam(value = "客户端ID",required = true) @RequestParam(required = false) String clientId){

        JpowerAssert.notEmpty(clientId,JpowerError.Arg,"客户端ID不可为空");

        if (Fc.isBlank(id)){
            id = TOP_CODE;
        }

        return ReturnJsonUtil.data(coreFunctionService.listButByMenu(ShieldUtil.getUserRole(), id, clientId));
    }

    @Function(value = "功能点同步",alias = "同步", menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_FUNCTION", code = "SYSTEM_FUNCTION_GENERATE",type = Menu.TYPE.BTN)
    })
    @ApiOperation("生成功能点")
    @PostMapping(value = "/generate", produces="application/json")
    public ResponseData generate(){
        return ReturnJsonUtil.status(coreFunctionService.generateFunction());
    }

    @Function(value = "菜单开关",alias = "同步", menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_FUNCTION", code = "SYSTEM_FUNCTION_HIDE",type = Menu.TYPE.BTN)
    })
    @ApiOperation("菜单开关")
    @PostMapping(value = "/hide", produces="application/json")
    public ResponseData hide(@ApiParam(value = "主键",required = true) String id,@ApiParam(value = "是否隐藏",required = true) Boolean hide){
        JpowerAssert.notEmpty(id,JpowerError.Arg,"主键不可为空");
        JpowerAssert.notNull(hide,JpowerError.Arg,"是否隐藏不可为空");

        TbCoreFunction function = new TbCoreFunction();
        function.setId(id);
        function.setIsHide(hide);
        return ReturnJsonUtil.status(coreFunctionService.updateById(function));
    }
}
