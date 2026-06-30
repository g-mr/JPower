package com.qidiangk.smart.maxkb.controller.api;

import com.qidiangk.smart.maxkb.api.feign.AgentChatClient;
import com.qidiangk.smart.maxkb.service.IChatService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.jpower.core.util.rsp.R;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * 聊天Feign
 *
 * @author mr.g
 */
@Hidden
@RestController
@RequestMapping("/feign/chat")
@RequiredArgsConstructor
public class ChatClientController implements AgentChatClient {

    private final IChatService chatService;

    /**
     * 智能体对话
     *
     * @param id     智能体ID
     * @param issue  问题
     * @param reChat 是否重新开始对话
     * @return 回复内容
     * @author mr.g
     **/
    @Override
    @GetMapping(value = "/message", produces=APPLICATION_JSON_VALUE)
    public R<String> message(@RequestParam("phone") String phone,
                             @RequestParam("id") String id,
                             @RequestParam("issue") String issue,
                             @RequestParam("reChat") Boolean reChat) {
        return R.data(chatService.chat(phone, id, issue, reChat));
    }
}
