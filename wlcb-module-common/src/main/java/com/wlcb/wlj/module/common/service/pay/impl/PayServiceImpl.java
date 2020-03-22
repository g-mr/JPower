package com.wlcb.wlj.module.common.service.pay.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wlcb.wlj.module.base.vo.ResponseData;
import com.wlcb.wlj.module.common.page.PaginationContext;
import com.wlcb.wlj.module.common.service.pay.PayService;
import com.wlcb.wlj.module.common.utils.*;
import com.wlcb.wlj.module.common.utils.constants.Constants;
import com.wlcb.wlj.module.common.utils.constants.ConstantsEnum;
import com.wlcb.wlj.module.dbs.dao.pay.TblWecharRedMapper;
import com.wlcb.wlj.module.dbs.entity.pay.TblWecharRed;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author mr.gmac
 */
@Service("payService")
public class PayServiceImpl implements PayService {

    private static final Logger logger = LoggerFactory.getLogger(PayServiceImpl.class);

    /**
     * @Author 郭丁志
     * @Description //TODO 红包支付接口
     * @Date 21:48 2020-03-21
     * @Param
     * @return
     **/
    @Value("${wx.transfers_url}")
    private String transfersUrl;
    /**
     * @Author 郭丁志
     * @Description //TODO 查单接口
     * @Date 21:48 2020-03-21
     **/
    @Value("${wx.transfers_info_url}")
    private String transfersInfoUrl;
    @Value("${wx.redpack_url}")
    private String redpackUrl;
    @Value("${wx.redpack_info_url}")
    private String redpackInfoUrl;
    /**
     * @Author 郭丁志
     * @Description //TODO appid
     * @Date 21:48 2020-03-21
     **/
    @Value("${wx.appid}")
    private String appid;
    /**
     * @Author 郭丁志
     * @Description //TODO 商户号
     * @Date 21:48 2020-03-21
     **/
    @Value("${wx.mchid}")
    private String mchid;
    /**
     * @Author 郭丁志
     * @Description //TODO 密钥key
     * @Date 21:48 2020-03-21
     **/
    @Value("${wx.red_key}")
    private String redKey;
    @Value("${wx.cer_path}")
    private String cerPath;
    @Value("${wx.ip}")
    private String ip;

    @Autowired
    private TblWecharRedMapper redMapper;

    @Override
    public ResponseData paymentChange(TblWecharRed wecharRed){

        if (StringUtils.isBlank(wecharRed.getOrderNum())){
            wecharRed.setOrderNum(StrUtil.createOrderId(mchid));
        }

        Map<String,String> map = new HashMap<>();
        map.put("mch_appid",appid);
        map.put("mchid",mchid);
        map.put("nonce_str", UUIDUtil.getUUID());
        map.put("partner_trade_no",wecharRed.getOrderNum());
        map.put("openid",wecharRed.getOpenid());
        map.put("check_name","FORCE_CHECK");
        map.put("re_user_name",wecharRed.getReUserName());
        map.put("amount",wecharRed.getPrice());
        map.put("desc",wecharRed.getNote());
        map.put("spbill_create_ip",ip);

        String keyValue = StrUtil.getAsciiKeyValue(map)+"key="+redKey;
        map.put("sign", MD5.parseStrToMd5U32(keyValue));

        String xmlStr = StrUtil.mapToXmlStr(map);

        String resPoseStr = HttpClient.requestWithCert(transfersUrl,xmlStr,cerPath,mchid);
            //HttpClient.sendPost(payUrl,xmlStr,cerPath,mchid);

        logger.info("企业付款完成，订单号={}，返回信息={}",wecharRed.getOrderNum(),resPoseStr);

        // 将字符串转为XML
        Document doc = null;
        try {
            doc = DocumentHelper.parseText(resPoseStr);
        } catch (DocumentException e) {
            logger.error("解析XML出错，xml={},error={}",resPoseStr,e.getMessage());
            return ReturnJsonUtil.printJson(Constants.RECODE_ERROR,"微信返回数据解析失败，请检查网络是否通畅",false);
        }
        // 获取根节点
        Element rootElt = doc.getRootElement();
        wecharRed.setReturnCode(rootElt.elementTextTrim("return_code"));
        wecharRed.setReturnMsg(rootElt.elementTextTrim("return_msg"));
        wecharRed.setResultCode(rootElt.elementTextTrim("result_code"));
        wecharRed.setErrCode(rootElt.elementTextTrim("err_code"));
        wecharRed.setErrCodeDes(rootElt.elementTextTrim("err_code_des"));
        wecharRed.setWxPaymentNo(rootElt.elementTextTrim("payment_no"));
        wecharRed.setWxPaymentTime(rootElt.elementTextTrim("payment_time"));
        wecharRed.setIp(ip);
        wecharRed.setId(UUIDUtil.getUUID());
        wecharRed.setPayType(1);

        Integer count = redMapper.inster(wecharRed);

        wecharRed.setIp("");

        if (StringUtils.equals(wecharRed.getReturnCode(),ConstantsEnum.WECHAR_RED_CODE.SUCCESS.getValue())){
            if (StringUtils.equals(wecharRed.getResultCode(),ConstantsEnum.WECHAR_RED_CODE.SUCCESS.getValue())){
                return ReturnJsonUtil.printJson(Constants.RECODE_SUCCESS,"发送成功",wecharRed,true);
            }else{
                logger.info("企业付款发送错误,params={}", JSON.toJSONString(wecharRed));
                return ReturnJsonUtil.printJson(Constants.RECODE_BUSINESS,ConstantsEnum.WECHAR_RED_CODE.getName(wecharRed.getErrCode()),wecharRed,false);
            }
        }else{
            logger.info("企业付款失败，orderNUM={},err_msg={}",wecharRed.getOrderNum(),wecharRed.getReturnMsg());
            return ReturnJsonUtil.printJson(Constants.RECODE_ERROR,"微信接口调用失败",wecharRed,false);
        }
    }

