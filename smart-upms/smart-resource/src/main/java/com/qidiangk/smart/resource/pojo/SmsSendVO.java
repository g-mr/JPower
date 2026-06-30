package com.qidiangk.smart.resource.pojo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import com.qidiangk.smart.common.validated.Mobile;

import java.io.Serializable;
import java.util.Map;

/**
 * 短信发送入参
 *
 * @author mr.g
 */
@Data
public class SmsSendVO implements Serializable {

	@NotBlank(message = "手机号不能为空")
	@Mobile
	@Schema(description = "手机号", requiredMode = Schema.RequiredMode.REQUIRED)
	private String phone;

	@NotBlank(message = "模板编码不能为空")
	@Schema(description = "短信模板编码", requiredMode = Schema.RequiredMode.REQUIRED)
	private String codeName;

	@Schema(description = "模板参数")
	private Map<String, String> templateParams;

}
