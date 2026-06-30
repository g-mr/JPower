package com.qidiangk.smart.maxkb.client.po.agent;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.Instant;
import java.util.Date;

/**
 * 智能体列表DO
 *
 * @author mr.g
 */
@Data
public class ApplicationListDO {

    @Schema(description = "应用ID")
    private String id;

    @Schema(description = "应用名称")
    private String name;

    @Schema(description = "应用描述")
    private String desc;

    @Schema(description = "是否发布")
    private Boolean isPublish;

    @Schema(description = "应用类型")
    private String type;

    @Schema(description = "资源类型")
    private String resourceType;

    @Schema(description = "工作区ID")
    private String workspaceId;

    @Schema(description = "文件夹ID")
    private String folderId;

    @Schema(description = "用户ID")
    private String userId;

    @Schema(description = "昵称")
    private String nickName;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "更新时间")
    private Date updateTime;

    @Schema(description = "发布时间")
    private Date publishTime;

    @Schema(description = "图标")
    private String icon;

}
