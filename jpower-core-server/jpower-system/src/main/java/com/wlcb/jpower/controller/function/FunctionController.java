package com.wlcb.jpower.controller.function;

import cn.hutool.core.lang.tree.Tree;
import com.wlcb.jpower.dbs.entity.function.TbCoreFunction;
import com.wlcb.jpower.module.base.enums.JpowerError;
import com.wlcb.jpower.module.base.exception.JpowerAssert;
import com.wlcb.jpower.module.base.vo.ResponseData;
import com.wlcb.jpower.module.common.cache.CacheNames;
import com.wlcb.jpower.module.common.controller.BaseController;
import com.wlcb.jpower.module.common.node.ForestNodeMerger;
import com.wlcb.jpower.module.common.utils.*;
import com.wlcb.jpower.module.common.utils.constants.ConstantsEnum;
import com.wlcb.jpower.module.common.utils.constants.ConstantsReturn;
import com.wlcb.jpower.module.common.utils.constants.JpowerConstants;
import com.wlcb.jpower.module.mp.support.Condition;
import com.wlcb.jpower.service.role.CoreFunctionService;
import com.wlcb.jpower.vo.FunctionVo;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.Map;

/**
 * @author mr.gmac
 */
@Api(tags = "菜单管理")
@RestController
@RequestMapping("/core/function")
@AllArgsConstructor
public class FunctionController extends BaseController {

    private CoreFunctionService coreFunctionService;

