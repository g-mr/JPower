package com.qidiangk.smart.system.api.dto;

import cn.hutool.core.date.DatePattern;
import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @author mr.g
 * @date 2026-2-8 21:15
 * @description
 */
@Data
public class TenantDTO implements Serializable {

    @Schema(description = "主键")
    private Long id;
    @Schema(description = "租户Code",hidden = true)
    private String tenantCode;
    @Schema(description = "租户名称")
    private String tenantName;
    @Schema(description = "域名地址")
    private String domain;
    @Schema(description = "联系人")
    private String contactName;
    @Schema(description = "联系电话")
    private String contactPhone;
    @Schema(description = "联系地址")
    private String address;
    @Schema(description = "账号额度")
    private Integer accountNumber;
    @DateTimeFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    @JsonFormat(shape = JsonFormat.Shape.STRING,timezone = "GMT+8", pattern = DatePattern.NORM_DATETIME_PATTERN,locale = "zh_CN")
    @JSONField(format= DatePattern.NORM_DATETIME_PATTERN)
    @Schema(description = "过期时间")
    private Date expireTime;
    @Schema(description = "授权码",hidden = true)
    private String licenseKey;

}
