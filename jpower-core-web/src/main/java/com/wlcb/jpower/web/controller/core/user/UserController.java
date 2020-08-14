package com.wlcb.jpower.web.controller.core.user;

import com.github.pagehelper.PageInfo;
import com.wlcb.jpower.module.base.annotation.Log;
import com.wlcb.jpower.module.base.enums.BusinessType;
import com.wlcb.jpower.module.base.enums.JpowerError;
import com.wlcb.jpower.module.base.exception.JpowerAssert;
import com.wlcb.jpower.module.base.vo.ResponseData;
import com.wlcb.jpower.module.common.controller.BaseController;
import com.wlcb.jpower.module.common.page.PaginationContext;
import com.wlcb.jpower.module.common.service.core.user.CoreUserRoleService;
import com.wlcb.jpower.module.common.service.core.user.CoreUserService;
import com.wlcb.jpower.module.common.support.BeanExcelUtil;
import com.wlcb.jpower.module.common.utils.*;
import com.wlcb.jpower.module.common.utils.constants.ConstantsEnum;
import com.wlcb.jpower.module.common.utils.constants.ConstantsReturn;
import com.wlcb.jpower.module.common.utils.constants.ConstantsUtils;
import com.wlcb.jpower.module.common.utils.constants.ParamsConstants;
import com.wlcb.jpower.module.common.utils.param.ParamConfig;
import com.wlcb.jpower.module.dbs.entity.core.user.TbCoreUser;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Map;

@Api(tags = "用户管理")
@RestController
@AllArgsConstructor
@RequestMapping("/core/user")
public class UserController extends BaseController {

    private CoreUserService coreUserService;
    private CoreUserRoleService coreUserRoleService;

