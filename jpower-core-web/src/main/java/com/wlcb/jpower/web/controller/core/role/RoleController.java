package com.wlcb.jpower.web.controller.core.role;

import com.wlcb.jpower.module.base.annotation.Log;
import com.wlcb.jpower.module.base.enums.JpowerError;
import com.wlcb.jpower.module.base.exception.JpowerAssert;
import com.wlcb.jpower.module.base.vo.ResponseData;
import com.wlcb.jpower.module.common.controller.BaseController;
import com.wlcb.jpower.module.common.service.core.user.CoreRoleService;
import com.wlcb.jpower.module.common.service.core.user.CoreRolefunctionService;
import com.wlcb.jpower.module.common.utils.BeanUtil;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.ReturnJsonUtil;
import com.wlcb.jpower.module.common.utils.constants.ConstantsReturn;
import com.wlcb.jpower.module.common.utils.constants.JpowerConstants;
import com.wlcb.jpower.module.dbs.entity.core.role.TbCoreRole;
import com.wlcb.jpower.module.dbs.vo.RoleVo;
import com.wlcb.jpower.module.mp.support.Condition;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Api(tags = "角色管理")
@RestController
@AllArgsConstructor
@RequestMapping("/core/role")
public class RoleController extends BaseController {

    private CoreRoleService coreRoleService;
    private CoreRolefunctionService coreRolefunctionService;

    @ApiOperation("查询角色树结构")
    @RequestMapping(value = "/listTree",method = {RequestMethod.GET,RequestMethod.POST},produces="application/json")
    public ResponseData<List<RoleVo>> listTree(TbCoreRole coreRole){
        List<RoleVo> list = coreRoleService.listTree(Condition.getQueryWrapper(coreRole)
                .lambda().orderByAsc(TbCoreRole::getCreateTime)
                , RoleVo.class);
        return ReturnJsonUtil.ok("获取成功", list);
    }

    @ApiOperation("新增角色")
    @RequestMapping(value = "/add",method = {RequestMethod.POST},produces="application/json")
    public ResponseData add(TbCoreRole coreRole){

        ResponseData responseData = BeanUtil.allFieldIsNULL(coreRole,
                "code","name");

        if (responseData.getCode() == ConstantsReturn.RECODE_NULL){
            return responseData;
        }

        if (StringUtils.isBlank(coreRole.getParentId())){
            coreRole.setParentId(JpowerConstants.TOP_CODE);
        }

        if (coreRole.getIsSysRole() == null){
            coreRole.setIsSysRole(1);
        }

        TbCoreRole role = coreRoleService.selectRoleByCode(coreRole.getCode());
        if (role != null){
            return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_BUSINESS,"该角色已存在", false);
        }

        Boolean is = coreRoleService.add(coreRole);

        if (is){
            return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_SUCCESS,"新增成功", true);
        }else {
            return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_FAIL,"新增失败", false);
        }
    }

    @ApiOperation("删除角色")
    @RequestMapping(value = "/deleteStatus",method = {RequestMethod.DELETE},produces="application/json")
    public ResponseData deleteStatus(@ApiParam(value = "主键 多个逗号分割",required = true) @RequestParam String ids){

        JpowerAssert.notEmpty(ids, JpowerError.Arg,"ids不可为空");

        Integer c = coreRoleService.listByPids(ids);
        if (c > 0){
            return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_BUSINESS,"该角色存在下级角色，请先删除下级角色", false);
        }

        Boolean is = coreRoleService.removeByIds(Fc.toStrList(ids));

        if (is){
            return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_SUCCESS,"删除成功", true);
        }else {
            return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_FAIL,"删除失败", false);
        }
    }

    @ApiOperation(value = "修改角色信息",notes = "主键不用传")
    @RequestMapping(value = "/update",method = {RequestMethod.PUT},produces="application/json")
    public ResponseData update(TbCoreRole coreRole){

        JpowerAssert.notEmpty(coreRole.getId(), JpowerError.Arg,"id不可为空");

        if (StringUtils.isNotBlank(coreRole.getCode())){
            TbCoreRole role = coreRoleService.selectRoleByCode(coreRole.getCode());
            if (role != null && !StringUtils.equals(role.getId(),coreRole.getId())){
                return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_BUSINESS,"该角色已存在", false);
            }
        }

        Boolean is = coreRoleService.update(coreRole);

        if (is){
            return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_SUCCESS,"修改成功", true);
        }else {
            return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_FAIL,"修改失败", false);
        }
    }

    @ApiOperation("查询角色的权限")
    @RequestMapping(value = "/roleFunction",method = {RequestMethod.GET},produces="application/json")
    public ResponseData<List<Map<String,Object>>> roleFunction(@ApiParam(value = "角色主键",required = true) @RequestParam String roleId){

        JpowerAssert.notEmpty(roleId, JpowerError.Arg,"角色id不可为空");

        List<Map<String,Object>> roleFunction = coreRolefunctionService.selectRoleFunctionByRoleId(roleId);
        return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_SUCCESS,"查询成功", roleFunction, true);
    }

    @ApiOperation("重新给角色赋权")
    @Log(title = "重新给角色赋权",isSaveLog = true)
    @RequestMapping(value = "/addFunction",method = {RequestMethod.POST},produces="application/json")
    public ResponseData addFunction(@ApiParam(value = "角色主键",required = true) @RequestParam String roleId,
                                    @ApiParam(value = "菜单主键 多个逗号分割") @RequestParam(required = false) String functionIds){

        JpowerAssert.notEmpty(roleId, JpowerError.Arg,"角色id不可为空");

        Integer count = coreRolefunctionService.addRolefunctions(roleId,functionIds);

        if (count > 0){
            return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_SUCCESS,"设置成功", true);
        }else {
            return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_FAIL,"设置失败", false);
        }
    }
}
