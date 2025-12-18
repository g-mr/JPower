package top.jpower.core.auth.config.interceptor;

import com.alibaba.fastjson2.JSON;
import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;
import top.jpower.core.auth.config.data.SystemClient;
import top.jpower.core.auth.dto.UserInfo;
import top.jpower.core.auth.properties.AuthProperties;
import top.jpower.core.auth.utils.ShieldUtil;
import top.jpower.core.util.constants.TokenConstant;
import top.jpower.core.util.rsp.ReturnJsonUtil;
import top.jpower.core.util.utils.Fc;
import top.jpower.core.util.utils.WebUtil;

import java.util.List;

/**
 * 客户端拦截器
 *
 * @author mr.g
 **/
@Slf4j
@AllArgsConstructor
public class ClientInterceptor implements HandlerInterceptor {

    private final List<AuthProperties.Client> clientCodes;
    private final List<String> skipUrls;

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public boolean preHandle(@Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response, @Nonnull Object handler){
        //放行接口不拦截
        if (skipUrls.stream().anyMatch(pattern -> antPathMatcher.match(pattern, request.getServletPath()))){
            return Boolean.TRUE;
        }

        boolean isSkip = clientCodes.stream()
                .filter(client -> isIntercept(client,request))
                .map(client -> Boolean.TRUE)
                .findFirst()
                .orElse(Boolean.FALSE);

        if (!isSkip || !validateClient()){
            log.warn("客户端认证失败，请求接口：{}，请求IP：{}，请求参数：{}，请求客户端信息：{}", request.getRequestURI(), WebUtil.getIp(request), JSON.toJSONString(request.getParameterMap()), ShieldUtil.getClientCodeFromHeader()+":"+ShieldUtil.getClientSecretFromHeader());
            WebUtil.renderJson(response, ReturnJsonUtil.print(HttpStatus.NOT_ACCEPTABLE.value(),"无效的客户端请求",false));
        }
        return isSkip;
    }

    /**
     * 验证密钥是否正确
     *
     * @author mr.g
     * @return boolean
     **/
    private boolean validateClient() {
        return Fc.equalsValue(SystemClient.client(ShieldUtil.getClientCodeFromHeader()), ShieldUtil.getClientSecretFromHeader());
    }

    private boolean isIntercept(AuthProperties.Client client,HttpServletRequest request) {
        UserInfo user = ShieldUtil.getUser(request);

        if (client.getPath().stream().anyMatch(pattern -> antPathMatcher.match(pattern, request.getServletPath()))){
            return (Fc.notNull(user) && Fc.equalsValue(client.getCode(), user.getClientCode()) && Fc.equalsValue(client.getCode(), ShieldUtil.getClientCodeFromHeader()))
                    ||
                   (Fc.equalsValue(client.getCode(), ShieldUtil.getClientCodeFromHeader()) && (Fc.isNotBlank(request.getHeader(TokenConstant.PASS_HEADER_NAME))));
        }

        return Boolean.FALSE;
    }

}
