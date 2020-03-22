package com.wlcb.wlj.web.corporate.controller;

import com.alibaba.fastjson.JSONObject;
import com.wlcb.wlj.module.base.vo.ResponseData;
import com.wlcb.wlj.module.common.service.corporate.CorporateService;
import com.wlcb.wlj.module.common.service.corporate.RecordService;
import com.wlcb.wlj.module.common.utils.*;
import com.wlcb.wlj.module.dbs.entity.base.PageBean;
import com.wlcb.wlj.module.dbs.entity.corporate.*;
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
@RequestMapping("kakou")
public class KakouController {

    private static final Logger logger = LoggerFactory.getLogger(KakouController.class);

    @Resource
    private CorporateService corporateService;
    @Resource
    private RecordService recordService;

    @RequestMapping(value = "/list",method = {RequestMethod.GET,RequestMethod.POST},produces="application/json")
    public ResponseData list(TblCsrrgCorporateKakou kakou, HttpServletRequest request, HttpServletResponse response){

        if (StringUtils.isNotBlank(kakou.getQuxian())){
            kakou.setQuxian(QuxianUtils.simplifyQuxian(kakou.getQuxian()));
        }

        PageBean<Map<String, String>> list = corporateService.listKakou(kakou);

        return ReturnJsonUtil.printJson(200,"查询成功",list,true);
    }

    @RequestMapping(value = "/add",method = RequestMethod.POST,produces="application/json")
    public ResponseData add(TblCsrrgCorporateKakou kakou, HttpServletRequest request, HttpServletResponse response){

        ResponseData responseData = BeanUtil.allFieldIsNULL(kakou,
                "corporateId",
                "kakouId",
                "recordId");

        if (responseData.getCode() == 406){
            return responseData;
        }

        TblCsrrgCorporate csrrgCorporate = corporateService.selectById(kakou.getCorporateId());
        if (csrrgCorporate == null){
            return ReturnJsonUtil.printJson(404,"企业不存在",false);
        }

        TblCsrrgRecord record = recordService.detail(kakou.getRecordId());
        if (record == null){
            return ReturnJsonUtil.printJson(404,"联系人不存在",false);
        }
        kakou.setRecordOpenid(record.getApplicantOpenid());

        Integer c = corporateService.selectKakouById(kakou.getKakouId());
        if (c<=0){
            return ReturnJsonUtil.printJson(404,"卡口不存在",false);
        }

        Integer count = corporateService.addKakou(kakou);

        if (count > 0){
            return ReturnJsonUtil.printJson(201,"新增成功",true);
        }else{
            return ReturnJsonUtil.printJson(400,"新增失败",false);
        }

    }

    @RequestMapping(value = "/update",method = RequestMethod.PUT,produces="application/json")
    public ResponseData update(TblCsrrgLog log,String refuseReason, HttpServletRequest request, HttpServletResponse response){

        ResponseData responseData = BeanUtil.allFieldIsNULL(log,
                "keyId",
                "userId",
                "name",
                "status");

        if (responseData.getCode() == 406){
            return responseData;
        }

        Integer count = corporateService.updateKakouStatus(log,refuseReason);

        if (count > 0){
            return ReturnJsonUtil.printJson(201,"更新成功",true);
        }else{
            return ReturnJsonUtil.printJson(400,"更新失败",false);
        }
    }

    @RequestMapping(value = "/delete",method = RequestMethod.DELETE,produces="application/json")
    public ResponseData delete(String id, HttpServletRequest request, HttpServletResponse response){

        if (StringUtils.isBlank(id)){
            return ReturnJsonUtil.printJson(406,"id不可为空",false);
        }

        Integer count = corporateService.deleteKakou(id);

        if (count > 0){
            return ReturnJsonUtil.printJson(201,"删除成功",true);
        }else{
            return ReturnJsonUtil.printJson(400,"删除失败",false);
        }
    }

}
