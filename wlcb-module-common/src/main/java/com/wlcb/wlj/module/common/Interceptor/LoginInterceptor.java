package com.wlcb.wlj.module.common.Interceptor;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.wlcb.wlj.module.base.vo.ResponseData;
import com.wlcb.wlj.module.common.utils.JWTUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * @ClassName LoginInterceptor
 * @Description TODO 用户拦截器
 * @Author 郭丁志
 * @Date 2020-01-31 16:32
 * @Version 1.0
 */
@Component
public class LoginInterceptor extends HandlerInterceptorAdapter {

    private static final Logger logger = LoggerFactory.getLogger(LoginInterceptor.class);

    @Value("${isFilter:1}")
    Integer isFilter;

    /**
     * @Author 郭丁志
     * @Description //TODO 请求处理前调用,我们只需要在这里写验证登陆状态的业务逻辑，就可以在用户调用指定接口之前验证登陆状态了
     * @Date 23:06 2020-02-19
     * @Param [request, response, handler]
     * @return boolean
     **/
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(isFilter == 1){

            boolean b = JWTUtils.parseJWT(request);
            if (!b){
                ResponseData responseData = new ResponseData();
                responseData.setCode(401);
                responseData.setStatus(false);
                responseData.setMessage("您没有权限访问");
                sendJsonMessage(response,responseData);
            }
            return b;
        }else {
            return true;
        }
    }

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
     * @Description //TODO 请求处理后，渲染ModelAndView前调用
     * @Date 20:06 2020-02-15
     * @Param [request, response, handler, modelAndView]
     * @return void
     **/
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }
    /**
     * @Author 郭丁志
     * @Description //TODO 渲染ModelAndView后调用
     * @Date 20:06 2020-02-15
     * @Param [request, response, handler, ex]
     * @return void
     **/
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }

}
