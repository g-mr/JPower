package com.wlcb.ylth.module.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wlcb.ylth.module.dbs.entity.user.TblZhengwuUser;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * @ClassName JkQrcode
 * @Description TODO 获取电子健康二维码
 * @Author 郭丁志
 * @Date 2020-03-26 21:57
 * @Version 1.0
 */
public class JkQrcode {

    private static final Logger logger = LoggerFactory.getLogger(JkQrcode.class);

    private String jkUrl = SysProperties.getInstance().getProperties("jk.url");
    private String accessNo = SysProperties.getInstance().getProperties("jk.access_no");
    private String signKey = SysProperties.getInstance().getProperties("jk.sign_key");
    private String appId = SysProperties.getInstance().getProperties("jk.app_id");
    private String key = SysProperties.getInstance().getProperties("jk.key");

    private static JkQrcode configuration = null;

    public static JkQrcode getInstance(){
        if(configuration == null){
            configuration = new JkQrcode();
        }
        return configuration;
    }

    private JSONObject jkParams(String method){
        JSONObject json = new JSONObject();
        json.put("access_no",accessNo);
        json.put("method",method);
        json.put("version","v1.0");
        json.put("encrypt_type","2");
        json.put("sign_type","3");

        return json;
    }

    private JSONObject jkRequestParams(){
        JSONObject json = new JSONObject();
        json.put("app_id",appId);
        json.put("timestamp", DateUtils.getDate(new Date(),DateUtils.dateTimeFormat));

        return json;
    }

    private String requestXml(){
        StringBuffer biz_context = new StringBuffer();

        biz_context.append("<access>");
        biz_context.append("<access_type_code>2</access_type_code>");
        biz_context.append("<access_no>").append(accessNo).append("</access_no>");
        biz_context.append("<trade_no>").append(UUIDUtil.getUUID()).append("</trade_no>");
        biz_context.append("</access>");

        return biz_context.toString();
    }

    private JSONObject params(String method,String param){
        JSONObject json = jkParams(method);
        JSONObject bizJson = jkRequestParams();

        StringBuffer biz_context = new StringBuffer("<request>");

        biz_context.append(requestXml());
        biz_context.append(param);
        biz_context.append("</request>");

        bizJson.put("body",biz_context.toString());

        logger.info("加密前数据:{}",bizJson);

        json.put("biz_content", DESUtil.encrypt(JSON.toJSONString(bizJson),key));
        String stringA = JSON.toJSONString(bizJson)+signKey;
        json.put("sign", MD5.parseStrToMd5U32(stringA));

        return json;
    }

    private Document parsRespose(String method, String resPoseStr){
        JSONObject rejson = JSON.parseObject(resPoseStr);
        if (!StringUtils.equals(rejson.getString("code"),"1")){
            logger.error("{}接口请求错误，返回信息=「{}」",method,resPoseStr);
            return null;
        }

        Document doc = null;
        try {
            String xmlStr = DESUtil.decrypt(rejson.getString("data"),key);
            logger.info("返回数据解密后到xml数据：{}",xmlStr);
            doc = DocumentHelper.parseText(xmlStr);
        } catch (DocumentException e) {
            logger.error("解析XML出错，xml={},error={}",resPoseStr,e.getMessage());
        }

        return doc;
    }

    public String getJkInfo(TblZhengwuUser wx){

        StringBuffer biz_context = new StringBuffer();

        biz_context.append("<datas>");
        biz_context.append("<rhcv_id/>");
        biz_context.append("<id_card_type_code>01</id_card_type_code>");
        biz_context.append("<id_card_value>").append(wx.getIdcard()).append("</id_card_value>");
        biz_context.append("<name>").append(wx.getName()).append("</name>");
        biz_context.append("<qrcode_photo_flag>1</qrcode_photo_flag>");
        biz_context.append("</datas>");

        JSONObject json = params("card.manager.system.info",biz_context.toString());

        logger.info("请求{}接口,请求参数={}",jkUrl,JSON.toJSONString(json));
        String resPoseStr = HttpClient.doPostJson(jkUrl, JSON.toJSONString(json));

        Document doc = parsRespose("card.manager.system.info",resPoseStr);

        Element rootElt = doc.getRootElement();

        String returnCode = rootElt.element("process_result").elementTextTrim("return_code");

        if (StringUtils.equals(returnCode,"1")){
            String qrcodeText = rootElt.element("info").elementTextTrim("static_qrcode_text");

            logger.info("{}接口请求完成，二维码数据={}",jkUrl,qrcodeText);

            return qrcodeText;
        }else if (StringUtils.equals(returnCode,"0602")){
            //没有记录去注册
            return addJkInfo(wx);
        }else {
            logger.error("{}接口请求异常，返回信息={},用户ID={}","card.manager.system.info",resPoseStr,wx.getId());
        }

        return null;
    }

