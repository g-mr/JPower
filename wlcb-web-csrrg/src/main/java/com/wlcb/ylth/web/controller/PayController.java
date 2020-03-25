package com.wlcb.ylth.web.controller;

import com.github.pagehelper.PageInfo;
import com.wlcb.ylth.module.base.vo.ResponseData;
import com.wlcb.ylth.module.common.service.pay.PayService;
import com.wlcb.ylth.module.common.utils.BeanUtil;
import com.wlcb.ylth.module.common.utils.MD5;
import com.wlcb.ylth.module.common.utils.ReturnJsonUtil;
import com.wlcb.ylth.module.common.utils.constants.Constants;
import com.wlcb.ylth.module.dbs.entity.pay.TblWecharRed;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Calendar;
import java.util.Date;

/**
 * @ClassName PayController
 * @Description TODO 红包入口
 * @Author 郭丁志
 * @Date 2020-03-20 01:13
 * @Version 1.0
 */
@RestController
@RequestMapping("pay")
public class PayController {

    private static final Logger logger = LoggerFactory.getLogger(PayController.class);

    @Resource
    private PayService payService;

    /**
     * @Author 郭丁志
     * @Description //TODO 企业付款到零钱
     * @Date 17:09 2020-03-21
     * @Param [wecharRed, request, response]
     * @return ResponseData
     **/
    @RequestMapping(value = "/paymentChange",method = RequestMethod.POST,produces="application/json")
    public ResponseData paymentChange(TblWecharRed wecharRed,String key, HttpServletRequest request, HttpServletResponse response){

        ResponseData responseData = BeanUtil.allFieldIsNULL(wecharRed,
                "openid",
                "reUserName",
                "price",
                "note");

        if (responseData.getCode() == Constants.RECODE_NULL){
            return responseData;
        }

        if (StringUtils.isNotBlank(wecharRed.getOrderNum())){

            wecharRed = payService.selectDetailByOrderNum(wecharRed.getOrderNum());
            if (wecharRed == null){
                return ReturnJsonUtil.printJson(Constants.RECODE_NOTFOUND,"该订单不存在",false);
            }
        }

        String keyBeforeMd5 = wecharRed.getOpenid()+"&"+wecharRed.getReUserName()+"&"+wecharRed.getPrice()+"&"+wecharRed.getNote()+"csrrgPayment";
        String keyAfterMd5 = MD5.parseStrToMd5U32(keyBeforeMd5);
        if(StringUtils.isBlank(key) || !keyAfterMd5.equals(key)) {
            logger.info("检测到信息被篡改，不可进行企业付款～～～open={}",wecharRed.getOpenid());
            return ReturnJsonUtil.printJson(301,"非法提交",false);
        }

        return payService.paymentChange(wecharRed);
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 通过订单号查付款单
     * @Date 21:38 2020-03-21
     * @Param [orderNum, request, response]
     * @return ResponseData
     **/
    @RequestMapping(value = "/paymentChangeInfo",method = RequestMethod.GET,produces="application/json")
    public ResponseData paymentChangeInfo(String orderNum, HttpServletRequest request, HttpServletResponse response){

        if (StringUtils.isBlank(orderNum)){
            return ReturnJsonUtil.printJson(Constants.RECODE_NULL,"订单号不可为空",false);
        }

        TblWecharRed wecharRed = payService.selectDetailByOrderNum(orderNum);

        if (wecharRed == null){
            return ReturnJsonUtil.printJson(Constants.RECODE_NOTFOUND,"该订单不存在",false);
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_YEAR,-30);
        Date sDate = cal.getTime();

        if (wecharRed.getCreateTime().getTime() < sDate.getTime()){
            return ReturnJsonUtil.printJson(Constants.RECODE_BUSINESS,"只能查询30天之内的订单",false);
        }


        return payService.paymentChangeInfo(wecharRed);
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 查询发送记录
     * @Date 21:38 2020-03-21
     * @Param [orderNum, request, response]
     * @return ResponseData
     **/
    @RequestMapping(value = "/redList",method = {RequestMethod.GET,RequestMethod.POST},produces="application/json")
    public ResponseData redList(TblWecharRed wecharRed, HttpServletRequest request, HttpServletResponse response){

        PageInfo<TblWecharRed> redList = payService.redList(wecharRed);

        return ReturnJsonUtil.printJson(Constants.RECODE_SUCCESS,"查询成功",redList,true);
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 发送包
     * @Date 14:59 2020-03-22
     * @Param [wecharRed, request, response]
     * @return ResponseData
     **/
    @RequestMapping(value = "/redEnvelope",method = RequestMethod.POST,produces="application/json")
    public ResponseData redEnvelope(TblWecharRed wecharRed,String wishing,String actName,String key, HttpServletRequest request, HttpServletResponse response){

        ResponseData responseData = BeanUtil.allFieldIsNULL(wecharRed,
                "openid",
                "reUserName",
                "price",
                "note");

        if (responseData.getCode() == Constants.RECODE_NULL){
            return responseData;
        }

        if (StringUtils.isBlank(wishing)){
            return ReturnJsonUtil.printJson(Constants.RECODE_NULL,"红包祝福语不可为空",false);
        }

        if (StringUtils.isBlank(actName)){
            return ReturnJsonUtil.printJson(Constants.RECODE_NULL,"活动名称不可为空",false);
        }

        if (StringUtils.isNotBlank(wecharRed.getOrderNum())){

            wecharRed = payService.selectDetailByOrderNum(wecharRed.getOrderNum());
            if (wecharRed == null){
                return ReturnJsonUtil.printJson(Constants.RECODE_NOTFOUND,"该订单不存在",false);
            }
        }


        String keyBeforeMd5 = wecharRed.getOpenid()+"&"+wecharRed.getReUserName()+"&"+wecharRed.getPrice()+"&"+wecharRed.getNote()+"csrrgRed";
        String keyAfterMd5 = MD5.parseStrToMd5U32(keyBeforeMd5);
        if(StringUtils.isBlank(key) || !keyAfterMd5.equals(key)) {
            logger.info("检测到信息被篡改，不可发送现金红包～～～open={}",wecharRed.getOpenid());
            return ReturnJsonUtil.printJson(301,"非法提交",false);
        }

        return payService.redEnvelope(wecharRed,wishing,actName);
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 查询红包发送详情
     * @Date 21:38 2020-03-21
     * @Param [orderNum, request, response]
     * @return ResponseData
     **/
    @RequestMapping(value = "/getRedInfo",method = RequestMethod.GET,produces="application/json")
    public ResponseData getRedInfo(String orderNum, HttpServletRequest request, HttpServletResponse response){

        if (StringUtils.isBlank(orderNum)){
            return ReturnJsonUtil.printJson(Constants.RECODE_NULL,"订单号不可为空",false);
        }

        TblWecharRed wecharRed = payService.selectDetailByOrderNum(orderNum);

        if (wecharRed == null){
            return ReturnJsonUtil.printJson(Constants.RECODE_NOTFOUND,"该订单不存在",false);
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_YEAR,-30);
        Date sDate = cal.getTime();

        if (wecharRed.getCreateTime().getTime() < sDate.getTime()){
            return ReturnJsonUtil.printJson(Constants.RECODE_BUSINESS,"只能查询30天之内的订单",false);
        }


        return payService.getRedInfo(wecharRed);
    }

}
