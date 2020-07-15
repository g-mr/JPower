package com.wlcb.jpower.module.common.Interceptor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.wlcb.jpower.module.base.vo.ResponseData;
import com.wlcb.jpower.module.common.service.redis.RedisUtils;
import com.wlcb.jpower.module.common.utils.IpUtils;
import com.wlcb.jpower.module.common.utils.JWTUtils;
import com.wlcb.jpower.module.common.utils.constants.ConstantsEnum;
import com.wlcb.jpower.module.common.utils.constants.ConstantsUtils;
import com.wlcb.jpower.module.common.utils.param.ParamConfig;
import com.wlcb.jpower.module.dbs.config.*;
import com.wlcb.jpower.module.dbs.entity.core.function.TbCoreFunction;
import com.wlcb.jpower.module.dbs.entity.core.user.TbCoreUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    /** ip白名单 **/
    private final String ip_list = "ip_list";

    //多少时间内要刷新token
    private static Long refToken = 10 * 60 * 1000L;
    //续期token过期时间
    public static final String tokenExpired = "tokenExpired";
    public static final Long tokenExpiredDefVal = 2400000L;

    @Autowired
    private RedisUtils redisUtils;

    @Value("${isLogin:}")
    private Integer isSystemLogin;

    /**
     * @Author 郭丁志
     * @Description //TODO 请求处理前调用,我们只需要在这里写验证登陆状态的业务逻辑，就可以在用户调用指定接口之前验证登陆状态了
     * @Date 23:06 2020-02-19
     * @Param [request, response, handler]
     * @return boolean
     **/
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        Integer isl = isSystemLogin == null?ParamConfig.getInt(isLogin):isSystemLogin;

        String ip = IpUtils.getRemortIP(request);
        String ips = ParamConfig.getString(ip_list);
        logger.info("请求来自：{}————可以通过的IP：{}",ip,ips);

        if(1 == isl && !StringUtils.contains(ips,ip)){

            //下面注释得方法是新得拦截逻辑，等tb_core_user表上线，需要使用下面得代码同步上线
            ResponseData responseData = new ResponseData();
            responseData.setCode(401);
            responseData.setStatus(false);
            responseData.setMessage("您没有权限访问");

            String currentPath = request.getServletPath();

            String jwt = request.getHeader("Authorization");
            if (StringUtils.isBlank(jwt)){
                Cookie[] cookies = request.getCookies();
                if (cookies!=null){
                    for (Cookie cookie : cookies) {
                        if (StringUtils.equals(cookie.getName(),"Authorization")){
                            jwt = cookie.getValue();
                            logger.info("通过cookie获取到token",cookie.getValue());
                            break;
                        }
                    }
                }
            }


            logger.info("{}已进入鉴权拦截器,token={}",currentPath,jwt);


            TbCoreUser coreUser = null;
            if (StringUtils.isBlank(jwt)) {
                logger.info("未登陆或登陆超时！jwt是空的,请求地址={}",currentPath);

                //这里需要新增匿名用户,没有登陆得用户默认给一个匿名用户
                coreUser = new TbCoreUser();
                coreUser.setLoginId("anonymous");
                coreUser.setId("2");
                coreUser.setUserName("匿名用户");
                coreUser.setIsSysUser(0);
                coreUser.setUserType(9);
            }else {
                try{
                    Claims c = JWTUtils.parseJWT(jwt);
                    coreUser = JSON.parseObject(c.getSubject(),TbCoreUser.class);
                    logger.info("{}用户token已解析，用户信息={}", c.get("userId"),JSON.toJSONString(coreUser));

                    long time = c.getExpiration().getTime() - System.currentTimeMillis();
                    //过期时间小于10分钟则获取新的token
                    if(time <= refToken){
                        try {
                            Map<String, Object> payload = new HashMap<String, Object>();
                            payload.put("userId", c.get("userId"));
                            payload.put("isSysUser", c.get("isSysUser"));
                            if (1 == c.get("isSysUser")){
                                payload.put("openid", c.get("openid"));
                            }
                            String token = JWTUtils.createJWT(JSON.toJSONString(coreUser),payload, ParamConfig.getLong(tokenExpired,tokenExpiredDefVal));

                            if (StringUtils.isBlank(token)){
                                logger.error("token生成错误，token={}",token);
                            }
                            response.setHeader("token",token);
                            redisUtils.set(ConstantsUtils.USER_KEY+c.get("userId")+":"+c.get("isSysUser"),redisUtils.get(ConstantsUtils.USER_KEY+c.get("userId")+":"+c.get("isSysUser")),ParamConfig.getLong(tokenExpired,tokenExpiredDefVal),TimeUnit.MILLISECONDS);

                            logger.info("{}用户已刷新，用户id={},旧token={},新token={}",coreUser.getLoginId(),c.get("userId"),jwt,token);
                        } catch (Exception e) {
                            logger.error("刷新token出错：{}",e.getMessage());
                        }
                    }


                }catch (ExpiredJwtException e){
                    Claims claims = e.getClaims();
                    TbCoreUser user = JSON.parseObject(claims.getSubject(),TbCoreUser.class);
                    logger.info("{}{}用户登陆超时请重新登录！用户ID={},token={},error={}",user.getLoginId(), ConstantsEnum.USER_TYPE.getName(user.getUserType()),claims.get("userId"),jwt,e.getMessage());
                    responseData.setCode(403);
                    responseData.setMessage("用户登陆超时请重新登录");
                }catch (Exception e){
                    logger.error("JWT解析出错！error={},token={}",e.getMessage(),jwt);
                    responseData.setCode(405);
                    responseData.setMessage("JWT解析出错");
                }
            }

            if(coreUser != null){
                List<TbCoreFunction> functionList = (List<TbCoreFunction>) redisUtils.get(ConstantsUtils.USER_KEY+coreUser.getId());
                boolean isAuthority = findAuthority(currentPath,functionList);
                if (isAuthority){
                    //存储当前登录用户
                    LoginUserContext.set(coreUser);
                    LoginUserContext.setIp(ip);
                    return true;
                }
            }

            sendJsonMessage(response,responseData);
            return false;
        }else {
            TbCoreUser coreUser = new TbCoreUser();
            if (StringUtils.contains(ips,ip)){
                logger.info("接口请求已进入--{}--{}",ip,request.getServletPath());
                coreUser.setUserName(ip);
                coreUser.setLoginId(ip);
                coreUser.setUserType(9);
                coreUser.setIsSysUser(2);
                coreUser.setId("3");
            }else {
                coreUser = new TbCoreUser();
                coreUser.setLoginId("anonymous");
                coreUser.setId("2");
                coreUser.setUserName("匿名用户");
                coreUser.setIsSysUser(0);
                coreUser.setUserType(9);
            }
            LoginUserContext.set(coreUser);
            LoginUserContext.setIp(ip);
            return true;
        }
    }

    /**
     * @author 郭丁志
     * @Description //TODO 判断是否有权限
     * @date 1:33 2020/6/2 0002
     * @param currentPath 当前url
     * @param functionList 权限列表
     * @return boolean
     */
    private boolean findAuthority(String currentPath, List<TbCoreFunction> functionList) {
        if (functionList != null){
            for (TbCoreFunction tbCoreFunction : functionList) {
                //如果存在则说明有权限
                if (StringUtils.equals(tbCoreFunction.getUrl(),currentPath)){
                    return true;
                }
            }
        }
        return false;
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
        //接口请求完成后删除掉存储的用户信息
        LoginUserContext.remove();
    }

}
