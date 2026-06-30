package com.qidiangk.smart.maxkb.client.po.agent;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 智能体列表DO
 *
 * @author mr.g
 */
@Data
public class ApplicationKeyDO {

    @Schema(description = "密钥ID")
    private String id;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "更新时间")
    private Date updateTime;

    @Schema(description = "密钥字符串")
    private String secretKey;

    @Schema(description = "工作空间ID")
    private String workspaceId;

    @Schema(description = "是否启用")
    private Boolean isActive;

    @Schema(description = "是否允许跨域")
    private Boolean allowCrossDomain;

    @Schema(description = "允许跨域域名列表")
    private List<String> crossDomainList;

    @Schema(description = "过期时间")
    private Date expireTime;

    @Schema(description = "是否永久有效")
    private Boolean isPermanent;

    @Schema(description = "关联用户ID")
    private String user;

    @Schema(description = "关联应用ID")
    private String application;

}
