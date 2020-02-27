package com.wlcb.web.corporate.controller;

import com.wlcb.module.base.vo.ResponseData;
import com.wlcb.module.common.service.corporate.CorporateService;
import com.wlcb.module.common.utils.ReturnJsonUtil;
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

}
