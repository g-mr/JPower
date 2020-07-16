package com.wlcb.jpower.web.controller.core.role;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.wlcb.jpower.module.base.annotation.Log;
import com.wlcb.jpower.module.base.enums.JpowerError;
import com.wlcb.jpower.module.base.exception.JpowerAssert;
import com.wlcb.jpower.module.base.vo.ResponseData;
import com.wlcb.jpower.module.common.controller.BaseController;
import com.wlcb.jpower.module.common.service.core.user.CoreRoleService;
import com.wlcb.jpower.module.common.service.core.user.CoreRolefunctionService;
import com.wlcb.jpower.module.common.utils.BeanUtil;
import com.wlcb.jpower.module.common.utils.ReturnJsonUtil;
import com.wlcb.jpower.module.common.utils.constants.ConstantsReturn;
import com.wlcb.jpower.module.dbs.entity.core.role.TbCoreRole;
import com.wlcb.jpower.module.dbs.entity.core.role.TbCoreRoleFunction;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @ClassName RoleController
 * @Description TODO 角色相关
 * @Author 郭丁志
 * @Date 2020-02-13 14:10
 * @Version 1.0
 */
@RestController
@RequestMapping("/core/role")
public class RoleController extends BaseController {

    @Resource
    private CoreRoleService coreRoleService;
    @Resource
    private CoreRolefunctionService coreRolefunctionService;

    /**
     * @Author 郭丁志
     * @Description //TODO 查询下级角色列表
     * @Date 09:41 2020-05-19
     * @Param [coreUser]
     * @return com.wlcb.jpower.module.base.vo.ResponseData
     **/
    @RequestMapping(value = "/listByParent",method = {RequestMethod.GET,RequestMethod.POST},produces="application/json")
    public ResponseData listByParent(TbCoreRole coreRole){

        if(StringUtils.isBlank(coreRole.getParentId()) && StringUtils.isBlank(coreRole.getParentCode())){
            coreRole.setParentCode("-1");
        }

        List<TbCoreRole> list = coreRoleService.listByParent(coreRole);
        return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_SUCCESS,"获取成功", JSON.toJSON(list),true);
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 新增角色
     * @Date 10:14 2020-05-19
     * @Param [coreUser]
     * @return com.wlcb.jpower.module.base.vo.ResponseData
     **/
    @RequestMapping(value = "/add",method = {RequestMethod.POST},produces="application/json")
    public ResponseData add(TbCoreRole coreRole){

        ResponseData responseData = BeanUtil.allFieldIsNULL(coreRole,
                "code","name");

        if (responseData.getCode() == ConstantsReturn.RECODE_NULL){
            return responseData;
        }

        if (StringUtils.isBlank(coreRole.getParentCode()) || StringUtils.isBlank(coreRole.getParentId())){
            coreRole.setParentCode("-1");
            coreRole.setParentId("-1");
        }

        if (coreRole.getIsSysRole() == null){
            coreRole.setIsSysRole(0);
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

    /**
     * @Author 郭丁志
     * @Description //TODO 删除角色
     * @Date 11:27 2020-05-19
     * @Param [coreUser]
     * @return com.wlcb.jpower.module.base.vo.ResponseData
     **/
    @RequestMapping(value = "/deleteStatus",method = {RequestMethod.DELETE},produces="application/json")
    public ResponseData deleteStatus(String ids){

        JpowerAssert.notEmpty(ids, JpowerError.Arg,"ids不可为空");

        Integer c = coreRoleService.listByPids(ids);
        if (c > 0){
            return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_BUSINESS,"该角色存在下级角色，请先删除下级角色", false);
        }

        Boolean is = coreRoleService.deleteStatus(ids);

        if (is){
            return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_SUCCESS,"删除成功", true);
        }else {
            return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_FAIL,"删除失败", false);
        }
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 修改角色信息
     * @Date 11:31 2020-05-19
     * @Param [coreUser]
     * @return com.wlcb.jpower.module.base.vo.ResponseData
     **/
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

    /**
     * @author 郭丁志
     * @Description //TODO 查询角色权限
     * @date 23:53 2020/5/26 0026
     * @param roleId 
     * @return com.wlcb.jpower.module.base.vo.ResponseData
     */
    @RequestMapping(value = "/roleFunction",method = {RequestMethod.GET},produces="application/json")
    public ResponseData roleFunction(String roleId){

        JpowerAssert.notEmpty(roleId, JpowerError.Arg,"角色id不可为空");

        List<Map<String,Object>> roleFunction = coreRolefunctionService.selectRoleFunctionByRoleId(roleId);
        return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_SUCCESS,"查询成功", roleFunction, true);
    }

    /**
     * @author 郭丁志
     * @Description //TODO 重新给角色赋权
     * @date 0:13 2020/5/27 0027
     * @param roleId
     * @return com.wlcb.jpower.module.base.vo.ResponseData
     */
    @Log(title = "重新给角色赋权",isSaveLog = true)
    @RequestMapping(value = "/addFunction",method = {RequestMethod.POST},produces="application/json")
    public ResponseData addFunction(String roleId,String functionIds){

        JpowerAssert.notEmpty(roleId, JpowerError.Arg,"角色id不可为空");

        Integer count = coreRolefunctionService.addRolefunctions(roleId,functionIds);

        if (count > 0){
            return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_SUCCESS,"设置成功", true);
        }else {
            return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_FAIL,"设置失败", false);
        }
    }
}
