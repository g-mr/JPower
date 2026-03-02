package top.jpower.system.controller.function;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.util.NumberUtil;
import io.swagger.annotations.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import top.jpower.common.constants.CacheNames;
import top.jpower.core.auth.annotation.Function;
import top.jpower.core.auth.annotation.Menu;
import top.jpower.core.auth.utils.ShieldUtil;
import top.jpower.core.boot.controller.BaseController;
import top.jpower.core.dbs.mp.support.Condition;
import top.jpower.core.dbs.support.ForestNodeMerger;
import top.jpower.core.exception.enums.JpowerError;
import top.jpower.core.exception.throwable.JpowerAssert;
import top.jpower.core.exception.throwable.JpowerException;
import top.jpower.core.redis.cache.CacheUtil;
import top.jpower.core.util.rsp.R;
import top.jpower.core.util.utils.BeanUtil;
import top.jpower.core.util.utils.Fc;
import top.jpower.core.util.utils.MapUtil;
import top.jpower.system.dbs.entity.client.TbCoreClient;
import top.jpower.system.dbs.entity.function.CoreFunction;
import top.jpower.system.service.client.CoreClientService;
import top.jpower.system.service.role.CoreFunctionService;
import top.jpower.system.vo.FunctionVo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static top.jpower.common.constants.ServiceCodeConstants.NOT_FOUND_CLIENT;
import static top.jpower.core.util.constants.JpowerConstants.TOP_CODE;

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
    private final CoreClientService clientService;

    @Function(value = "菜单按钮树形",menus = {
		@Menu(client = "admin",menuCode = "SYSTEM_FUNCTION",code = "SYSTEM_FUNCTION_MENUBTN",type = Menu.TYPE.INTERFACE)
    })
    @Operation(summary = "查询登录用户所有菜单按钮树形结构")
    @GetMapping(value = "/treeMenuTypeByClientId", produces = APPLICATION_JSON_VALUE)
    public R<List<Tree<String>>> treeMenuTypeByClientId(@Parameter(description = "客户端ID") Long clientId) {
        if (Fc.isNull(clientId)) {
            return R.data(ListUtil.empty());
        }
        return R.data(coreFunctionService.treeMenuTypeByClientId(ShieldUtil.getUserRole(), clientId));
    }


    @Function(value = "树形按钮",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_ROLE",btnCode = "SYSTEM_ROLE_SELECT_URL",code = "SYSTEM_ROLE_BUT_TREE",type = Menu.TYPE.INTERFACE)
    })
    @Operation(summary = "登录用户树形按钮接口", notes = "当不传菜单ID时，会查出顶级按钮接口；单独查一个菜单时，不会把顶级按钮接口返回")
    @GetMapping(value = "/treeButByMenu", produces = APPLICATION_JSON_VALUE)
    public R<List<Tree<String>>> treeButByMenu(@Parameter(description = "菜单Id",required = true) @RequestParam(required = false,defaultValue = TOP_CODE) Long id,
                                                            @Parameter(description = "客户端ID",required = true) @RequestParam(required = false) Long clientId){

        JpowerAssert.notNull(clientId,JpowerError.Arg,"客户端ID不可为空");

        return R.data(coreFunctionService.treeButByMenu(ShieldUtil.getUserRole(), Fc.toLong(id, Fc.toLong(TOP_CODE)), clientId));
    }

    @Function(value = "接口资源",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_ROLE", btnCode = "SYSTEM_ROLE_SELECT_URL",code = "ROLE_INTERFACE_LIST",type = Menu.TYPE.INTERFACE)
    })
    @Operation(summary = "接口资源")
    @GetMapping(value = "/listInterface", produces = APPLICATION_JSON_VALUE)
    public R<List<Map<String, Object>>> listInterface(@Parameter(description = "客户端ID",required = true) @RequestParam(required = false) Long clientId){
        JpowerAssert.notNull(clientId,JpowerError.Arg,"客户端ID不可为空");

        return R.data(coreFunctionService.listInterface(ShieldUtil.getUserRole(), clientId));
    }

    @Function(value = "菜单列表",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_FUNCTION",code = "CHILD_FUNCTION",type = Menu.TYPE.INTERFACE)
    })
    @Operation(summary = "根据父节点查询子节点功能")
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
    @RequestMapping(value = "/listByParent",method = {RequestMethod.GET,RequestMethod.POST},produces = APPLICATION_JSON_VALUE)
    public R<List<FunctionVo>> list(@ApiIgnore @RequestParam Map<String,Object> coreFunction){
        JpowerAssert.notNull(MapUtil.getLong(coreFunction,"clientId_eq"),JpowerError.Arg,"客户端ID不可为空");

        coreFunction.remove("clientId");
        coreFunction.remove("parentId");
        coreFunction.remove("functionType");
        coreFunction.remove("menuId");

        if(Fc.isNull(Fc.toLong(coreFunction.get("parentId_eq")))){
            coreFunction.put("parentId_eq", Fc.toLong(TOP_CODE));
        }

        List<FunctionVo> list = coreFunctionService.listFunction(coreFunction);
        return R.ok("获取成功", list);
    }

    @Function(value = "新增",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_FUNCTION",code = "SYSTEM_FUNCTION_ADD",type = Menu.TYPE.BTN)
    })
    @Operation(summary = "新增")
    @PostMapping(value = "/add", produces = APPLICATION_JSON_VALUE)
    public R<Long> add(CoreFunction coreFunction){
        JpowerAssert.notEmpty(coreFunction.getFunctionName(),JpowerError.Arg,"名称不可为空");
        JpowerAssert.notEmpty(coreFunction.getCode(),JpowerError.Arg,"编码不可为空");
        JpowerAssert.notEmpty(coreFunction.getUrl(),JpowerError.Arg,"URL不可为空");
        JpowerAssert.notNull(coreFunction.getClientId(),JpowerError.Arg,"客户端ID不可为空");
        JpowerAssert.notNull(coreFunction.getFunctionType(),JpowerError.Arg,"功能类型不可为空");

        if(Fc.isNull(coreFunction.getParentId())){
            coreFunction.setParentId(Fc.toLong(TOP_CODE));
        }
        if(Fc.isEmpty(coreFunction.getIsHide())){
            coreFunction.setIsHide(Boolean.FALSE);
        }

        CoreFunction function = coreFunctionService.selectFunctionByCode(coreFunction.getCode());
        if (function != null){
            return R.fail("该菜单已存在");
        }

        if (coreFunctionService.add(coreFunction)){
            CacheUtil.clear(CacheNames.FUNCTION_KEY);
            return R.ok("新增成功",coreFunction.getId());
        }else {
            return R.fail("新增失败");
        }
    }

    @Function(value = "删除",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_FUNCTION",code = "SYSTEM_FUNCTION_DELETE",type = Menu.TYPE.BTN)
    })
    @Operation(summary = "删除")
    @RequestMapping(value = "/delete",method = {RequestMethod.DELETE},produces = APPLICATION_JSON_VALUE)
    public R delete(@Parameter(description = "主键 多个逗号分割",required = true) @RequestParam String ids){

        JpowerAssert.notEmpty(ids, JpowerError.Arg, "ids不可为空");

        long c = coreFunctionService.listByPids(Fc.toLongList(ids));
        JpowerAssert.geZero(c,JpowerError.Business, "该菜单存在下级菜单，请先删除下级菜单");

        if (coreFunctionService.delete(Fc.toLongList(ids))){
            CacheUtil.clear(CacheNames.FUNCTION_KEY);
            return R.ok("删除成功");
        }else {
            return R.fail("删除失败");
        }
    }

    @Function(value = "修改",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_FUNCTION",code = "SYSTEM_FUNCTION_UPDATE",type = Menu.TYPE.BTN)
    })
    @Operation(summary = "修改")
    @RequestMapping(value = "/update",method = {RequestMethod.PUT},produces = APPLICATION_JSON_VALUE)
    public R update(CoreFunction coreFunction){

        JpowerAssert.notNull(coreFunction.getId(), JpowerError.Arg, "id不可为空");

        if (StringUtils.isNotBlank(coreFunction.getCode())){
            CoreFunction function = coreFunctionService.selectFunctionByCode(coreFunction.getCode());
            if (function != null && !NumberUtil.equals(function.getId(),function.getId())){
                return R.fail("该菜单已存在");
            }
        }

        if (coreFunctionService.update(coreFunction) ){
            CacheUtil.clear(CacheNames.FUNCTION_KEY);
            return R.ok("修改成功");
        }else {
            return R.fail("修改失败");
        }
    }

    @Function(value = "设置层级",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_FUNCTION",code = "SYSTEM_FUNCTION_HIERARCHY",type = Menu.TYPE.BTN)
    })
    @Operation(summary = "保存层级")
    @PostMapping(value = "/saveHierarchy", produces = APPLICATION_JSON_VALUE)
    public R saveHierarchy(@Parameter(description = "上级ID",required = true) @RequestParam(defaultValue = TOP_CODE) Long parentId, @Parameter(description = "主键，多个逗号分割",required = true) String ids){

        JpowerAssert.notEmpty(ids, JpowerError.Arg, "ids不可为空");

        boolean is = coreFunctionService.hierarchySave(parentId, Fc.toLongList(Fc.toStr(ids,TOP_CODE)));

        if (is){
            CacheUtil.clear(CacheNames.FUNCTION_KEY);
            return R.ok("设置成功");
        }else {
            return R.fail("设置失败");
        }
    }

    @Function(value = "功能权限",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_ROLE",code = "SYSTEM_ROLE_SELECT_URL",type = Menu.TYPE.BTN)
    })
    @Operation(summary = "根据角色ID查询所有的权限ID")
    @RequestMapping(value = "/queryUrlIdByRole",method = {RequestMethod.GET},produces = APPLICATION_JSON_VALUE)
    public R<Set<Long>> queryUrlIdByRole(@Parameter(description = "角色ID 多个逗号分割",required = true) @RequestParam String roleIds){
        Set<Long> list = coreFunctionService.queryUrlIdByRole(Fc.toLongList(roleIds));
        return R.data(list);
    }

    @Operation(summary = "懒加载登录用户所有功能树形结构")
    @RequestMapping(value = "/lazyTree",method = {RequestMethod.GET},produces = APPLICATION_JSON_VALUE)
    public R<List<Tree<Long>>> lazyTree(@Parameter(description = "父级编码",defaultValue = TOP_CODE,required = true) @RequestParam(defaultValue = TOP_CODE) Long parentId){
        List<Long> roleIds = ShieldUtil.getUserRole();
        List<Tree<Long>> list = ShieldUtil.isRoot()?coreFunctionService.tree(Condition.getLambdaTreeWrapper(CoreFunction.class,CoreFunction::getId,CoreFunction::getParentId)
                .lazy(parentId)
                .select(CoreFunction::getFunctionName,CoreFunction::getUrl)
                .orderByAsc(CoreFunction::getSort)):
                coreFunctionService.lazyTreeByRole(parentId,roleIds);
        return R.data(list);
    }

    @Operation(summary = "页面菜单获取")
    @GetMapping(value = "/listMenuTree", produces = APPLICATION_JSON_VALUE)
    public R<List<Tree<Long>>> listMenuTree(@Parameter(description = "顶部菜单ID") Long topMenuId){
        List<Long> roleIds = ShieldUtil.getUserRole();
        return R.data(ForestNodeMerger.mergeTree(BeanUtil.copyToList(coreFunctionService.listMenuByRoleId(roleIds,ShieldUtil.getClientCode(),topMenuId, Boolean.TRUE),FunctionVo.class)));
    }

    @Operation(summary = "查询登录用户所有按钮接口资源（用于页面权限）", notes = "用于页面权限判断，会把顶级按钮一起返回，顶级按钮代表所有菜单都可拥有权限")
    @GetMapping(value = "/listBut", produces = APPLICATION_JSON_VALUE)
    public R<List<String>> listBut(){
        List<String> list = coreFunctionService.listBtnByRoleId(ShieldUtil.getUserRole());
        return R.ok("查询成功", list);
    }

    @Operation(summary = "查询登录用户所有功能的树形列表")
    @GetMapping(value = "/listTree", produces = APPLICATION_JSON_VALUE)
    public R<List<Tree<Long>>> listTree(){
        List<Tree<Long>> list = ShieldUtil.isRoot()?
                coreFunctionService.tree(Condition.getLambdaTreeWrapper(CoreFunction.class,CoreFunction::getId,CoreFunction::getParentId)
                        .eq(CoreFunction::getClientId, clientService.queryIdByCode(ShieldUtil.getClientCode()).orElseThrow(() -> new JpowerException(JpowerError.NotFind.getCode(), NOT_FOUND_CLIENT)))):
                coreFunctionService.listTreeByRoleId(ShieldUtil.getUserRole());
        return R.data(list);
    }

    @Function(value = "菜单树形",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_ROLE",btnCode = "SYSTEM_ROLE_SELECT_URL",code = "ROLE_MENU_TREE",type = Menu.TYPE.INTERFACE),
            @Menu(client = "admin",menuCode = "SYSTEM_FUNCTION",code = "SYSTEM_FUNCTION_MENU",type = Menu.TYPE.INTERFACE)
    })
    @Operation(summary = "查询登录用户所有菜单树形结构")
    @GetMapping(value = "/menuTree", produces = APPLICATION_JSON_VALUE)
    public R<List<Tree<Long>>> menuTree(@Parameter(description = "客户端ID") Long clientId,@Parameter(description = "顶部菜单ID") Long topMenuId){
        if (Fc.isNull(clientId)){
            return R.data(ListUtil.empty());
        }
        return R.data(coreFunctionService.menuTreeByRoleIds(ShieldUtil.getUserRole(),clientId,topMenuId));
    }

    @Function(value = "客户端功能树",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_TENANT",code = "CLIENT_MENU_TREE",type = Menu.TYPE.INTERFACE)
    })
    @Operation(summary = "查询登录用户所有菜单树形结构并根据客户端区分")
    @GetMapping(value = "/clientMenuTree", produces = APPLICATION_JSON_VALUE)
    public R<List<Tree<Long>>> clientMenuTree(){

        List<CoreFunction> list = coreFunctionService.menuByRoleIds(ShieldUtil.getUserRole());

        List<TbCoreClient> clients = clientService.list(Condition.<TbCoreClient>getQueryWrapper().lambda().orderByAsc(TbCoreClient::getSortNum));

        List<Map<String,Object>> listMap = new ArrayList<>(clients.size()+list.size());

        listMap.addAll(clients.stream().map(client-> {

            Map<String,Object> map = MapUtil.newHashMap(3);
            map.put("disabled",Boolean.TRUE);

            list.forEach(f->{
                if (NumberUtil.equals(f.getParentId(),Fc.toLong(TOP_CODE)) && NumberUtil.equals(f.getClientId(),client.getId())){
                    f.setParentId(client.getId());
                    map.put("disabled",Boolean.FALSE);
                }
            });

            map.put("name",client.getName());
            map.put("id",client.getId());
            map.put("parentId",Fc.toLong(TOP_CODE));
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

        return R.data(ForestNodeMerger.mergeTree(listMap));
    }

    @Function(value = "功能点同步",alias = "同步", menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_FUNCTION", code = "SYSTEM_FUNCTION_GENERATE",type = Menu.TYPE.BTN)
    })
    @Operation(summary = "生成功能点")
    @PostMapping(value = "/generate", produces = APPLICATION_JSON_VALUE)
    public R generate(){
        boolean is =  coreFunctionService.generateFunction();
        if (is){
            CacheUtil.clear(CacheNames.FUNCTION_KEY);
        }
        return R.status(is);
    }

    @Function(value = "菜单开关",alias = "同步", menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_FUNCTION", code = "SYSTEM_FUNCTION_HIDE",type = Menu.TYPE.BTN)
    })
    @Operation(summary = "菜单开关")
    @PostMapping(value = "/hide", produces = APPLICATION_JSON_VALUE)
    public R hide(@Parameter(description = "主键",required = true) Long id,@Parameter(description = "是否隐藏",required = true) Boolean hide){
        JpowerAssert.notNull(id,JpowerError.Arg,"主键不可为空");
        JpowerAssert.notNull(hide,JpowerError.Arg,"是否隐藏不可为空");

        CoreFunction function = new CoreFunction();
        function.setId(id);
        function.setIsHide(hide);
        return R.status(coreFunctionService.updateById(function));
    }
}
