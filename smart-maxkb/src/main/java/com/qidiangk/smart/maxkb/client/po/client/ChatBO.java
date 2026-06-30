package com.qidiangk.smart.maxkb.client.po.client;

import lombok.Builder;
import lombok.Getter;

/**
 * 聊天请求体
 *
 * @author mr.g
 */
@Getter
@Builder
public class ChatBO {

    private String message;
    private Boolean reChat;
    private Boolean stream;

}
