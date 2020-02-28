package com.wlcb.wlj.web.corporate.controller;

import com.wlcb.wlj.module.base.vo.ResponseData;
import com.wlcb.wlj.module.common.service.corporate.CorporateService;
import com.wlcb.wlj.module.common.service.corporate.RecordService;
import com.wlcb.wlj.module.common.service.corporate.ReworkService;
import com.wlcb.wlj.module.common.utils.DateUtils;
import com.wlcb.wlj.module.common.utils.ReturnJsonUtil;
import com.wlcb.wlj.module.common.utils.UUIDUtil;
import com.wlcb.wlj.module.common.utils.constants.ConstantsEnum;
import com.wlcb.wlj.module.dbs.entity.base.PageBean;
import com.wlcb.wlj.module.dbs.entity.corporate.TblCsrrgCorporate;
import com.wlcb.wlj.module.dbs.entity.corporate.TblCsrrgLog;
import com.wlcb.wlj.module.dbs.entity.corporate.TblCsrrgRework;
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
import java.util.Date;

/**
 * @ClassName ReworkController
 * @Description TODO 复工调用入口Controller
 * @Author 郭丁志
 * @Date 2020-02-13 14:10
 * @Version 1.0
 */
@RestController
@RequestMapping("rework")
public class ReworkController {

    private static final Logger logger = LoggerFactory.getLogger(ReworkController.class);

    @Resource
    private ReworkService reworkService;

    @Resource
    private CorporateService corporateService;

    @Resource
    private RecordService recordService;

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

