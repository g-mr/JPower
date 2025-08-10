package top.jpower.boot.config;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import com.alibaba.fastjson2.JSON;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import top.jpower.common.constants.CacheNames;
import top.jpower.core.auth.dto.UserInfo;
import top.jpower.core.auth.properties.AuthDefExculdesUrl;
import top.jpower.core.auth.properties.AuthProperties;
import top.jpower.core.auth.utils.JwtUtil;
import top.jpower.core.auth.utils.ShieldUtil;
import top.jpower.core.auth.utils.constant.RoleConstant;
import top.jpower.core.dbs.datascope.DataScope;
import top.jpower.core.deploy.property.JpowerProperties;
import top.jpower.core.redis.cache.RedisService;
import top.jpower.core.util.constants.JpowerConstants;
import top.jpower.core.util.constants.StringPool;
import top.jpower.core.util.constants.TokenConstant;
import top.jpower.core.util.rsp.ResponseData;
import top.jpower.core.util.rsp.ReturnJsonUtil;
import top.jpower.core.util.utils.BeanUtil;
import top.jpower.core.util.utils.ChainMap;
import top.jpower.core.util.utils.Fc;
import top.jpower.core.util.utils.WebUtil;
import top.jpower.jpower.dbs.entity.function.TbCoreDataScope;
import top.jpower.jpower.service.role.CoreDataScopeService;
import top.jpower.jpower.service.role.CoreFunctionService;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static top.jpower.core.auth.utils.constant.RoleConstant.ANONYMOUS_ID;
import static top.jpower.core.auth.utils.constant.RoleConstant.ROOT_ID;
import static top.jpower.core.util.constants.JpowerConstants.HEADER_MENU;


/**
 * @ClassName AuthFilter
 * @Description TODO BOOT项目鉴权登录
 * @Author 郭丁志
 * @Date 2020-08-31 16:13
 * @Version 1.0
 */
@RequiredArgsConstructor
@Component
@Order(999)
@Slf4j
public class AuthFilter implements Filter {

    private final RedisService redisUtil;
    private final AuthProperties authProperties;
    private final JpowerProperties jpowerProperties;
    private final CoreDataScopeService dataScopeService;
    private final CoreFunctionService coreFunctionService;

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    /** 测试环境是否需要进行权限验证 **/
    @Value("${jpower.test.is-login:false}")
    private boolean isLogin;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest)request;
//        开发环境不走鉴权，测试环境判断是否开启了鉴权
        if (Fc.equals(jpowerProperties.getEnv(), JpowerConstants.DEV_CODE) || (Fc.equals(jpowerProperties.getEnv(), JpowerConstants.TEST_CODE) && Fc.equals(isLogin,false))){
            chain.doFilter(request, response);
            return;
        }

        String currentPath = httpRequest.getServletPath();

        //不鉴权得URL
        if (isSkip(currentPath)){
            chain.doFilter(request, response);
            return;
        }

        String token = JwtUtil.getToken(httpRequest);
        if (Fc.isNotBlank(token)){

            if (!redisUtil.exist(CacheNames.TOKEN_URL_KEY + token)){
                ResponseData<String> responseData = ReturnJsonUtil.print(HttpStatus.PROXY_AUTHENTICATION_REQUIRED.value(),"令牌已过期，请重新登录",false);

                WebUtil.renderJson((HttpServletResponse) response,responseData);
                return;
            }

            UserInfo user = ShieldUtil.getUser(httpRequest);
            if (Fc.isNull(user) || !isAuthByToken(token, currentPath)) {
                ResponseData<String> responseData = ReturnJsonUtil.print(HttpStatus.UNAUTHORIZED.value(),"请求未授权",false);
                WebUtil.renderJson((HttpServletResponse) response,responseData);
                return;
            }

            Object dataAuth = redisUtil.valueOps().get(CacheNames.TOKEN_DATA_SCOPE_KEY + token);
            Map<String,List> map = Fc.isNull(dataAuth) ? ChainMap.<String,List>create().build() : (Map<String, List>) dataAuth;
            chain.doFilter(addHeader(httpRequest, StringPool.EMPTY, JSON.toJSONString(map.getOrDefault(httpRequest.getHeader(HEADER_MENU), ListUtil.empty()))), response);
            return;
        }else {
            //白名单
            String ip = WebUtil.getIp(httpRequest);
            if (Fc.contains(authProperties.getWhileIp(),ip)){
                chain.doFilter(addHeader(httpRequest,ip, StringPool.EMPTY), response);
                return;
            }

            //匿名用户
            List<String> listUrl = coreFunctionService.getUrlsByRoleIds(Collections.singletonList(ANONYMOUS_ID), ShieldUtil.getClientCodeFromHeader());
            if(isAuth(listUrl, currentPath)){

                String menuCode = httpRequest.getHeader(HEADER_MENU);
                List<DataScope> dataScopes = null;
                if (Fc.isNotBlank(menuCode)){
                    List<TbCoreDataScope> list = dataScopeService.getDataScopeByRoleAndMenu(Collections.singletonList(RoleConstant.ANONYMOUS_ID), menuCode);
                    dataScopes = BeanUtil.copyToList(list,DataScope.class);
                }

                chain.doFilter(addHeader(httpRequest, RoleConstant.ANONYMOUS, Fc.isNotEmpty(dataScopes)?JSON.toJSONString(dataScopes):StringPool.EMPTY), response);
                return;
            }
        }

        ResponseData<String> responseData = ReturnJsonUtil.print(HttpStatus.UNAUTHORIZED.value(),"缺失令牌，鉴权失败",false);
        WebUtil.renderJson((HttpServletResponse) response,responseData);

    }

    /**
     * 是否拥有权限
     * @Author mr.g
     * @param token TOKEN
     * @param currentPath 请求地址
     * @return boolean
     **/
    private boolean isAuthByToken(String token,String currentPath){
        Object o = redisUtil.valueOps().get(CacheNames.TOKEN_URL_KEY + token);
        List<String> listUrl = Fc.isNull(o)?new ArrayList<>():(List<String>) o;
        return isAuth(listUrl, currentPath);
    }

    /**
     * 是否拥有权限
     * @Author mr.g
     * @param listUrl url
     * @param currentPath 请求地址
     * @return boolean
     **/
    private boolean isAuth(List<String> listUrl,String currentPath){
        if (CollUtil.safeContains(ShieldUtil.getUserRole(), ROOT_ID)){
            return Boolean.TRUE;
        }
        return listUrl.stream().anyMatch(pattern -> antPathMatcher.match(pattern, currentPath));
    }

    private boolean isSkip(String path) {
        return AuthDefExculdesUrl.getExculudesUrl().stream().anyMatch(pattern -> antPathMatcher.match(pattern, path))
                || authProperties.getSkipUrl().stream().anyMatch(pattern -> antPathMatcher.match(pattern, path));
    }

    private ServletRequest addHeader(HttpServletRequest request,String value, String dataScope) {
        MutableHttpServletRequest mutableRequest = new MutableHttpServletRequest(request);
        mutableRequest.putHeader(TokenConstant.PASS_HEADER_NAME, value);
        mutableRequest.putHeader(TokenConstant.DATA_SCOPE_NAME,dataScope);
        return mutableRequest;
    }

}
