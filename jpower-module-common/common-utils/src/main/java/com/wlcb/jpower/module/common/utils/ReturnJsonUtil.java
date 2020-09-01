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

    public static <T> ResponseData<T> printJson(Integer code, String msg, T data, boolean status){

        ResponseData<T> r = new ResponseData();
        r.setMessage(msg);
        r.setCode(code);
        r.setData(data);
        r.setStatus(status);

        return r;
    }

    public static <T> ResponseData<T> printJson(Integer code,String msg,boolean status){
        ResponseData<T> r = new ResponseData();
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
     * @author 郭丁志
     * @Description //TODO 没找到
     * @date 21:43 2020/7/26 0026
     * @return com.wlcb.jpower.module.base.vo.ResponseData
     */
    public static <T> ResponseData<T> notFind(String msg){
        return printJson(ConstantsReturn.RECODE_NOTFOUND, msg, false);
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 成功的情况
     * @Date 00:29 2020-03-06
     * @Param [msg, data]
     * @return ResponseData
     **/
    public static <T> ResponseData<T> ok(String msg, T data){
        return printJson(ConstantsReturn.RECODE_SUCCESS, msg, data, true);
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 成功的通知
     * @Date 00:29 2020-03-06
     * @Param [msg, data]
     * @return ResponseData
     **/
    public static <T> ResponseData<T> ok(String msg){
        return ok( msg, null);
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 失败的情况
     * @Date 00:29 2020-03-06
     * @Param [msg, data]
     * @return ResponseData
     **/
    public static <T> ResponseData<T> fail(String msg, T data){
        return printJson(ConstantsReturn.RECODE_FAIL, msg, data, false);
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 失败的通知
     * @Date 00:29 2020-03-06
     * @Param [msg, data]
     * @return ResponseData
     **/
    public static <T> ResponseData<T> fail(String msg){
        return fail( msg, null);
    }

    public static void main(String[] args) {
    }

    public static <T> ResponseData<T> status(Boolean is) {
        if(is){
            return ok("操作成功");
        }else {
            return fail("操作失败");
        }

    }

    /**
     * @Author 郭丁志
     * @Description //TODO 业务失败
     * @Date 15:16 2020-07-31
     * @Param [该客户端已存在]
     * @return com.wlcb.jpower.module.base.vo.ResponseData
     **/
    public static <T> ResponseData<T> busFail(String msg) {
        return printJson(ConstantsReturn.RECODE_BUSINESS,msg,false);

    }
}