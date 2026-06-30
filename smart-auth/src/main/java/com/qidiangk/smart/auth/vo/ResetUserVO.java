package com.qidiangk.smart.auth.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @author mr.g
 */
@Data
public class ResetUserVO implements Serializable {

	@Schema(description = "用户昵称")
	private String nickName;
	@Schema(description = "用户ID")
	private Long userId;
	@Schema(description = "消息ID")
	private String msgId;

}