    /**
     * @Author 郭丁志
     * @Description //TODO 查询申请列表
     * @Date 21:52 2020-02-27
     * @Param [record, request, response]
     * @return ResponseData
     **/
    @RequestMapping(value = "/list",method = RequestMethod.POST,produces="application/json")
    public ResponseData list(String corporateId, HttpServletRequest request, HttpServletResponse response){

        if (StringUtils.isBlank(corporateId)){
            return ReturnJsonUtil.printJson(-1,"企业ID不可为空",false);
        }

        PageBean<TblCsrrgRework> pageBean = reworkService.listPage(corporateId);

        return ReturnJsonUtil.printJson(0,"查询成功",pageBean,true);
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 查询复工详情
     * @Date 23:11 2020-02-28
     * @Param [corporateId, request, response]
     * @return ResponseData
     **/
    @RequestMapping(value = "/detail",method = RequestMethod.POST,produces="application/json")
    public ResponseData detail(String id, HttpServletRequest request, HttpServletResponse response){

        TblCsrrgRework rework = reworkService.detail(id);

        return ReturnJsonUtil.printJson(0,"查询成功",rework,true);
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 更新复工审核状态
     * @Date 23:11 2020-02-28
     * @Param [log, request, response]
     * @return ResponseData
     **/
    @RequestMapping(value = "/updateReworkStatus",method = RequestMethod.POST,produces="application/json")
    public ResponseData updateReworkStatus(TblCsrrgLog log,String refuseReason, HttpServletRequest request, HttpServletResponse response){

        if (StringUtils.isBlank(log.getOpenid())){
            return ReturnJsonUtil.printJson(-1,"操作人OPENID不可为空",false);
        }

        if (StringUtils.isBlank(log.getUserId())){
            return ReturnJsonUtil.printJson(-1,"操作人ID不可为空",false);
        }

        if (StringUtils.isBlank(log.getName())){
            return ReturnJsonUtil.printJson(-1,"操作人姓名不可为空",false);
        }

        if (StringUtils.isBlank(log.getKeyId())){
            return ReturnJsonUtil.printJson(-1,"复工记录ID不可为空",false);
        }

        if (log.getStatus() == null){
            return ReturnJsonUtil.printJson(-1,"复工审核状态不可为空",false);
        }

        if (!ConstantsEnum.APPLICANT_STATUS.SUCCESS.getValue().equals(log.getStatus()) && !ConstantsEnum.APPLICANT_STATUS.FAIL.getValue().equals(log.getStatus())){
            return ReturnJsonUtil.printJson(-1,"复工审核状态不合法",false);
        }

        Integer count  = reworkService.updateReworkStatus(log,refuseReason);

        if (count > 0){
            return ReturnJsonUtil.printJson(0,"更新成功",true);
        }else{
            return ReturnJsonUtil.printJson(-1,"更新失败",false);
        }

    }

    /**
     * @Author 郭丁志
     * @Description //TODO 新增复工申请
     * @Date 23:11 2020-02-28
     * @Param [corporateId, request, response]
     * @return ResponseData
     **/
    @RequestMapping(value = "/addRework",method = RequestMethod.POST,produces="application/json")
    public ResponseData addRework(TblCsrrgRework rework, MultipartFile planFile, MultipartFile committedFile, HttpServletRequest request, HttpServletResponse response){

        if (rework.getCorporatePeopleSum() == null){
            return ReturnJsonUtil.printJson(-1,"企业人数不可为空",false);
        }

        if (rework.getCorporateRepSum() == null){
            return ReturnJsonUtil.printJson(-1,"外地返工人数不可为空",false);
        }

        if (StringUtils.isBlank(rework.getCorporateId())){
            return ReturnJsonUtil.printJson(-1,"企业ID不可为空",false);
        }

        if (StringUtils.isBlank(rework.getOpenid())){
            return ReturnJsonUtil.printJson(-1,"openid不可为空",false);
        }

        if (planFile==null || planFile.isEmpty()){
            return ReturnJsonUtil.printJson(-1,"企业防疫预案文件不可为空",false);
        }

        if (committedFile==null || committedFile.isEmpty()){
            return ReturnJsonUtil.printJson(-1,"承诺书文件不可为空",false);
        }

        TblCsrrgCorporate csrrgCorporate = corporateService.selectById(rework.getCorporateId());
        if (csrrgCorporate == null){
            logger.info("该企业不存在，不可申请复工：{}",rework.getCorporateId());
            return ReturnJsonUtil.printJson(-1,"未查找到该企业："+rework.getCorporateId(),false);
        }

        Integer i = recordService.selectCountByCidAndOid(rework.getOpenid(),rework.getCorporateId());
        if (i <= 0){
            logger.info("该openId未关联该企业，不可申请复工：企业ID={}，openID={}",rework.getCorporateId(),rework.getOpenid());
            return ReturnJsonUtil.printJson(-1,"该openId未关联该企业，不可申请",false);
        }

        try {
            String planPath = saveFile(planFile);
            if (StringUtils.equals(planPath,"-1")){
                return ReturnJsonUtil.printJson(-1,"企业防疫预案文件类型不符，请上传"+fileSuffixName+"文件类型",false);
            }
            rework.setPlanPath(planPath);

            String committedPath = saveFile(committedFile);
            if (StringUtils.equals(committedPath,"-1")){
                return ReturnJsonUtil.printJson(-1,"承诺书文件类型不符，请上传"+fileSuffixName+"文件类型",false);
            }
            rework.setCommittedPath(committedPath);
        }catch (Exception e){
            logger.error("文件上传出错：{}",e.getMessage());
        }

        Integer count = reworkService.addRework(rework);

        if (count > 0){
            return ReturnJsonUtil.printJson(0,"新增成功",true);
        }else {
            return ReturnJsonUtil.printJson(-1,"新增失败",false);
        }
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 上传文件
     * @Date 23:50 2020-02-28
     * @Param [file]
     * @return java.lang.String
     **/
    private String saveFile(MultipartFile file) throws Exception{
        String fileName = UUIDUtil.getUUID();
        //获得文件后缀名
        String suffixName=file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);

        if (!StringUtils.containsIgnoreCase(fileSuffixName,suffixName)){
            return "-1";
        }

        String imgPath = "reworkfile" + File.separator + DateUtils.getDate(new Date(), "yyyy-MM-dd") + File.separator + fileName + "." + suffixName;

        File imgFile = new File(fileParentPath+File.separator+imgPath);

        if(!imgFile.getParentFile().exists()){
            imgFile.getParentFile().mkdirs();
        }

        file.transferTo(imgFile);

        logger.info("文件保存成，文件路径={}",imgFile.getAbsolutePath());

        return imgPath;
    }


    /**
     * @Author 郭丁志
     * @Description //TODO 报错接口，测试日志
     * @Date 23:11 2020-02-28
     * @Param [corporateId, request, response]
     * @return ResponseData
     **/
    @RequestMapping(value = "/ok",method = RequestMethod.GET,produces="application/json")
    public ResponseData ok(HttpServletRequest request, HttpServletResponse response){

        int a = Integer.parseInt("报错吧");

        return ReturnJsonUtil.printJson(0,"查询成功",true);
    }

}