    @Override
    public ResponseData paymentChangeInfo(TblWecharRed wecharRed){
        Map<String,String> map = new HashMap<>();
        map.put("nonce_str", UUIDUtil.getUUID());
        map.put("partner_trade_no",wecharRed.getOrderNum());
        map.put("mch_id",mchid);
        map.put("appid",appid);

        String keyValue = StrUtil.getAsciiKeyValue(map)+"key="+redKey;
        map.put("sign",MD5.parseStrToMd5U32(keyValue));

        String xmlStr = StrUtil.mapToXmlStr(map);
        String resPoseStr = HttpClient.requestWithCert(transfersInfoUrl,xmlStr,cerPath,mchid);

        logger.info("查单请求完成,订单号={}，返回信息={}",wecharRed.getOrderNum(),resPoseStr);

        // 将字符串转为XML
        Document doc = null;
        try {
            doc = DocumentHelper.parseText(resPoseStr);
        } catch (DocumentException e) {
            logger.error("解析XML出错，xml={},error={}",resPoseStr,e.getMessage());
            return ReturnJsonUtil.printJson(Constants.RECODE_ERROR,"微信返回数据解析失败，请检查网络是否通畅",false);
        }

        // 获取根节点
        Element rootElt = doc.getRootElement();
        if(StringUtils.equals(rootElt.elementTextTrim("return_code"),ConstantsEnum.WECHAR_RED_CODE.SUCCESS.getValue())){
            wecharRed.setResultCode(rootElt.elementTextTrim("result_code"));
            wecharRed.setErrCode(rootElt.elementTextTrim("err_code"));
            wecharRed.setErrCodeDes(rootElt.elementTextTrim("err_code_des"));
            wecharRed.setWxPaymentNo(rootElt.elementTextTrim("payment_no"));
            wecharRed.setWxPaymentTime(rootElt.elementTextTrim("payment_time"));

            Integer count = redMapper.updateByOrderNum(wecharRed);

            return ReturnJsonUtil.printJson(Constants.RECODE_SUCCESS,"查询成功",wecharRed,true);

        }else {
            logger.info("查询企业付款单失败，orderNUM={},err_msg={}",wecharRed.getOrderNum(),rootElt.elementTextTrim("return_msg"));
            return ReturnJsonUtil.printJson(Constants.RECODE_ERROR,"微信接口调用失败",false);
        }

    }

    @Override
    public TblWecharRed selectDetailByOrderNum(String orderNum) {
        return redMapper.selectByOrderNum(orderNum);
    }

    @Override
    public PageInfo<TblWecharRed> redList(TblWecharRed wecharRed) {
        PageHelper.startPage(PaginationContext.getPageNum(), PaginationContext.getPageSize());
        List<TblWecharRed> list = redMapper.listAll(wecharRed);
        return new PageInfo<>(list);
    }

