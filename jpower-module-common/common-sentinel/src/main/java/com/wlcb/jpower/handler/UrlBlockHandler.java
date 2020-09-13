package com.wlcb.jpower.handler;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wlcb.jpower.module.common.utils.constants.StringPool;
import com.wlcb.jpower.utils.ErrorMsg;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName JpowerUrlBlockHandler
 * @Description TODO 限流降级后出返回结果
 * @Author 郭丁志
 * @Date 2020/9/12 0012 21:41
 * @Version 1.0
 */
@Component
public class UrlBlockHandler implements BlockExceptionHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, BlockException ex) throws Exception {
        response.setStatus(500);
        response.setCharacterEncoding(StringPool.UTF_8);
        response.setHeader("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        new ObjectMapper()
                .writeValue(response.getWriter(),ErrorMsg.blockException(ex));
    }
}
