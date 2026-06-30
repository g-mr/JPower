package com.qidiangk.smart.auth.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 验证码VO
 *
 * @author mr.g
 */
@Data
public class CaptchaVO implements Serializable {

	@Schema(description = "验证码key")
	private String key;
	@Schema(description = "验证码图片")
	private String image;

}
