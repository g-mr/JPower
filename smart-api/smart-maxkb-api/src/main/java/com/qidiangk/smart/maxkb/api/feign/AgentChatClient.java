package com.qidiangk.smart.maxkb.api.feign;

import com.qidiangk.smart.common.constants.AppConstant;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import top.jpower.core.util.rsp.R;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * MAXKB 聊天客户端
 * 
 * @author mr.g
 **/
@FeignClient(value = AppConstant.JPOWER_SMART_MAXKB, path = "/feign/chat")
public interface AgentChatClient {

    /**
     * 智能体对话
     *
     * @param id     智能体ID
     * @param issue  问题
     * @param reChat 是否重新开始对话
     * @return 回复内容
     * @author mr.g
     **/
    @GetMapping(value = "/message", produces=APPLICATION_JSON_VALUE)
    R<String> message(@RequestParam("phone") String phone,
                      @RequestParam("id") String id,
                      @RequestParam("issue") String issue,
                      @RequestParam("reChat") Boolean reChat);

}