    @ApiOperation("查询用户分页列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum",value = "第几页",defaultValue = "1",paramType = "query",dataType = "int",required = true),
            @ApiImplicitParam(name = "pageSize",value = "每页长度",defaultValue = "10",paramType = "query",dataType = "int",required = true)
    })
    @RequestMapping(value = "/list",method = {RequestMethod.GET,RequestMethod.POST},produces="application/json")
    public ResponseData<PageInfo<TbCoreUser>> list(TbCoreUser coreUser){
        PaginationContext.startPage();
        List<TbCoreUser> list = coreUserService.list(coreUser);
        return ReturnJsonUtil.ok("获取成功", new PageInfo<>(list));
    }

    @ApiOperation("查询用户详情")
    @RequestMapping(value = "/get",method = RequestMethod.GET,produces="application/json")
    public ResponseData<TbCoreUser> list(@ApiParam(value = "主键",required = true) @RequestParam String id){
        JpowerAssert.notEmpty(id, JpowerError.Arg,"id不可为空");

        TbCoreUser user = coreUserService.selectUserById(id);
        return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_SUCCESS,"查询成功", user, true);
    }

    @ApiOperation(value = "新增",notes = "主键不用传")
    @RequestMapping(value = "/add",method = {RequestMethod.POST},produces="application/json")
    public ResponseData add(TbCoreUser coreUser){

        JpowerAssert.notEmpty(coreUser.getLoginId(), JpowerError.Arg,"用户名不可为空");

        if (coreUser.getIdType() != null && ConstantsEnum.ID_TYPE.ID_CARD.getValue().equals(coreUser.getIdType())){
            if (!StrUtil.cardCodeVerifySimple(coreUser.getIdNo())){
                return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_BUSINESS,"身份证不合法", false);
            }
        }

        if (StringUtils.isNotBlank(coreUser.getTelephone()) && !StrUtil.isPhone(coreUser.getTelephone()) ){
            return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_BUSINESS,"手机号不合法", false);
        }

        if (StringUtils.isNotBlank(coreUser.getEmail()) && !StrUtil.isEmail(coreUser.getEmail()) ){
            return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_BUSINESS,"邮箱不合法", false);
        }

        TbCoreUser user = coreUserService.selectUserLoginId(coreUser.getLoginId());
        if (user != null){
            return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_BUSINESS,"该登录用户名已存在", false);
        }

        coreUser.setPassword(DigestUtil.encrypt(MD5.parseStrToMd5U32(ParamConfig.getString(ParamsConstants.USER_DEFAULT_PASSWORD, ConstantsUtils.DEFAULT_USER_PASSWORD))));
        coreUser.setUserType(ConstantsEnum.USER_TYPE.USER_TYPE_SYSTEM.getValue());
        Boolean is = coreUserService.save(coreUser);

        if (is){
            return ReturnJsonUtil.ok("新增成功");
        }else {
            return ReturnJsonUtil.fail("新增失败");
        }
    }

    @ApiOperation(value = "删除用户")
    @Log(title = "删除登录用户",businessType = BusinessType.DELETE)
    @RequestMapping(value = "/delete",method = {RequestMethod.DELETE},produces="application/json")
    public ResponseData delete(@ApiParam(value = "主键 多个逗号分割",required = true) @RequestParam String ids){

        JpowerAssert.notEmpty(ids, JpowerError.Arg,"ids不可为空");

        Boolean is = coreUserService.delete(ids);

        if (is){
            return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_SUCCESS,"删除成功", true);
        }else {
            return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_FAIL,"删除失败", false);
        }
    }

    @ApiOperation(value = "修改用户信息")
    @Log(title = "修改系统用户信息",businessType = BusinessType.UPDATE)
    @RequestMapping(value = "/update",method = {RequestMethod.PUT},produces="application/json")
    public ResponseData update(TbCoreUser coreUser){

        JpowerAssert.notEmpty(coreUser.getId(), JpowerError.Arg,"用户名不可为空");

        if (coreUser.getIdType() != null && ConstantsEnum.ID_TYPE.ID_CARD.getValue().equals(coreUser.getIdType())){
            if (!StrUtil.cardCodeVerifySimple(coreUser.getIdNo())){
                return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_BUSINESS,"身份证不合法", false);
            }
        }

        if (StringUtils.isNotBlank(coreUser.getTelephone()) && !StrUtil.isPhone(coreUser.getTelephone()) ){
            return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_BUSINESS,"手机号不合法", false);
        }

        if (StringUtils.isNotBlank(coreUser.getEmail()) && !StrUtil.isEmail(coreUser.getEmail()) ){
            return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_BUSINESS,"邮箱不合法", false);
        }

        if (StringUtils.isNotBlank(coreUser.getLoginId())){
            TbCoreUser user = coreUserService.selectUserLoginId(coreUser.getLoginId());
            if (user != null && !StringUtils.equals(user.getId(),coreUser.getId())){
                return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_BUSINESS,"该登录用户名已存在", false);
            }
        }

        coreUser.setPassword(null);
        Boolean is = coreUserService.update(coreUser);

        if (is){
            return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_SUCCESS,"修改成功", true);
        }else {
            return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_FAIL,"修改失败", false);
        }
    }

    @ApiOperation(value = "重置用户登陆密码")
    @RequestMapping(value = "/resetPassword",method = {RequestMethod.PUT},produces="application/json")
    public ResponseData resetPassword(@ApiParam(value = "主键 多个逗号分割",required = true) @RequestParam  String ids){

        String pass = DigestUtil.encrypt(MD5.parseStrToMd5U32(ParamConfig.getString(ParamsConstants.USER_DEFAULT_PASSWORD,ConstantsUtils.DEFAULT_USER_PASSWORD)));

        JpowerAssert.notEmpty(ids, JpowerError.Arg,"用户ids不可为空");

        Boolean is = coreUserService.updateUserPassword(ids,pass);

        if (is){
            return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_SUCCESS,ids.split(",").length+"位用户密码重置成功", true);
        }else {
            return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_FAIL,"重置失败", false);
        }
    }

    @ApiOperation(value = "批量导入用户")
    @RequestMapping(value = "/importUser",method = {RequestMethod.POST},produces="application/json")
    public ResponseData importUser(MultipartFile file){

        JpowerAssert.notTrue(file == null || file.isEmpty(),JpowerError.Arg,"文件不可为空");

        String path = fileParentPath + File.separator + "import" + File.separator + "user";

        try{
            String savePaths = MultipartFileUtil.saveFile(file,"xls,xlsx",path);

            File saveFile = new File(fileParentPath + File.separator + "import" + File.separator + "user" + savePaths);

            if (saveFile.exists()){
                BeanExcelUtil<TbCoreUser> beanExcelUtil = new BeanExcelUtil<>(TbCoreUser.class);
                List<TbCoreUser> list = beanExcelUtil.importExcel(saveFile);

                for (TbCoreUser coreUser : list) {
                    coreUser.setId(UUIDUtil.getUUID());
                    coreUser.setPassword(DigestUtil.encrypt(MD5.parseStrToMd5U32(ParamConfig.getString(ParamsConstants.USER_DEFAULT_PASSWORD,ConstantsUtils.DEFAULT_USER_PASSWORD))));
                    coreUser.setUserType(ConstantsEnum.USER_TYPE.USER_TYPE_SYSTEM.getValue());

                    //判断用户是否指定激活，如果没有指定就去读取默认配置
                    if (coreUser.getActivationStatus() == null){
                        Integer isActivation = ParamConfig.getInt(ParamsConstants.IS_ACTIVATION,ConstantsUtils.DEFAULT_USER_ACTIVATION);
                        coreUser.setActivationStatus(isActivation);
                    }
                    // 如果不是激活状态则去生成激活码
                    if (!ConstantsEnum.ACTIVATION_STATUS.ACTIVATION_YES.getValue().equals(coreUser.getActivationStatus())){
                        coreUser.setActivationCode(UUIDUtil.create10UUidNum());
                        coreUser.setActivationStatus(ConstantsEnum.ACTIVATION_STATUS.ACTIVATION_NO.getValue());
                    }

                }

                Boolean is = coreUserService.insterBatch(list);
                if (is){
                    return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_SUCCESS,"成功导入"+list.size()+"条", true);
                }else {
                    return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_FAIL,"导入失败", false);
                }
            }

            logger.error("文件上传出错，文件不存在,{}",saveFile.getAbsolutePath());
            return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_FAIL,"上传出错，请稍后重试", false);
        }catch (Exception e){
            logger.error("文件上传出错，error={}",e.getMessage());
            return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_ERROR,"上传出错，请稍后重试", false);
        }

    }

    @ApiOperation(value = "导出用户")
    @RequestMapping(value = "/exportUser",method = {RequestMethod.GET,RequestMethod.POST},produces="application/json")
    public ResponseData exportUser(TbCoreUser coreUser){
        List<TbCoreUser> list = coreUserService.list(coreUser);

        BeanExcelUtil<TbCoreUser> beanExcelUtil = new BeanExcelUtil<>(TbCoreUser.class,downloadPath);
        return beanExcelUtil.exportExcel(list,"用户列表");
    }

    @ApiOperation(value = "给用户重新设置角色")
    @RequestMapping(value = "/addRole",method = {RequestMethod.POST},produces="application/json")
    public ResponseData addRole(@ApiParam(value = "用户主键 多个逗号分割",required = true) @RequestParam String userIds,
                                @ApiParam(value = "角色主键 多个逗号分割") @RequestParam String roleIds){

        JpowerAssert.notEmpty(userIds, JpowerError.Arg,"userIds不可为空");

        String[] userIdss = userIds.split(",");
        JpowerAssert.notTrue(userIdss.length <= 0, JpowerError.Arg,"userIds不可为空");

        Boolean is = coreUserService.updateUsersRole(userIds,roleIds);

        if (is){
            return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_SUCCESS,"设置成功", true);
        }else {
            return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_FAIL,"设置失败", false);
        }
    }

    @ApiOperation(value = "查询用户所有角色")
    @RequestMapping(value = "/userRole",method = {RequestMethod.GET},produces="application/json")
    public ResponseData<List<Map<String,Object>>> userRole(@ApiParam(value = "用户主键",required = true) @RequestParam String userId){

        JpowerAssert.notEmpty(userId, JpowerError.Arg,"用户ID不可为空");

        List<Map<String,Object>> userRoleList = coreUserRoleService.selectUserRoleByUserId(userId);
        return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_SUCCESS,"查询成功",userRoleList, true);
    }

}
