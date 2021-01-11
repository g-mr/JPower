package com.wlcb.jpower.module.configurer.client;

import com.alibaba.fastjson.JSON;
import com.wlcb.jpower.module.common.auth.UserInfo;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.ReturnJsonUtil;
import com.wlcb.jpower.module.common.utils.SecureUtil;
import com.wlcb.jpower.module.common.utils.WebUtil;
import com.wlcb.jpower.module.common.utils.constants.TokenConstant;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName AuthInterceptor
 * @Description TODO
 * @Author 郭丁志
 * @Date 2020/8/30 0030 22:32
 * @Version 1.0
 */
@Slf4j
@AllArgsConstructor
public class ClientInterceptor extends HandlerInterceptorAdapter {

    private final String clientCode;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        UserInfo user = SecureUtil.getUser(request);
        if (user != null && Fc.equals(clientCode, user.getClientCode()) && Fc.equals(clientCode, SecureUtil.getClientCodeFromHeader())) {
            return true;
        } else {

            if (Fc.equals(clientCode, SecureUtil.getClientCodeFromHeader()) && Fc.isNotBlank(request.getHeader(TokenConstant.PASS_HEADER_NAME))) {
                return true;
            }

            log.info("客户端认证失败，请求接口：{}，请求IP：{}，请求参数：{}", request.getRequestURI(), WebUtil.getIP(request), JSON.toJSONString(request.getParameterMap()));
            ReturnJsonUtil.sendJsonMessage(response,ReturnJsonUtil.printJson(HttpStatus.NOT_ACCEPTABLE.value(),"无效的客户端请求",false));
            return false;
        }
    }

}
