package com.wlcb.jpower.module.common.interceptor;

import com.wlcb.jpower.module.base.vo.ResponseData;
import com.wlcb.jpower.module.common.auth.RoleConstant;
import com.wlcb.jpower.module.common.auth.UserInfo;
import com.wlcb.jpower.module.common.cache.CacheNames;
import com.wlcb.jpower.module.common.service.core.client.CoreClientService;
import com.wlcb.jpower.module.common.service.core.user.CoreFunctionService;
import com.wlcb.jpower.module.common.service.core.user.CoreRolefunctionService;
import com.wlcb.jpower.module.common.service.redis.RedisUtils;
import com.wlcb.jpower.module.common.utils.*;
import com.wlcb.jpower.module.common.utils.constants.ParamsConstants;
import com.wlcb.jpower.module.common.utils.param.ParamConfig;
import com.wlcb.jpower.module.dbs.config.*;
import com.wlcb.jpower.module.dbs.entity.core.client.TbCoreClient;
import com.wlcb.jpower.module.dbs.entity.core.function.TbCoreFunction;
import com.wlcb.jpower.module.dbs.entity.core.role.TbCoreRoleFunction;
import com.wlcb.jpower.module.mp.support.Condition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @ClassName LoginInterceptor
 * @Description TODO 用户拦截器
 * @Author 郭丁志
 * @Date 2020-01-31 16:32
 * @Version 1.0
 */
@Slf4j
@Component
public class LoginInterceptor extends HandlerInterceptorAdapter {

    /** ip白名单 **/
    private final String IP_LIST = ParamsConstants.IP_LIST;

    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private CoreFunctionService coreFunctionService;
    @Autowired
    private CoreRolefunctionService coreRolefunctionService;
    @Autowired
    private CoreClientService coreClientService;

    @Value("${jpower.client-code}")
    private String clientCode;

    /** 环境 **/
    @Value("${spring.profiles.active}")
    private String active;

    /**
     * @Author 郭丁志
     * @Description //TODO 请求处理前调用,我们只需要在这里写验证登陆状态的业务逻辑，就可以在用户调用指定接口之前验证登陆状态了
     * @Date 23:06 2020-02-19
     * @Param [request, response, handler]
     * @return boolean
     **/
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (Fc.equals(active,"dev")||Fc.equals(active,"test")){
            //开发环境和测试环境不走鉴权
            return true;
        }

        ResponseData responseData = ReturnJsonUtil.printJson(HttpStatus.UNAUTHORIZED.value(),"请求未授权",false);

        String currentPath = request.getServletPath();

        UserInfo user = SecureUtil.getUser(request);

        TbCoreClient coreClient = coreClientService.loadClientByClientCode(user.getClientCode());

        if (user != null && coreClient!=null && Fc.toStrList(clientCode).contains(SecureUtil.getClientCodeFromHeader()) && Fc.toStrList(clientCode).contains(user.getClientCode())) {

            List<String> listUrl = (List<String>) redisUtils.get(CacheNames.TOKEN_URL_KEY+JwtUtil.getToken(request));
            if (Fc.contains(listUrl.iterator(),currentPath)){
                log.warn("{}({}) 用户授权通过，请求接口：{}",user.getUserName(),user.getUserId(),currentPath);
                LoginUserContext.set(user);
                return true;
            }

        }else if (coreClient!=null && Fc.toStrList(clientCode).contains(SecureUtil.getClientCodeFromHeader())) {
            String ip = WebUtil.getIP(request);
            TbCoreFunction function = coreFunctionService.selectFunctionByUrl(currentPath);
            if (function != null){
                if(Fc.contains(Fc.toStrList(ParamConfig.getString(IP_LIST)).iterator(),ip) && Fc.isNotBlank(coreClient.getRoleIds())){
                    //白名单登录
                    if (StringUtil.equalsIgnoreCase(coreClient.getRoleIds(),"all") ||
                            coreRolefunctionService.countByRoleIdsAndFunctionId(Fc.toStrList(coreClient.getRoleIds()),function.getId()) > 0){
                        log.warn("{} 白名单授权通过，请求接口：{}",ip,currentPath);
                        user.setUserName(ip);
                        user.setLoginId(ip);
                        user.setUserType(RoleConstant.ANONYMOUS_UESR_TYPE);
                        user.setIsSysUser(UserInfo.TBALE_USER_TYPE_WHILT);
                        user.setUserId(ip);
                        LoginUserContext.set(user);
                        return true;
                    }

                }else {
                    //匿名用户登录
                    Integer roleCount = coreRolefunctionService.count(Condition.<TbCoreRoleFunction>getQueryWrapper().lambda()
                            .eq(TbCoreRoleFunction::getRoleId,RoleConstant.ANONYMOUS_ID)
                            .eq(TbCoreRoleFunction::getFunctionId,function.getId()));
                    if(roleCount>0){
                        log.warn("{} 授权通过，请求接口：{}", RoleConstant.ANONYMOUS_NAME,currentPath);
                        user = new UserInfo();
                        user.setLoginId(RoleConstant.ANONYMOUS);
                        user.setUserId(RoleConstant.ANONYMOUS_ID);
                        user.setUserName(RoleConstant.ANONYMOUS_NAME);
                        user.setUserType(RoleConstant.ANONYMOUS_UESR_TYPE);
                        LoginUserContext.set(user);
                        return true;
                    }
                }
            }
        }else {
            responseData = ReturnJsonUtil.printJson(HttpStatus.UNAUTHORIZED.value(),"无效的客户端",false);
        }
        ReturnJsonUtil.sendJsonMessage(response,responseData);
        return false;
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
