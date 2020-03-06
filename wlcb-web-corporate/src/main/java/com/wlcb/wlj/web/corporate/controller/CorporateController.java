package com.wlcb.wlj.web.corporate.controller;

import com.alibaba.fastjson.JSONObject;
import com.wlcb.wlj.module.base.vo.ResponseData;
import com.wlcb.wlj.module.common.service.corporate.CorporateService;
import com.wlcb.wlj.module.common.utils.*;
import com.wlcb.wlj.module.dbs.entity.base.PageBean;
import com.wlcb.wlj.module.dbs.entity.corporate.TblCsrrgCorporate;
import com.wlcb.wlj.module.dbs.entity.corporate.TblCsrrgCorporateReview;
import com.wlcb.wlj.module.dbs.entity.corporate.TblCsrrgLog;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @ClassName TiwenController
 * @Description TODO 体温调用入口Controller
 * @Author 郭丁志
 * @Date 2020-02-13 14:10
 * @Version 1.0
 */
@RestController
@RequestMapping("corporate")
public class CorporateController {

    private static final Logger logger = LoggerFactory.getLogger(CorporateController.class);

    @Resource
    private CorporateService corporateService;

    /**
     * @Author 郭丁志
     * @Description //TODO 文件保存路径
     * @Date 20:46 2020-02-27
     * @Param
     * @return
     **/
    @Value("${fileParentPath}")
    private String fileParentPath;
    /**
     * @Author 郭丁志
     * @Description //TODO 支持上传的文件类型；用,分隔
     * @Date 21:09 2020-02-27
     * @Param
     * @return
     **/
    @Value("${fileSuffixName}")
    private String fileSuffixName;

