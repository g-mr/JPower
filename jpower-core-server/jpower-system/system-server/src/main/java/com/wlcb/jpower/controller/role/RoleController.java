package com.wlcb.jpower.controller.role;

import com.wlcb.jpower.dbs.entity.role.TbCoreRole;
import com.wlcb.jpower.module.base.annotation.Log;
import com.wlcb.jpower.module.base.enums.JpowerError;
import com.wlcb.jpower.module.base.exception.JpowerAssert;
import com.wlcb.jpower.module.base.vo.ResponseData;
import com.wlcb.jpower.module.common.controller.BaseController;
import com.wlcb.jpower.module.common.utils.BeanUtil;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.ReturnJsonUtil;
import com.wlcb.jpower.module.common.utils.constants.ConstantsEnum;
import com.wlcb.jpower.module.common.utils.constants.ConstantsReturn;
import com.wlcb.jpower.module.common.utils.constants.JpowerConstants;
import com.wlcb.jpower.module.mp.support.Condition;
import com.wlcb.jpower.service.role.CoreFunctionService;
import com.wlcb.jpower.service.role.CoreRoleService;
import com.wlcb.jpower.service.role.CoreRolefunctionService;
import com.wlcb.jpower.vo.RoleVo;
import com.wlcb.jpower.wrapper.BaseDictWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api(tags = "角色管理")
@RestController
@AllArgsConstructor
@RequestMapping("/core/role")
public class RoleController extends BaseController {

    private CoreRoleService coreRoleService;
    private CoreRolefunctionService coreRolefunctionService;
    private CoreFunctionService coreFunctionService;

    @ApiOperation(value = "查询匿名用户是否拥有URL的权限", hidden = true)
    @GetMapping("/queryRoleByUrl")
    public ResponseData<Boolean> queryRoleByUrl(String url){
        return ReturnJsonUtil.ok("查询成功",coreFunctionService.queryRoleByUrl(url)>0?Boolean.TRUE:Boolean.FALSE);
    }

    @ApiOperation("查询角色树结构列表")
    @RequestMapping(value = "/listTree",method = {RequestMethod.GET,RequestMethod.POST},produces="application/json")
    public ResponseData<List<RoleVo>> listTree(TbCoreRole coreRole){
        List<RoleVo> list = coreRoleService.listTree(Condition.getQueryWrapper(coreRole)
                .lambda().orderByAsc(TbCoreRole::getCreateTime)
                , RoleVo.class);
        return ReturnJsonUtil.ok("获取成功", BaseDictWrapper.dict(list,RoleVo.class));
    }

    @ApiOperation("新增角色")
    @RequestMapping(value = "/add",method = {RequestMethod.POST},produces="application/json")
    public ResponseData add(TbCoreRole coreRole){

        BeanUtil.allFieldIsNULL(coreRole,
                "code","name");

        if (StringUtils.isBlank(coreRole.getParentId())){
            coreRole.setParentId(JpowerConstants.TOP_CODE);
        }

        if (Fc.isNull(coreRole.getIsSysRole())){
            coreRole.setIsSysRole(ConstantsEnum.YN01.N.getValue());
        }

        TbCoreRole role = coreRoleService.selectRoleByCode(coreRole.getCode());
        if (role != null){
            return ReturnJsonUtil.busFail("该角色已存在");
        }

        return ReturnJsonUtil.status(coreRoleService.add(coreRole));
    }

    @ApiOperation("删除角色")
    @RequestMapping(value = "/deleteStatus",method = {RequestMethod.DELETE},produces="application/json")
    public ResponseData deleteStatus(@ApiParam(value = "主键 多个逗号分割",required = true) @RequestParam String ids){

        JpowerAssert.notEmpty(ids, JpowerError.Arg,"ids不可为空");

        Integer c = coreRoleService.listByPids(ids);
        if (c > 0){
            return ReturnJsonUtil.busFail("该角色存在下级角色，请先删除下级角色");
        }

        return ReturnJsonUtil.status(coreRoleService.removeReal(Condition.<TbCoreRole>getQueryWrapper().lambda()
                .in(TbCoreRole::getId,Fc.toStrList(ids))
                .eq(TbCoreRole::getIsSysRole,ConstantsEnum.YN01.N.getValue())));
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

        return ReturnJsonUtil.status(coreRoleService.update(coreRole));
    }

    @ApiOperation("查询角色的权限")
    @RequestMapping(value = "/roleFunction",method = {RequestMethod.GET},produces="application/json")
    public ResponseData<List<Map<String,Object>>> roleFunction(@ApiParam(value = "角色主键",required = true) @RequestParam String roleId){

        JpowerAssert.notEmpty(roleId, JpowerError.Arg,"角色id不可为空");

        List<Map<String,Object>> roleFunction = coreRolefunctionService.selectRoleFunctionByRoleId(roleId);
        return ReturnJsonUtil.ok("查询成功", roleFunction);
    }

    @ApiOperation("重新给角色赋权")
    @Log(title = "重新给角色赋权",isSaveLog = true)
    @RequestMapping(value = "/addFunction",method = {RequestMethod.POST},produces="application/json")
    public ResponseData addFunction(@ApiParam(value = "角色主键",required = true) @RequestParam String roleId,
                                    @ApiParam(value = "菜单主键 多个逗号分割") @RequestParam(required = false) String functionIds){

        JpowerAssert.notEmpty(roleId, JpowerError.Arg,"角色id不可为空");
        JpowerAssert.notNull(coreRoleService.getById(roleId),JpowerError.BUSINESS,"该角色不存在");

        Integer count = coreRolefunctionService.addRolefunctions(roleId,functionIds);

        if (count > 0){
            return ReturnJsonUtil.ok("设置成功");
        }else {
            return ReturnJsonUtil.fail("设置失败");
        }
    }
}
