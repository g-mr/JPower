package com.wlcb.jpower.module.base.utils;

import com.alibaba.fastjson.JSONObject;
import com.wlcb.jpower.module.base.model.LogDto;
import com.wlcb.jpower.module.common.auth.UserInfo;
import com.wlcb.jpower.module.common.deploy.props.JpowerProperties;
import com.wlcb.jpower.module.common.deploy.server.ServerInfo;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.WebUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 补全日志信息
 *
 * @Author mr.g
 * @Date 2021/5/1 0001 17:15
 */
public class FieldCompletionUtil {

    public static void requestInfo(LogDto operLog, HttpServletRequest request){
        if (Fc.isNull(request)){
            return;
        }

        Map<String, String[]> map = request.getParameterMap();
        String params = JSONObject.toJSONString(map);
        operLog.setUrl(request.getRequestURI());
        operLog.setParam(params);
        operLog.setMethod(request.getMethod());
        operLog.setRemoteIp(WebUtil.getIP());
    }

    public static void userInfo(LogDto operLog, UserInfo currentUser){
        if (Fc.isNull(currentUser)){
            return;
        }

        operLog.setClientCode(currentUser.getClientCode());
        operLog.setOperName(currentUser.getUserName());
        operLog.setOperUserType(currentUser.getIsSysUser());
    }

    public static void serverInfo(LogDto operateLog, ServerInfo serverInfo, JpowerProperties properties) {
        operateLog.setEnv(properties.getEnv());
        operateLog.setServiceName(properties.getName());
        operateLog.setServerHost(serverInfo.getHostName());
        operateLog.setServerIp(serverInfo.getIp());
    }
}
