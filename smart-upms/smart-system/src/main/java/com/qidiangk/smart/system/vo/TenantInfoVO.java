package com.qidiangk.smart.system.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * 租户信息
 *
 * @author mr.g
 */
@Data
public class TenantInfoVO implements Serializable {

	@Schema(description = "租户编码")
	private String tenantCode;
	@Schema(description = "租户域名")
	private String domain;
	@Schema(description = "租户名称")
	private String title;
	@Schema(description = "租户配置")
	private Map<String, String> config;

}
