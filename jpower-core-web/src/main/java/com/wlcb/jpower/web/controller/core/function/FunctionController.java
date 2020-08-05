package com.wlcb.jpower.web.controller.core.function;

import com.alibaba.fastjson.JSON;
import com.wlcb.jpower.module.base.enums.JpowerError;
import com.wlcb.jpower.module.base.exception.JpowerAssert;
import com.wlcb.jpower.module.base.vo.ResponseData;
import com.wlcb.jpower.module.common.controller.BaseController;
import com.wlcb.jpower.module.common.node.ForestNodeMerger;
import com.wlcb.jpower.module.common.node.Node;
import com.wlcb.jpower.module.common.service.core.user.CoreFunctionService;
import com.wlcb.jpower.module.common.utils.BeanUtil;
import com.wlcb.jpower.module.common.utils.ReturnJsonUtil;
import com.wlcb.jpower.module.common.utils.constants.ConstantsReturn;
import com.wlcb.jpower.module.dbs.entity.core.function.TbCoreFunction;
import com.wlcb.jpower.module.dbs.vo.FunctionVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName RoleController
 * @Description TODO 菜单相关
 * @Author 郭丁志
 * @Date 2020-02-13 14:10
 * @Version 1.0
 *
 */
@RestController
@RequestMapping("/core/function")
public class FunctionController extends BaseController {

    @Resource
    private CoreFunctionService coreFunctionService;

