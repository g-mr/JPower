package com.wlcb.jpower.module.common.Interceptor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.wlcb.jpower.module.base.vo.ResponseData;
import com.wlcb.jpower.module.common.service.redis.RedisUtils;
import com.wlcb.jpower.module.common.utils.JWTUtils;
import com.wlcb.jpower.module.common.utils.param.ParamConfig;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

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

    private final String isLogin = "isLogin";

    /** 用户权限redis Key **/
    private final String USER_KEY = "user:loginFunction:";

    @Autowired
    private RedisUtils redisUtils;

    /**
     * @Author 郭丁志
     * @Description //TODO 请求处理前调用,我们只需要在这里写验证登陆状态的业务逻辑，就可以在用户调用指定接口之前验证登陆状态了
     * @Date 23:06 2020-02-19
     * @Param [request, response, handler]
     * @return boolean
     **/
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(ParamConfig.getInt(isLogin) == 1){

            if(ParamConfig.getInt(isLogin) == 1){

                boolean b = JWTUtils.parseJWT(request, response);
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

            //下面注释得方法是新得拦截逻辑，等tb_core_user表上线，需要使用下面得代码同步上线
//            ResponseData responseData = new ResponseData();
//            responseData.setCode(401);
//            responseData.setStatus(false);
//            responseData.setMessage("您没有权限访问");
//
//            String currentPath = request.getServletPath();
//
//            String jwt = request.getHeader("Authorization");
//            if (StringUtils.isBlank(jwt)){
//                Cookie[] cookies = request.getCookies();
//                if (cookies!=null){
//                    for (Cookie cookie : cookies) {
//                        if (StringUtils.equals(cookie.getName(),"Authorization")){
//                            jwt = cookie.getValue();
//                            logger.info("通过cookie获取到token",cookie.getValue());
//                            break;
//                        }
//                    }
//                }
//            }
//
//
//            logger.info("{}已进入鉴权拦截器,token={}",currentPath,jwt);
//
//            if (StringUtils.isBlank(jwt)) {
//                logger.info("未登陆或登陆超时！jwt是空的,请求地址={}",currentPath);
//
//                //这里需要新增匿名用户
//            }
//
//            JSONObject jsonObject = JWTUtils.parsingJwt(jwt);
//            if (jsonObject.getBoolean("status")){
//
////                String userId = jsonObject.getString("userId");
//                JSONObject functionJson = (JSONObject) redisUtils.get(USER_KEY+jwt);
//
//                if (functionJson!=null && functionJson.containsKey(currentPath)){
//
//                    if (jsonObject.containsKey("token")){
//                        response.setHeader("token",jsonObject.getString("token"));
//                        Long time = ParamConfig.getLong(JWTUtils.tokenExpired,JWTUtils.tokenExpiredDefVal);
//                        redisUtils.set(USER_KEY+jsonObject.getString("token"),functionJson,time, TimeUnit.SECONDS);
//                    }
//
//                    return true;
//                }
//            }
//            sendJsonMessage(response,responseData);
//            return false;
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
