package com.wlcb.jpower.controller.function;

import com.wlcb.jpower.dbs.entity.function.TbCoreFunction;
import com.wlcb.jpower.module.base.enums.JpowerError;
import com.wlcb.jpower.module.base.exception.JpowerAssert;
import com.wlcb.jpower.module.base.vo.ResponseData;
import com.wlcb.jpower.module.common.controller.BaseController;
import com.wlcb.jpower.module.common.node.ForestNodeMerger;
import com.wlcb.jpower.module.common.node.Node;
import com.wlcb.jpower.module.common.utils.BeanUtil;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.ReturnJsonUtil;
import com.wlcb.jpower.module.common.utils.constants.ConstantsReturn;
import com.wlcb.jpower.module.common.utils.constants.JpowerConstants;
import com.wlcb.jpower.service.role.CoreFunctionService;
import com.wlcb.jpower.vo.FunctionVo;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    public ResponseData<List<TbCoreFunction>> list(@ApiIgnore @RequestParam Map<String,Object> coreFunction){

        if(StringUtils.isBlank(Fc.toStr(coreFunction.get("parentId_eq")))){
            coreFunction.put("parentId_eq","-1");
        }

        List<TbCoreFunction> list = coreFunctionService.listByParent(coreFunction);
        return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_SUCCESS,"获取成功", list,true);
    }

    @ApiOperation("新增菜单")
    @RequestMapping(value = "/add",method = {RequestMethod.POST},produces="application/json")
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
            return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_SUCCESS,"新增成功", true);
        }else {
            return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_FAIL,"新增失败", false);
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
            return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_SUCCESS,"删除成功", true);
        }else {
            return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_FAIL,"删除失败", false);
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
            return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_SUCCESS,"修改成功", true);
        }else {
            return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_FAIL,"修改失败", false);
        }
    }

    @ApiOperation("懒加载所有功能树形结构")
    @RequestMapping(value = "/lazyTree",method = {RequestMethod.GET},produces="application/json")
    public ResponseData<List<Node>> lazyTree(@ApiParam(value = "父级编码",defaultValue = JpowerConstants.TOP_CODE,required = true) @RequestParam String parentId){
        List<Node> list = coreFunctionService.lazyTree(parentId);
        return ReturnJsonUtil.ok("查询成功",list);
    }

    @ApiOperation("懒加载角色所有权限功能树形结构")
    @RequestMapping(value = "/lazyTreeByRole",method = {RequestMethod.GET},produces="application/json")
    public ResponseData<List<Node>> lazyTreeByRole(@ApiParam(value = "父级编码",defaultValue = JpowerConstants.TOP_CODE,required = true) @RequestParam String parentId,
                                                   @ApiParam(value = "角色ID 多个逗号分割",required = true) @RequestParam String roleIds){
        JpowerAssert.notEmpty(roleIds, JpowerError.Arg, "角色id不可为空");
        List<Node> list = coreFunctionService.lazyTreeByRole(parentId,roleIds);
        return ReturnJsonUtil.ok("查询成功",list);
    }

    @ApiOperation("根据角色ID查询所有菜单树形结构")
    @RequestMapping(value = "/listMenuTree",method = {RequestMethod.GET},produces="application/json")
    public ResponseData<List<FunctionVo>> listMenuTree(@ApiParam(value = "角色ID 多个逗号分割",required = true) @RequestParam String roleIds){
        JpowerAssert.notEmpty(roleIds, JpowerError.Arg, "角色id不可为空");
        List<TbCoreFunction> list = coreFunctionService.listMenuByRoleId(roleIds);
        List<FunctionVo> collect = list.stream().map(function -> BeanUtil.copy(function, FunctionVo.class)).collect(Collectors.toList());
        return ReturnJsonUtil.ok("查询成功", ForestNodeMerger.merge(collect));
    }

    @ApiOperation("根据权限查询一个菜单下的所有按钮")
    @RequestMapping(value = "/listBut",method = {RequestMethod.GET},produces="application/json")
    public ResponseData<List<TbCoreFunction>> listBut(@ApiParam(value = "角色ID 多个逗号分割",required = true) @RequestParam String roleIds,
                                @ApiParam(value = "菜单Id",required = true) @RequestParam String id){
        JpowerAssert.notEmpty(roleIds, JpowerError.Arg, "角色id不可为空");
        JpowerAssert.notEmpty(id, JpowerError.Arg, "菜单id不可为空");
        List<TbCoreFunction> list = coreFunctionService.listBtnByRoleIdAndPcode(roleIds,id);
        return ReturnJsonUtil.ok("查询成功", list);
    }

    @ApiOperation("根据角色ID查询所有功能的树形结构")
    @RequestMapping(value = "/listTree",method = {RequestMethod.GET},produces="application/json")
    public ResponseData<List<FunctionVo>> listTree(@ApiParam(value = "角色ID 多个逗号分割",required = true) @RequestParam String roleIds){
        JpowerAssert.notEmpty(roleIds, JpowerError.Arg, "角色id不可为空");
        List<FunctionVo> list = coreFunctionService.listTreeByRoleId(roleIds);
        return ReturnJsonUtil.ok("查询成功", list);
    }

    @PostMapping("/function/putRedisAllFunctionByRoles")
    public boolean putRedisAllFunctionByRoles(@RequestParam List<String> roleIds, @RequestParam Long expiresIn, @RequestParam String accessToken){
        coreFunctionService.putRedisAllFunctionByRoles(roleIds,expiresIn,accessToken);
        return true;
    }

}