    @Override
    public ResponseData redEnvelope(TblWecharRed wecharRed, String wishing, String actName) {
        if (StringUtils.isBlank(wecharRed.getOrderNum())){
            wecharRed.setOrderNum(StrUtil.createOrderId(mchid));
        }

        Map<String,String> map = new HashMap<>();
        map.put("nonce_str", UUIDUtil.getUUID());
        map.put("mch_billno",wecharRed.getOrderNum());
        map.put("mch_id",mchid);
        map.put("wxappid",appid);
        map.put("send_name","城市人人管");
        map.put("re_openid",wecharRed.getOpenid());
        map.put("total_amount",wecharRed.getPrice());
        map.put("total_num","1");
        map.put("wishing",wishing);
        map.put("client_ip",ip);
        map.put("act_name",actName);
        map.put("remark",wecharRed.getNote());


        String keyValue = StrUtil.getAsciiKeyValue(map)+"key="+redKey;
        map.put("sign", MD5.parseStrToMd5U32(keyValue));
//        try {
//            map.put("sign", WXPayUtil.generateSignature(map, redKey));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        String xmlStr = StrUtil.mapToXmlStr(map);

        String resPoseStr = HttpClient.requestWithCert(redpackUrl,xmlStr,cerPath,mchid);
        //HttpClient.sendPost(payUrl,xmlStr,cerPath,mchid);

        logger.info("红包请求完成，订单号={}，返回信息={}",wecharRed.getOrderNum(),resPoseStr);

        // 将字符串转为XML
        Document doc = null;
        try {
            doc = DocumentHelper.parseText(resPoseStr);
        } catch (DocumentException e) {
            logger.error("解析XML出错，xml={},error={}",resPoseStr,e.getMessage());
            return ReturnJsonUtil.printJson(Constants.RECODE_ERROR,"微信返回数据解析失败，请检查网络是否通畅",false);
        }
        // 获取根节点
        Element rootElt = doc.getRootElement();
        wecharRed.setReturnCode(rootElt.elementTextTrim("return_code"));
        wecharRed.setReturnMsg(rootElt.elementTextTrim("return_msg"));
        wecharRed.setResultCode(rootElt.elementTextTrim("result_code"));
        wecharRed.setErrCode(rootElt.elementTextTrim("err_code"));
        wecharRed.setErrCodeDes(rootElt.elementTextTrim("err_code_des"));
        wecharRed.setWxPaymentNo(rootElt.elementTextTrim("send_listid"));
        wecharRed.setId(UUIDUtil.getUUID());
        wecharRed.setIp(ip);
        wecharRed.setPayType(2);

        Integer count = redMapper.inster(wecharRed);

        wecharRed.setIp("");

        if (StringUtils.equals(wecharRed.getReturnCode(),ConstantsEnum.WECHAR_RED_CODE.SUCCESS.getValue())){
            if (StringUtils.equals(wecharRed.getResultCode(),ConstantsEnum.WECHAR_RED_CODE.SUCCESS.getValue())){
                return ReturnJsonUtil.printJson(Constants.RECODE_SUCCESS,"发送成功",wecharRed,true);
            }else{
                logger.info("微信红包发送错误,params={}", JSON.toJSONString(wecharRed));
                return ReturnJsonUtil.printJson(Constants.RECODE_BUSINESS,"发送错误，请关注err_code",wecharRed,false);
            }
        }else{
            logger.info("查询红包单失败，orderNUM={},err_msg={}",wecharRed.getOrderNum(),wecharRed.getReturnMsg());
            return ReturnJsonUtil.printJson(Constants.RECODE_ERROR,"微信接口调用失败",wecharRed,false);
        }
    }

    @Override
    public ResponseData getRedInfo(TblWecharRed wecharRed) {
        Map<String,String> map = new HashMap<>();
        map.put("nonce_str", UUIDUtil.getUUID());
        map.put("mch_billno",wecharRed.getOrderNum());
        map.put("mch_id",mchid);
        map.put("appid",appid);
        map.put("bill_type","MCHT");

        String keyValue = StrUtil.getAsciiKeyValue(map)+"key="+redKey;
        map.put("sign",MD5.parseStrToMd5U32(keyValue));

        String xmlStr = StrUtil.mapToXmlStr(map);
        String resPoseStr = HttpClient.requestWithCert(redpackInfoUrl,xmlStr,cerPath,mchid);

        logger.info("查单请求完成,订单号={}，返回信息={}",wecharRed.getOrderNum(),resPoseStr);

        // 将字符串转为XML
        Document doc = null;
        try {
            doc = DocumentHelper.parseText(resPoseStr);
        } catch (DocumentException e) {
            logger.error("解析XML出错，xml={},error={}",resPoseStr,e.getMessage());
            return ReturnJsonUtil.printJson(Constants.RECODE_ERROR,"微信返回数据解析失败，请检查网络是否通畅",false);
        }

        // 获取根节点
        Element rootElt = doc.getRootElement();
        if(StringUtils.equals(rootElt.elementTextTrim("return_code"),ConstantsEnum.WECHAR_RED_CODE.SUCCESS.getValue())){
            wecharRed.setResultCode(rootElt.elementTextTrim("result_code"));
            wecharRed.setErrCode(rootElt.elementTextTrim("err_code"));
            wecharRed.setErrCodeDes(rootElt.elementTextTrim("err_code_des"));
            wecharRed.setWxPaymentNo(rootElt.elementTextTrim("detail_id"));
            if (rootElt.element("hblist")!=null && rootElt.element("hblist").element("hbinfo") != null){
                String rcv_time = rootElt.element("hblist").element("hbinfo").elementTextTrim("rcv_time");
                wecharRed.setWxPaymentTime(rcv_time);
            }
            Integer count = redMapper.updateByOrderNum(wecharRed);

            Map<String,String> returnMap = StrUtil.iterateElement(rootElt);
            returnMap.remove("send_type");
            returnMap.remove("hb_type");
            returnMap.remove("mch_id");
            returnMap.remove("total_num");
            return ReturnJsonUtil.printJson(Constants.RECODE_SUCCESS,"查询成功",returnMap,true);

        }else {
            logger.info("查询企业付款单失败，orderNUM={},err_msg={}",wecharRed.getOrderNum(),rootElt.elementTextTrim("return_msg"));
            return ReturnJsonUtil.printJson(Constants.RECODE_ERROR,"微信接口调用失败",false);
        }
    }

}
