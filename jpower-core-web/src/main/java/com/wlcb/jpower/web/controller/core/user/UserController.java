package com.wlcb.jpower.web.controller.core.user;

import com.alibaba.fastjson.JSON;
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
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * @ClassName UserController
 * @Description TODO 登录用户相关
 * @Author 郭丁志
 * @Date 2020-02-13 14:10
 * @Version 1.0
 */
@RestController
@RequestMapping("/core/user")
public class UserController extends BaseController {

    @Resource
    private CoreUserService coreUserService;
    @Resource
    private CoreUserRoleService coreUserRoleService;

    /**
     * @Author 郭丁志
     * @Description //TODO 查询用户列表
     * @Date 09:41 2020-05-19
     * @Param [coreUser]
     * @return com.wlcb.jpower.module.base.vo.ResponseData
     **/
    @RequestMapping(value = "/list",method = {RequestMethod.GET,RequestMethod.POST},produces="application/json")
    public ResponseData list(TbCoreUser coreUser){
        PaginationContext.startPage();
        List<TbCoreUser> list = coreUserService.list(coreUser);

        return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_SUCCESS,"获取成功", JSON.toJSON(new PageInfo<>(list)),true);
    }

    /**
     * @author 郭丁志
     * @Description //TODO 查询用户详情
     * @date 23:29 2020/5/26 0026
     * @param id 用户ID
     * @return com.wlcb.jpower.module.base.vo.ResponseData
     */
    @RequestMapping(value = "/get",method = RequestMethod.GET,produces="application/json")
    public ResponseData list(String id){
        JpowerAssert.notEmpty(id, JpowerError.Arg,"id不可为空");

        TbCoreUser user = coreUserService.selectUserById(id);
        return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_SUCCESS,"查询成功", user, true);
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 新增一个用户
     * @Date 10:14 2020-05-19
     * @Param [coreUser]
     * @return com.wlcb.jpower.module.base.vo.ResponseData
     **/
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
        Boolean is = coreUserService.add(coreUser);

        if (is){
            return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_SUCCESS,"新增成功", true);
        }else {
            return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_FAIL,"新增失败", false);
        }
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 删除登录用户
     * @Date 11:27 2020-05-19
     * @Param [coreUser]
     * @return com.wlcb.jpower.module.base.vo.ResponseData
     **/
    @Log(title = "删除登录用户",businessType = BusinessType.DELETE)
    @RequestMapping(value = "/delete",method = {RequestMethod.DELETE},produces="application/json")
    public ResponseData delete(String ids){

        JpowerAssert.notEmpty(ids, JpowerError.Arg,"ids不可为空");

        Boolean is = coreUserService.delete(ids);

        if (is){
            return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_SUCCESS,"删除成功", true);
        }else {
            return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_FAIL,"删除失败", false);
        }
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 修改登录用户信息
     * @Date 11:31 2020-05-19
     * @Param [coreUser]
     * @return com.wlcb.jpower.module.base.vo.ResponseData
     **/
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

    /**
     * @author 郭丁志
     * @Description //TODO 重置用户登陆密码
     * @date 1:12 2020/5/24 0024
     * @param ids 用户主键ID
     * @return com.wlcb.jpower.module.base.vo.ResponseData
     */
    @RequestMapping(value = "/resetPassword",method = {RequestMethod.PUT},produces="application/json")
    public ResponseData resetPassword(String ids){

        String pass = DigestUtil.encrypt(MD5.parseStrToMd5U32(ParamConfig.getString(ParamsConstants.USER_DEFAULT_PASSWORD,ConstantsUtils.DEFAULT_USER_PASSWORD)));

        JpowerAssert.notEmpty(ids, JpowerError.Arg,"用户ids不可为空");

        Boolean is = coreUserService.updateUserPassword(ids,pass);

        if (is){
            return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_SUCCESS,ids.split(",").length+"位用户密码重置成功", true);
        }else {
            return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_FAIL,"重置失败", false);
        }
    }

    /**
     * @author 郭丁志
     * @Description //TODO 批量导入用户
     * @date 3:14 2020/5/24 0024
     * @param file 导入文件
     * @return com.wlcb.jpower.module.base.vo.ResponseData
     */
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

    /**
     * @author 郭丁志
     * @Description //TODO 导出用户
     * @date 23:42 2020/5/24 0024
     * @param coreUser
     * @return com.wlcb.jpower.module.base.vo.ResponseData
     */
    @RequestMapping(value = "/exportUser",method = {RequestMethod.GET,RequestMethod.POST},produces="application/json")
    public ResponseData exportUser(TbCoreUser coreUser){
        List<TbCoreUser> list = coreUserService.list(coreUser);

        BeanExcelUtil<TbCoreUser> beanExcelUtil = new BeanExcelUtil<>(TbCoreUser.class,downloadPath);
        return beanExcelUtil.exportExcel(list,"用户列表");
    }

    /**
     * @author 郭丁志
     * @Description //TODO 给用户重新设置角色
     * @date 0:28 2020/5/25 0025
     * @param userIds 用户ID 多个用,分割
     * @param roleIds 角色ID 多个用,分割
     * @return com.wlcb.jpower.module.base.vo.ResponseData
     */
    @RequestMapping(value = "/addRole",method = {RequestMethod.POST},produces="application/json")
    public ResponseData addRole(String userIds,String roleIds){

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

    /**
     * @author 郭丁志
     * @Description //TODO 查询用户所有角色
     * @date 22:48 2020/5/26 0026
     * @param userId 用户ID
     * @return com.wlcb.jpower.module.base.vo.ResponseData
     */
    @RequestMapping(value = "/userRole",method = {RequestMethod.GET},produces="application/json")
    public ResponseData userRole(String userId){

        JpowerAssert.notEmpty(userId, JpowerError.Arg,"用户ID不可为空");

        List<Map<String,Object>> userRoleList = coreUserRoleService.selectUserRoleByUserId(userId);
        return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_SUCCESS,"查询成功",userRoleList, true);
    }

}
