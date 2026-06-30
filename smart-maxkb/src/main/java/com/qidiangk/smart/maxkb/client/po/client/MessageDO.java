package com.qidiangk.smart.maxkb.client.po.client;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 聊天回复消息
 *
 * @author mr.g
 */
@Data
public class MessageDO {

    @Schema(description = "消息内容")
    private String content;
}
