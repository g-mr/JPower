package com.wlcb.jpower.config;

import com.wlcb.jpower.module.base.vo.ResponseData;
import com.wlcb.jpower.module.common.auth.UserInfo;
import com.wlcb.jpower.module.common.cache.CacheNames;
import com.wlcb.jpower.module.common.redis.RedisUtil;
import com.wlcb.jpower.module.common.service.core.user.CoreFunctionService;
import com.wlcb.jpower.module.common.utils.*;
import com.wlcb.jpower.module.common.utils.constants.AppConstant;
import com.wlcb.jpower.module.properties.AuthDefExculdesUrl;
import com.wlcb.jpower.module.properties.AuthProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @ClassName AuthFilter
 * @Description TODO 鉴权登录
 * @Author 郭丁志
 * @Date 2020-08-31 16:13
 * @Version 1.0
 */
@Component
@RefreshScope
@Order(999)
@Slf4j
public class AuthFilter implements Filter {

    @Autowired
    private RedisUtil redisUtil;

    @Resource
    private AuthProperties authProperties;

    @Autowired
    private CoreFunctionService coreFunctionService;

    /** 环境 **/
    @Value("${spring.profiles.active}")
    private String active;

    /** 测试环境是否需要进行权限验证 **/
    @Value("${jpower.test.is-login:false}")
    private boolean isLogin;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest)request;

        //开发环境不走鉴权，测试环境判断是否开启了鉴权
        if (Fc.equals(active, AppConstant.DEV_CODE) || (Fc.equals(active,AppConstant.TEST_CODE) && Fc.equals(isLogin,false))){
            chain.doFilter(request, response);
        }

        String currentPath = httpRequest.getServletPath();

        //不鉴权得URL
        if (AuthDefExculdesUrl.getExculudesUrl().stream().map(url -> url.replace(AuthDefExculdesUrl.TARGET, AuthDefExculdesUrl.REPLACEMENT)).anyMatch(currentPath::contains)){
            chain.doFilter(request, response);
        }

        try {
            String token = JwtUtil.getToken(httpRequest);
            if (Fc.isNotBlank(token)){
                UserInfo user = SecureUtil.getUser();
                List<String> listUrl = (List<String>) redisUtil.get(CacheNames.TOKEN_URL_KEY + token);
                if (Fc.isNull(user) || !Fc.contains(listUrl.iterator(), currentPath)) {
                    ResponseData responseData = ReturnJsonUtil.printJson(HttpStatus.UNAUTHORIZED.value(),"请求未授权",false);
                    ReturnJsonUtil.sendJsonMessage((HttpServletResponse) response,responseData);
                }
            }else {
                String ip = WebUtil.getIP();
                if (Fc.contains(authProperties.getWhileIp().iterator(),ip)){
    //                httpRequest.header
                    chain.doFilter(request, response);
                }

                Integer roleCount = coreFunctionService.queryRoleByUrl(currentPath);
                if(roleCount>0){
                    chain.doFilter(request, response);
                }
            }

            ResponseData responseData = ReturnJsonUtil.printJson(HttpStatus.UNAUTHORIZED.value(),"缺失令牌，鉴权失败",false);

            ReturnJsonUtil.sendJsonMessage((HttpServletResponse) response,responseData);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
