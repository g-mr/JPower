package com.wlcb.jpower.handler;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.RequestOriginParser;
import com.wlcb.jpower.module.common.utils.SecureUtil;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName OriginParserHandler
 * @Description TODO 设置流控 来源
 * @Author 郭丁志
 * @Date 2020/9/13 0013 0:09
 * @Version 1.0
 */
@Component
public class OriginParserHandler implements RequestOriginParser {
    @Override
    public String parseOrigin(HttpServletRequest request) {
        try {
            return SecureUtil.getClientCodeFromHeader();
        }catch (Exception e){
            return "";
        }

    }
}