    @ApiOperation("根据父节点查询子节点功能")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "parentId_eq",value = "父级节点",defaultValue = JpowerConstants.TOP_CODE,required = true,paramType = "query"),
            @ApiImplicitParam(name = "alias",value = "别名",paramType = "query"),
            @ApiImplicitParam(name = "code",value = "编码",paramType = "query"),
            @ApiImplicitParam(name = "isMenu_eq",value = "是否菜单 字典YN01",paramType = "query"),
            @ApiImplicitParam(name = "functionName",value = "功能名称",paramType = "query"),
            @ApiImplicitParam(name = "url",value = "功能URL",paramType = "query")
    })
    @RequestMapping(value = "/listByParent",method = {RequestMethod.GET,RequestMethod.POST},produces="application/json")
    public ResponseData<List<?>> list(@ApiIgnore @RequestParam Map<String,Object> coreFunction){
        coreFunction.remove("parentId");

        int size = coreFunction.size();

        if (coreFunction.containsKey("parentId_eq")){
            size--;
        }
        if (coreFunction.containsKey("isMenu_eq")){
            size--;
        }


        if(StringUtils.isBlank(Fc.toStr(coreFunction.get("parentId_eq"))) && size <= 0 ){
            coreFunction.put("parentId_eq",JpowerConstants.TOP_CODE);
        }

        List<FunctionVo> list = coreFunctionService.listFunction(coreFunction);
        if (coreFunction.containsKey("parentId_eq")){
            return ReturnJsonUtil.ok("获取成功", list);
        }else {
            return ReturnJsonUtil.ok("获取成功", ForestNodeMerger.mergeTree(list));
        }
    }

    @ApiOperation("新增")
    @PostMapping(value = "/add", produces="application/json")
    public ResponseData add(TbCoreFunction coreFunction){
        JpowerAssert.notEmpty(coreFunction.getFunctionName(),JpowerError.Arg,"名称不可为空");
        JpowerAssert.notEmpty(coreFunction.getCode(),JpowerError.Arg,"编码不可为空");
        JpowerAssert.notEmpty(coreFunction.getUrl(),JpowerError.Arg,"URL不可为空");
        JpowerAssert.notNull(coreFunction.getIsMenu(),JpowerError.Arg,"是否菜单不可为空");

        if(StringUtils.isBlank(coreFunction.getParentId())){
            coreFunction.setParentId("-1");
        }

        TbCoreFunction function = coreFunctionService.selectFunctionByCode(coreFunction.getCode());
        if (function != null){
            return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_BUSINESS,"该菜单已存在", false);
        }

        Boolean is = coreFunctionService.add(coreFunction);

        if (is){
            CacheUtil.clear(CacheNames.SYSTEM_REDIS_CACHE);
            return ReturnJsonUtil.ok("新增成功");
        }else {
            return ReturnJsonUtil.fail("新增失败");
        }
    }

    @ApiOperation("删除")
    @RequestMapping(value = "/delete",method = {RequestMethod.DELETE},produces="application/json")
    public ResponseData delete(@ApiParam(value = "主键 多个逗号分割",required = true) @RequestParam String ids){

        JpowerAssert.notEmpty(ids, JpowerError.Arg, "ids不可为空");

        long c = coreFunctionService.listByPids(ids);
        if (c > 0){
            return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_BUSINESS,"该菜单存在下级菜单，请先删除下级菜单", false);
        }

        Boolean is = coreFunctionService.delete(ids);

        if (is){
            CacheUtil.clear(CacheNames.SYSTEM_REDIS_CACHE);
            return ReturnJsonUtil.ok("删除成功");
        }else {
            return ReturnJsonUtil.fail("删除失败");
        }
    }

    @ApiOperation("修改")
    @RequestMapping(value = "/update",method = {RequestMethod.PUT},produces="application/json")
    public ResponseData update(TbCoreFunction coreFunction){

        JpowerAssert.notEmpty(coreFunction.getId(), JpowerError.Arg, "id不可为空");

        if (StringUtils.isNotBlank(coreFunction.getCode())){
            TbCoreFunction function = coreFunctionService.selectFunctionByCode(coreFunction.getCode());
            if (function != null && !StringUtils.equals(function.getId(),function.getId())){
                return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_BUSINESS,"该菜单已存在", false);
            }
        }

        Boolean is = coreFunctionService.update(coreFunction);

        if (is){
            CacheUtil.clear(CacheNames.SYSTEM_REDIS_CACHE);
            return ReturnJsonUtil.ok("修改成功");
        }else {
            return ReturnJsonUtil.fail("修改失败");
        }
    }

    @ApiOperation("根据角色ID查询所有的权限ID")
    @RequestMapping(value = "/queryUrlIdByRole",method = {RequestMethod.GET},produces="application/json")
    public ResponseData<List<String>> queryUrlIdByRole(@ApiParam(value = "角色ID 多个逗号分割",required = true) @RequestParam String roleIds){
        List<String> list = coreFunctionService.queryUrlIdByRole(roleIds);
        return ReturnJsonUtil.ok("查询成功",list);
    }

    @ApiOperation("懒加载登录用户所有功能树形结构")
    @RequestMapping(value = "/lazyTree",method = {RequestMethod.GET},produces="application/json")
    public ResponseData<List<Tree<String>>> lazyTree(@ApiParam(value = "父级编码",defaultValue = JpowerConstants.TOP_CODE,required = true) @RequestParam(defaultValue = JpowerConstants.TOP_CODE) String parentId){
        List<String> roleIds = SecureUtil.getUserRole();
        List<Tree<String>> list = SecureUtil.isRoot()?coreFunctionService.tree(Condition.getLambdaTreeWrapper(TbCoreFunction.class,TbCoreFunction::getId,TbCoreFunction::getParentId)
                .lazy(parentId)
                .select(TbCoreFunction::getFunctionName,TbCoreFunction::getUrl)
                .orderByAsc(TbCoreFunction::getSort)):
                coreFunctionService.lazyTreeByRole(parentId,roleIds);
        return ReturnJsonUtil.ok("查询成功",list);
    }

    @ApiOperation("页面菜单获取")
    @GetMapping(value = "/listMenuTree", produces="application/json")
    public ResponseData<List<Tree<String>>> listMenuTree(){
        List<String> roleIds = SecureUtil.getUserRole();

        List<TbCoreFunction> list = SecureUtil.isRoot()
                ?
                coreFunctionService.list(Condition.<TbCoreFunction>getQueryWrapper().lambda()
                .eq(TbCoreFunction::getIsMenu, ConstantsEnum.YN01.Y.getValue())
                .orderByAsc(TbCoreFunction::getSort))
                :
                coreFunctionService.listMenuByRoleId(roleIds);
        return ReturnJsonUtil.ok("查询成功", ForestNodeMerger.mergeTree(BeanUtil.copyToList(list,FunctionVo.class)));
    }

    @ApiOperation(value = "（用于页面权限）查询登录用户一个菜单下的所有按钮接口资源", notes = "用于页面权限判断，会把顶级按钮一起返回，顶级按钮代表所有菜单都可拥有权限")
    @GetMapping(value = "/listBut", produces="application/json")
    public ResponseData<List<TbCoreFunction>> listBut(@ApiParam(value = "菜单Id",required = true) @RequestParam String id){
        JpowerAssert.notEmpty(id, JpowerError.Arg, "菜单id不可为空");
        List<TbCoreFunction> list = coreFunctionService.listBtnByRoleIdAndPcode(SecureUtil.getUserRole(),id);
        return ReturnJsonUtil.ok("查询成功", list);
    }

    @ApiOperation("查询登录用户所有功能的树形列表")
    @GetMapping(value = "/listTree", produces="application/json")
    public ResponseData<List<Tree<String>>> listTree(){
        List<Tree<String>> list = SecureUtil.isRoot()?
                coreFunctionService.tree(Condition.getLambdaTreeWrapper(TbCoreFunction.class,TbCoreFunction::getId,TbCoreFunction::getParentId)):
                coreFunctionService.listTreeByRoleId(SecureUtil.getUserRole());
        return ReturnJsonUtil.ok("查询成功", list);
    }

    @ApiOperation("查询登录用户所有菜单树形结构")
    @GetMapping(value = "/menuTree", produces="application/json")
    public ResponseData<List<Tree<String>>> menuTree(){
        return ReturnJsonUtil.ok("查询成功", coreFunctionService.menuTreeByRoleIds(SecureUtil.getUserRole()));
    }

    @ApiOperation(value = "查询登录用户一个菜单下的所有按钮接口资源", notes = "当不传菜单ID时，会查出顶级资源；单独查一个菜单时，不会把顶级按钮返回")
    @GetMapping(value = "/listButByMenu", produces="application/json")
    public ResponseData<List<TbCoreFunction>> listButByMenu(@ApiParam(value = "菜单Id",required = true) @RequestParam(required = false,defaultValue = JpowerConstants.TOP_CODE) String id){
        if (Fc.isBlank(id)){
            id = JpowerConstants.TOP_CODE;
        }
        return ReturnJsonUtil.ok("查询成功", coreFunctionService.listButByMenu(SecureUtil.getUserRole(),id));
    }
}
