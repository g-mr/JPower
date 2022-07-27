package com.wlcb.jpower.module.configurer.client;

import com.alibaba.fastjson.JSON;
import com.wlcb.jpower.module.common.auth.UserInfo;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.ReturnJsonUtil;
import com.wlcb.jpower.module.common.utils.ShieldUtil;
import com.wlcb.jpower.module.common.utils.WebUtil;
import com.wlcb.jpower.module.common.utils.constants.TokenConstant;
import com.wlcb.jpower.module.properties.AuthProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Nonnull;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @ClassName AuthInterceptor
 * @Description TODO
 * @Author 郭丁志
 * @Date 2020/8/30 0030 22:32
 * @Version 1.0
 */
@Slf4j
@AllArgsConstructor
public class ClientInterceptor implements HandlerInterceptor {

    private final List<AuthProperties.Client> clientCodes;

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public boolean preHandle(@Nonnull HttpServletRequest request,@Nonnull HttpServletResponse response,@Nonnull Object handler){
        boolean isSkip = clientCodes.stream().filter(client -> isIntercept(client,request)).map(client -> true).findFirst().orElse(false);

        if (!isSkip){
            log.warn("客户端认证失败，请求接口：{}，请求IP：{}，请求参数：{}", request.getRequestURI(), WebUtil.getIp(request), JSON.toJSONString(request.getParameterMap()));
            WebUtil.renderJson(response,ReturnJsonUtil.print(HttpStatus.NOT_ACCEPTABLE.value(),"无效的客户端请求",false));
        }
        return isSkip;
    }

    private boolean isIntercept(AuthProperties.Client client,HttpServletRequest request) {
        UserInfo user = ShieldUtil.getUser(request);
        return (user != null && Fc.equals(client.getCode(), user.getClientCode()) && Fc.equals(client.getCode(), ShieldUtil.getClientCodeFromHeader())) ||
                (Fc.equals(client.getCode(), ShieldUtil.getClientCodeFromHeader()) && Fc.isNotBlank(request.getHeader(TokenConstant.PASS_HEADER_NAME))) &&
                client.getPath().stream().anyMatch(pattern -> antPathMatcher.match(pattern, request.getServletPath()));
    }

}
