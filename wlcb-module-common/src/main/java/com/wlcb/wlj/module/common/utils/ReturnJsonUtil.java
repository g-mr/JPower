package com.wlcb.wlj.module.common.utils;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.wlcb.wlj.module.base.vo.ResponseData;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * @ClassName ReturnJsonUtil
 * @Description TODO 返回实体
 * @Author 郭丁志
 * @Date 2020-01-28 00:24
 * @Version 1.0
 */
public class ReturnJsonUtil {

    public static ResponseData printJson(Integer code, String msg, Object data, boolean status){

        ResponseData r = new ResponseData();
        r.setMessage(msg);
        r.setCode(code);
        r.setData(data);
        r.setStatus(status);

        return r;
    }

    public static ResponseData printJson(Integer code,String msg,boolean status){
        ResponseData r = new ResponseData();
        r.setMessage(msg);
        r.setCode(code);
        r.setData(null);
        r.setStatus(status);
        return r;
    }

    /**
     * 将ResponseData对象转换成json格式并发送到客户端
     * @param response
     * @param responseData
     * @throws Exception
     */
    public static void sendJsonMessage(HttpServletResponse response, ResponseData responseData) throws Exception {
        response.setContentType("application/json; charset=utf-8");
        PrintWriter writer = response.getWriter();
        writer.print(JSONObject.toJSONString(responseData, SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteDateUseDateFormat));
        writer.close();
        response.flushBuffer();
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 成功的情况
     * @Date 00:29 2020-03-06
     * @Param [msg, data]
     * @return com.wlcb.wlj.module.base.vo.ResponseData
     **/
    public static ResponseData printJson(String msg, Object data){
        ResponseData r = new ResponseData();
        r.setMessage(msg);
        r.setCode(200);
        r.setData(data);
        r.setStatus(true);
        return r;
    }

    public static void main(String[] args) {
    }
}