    @RequestMapping(value = "/queryEnterpriseName",method = RequestMethod.POST,produces="application/json")
    public ResponseData queryEnterpriseName(String name, HttpServletRequest request, HttpServletResponse response){

        if(StringUtils.isBlank(name)){
            return ReturnJsonUtil.printJson(-1,"参数不合法",false);
        }

        List<Map<String,String>> list = corporateService.queryEnterpriseName(name);

        return ReturnJsonUtil.printJson(0,"查询成功",list,true);
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 根据法人身份证号码查询企业
     * @Date 21:57 2020-03-02
     * @Param [idcard, request, response]
     * @return com.wlcb.wlj.module.base.vo.ResponseData
     **/
    @RequestMapping(value = "/queryDetailByIdcard",method = RequestMethod.GET,produces="application/json")
    public ResponseData queryDetailByIdcard(String idcard, HttpServletRequest request, HttpServletResponse response){

        if(StringUtils.isBlank(idcard)){
            return ReturnJsonUtil.printJson(-1,"身份证号不可为空",false);
        }

        List<TblCsrrgCorporate> list = corporateService.queryDetailByIdcard(idcard);

        return ReturnJsonUtil.printJson(0,"查询成功",list,true);
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 查询有多少企业绑定了联系人
     * @Date 21:57 2020-03-02
     * @Param [idcard, request, response]
     * @return com.wlcb.wlj.module.base.vo.ResponseData
     **/
    @RequestMapping(value = "/countCorporateByRecord",method = RequestMethod.GET,produces="application/json")
    public ResponseData countCorporateByRecord(String quxian, HttpServletRequest request, HttpServletResponse response){

        String count = corporateService.countCorporateByRecord(quxian);

        return ReturnJsonUtil.printJson(0,"查询成功", JSONObject.parseObject(count),true);
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 查询有多少企业申请了复工
     * @Date 15:24 2020-03-06
     * @Param [quxian, request, response]
     * @return com.wlcb.wlj.module.base.vo.ResponseData
     **/
    @RequestMapping(value = "/countCorporateByRework",method = RequestMethod.GET,produces="application/json")
    public ResponseData countCorporateByRework(String quxian, HttpServletRequest request, HttpServletResponse response){

        String count = corporateService.countCorporateByRework(quxian);

        return ReturnJsonUtil.printJson(0,"查询成功", JSONObject.parseObject(count),true);
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 新增公司法人
     * @Date 21:57 2020-03-02
     * @Param [corporateReview, request, response]
     * @return com.wlcb.wlj.module.base.vo.ResponseData
     **/
    @RequestMapping(value = "/addCorporate",method = RequestMethod.POST,produces="application/json")
    public ResponseData addCorporate(TblCsrrgCorporateReview corporateReview, MultipartFile file, HttpServletRequest request, HttpServletResponse response){

        ResponseData responseData = BeanUtil.allFieldIsNULL(corporateReview,
                "organizationCode",
                "enterpriseName",
                "area",
                "enterpriseType",
                "legalPerson",
                "legalPhone",
                "liaisonName",
                "liaisonPhone",
                "enterpriseAuthority",
                "enterprisePhone",
                "registeredMoney",
                "enterpriseRange",
                "registeredDate",
                "legalIdcard",
                "liaisonIdcard");
        if (responseData.getCode() == -1){
            return responseData;
        }

        if(!StrUtil.isPhone(corporateReview.getLegalPhone())){
            return ReturnJsonUtil.printJson(-1,"legalPhone不合法",false);
        }

        if(!StrUtil.isPhone(corporateReview.getEnterprisePhone())){
            return ReturnJsonUtil.printJson(-1,"enterprisePhone不合法",false);
        }

        if(!StrUtil.isPhone(corporateReview.getLiaisonPhone())){
            return ReturnJsonUtil.printJson(-1,"liaisonPhone不合法",false);
        }

        if(!StrUtil.cardCodeVerifySimple(corporateReview.getLegalIdcard())){
            return ReturnJsonUtil.printJson(-1,"legalIdcard不合法",false);
        }

        if(!StrUtil.cardCodeVerifySimple(corporateReview.getLiaisonIdcard())){
            return ReturnJsonUtil.printJson(-1,"liaisonIdcar不合法",false);
        }

        if (file==null || file.isEmpty()){
            return ReturnJsonUtil.printJson(-1,"营业执照不可为空",false);
        }

        try {
            String fileName = UUIDUtil.getUUID();
            //获得文件后缀名
            String suffixName=file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);

            if (!StringUtils.containsIgnoreCase(fileSuffixName,suffixName)){
                return ReturnJsonUtil.printJson(-1,"文件类型不符，请上传"+fileSuffixName+"文件类型",false);
            }

            String imgPath = "corporatefile" + File.separator + DateUtils.getDate(new Date(), "yyyy-MM-dd") + File.separator + fileName + "." + suffixName;

            File imgFile = new File(fileParentPath+File.separator+imgPath);

            if(!imgFile.getParentFile().exists()){
                imgFile.getParentFile().mkdirs();
            }

            file.transferTo(imgFile);

            corporateReview.setAuthorityFile(imgPath);
            logger.info("文件保存成功，文件路径={}",imgFile.getAbsolutePath());
        } catch (IOException e) {
            logger.error("营业执照保存出错：{}",e.getMessage());
            return ReturnJsonUtil.printJson(-1,"系统出错，稍后重试",false);
        }

        Integer i = corporateService.countCorporateByReview(corporateReview.getOrganizationCode(),corporateReview.getEnterpriseName());
        if (i > 0){
            return ReturnJsonUtil.printJson(2,"该公司已提交申请，不可重复提交",false);
        }

        Integer count = corporateService.addCorporateReview(corporateReview);

        if(count > 0){
            return ReturnJsonUtil.printJson(0,"保存成功",true);
        }else{
            return ReturnJsonUtil.printJson(-1,"保存失败",false);
        }

    }

    /**
     * @Author 郭丁志
     * @Description //TODO 修改公司审核状态
     * @Date 21:57 2020-03-02
     * @Param [corporateReview, request, response]
     * @return com.wlcb.wlj.module.base.vo.ResponseData
     **/
    @RequestMapping(value = "/updateStatus",method = RequestMethod.PUT,produces="application/json")
    public ResponseData updateStatus(TblCsrrgLog log, String reason, HttpServletRequest request, HttpServletResponse response){

        ResponseData responseData = BeanUtil.allFieldIsNULL(log,
                "keyId",
                "userId",
                "name",
                "status");
        if (responseData.getCode() == -1){
            return responseData;
        }

        if (log.getStatus()<1||log.getStatus()>3){
            return ReturnJsonUtil.printJson(-1,"status参数不合法",false);
        }

        Integer count = corporateService.updateStatus(log,reason);

        if(count > 0){
            return ReturnJsonUtil.printJson(0,"更新成功",true);
        }else{
            return ReturnJsonUtil.printJson(-1,"更新失败",false);
        }
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 查询公司法人审核列表
     * @Date 21:57 2020-03-02
     * @Param [corporateReview, request, response]
     * @return com.wlcb.wlj.module.base.vo.ResponseData
     **/
    @RequestMapping(value = "/listPage",method = RequestMethod.GET,produces="application/json")
    public ResponseData listPage(TblCsrrgCorporateReview corporateReview, HttpServletRequest request, HttpServletResponse response){

        PageBean<TblCsrrgCorporateReview> corporateReviews = corporateService.listPage(corporateReview);

        return ReturnJsonUtil.printJson(0,"查询成功",corporateReviews,true);
    }

}