    private String addJkInfo(TblZhengwuUser wx) {

        StringBuffer biz_context = new StringBuffer();

        biz_context.append("<datas>");
        biz_context.append("<name>").append(wx.getName()).append("</name>");

        biz_context.append("<gender_code>").append(9).append("</gender_code>");
        biz_context.append("<nation_code>").append("01").append("</nation_code>");
        biz_context.append("<mobilephone>").append(wx.getPhone()).append("</mobilephone>");
        biz_context.append("<id_card_type_code>").append("01").append("</id_card_type_code>");
        biz_context.append("<id_card_value>").append(wx.getIdcard()).append("</id_card_value>");
        biz_context.append("<birthday>").append(StringUtils.isNotBlank(wx.getIdcard())?wx.getIdcard().substring(6,6+8):wx.getIdcard()).append("</birthday>");
        biz_context.append("<home_address>").append(wx.getQuxian()).append("</home_address>");
        biz_context.append("<registed_address>").append(wx.getQuxian()).append("</registed_address>");
        biz_context.append("<now_address>").append(wx.getQuxian()).append("</now_address>");
        biz_context.append("<contact_name>").append("</contact_name>");
        biz_context.append("<contact_phone>").append("</contact_phone>");
        biz_context.append("<qrcode_photo_flag>0</qrcode_photo_flag>");

        biz_context.append("</datas>");

        JSONObject json = params("card.manager.system.regist",biz_context.toString());

        logger.info("请求{}接口{}方法,请求参数={}",jkUrl,"card.manager.system.regist",JSON.toJSONString(json));
        String resPoseStr = HttpClient.doPostJson(jkUrl, JSON.toJSONString(json));

        Document doc = parsRespose("card.manager.system.info",resPoseStr);

        Element rootElt = doc.getRootElement();

        String returnCode = rootElt.element("process_result").elementTextTrim("return_code");

        if (StringUtils.equals(returnCode,"1")){
            String rhcvId = rootElt.element("info").elementTextTrim("rhcv_id");

            String qrcodeText = queryQrcode(rhcvId);

            if (StringUtils.isNotBlank(qrcodeText)){
                return qrcodeText;
            }else {
                logger.error("{}接口请求异常，用户ID={}","card.manager.system.qrcode",wx.getId());
            }

        }else {
            logger.error("{}接口请求异常，返回信息={},用户ID={}","card.manager.system.info",resPoseStr,wx.getId());
        }

        return null;
    }

    private String queryQrcode(String rhcvId) {

        String method = "card.manager.system.qrcode";

        StringBuffer biz_context = new StringBuffer();

        biz_context.append("<datas>");
        biz_context.append("<qr_type_code>1</qr_type_code>");
        biz_context.append("<rhcv_id>").append(rhcvId).append("</rhcv_id>");
        biz_context.append("</datas>");

        JSONObject json = params(method,biz_context.toString());

        logger.info("请求{}接口{}方法,请求参数={}",jkUrl,method,JSON.toJSONString(json));
        String resPoseStr = HttpClient.doPostJson(jkUrl, JSON.toJSONString(json));

        Document doc = parsRespose(method,resPoseStr);

        Element rootElt = doc.getRootElement();

        String returnCode = rootElt.element("process_result").elementTextTrim("return_code");

        if (StringUtils.equals(returnCode,"1")){
            return rootElt.element("info").elementTextTrim("qrcode_text");
        }else {
            logger.error("{}接口异常，返回信息={}",method,resPoseStr);
        }
        return null;
    }





}
