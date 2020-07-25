package com.wlcb.jpower.module.common.utils;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.wlcb.jpower.module.base.vo.ResponseData;
import com.wlcb.jpower.module.common.utils.constants.ConstantsReturn;

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
     * @return ResponseData
     **/
    public static ResponseData ok(String msg, Object data){
        return printJson(ConstantsReturn.RECODE_SUCCESS, msg, data, true);
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 成功的通知
     * @Date 00:29 2020-03-06
     * @Param [msg, data]
     * @return ResponseData
     **/
    public static ResponseData ok(String msg){
        return ok( msg, null);
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 失败的情况
     * @Date 00:29 2020-03-06
     * @Param [msg, data]
     * @return ResponseData
     **/
    public static ResponseData fail(String msg, Object data){
        return printJson(ConstantsReturn.RECODE_FAIL, msg, data, true);
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 失败的通知
     * @Date 00:29 2020-03-06
     * @Param [msg, data]
     * @return ResponseData
     **/
    public static ResponseData fail(String msg){
        return fail( msg, null);
    }

    public static void main(String[] args) {
    }

    public static ResponseData status(Boolean is) {
        if(is){
            return ok("操作成功");
        }else {
            return fail("操作失败");
        }

    }
}
