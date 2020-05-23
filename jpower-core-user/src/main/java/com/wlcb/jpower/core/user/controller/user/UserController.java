package com.wlcb.jpower.core.user.controller.user;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.wlcb.jpower.module.base.vo.ResponseData;
import com.wlcb.jpower.module.common.controller.BaseController;
import com.wlcb.jpower.module.common.page.PaginationContext;
import com.wlcb.jpower.module.common.service.core.user.CoreUserService;
import com.wlcb.jpower.module.common.utils.BeanUtil;
import com.wlcb.jpower.module.common.utils.ReturnJsonUtil;
import com.wlcb.jpower.module.common.utils.StrUtil;
import com.wlcb.jpower.module.common.utils.constants.ConstantsEnum;
import com.wlcb.jpower.module.common.utils.constants.ConstantsReturn;
import com.wlcb.jpower.module.dbs.entity.core.user.TbCoreUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

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
     * @Author 郭丁志
     * @Description //TODO 新增一个用户
     * @Date 10:14 2020-05-19
     * @Param [coreUser]
     * @return com.wlcb.jpower.module.base.vo.ResponseData
     **/
    @RequestMapping(value = "/add",method = {RequestMethod.POST},produces="application/json")
    public ResponseData add(TbCoreUser coreUser){

        ResponseData responseData = BeanUtil.allFieldIsNULL(coreUser,
                "createUser","loginId","password");

        if (responseData.getCode() == ConstantsReturn.RECODE_NULL){
            return responseData;
        }

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

        Integer count = coreUserService.add(coreUser);

        if (count > 0){
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
    @RequestMapping(value = "/delete",method = {RequestMethod.DELETE},produces="application/json")
    public ResponseData delete(String ids){

        if (StringUtils.isBlank(ids)){
            return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_BUSINESS,"ID不可为空", false);
        }

        Integer count = coreUserService.delete(ids);

        if (count > 0){
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
    @RequestMapping(value = "/update",method = {RequestMethod.PUT},produces="application/json")
    public ResponseData update(TbCoreUser coreUser){

        ResponseData responseData = BeanUtil.allFieldIsNULL(coreUser,
                "updateUser","id");

        if (responseData.getCode() == ConstantsReturn.RECODE_NULL){
            return responseData;
        }

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

        Integer count = coreUserService.update(coreUser);

        if (count > 0){
            return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_SUCCESS,"修改成功", true);
        }else {
            return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_FAIL,"修改失败", false);
        }
    }

}
