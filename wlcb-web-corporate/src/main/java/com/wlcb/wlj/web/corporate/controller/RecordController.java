package com.wlcb.wlj.web.corporate.controller;

import com.alibaba.fastjson.JSONObject;
import com.wlcb.wlj.module.base.vo.ResponseData;
import com.wlcb.wlj.module.common.service.corporate.CorporateService;
import com.wlcb.wlj.module.common.service.corporate.RecordService;
import com.wlcb.wlj.module.common.utils.DateUtils;
import com.wlcb.wlj.module.common.utils.ReturnJsonUtil;
import com.wlcb.wlj.module.common.utils.UUIDUtil;
import com.wlcb.wlj.module.common.utils.constants.ConstantsEnum;
import com.wlcb.wlj.module.dbs.entity.base.PageBean;
import com.wlcb.wlj.module.dbs.entity.corporate.TblCsrrgCorporate;
import com.wlcb.wlj.module.dbs.entity.corporate.TblCsrrgLog;
import com.wlcb.wlj.module.dbs.entity.corporate.TblCsrrgRecord;
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

/**
 * @ClassName TiwenController
 * @Description TODO 体温调用入口Controller
 * @Author 郭丁志
 * @Date 2020-02-13 14:10
 * @Version 1.0
 */
@RestController
@RequestMapping("record")
public class RecordController {

    private static final Logger logger = LoggerFactory.getLogger(RecordController.class);

    @Resource
    private RecordService recordService;
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

    /**
     * @Author 郭丁志
     * @Description //TODO 查询申请列表
     * @Date 21:52 2020-02-27
     * @Param [record, request, response]
     * @return ResponseData
     **/
    @RequestMapping(value = "/list",method = RequestMethod.POST,produces="application/json")
    public ResponseData list(TblCsrrgRecord record, HttpServletRequest request, HttpServletResponse response){

        PageBean<TblCsrrgRecord> pageBean = recordService.listPage(record);

        return ReturnJsonUtil.printJson(0,"查询成功",pageBean,true);
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 修改申请状态
     * @Date 21:56 2020-02-27
     * @Param [recordLog, request, response]
     * @return ResponseData
     **/
    @RequestMapping(value = "/updateRecordStatus",method = RequestMethod.PUT,produces="application/json")
    public ResponseData updateRecordStatus(TblCsrrgLog recordLog, String failReason, HttpServletRequest request, HttpServletResponse response){

//        if (StringUtils.isBlank(recordLog.getOpenid())){
//            return ReturnJsonUtil.printJson(-1,"操作人OPENID不可为空",false);
//        }

        if (StringUtils.isBlank(recordLog.getUserId())){
            return ReturnJsonUtil.printJson(-1,"操作人ID不可为空",false);
        }

        if (StringUtils.isBlank(recordLog.getName())){
            return ReturnJsonUtil.printJson(-1,"操作人姓名不可为空",false);
        }

        if (StringUtils.isBlank(recordLog.getKeyId())){
            return ReturnJsonUtil.printJson(-1,"申请记录ID不可为空",false);
        }

        if (recordLog.getStatus() == null){
            return ReturnJsonUtil.printJson(-1,"申请记录状态不可为空",false);
        }

        if (!ConstantsEnum.APPLICANT_STATUS.SUCCESS.getValue().equals(recordLog.getStatus()) && !ConstantsEnum.APPLICANT_STATUS.FAIL.getValue().equals(recordLog.getStatus())&& !ConstantsEnum.APPLICANT_STATUS.CONFIRM.getValue().equals(recordLog.getStatus())){
            return ReturnJsonUtil.printJson(-1,"申请记录状态不合法",false);
        }

        Integer count = recordService.updateRecordStatus(recordLog,failReason);

        if (count > 0){
            return ReturnJsonUtil.printJson(0,"修改成功",true);
        }else{
            return ReturnJsonUtil.printJson(-2,"修改失败",true);
        }

    }

    /**
     * @Author 郭丁志
     * @Description //TODO 查询详情
     * @Date 21:56 2020-02-27
     * @Param [id, request, response]
     * @return ResponseData
     **/
    @RequestMapping(value = "/detail",method = RequestMethod.POST,produces="application/json")
    public ResponseData detail(String id, HttpServletRequest request, HttpServletResponse response){

        if (StringUtils.isBlank(id)){
            return ReturnJsonUtil.printJson(-1,"参数不合法",false);
        }

        TblCsrrgRecord record = recordService.detail(id);

        return ReturnJsonUtil.printJson(0,"查询成功",record,true);
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 新增申请记录
     * @Date 21:52 2020-02-27
     * @Param [record, file, request, response]
     * @return ResponseData
     **/
    @RequestMapping(value = "/addRecord",method = RequestMethod.POST,produces="application/json")
    public ResponseData addRecord(TblCsrrgRecord record, MultipartFile file, HttpServletRequest request, HttpServletResponse response){


        if(StringUtils.isBlank(record.getApplicantName())){
            return ReturnJsonUtil.printJson(-1,"姓名不可空",false);
        }

        if(StringUtils.isBlank(record.getApplicantIdcard())){
            return ReturnJsonUtil.printJson(-1,"身份证号不可空",false);
        }

        if(StringUtils.isBlank(record.getEnterpriseName())){
            return ReturnJsonUtil.printJson(-1,"企业名称不可空",false);
        }

        if(StringUtils.isBlank(record.getApplicantOpenid())){
            return ReturnJsonUtil.printJson(-1,"applicantOpenid不可为空",false);
        }

        if(StringUtils.isBlank(record.getApplicantPhone())){
            return ReturnJsonUtil.printJson(-1,"电话不可为空",false);
        }

        if(StringUtils.isBlank(record.getCorporateId())){
            return ReturnJsonUtil.printJson(-1,"公司ID不可为空",false);
        }

        if (file!=null && !file.isEmpty()){
            try {
                String fileName = UUIDUtil.getUUID();
                //获得文件后缀名
                String suffixName=file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);

                if (!StringUtils.containsIgnoreCase(fileSuffixName,suffixName)){
                    return ReturnJsonUtil.printJson(-1,"文件类型不符，请上传"+fileSuffixName+"文件类型",false);
                }

                String imgPath = "recodefile" + File.separator + DateUtils.getDate(new Date(), "yyyy-MM-dd") + File.separator + fileName + "." + suffixName;

                File imgFile = new File(fileParentPath+File.separator+imgPath);

                if(!imgFile.getParentFile().exists()){
                    imgFile.getParentFile().mkdirs();
                }

                file.transferTo(imgFile);

                record.setFilePath(imgPath);
                logger.info("文件保存成，文件路径={}",imgFile.getAbsolutePath());
            } catch (IOException e) {
                logger.error("委托书保存出错：{}",e.getMessage());
                return ReturnJsonUtil.printJson(-1,"系统出错，稍后重试",false);
            }

            if(StringUtils.isBlank(record.getCorporateId())){
                return ReturnJsonUtil.printJson(-1,"企业ID不可为空",false);
            }

            TblCsrrgCorporate csrrgCorporate = corporateService.selectById(record.getCorporateId());
            if (csrrgCorporate == null){
                return ReturnJsonUtil.printJson(-1,"未查找到该企业："+record.getCorporateId(),false);
            }

            record.setEnterpriseName(csrrgCorporate.getEnterpriseName());

        }

        return recordService.addRecord(record);

    }

}
