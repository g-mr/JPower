package com.wlcb.wlj.web.corporate.controller;

import com.wlcb.wlj.module.base.vo.ResponseData;
import com.wlcb.wlj.module.common.service.corporate.CorporateService;
import com.wlcb.wlj.module.common.utils.ReturnJsonUtil;
import com.wlcb.wlj.module.dbs.entity.corporate.TblCsrrgCorporate;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    public ResponseData countCorporateByRecord( HttpServletRequest request, HttpServletResponse response){

        Integer list = corporateService.countCorporateByRecord();

        return ReturnJsonUtil.printJson(0,"查询成功",list,true);
    }

}
