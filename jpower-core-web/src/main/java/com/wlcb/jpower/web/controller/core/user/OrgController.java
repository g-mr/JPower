package com.wlcb.jpower.web.controller.core.user;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.wlcb.jpower.module.base.enums.JpowerError;
import com.wlcb.jpower.module.base.exception.JpowerAssert;
import com.wlcb.jpower.module.base.vo.ResponseData;
import com.wlcb.jpower.module.common.controller.BaseController;
import com.wlcb.jpower.module.common.page.PaginationContext;
import com.wlcb.jpower.module.common.service.core.user.CoreOrgService;
import com.wlcb.jpower.module.common.utils.BeanUtil;
import com.wlcb.jpower.module.common.utils.ReturnJsonUtil;
import com.wlcb.jpower.module.common.utils.constants.ConstantsReturn;
import com.wlcb.jpower.module.dbs.entity.core.user.TbCoreOrg;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @ClassName OrgController
 * @Description TODO 组织机构相关
 * @Author 郭丁志
 * @Date 2020-02-13 14:10
 * @Version 1.0
 */
@RestController
@RequestMapping("/core/org")
public class OrgController extends BaseController {

    @Resource
    private CoreOrgService coreOrgService;

    /**
     * @Author 郭丁志
     * @Description //TODO 查询组织机构列表
     * @Date 09:41 2020-05-19
     * @Param [coreUser]
     * @return com.wlcb.jpower.module.base.vo.ResponseData
     **/
    @RequestMapping(value = "/listByParent",method = {RequestMethod.GET,RequestMethod.POST},produces="application/json")
    public ResponseData listByParent(TbCoreOrg coreOrg){

        Object json;
        if (StringUtils.isBlank(coreOrg.getParentId()) && StringUtils.isBlank(coreOrg.getParentCode())){
            coreOrg.setParentCode("-1");
            PaginationContext.startPage();
            json = JSON.toJSON(new PageInfo<TbCoreOrg>(coreOrgService.listByParent(coreOrg)));
        }else {
            json = JSON.toJSON(coreOrgService.listByParent(coreOrg));
        }

        return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_SUCCESS,"获取成功", json,true);
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 新增一个组织机构
     * @Date 10:14 2020-05-19
     * @Param [coreUser]
     * @return com.wlcb.jpower.module.base.vo.ResponseData
     **/
    @RequestMapping(value = "/add",method = {RequestMethod.POST},produces="application/json")
    public ResponseData add(TbCoreOrg coreOrg){

        ResponseData responseData = BeanUtil.allFieldIsNULL(coreOrg,
                "name","code");

        if (responseData.getCode() == ConstantsReturn.RECODE_NULL){
            return responseData;
        }

        TbCoreOrg org = coreOrgService.selectOrgByCode(coreOrg.getCode());
        if (org != null){
            return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_BUSINESS,"该组织机构已存在", false);
        }

        if (StringUtils.isBlank(coreOrg.getParentCode())){
            coreOrg.setParentCode("-1");
            coreOrg.setParentId("-1");
        }
        Boolean is = coreOrgService.add(coreOrg);

        if (is){
            return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_SUCCESS,"新增成功", true);
        }else {
            return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_FAIL,"新增失败", false);
        }
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 删除组织机构
     * @Date 11:27 2020-05-19
     * @Param [coreUser]
     * @return com.wlcb.jpower.module.base.vo.ResponseData
     **/
    @RequestMapping(value = "/deleteStatus",method = {RequestMethod.DELETE},produces="application/json")
    public ResponseData deleteStatus(String ids){

        JpowerAssert.notEmpty(ids, JpowerError.Arg,"ids不可为空");

        Integer c = coreOrgService.listOrgByPids(ids);
        if (c > 0){
            return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_BUSINESS,"您选中的组织机构存在下级机构，请先删除下级机构", false);
        }

        Boolean is = coreOrgService.delete(ids);

        if (is){
            return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_SUCCESS,"删除成功", true);
        }else {
            return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_FAIL,"删除失败", false);
        }
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 修改组织机构信息
     * @Date 11:31 2020-05-19
     * @Param [coreUser]
     * @return com.wlcb.jpower.module.base.vo.ResponseData
     **/
    @RequestMapping(value = "/update",method = {RequestMethod.PUT},produces="application/json")
    public ResponseData update(TbCoreOrg coreOrg){

        JpowerAssert.notEmpty(coreOrg.getId(), JpowerError.Arg,"id不可为空");

        if (StringUtils.isNotBlank(coreOrg.getCode())){
            TbCoreOrg org = coreOrgService.selectOrgByCode(coreOrg.getCode());
            if (org != null && !StringUtils.equals(org.getId(),coreOrg.getId())){
                return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_BUSINESS,"该组织机构编码已存在", false);
            }
        }

        Boolean is = coreOrgService.update(coreOrg);

        if (is){
            return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_SUCCESS,"修改成功", true);
        }else {
            return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_FAIL,"修改失败", false);
        }
    }

}
