package com.wlcb.jpower.controller.function;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wlcb.jpower.dbs.entity.function.TbCoreFunction;
import com.wlcb.jpower.module.base.enums.JpowerError;
import com.wlcb.jpower.module.base.exception.JpowerAssert;
import com.wlcb.jpower.module.base.vo.ResponseData;
import com.wlcb.jpower.module.common.cache.CacheNames;
import com.wlcb.jpower.module.common.controller.BaseController;
import com.wlcb.jpower.module.common.node.ForestNodeMerger;
import com.wlcb.jpower.module.common.node.Node;
import com.wlcb.jpower.module.common.utils.*;
import com.wlcb.jpower.module.common.utils.constants.ConstantsEnum;
import com.wlcb.jpower.module.common.utils.constants.ConstantsReturn;
import com.wlcb.jpower.module.common.utils.constants.JpowerConstants;
import com.wlcb.jpower.module.common.utils.constants.StringPool;
import com.wlcb.jpower.module.mp.support.Condition;
import com.wlcb.jpower.service.role.CoreFunctionService;
import com.wlcb.jpower.vo.FunctionVo;
import com.wlcb.jpower.wrapper.BaseDictWrapper;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.Map;

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
            @ApiImplicitParam(name = "code_eq",value = "编码",paramType = "query"),
            @ApiImplicitParam(name = "isMenu_eq",value = "是否菜单 字典YN01",paramType = "query"),
            @ApiImplicitParam(name = "functionName",value = "功能名称",paramType = "query"),
            @ApiImplicitParam(name = "url",value = "功能URL",paramType = "query")
    })
    @RequestMapping(value = "/listByParent",method = {RequestMethod.GET,RequestMethod.POST},produces="application/json")
    public ResponseData<List<FunctionVo>> list(@ApiIgnore @RequestParam Map<String,Object> coreFunction){

        if(StringUtils.isBlank(Fc.toStr(coreFunction.get("parentId_eq")))){
            coreFunction.put("parentId_eq",JpowerConstants.TOP_CODE);
        }

        List<TbCoreFunction> list = coreFunctionService.listByParent(coreFunction);
        return ReturnJsonUtil.ok("获取成功", BaseDictWrapper.<TbCoreFunction,FunctionVo>builder().dict(list,FunctionVo.class));
    }

    @ApiOperation("新增菜单")
    @PostMapping(value = "/add", produces="application/json")
    public ResponseData add(TbCoreFunction coreFunction){

        BeanUtil.allFieldIsNULL(coreFunction,
                "functionName","code", "url", "isMenu");

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

    @ApiOperation("删除菜单")
    @RequestMapping(value = "/delete",method = {RequestMethod.DELETE},produces="application/json")
    public ResponseData delete(@ApiParam(value = "主键 多个逗号分割",required = true) @RequestParam String ids){

        JpowerAssert.notEmpty(ids, JpowerError.Arg, "ids不可为空");

        Integer c = coreFunctionService.listByPids(ids);
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

    @ApiOperation("修改菜单")
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
    public ResponseData<List<Node>> lazyTree(@ApiParam(value = "父级编码",defaultValue = JpowerConstants.TOP_CODE,required = true) @RequestParam(defaultValue = JpowerConstants.TOP_CODE) String parentId){
        List<String> roleIds = SecureUtil.getUserRole();

        List<Node> list = SecureUtil.isRoot()?coreFunctionService.tree(Condition.getTreeWrapper(TbCoreFunction::getId,TbCoreFunction::getParentId,TbCoreFunction::getFunctionName,TbCoreFunction::getUrl)
                .lazy(parentId).lambda()
                .orderByAsc(TbCoreFunction::getSort)):
                coreFunctionService.lazyTreeByRole(parentId,roleIds);
        return ReturnJsonUtil.ok("查询成功",list);
    }

    @ApiOperation("查询登录用户所有菜单树形结构")
    @GetMapping(value = "/menuTree", produces="application/json")
    public ResponseData<List<Node>> menuTree(){
        LambdaQueryWrapper<TbCoreFunction> wrapper = Condition.getTreeWrapper(TbCoreFunction::getId,TbCoreFunction::getParentId,TbCoreFunction::getFunctionName,TbCoreFunction::getUrl)
                .lambda()
                .eq(TbCoreFunction::getIsMenu, ConstantsEnum.YN01.Y.getValue())
                .orderByAsc(TbCoreFunction::getSort);

        if (!SecureUtil.isRoot()){
            String inSql = StringPool.SINGLE_QUOTE.concat(Fc.join(SecureUtil.getUserRole(),StringPool.SINGLE_QUOTE_CONCAT)).concat(StringPool.SINGLE_QUOTE);
            wrapper.inSql(TbCoreFunction::getId, StringUtil.format("select function_id from tb_core_role_function where role_id in ({})",inSql));
        }

        List<Node> list = coreFunctionService.tree(wrapper);
        return ReturnJsonUtil.ok("查询成功", list);
    }

    @ApiOperation("查询登录用户所有菜单树形列表结构")
    @RequestMapping(value = "/listMenuTree",method = {RequestMethod.GET},produces="application/json")
    public ResponseData<List<FunctionVo>> listMenuTree(){
        List<String> roleIds = SecureUtil.getUserRole();

        List<TbCoreFunction> list = SecureUtil.isRoot()?coreFunctionService.list(Condition.<TbCoreFunction>getQueryWrapper().lambda()
                .eq(TbCoreFunction::getIsMenu, ConstantsEnum.YN01.Y.getValue())
                .orderByAsc(TbCoreFunction::getSort)):
                coreFunctionService.listMenuByRoleId(roleIds);
        return ReturnJsonUtil.ok("查询成功", ForestNodeMerger.merge(BaseDictWrapper.<TbCoreFunction,FunctionVo>builder().dict(list,FunctionVo.class)));
    }

    @ApiOperation("查询登录用户一个菜单下的所有按钮")
    @RequestMapping(value = "/listBut",method = {RequestMethod.GET},produces="application/json")
    public ResponseData<List<TbCoreFunction>> listBut(@ApiParam(value = "菜单Id",required = true) @RequestParam String id){
        JpowerAssert.notEmpty(id, JpowerError.Arg, "菜单id不可为空");
        List<TbCoreFunction> list = coreFunctionService.listBtnByRoleIdAndPcode(SecureUtil.getUserRole(),id);
        return ReturnJsonUtil.ok("查询成功", list);
    }

    @ApiOperation("查询登录用户所有功能的树形列表")
    @RequestMapping(value = "/listTree",method = {RequestMethod.GET},produces="application/json")
    public ResponseData<List<FunctionVo>> listTree(){
        List<FunctionVo> list = SecureUtil.isRoot()?
                coreFunctionService.listTree(Condition.getQueryWrapper(),FunctionVo.class):
                coreFunctionService.listTreeByRoleId(SecureUtil.getUserRole());
        return ReturnJsonUtil.ok("查询成功", list);
    }

}
