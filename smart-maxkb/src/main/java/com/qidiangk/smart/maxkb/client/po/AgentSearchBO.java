package com.qidiangk.smart.maxkb.client.po;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.io.Serial;

/**
 * 智能体搜索条件
 *
 * @author mr.g
 */
@Getter
@Builder
public class AgentSearchBO extends SearchBO {

    @Serial
    private static final long serialVersionUID = -2086200032724171031L;

    @Schema(description = "文件夹ID")
    private String folderId;

    /**
     * published：发布
     * unpublished：未发布
     **/
    @Schema(description = "发布状态")
    private String publishStatus;

    @Schema(description = "智能体名称")
    private String name;
}