    /**
     * @Author 郭丁志
     * @Description //TODO 根据父节点查询子节点功能
     * @Date 15:13 2020-05-20
     * @Param [coreFunction]
     * @return com.wlcb.jpower.module.base.vo.ResponseData
     **/
    @RequestMapping(value = "/listByParent",method = {RequestMethod.GET,RequestMethod.POST},produces="application/json")
    public ResponseData list(TbCoreFunction coreFunction){

        if(StringUtils.isBlank(coreFunction.getParentCode())){
            coreFunction.setParentCode("-1");
        }

        List<TbCoreFunction> list = coreFunctionService.listByParent(coreFunction);
        return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_SUCCESS,"获取成功", JSON.toJSON(list),true);
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 新增菜单
     * @Date 15:31 2020-05-20
     * @Param [coreFunction]
     * @return com.wlcb.jpower.module.base.vo.ResponseData
     **/
    @RequestMapping(value = "/add",method = {RequestMethod.POST},produces="application/json")
    public ResponseData add(TbCoreFunction coreFunction){

        ResponseData responseData = BeanUtil.allFieldIsNULL(coreFunction,
                "functionName","code", "url", "isMenu");

        if (responseData.getCode() == ConstantsReturn.RECODE_NULL){
            return responseData;
        }

        if(StringUtils.isBlank(coreFunction.getParentCode())){
            coreFunction.setParentCode("-1");
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

    /**
     * @Author 郭丁志
     * @Description //TODO 删除菜单
     * @Date 15:37 2020-05-20
     * @Param [ids]
     * @return com.wlcb.jpower.module.base.vo.ResponseData
     **/
    @RequestMapping(value = "/delete",method = {RequestMethod.DELETE},produces="application/json")
    public ResponseData delete(String ids){

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

    /**
     * @Author 郭丁志
     * @Description //TODO 更新菜单
     * @Date 15:43 2020-05-20
     * @Param [coreRole]
     * @return com.wlcb.jpower.module.base.vo.ResponseData
     **/
    @RequestMapping(value = "/update",method = {RequestMethod.PUT},produces="application/json")
    public ResponseData update(TbCoreFunction coreFunction){

        JpowerAssert.notEmpty(coreFunction.getId(), JpowerError.Arg, "id不可为空");

        if (StringUtils.isNotBlank(coreFunction.getCode())){
            TbCoreFunction function = coreFunctionService.selectFunctionByCode(coreFunction.getCode());
            if (function != null && !StringUtils.equals(function.getId(),function.getId())){
                return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_BUSINESS,"该菜单已存在", false);
            }
        }

        if (StringUtils.isNotBlank(coreFunction.getUrl())){
            TbCoreFunction function = coreFunctionService.selectFunctionByUrl(coreFunction.getUrl());
            if (function != null && !StringUtils.equals(function.getId(),function.getId())){
                return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_BUSINESS,"该URL已存在", false);
            }
        }

        Boolean is = coreFunctionService.update(coreFunction);

        if (is){
            return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_SUCCESS,"修改成功", true);
        }else {
            return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_FAIL,"修改失败", false);
        }
    }

    /**
     * @author 郭丁志
     * @Description //TODO 懒加载所有功能树形结构
     * @date 22:48 2020/7/26 0026
     * @param parentCode
     * @return com.wlcb.jpower.module.base.vo.ResponseData
     */
    @RequestMapping(value = "/lazyTree",method = {RequestMethod.GET},produces="application/json")
    public ResponseData lazyTree(String parentCode){
        List<Node> list = coreFunctionService.lazyTree(parentCode);
        return ReturnJsonUtil.ok("查询成功",list);
    }

    /**
     * @author 郭丁志
     * @Description //TODO 懒加载角色所有权限功能树形结构
     * @date 22:55 2020/7/26 0026
     * @param parentCode
     * @return com.wlcb.jpower.module.base.vo.ResponseData
     */
    @RequestMapping(value = "/lazyTreeByRole",method = {RequestMethod.GET},produces="application/json")
    public ResponseData lazyTreeByRole(String parentCode,String roleIds){
        JpowerAssert.notEmpty(roleIds, JpowerError.Arg, "角色id不可为空");
        List<Node> list = coreFunctionService.lazyTreeByRole(parentCode,roleIds);
        return ReturnJsonUtil.ok("查询成功",list);
    }

    /**
     * @author 郭丁志
     * @Description //TODO 根据角色ID查询所有菜单树形结构
     * @date 22:55 2020/7/26 0026
     * @param roleIds
     * @return com.wlcb.jpower.module.base.vo.ResponseData
     */
    @RequestMapping(value = "/listMenuTree",method = {RequestMethod.GET},produces="application/json")
    public ResponseData listMenuTree(String roleIds){
        JpowerAssert.notEmpty(roleIds, JpowerError.Arg, "角色id不可为空");
        List<TbCoreFunction> list = coreFunctionService.listMenuByRoleId(roleIds);
        List<FunctionVo> collect = list.stream().map(function -> BeanUtil.copy(function, FunctionVo.class)).collect(Collectors.toList());
        return ReturnJsonUtil.ok("查询成功", ForestNodeMerger.merge(collect));
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 根据权限，查询一个菜单下的所有按钮
     * @Date 11:36 2020-07-30
     * @Param [roleIds, id]
     * @return com.wlcb.jpower.module.base.vo.ResponseData
     **/
    @RequestMapping(value = "/listBut",method = {RequestMethod.GET},produces="application/json")
    public ResponseData listBut(String roleIds,String code){
        JpowerAssert.notEmpty(roleIds, JpowerError.Arg, "角色id不可为空");
        JpowerAssert.notEmpty(code, JpowerError.Arg, "菜单code不可为空");
        List<TbCoreFunction> list = coreFunctionService.listBtnByRoleIdAndPcode(roleIds,code);
        return ReturnJsonUtil.ok("查询成功", list);
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 根据角色ID查询所有功能的树形结构
     * @Date 11:52 2020-07-30
     * @Param [roleIds]
     * @return com.wlcb.jpower.module.base.vo.ResponseData
     **/
    @RequestMapping(value = "/listTree",method = {RequestMethod.GET},produces="application/json")
    public ResponseData listTree(String roleIds){
        JpowerAssert.notEmpty(roleIds, JpowerError.Arg, "角色id不可为空");
        List<FunctionVo> list = coreFunctionService.listTreeByRoleId(roleIds);
        return ReturnJsonUtil.ok("查询成功", list);
    }

}
