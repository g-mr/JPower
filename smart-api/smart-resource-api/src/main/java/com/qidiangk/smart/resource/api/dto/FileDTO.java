package com.qidiangk.smart.resource.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 文件信息
 *
 * @author mr.g
 */
@Data
public class FileDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -6889020017108014146L;

    @Schema(description = "文件名称")
    private String name;
    @Schema(description = "文件大小")
    private Long fileSize;
    @Schema(description = "文件类型")
    private String fileType;
    @Schema(description = "文件路径")
    private String path;
    @Schema(description = "文件标识")
    private String mark;
    @Schema(description = "文件内容")
    private byte[] content;
    @Schema(description = "存储类型")
    private String storageType;
    @Schema(description = "备注")
    private String note;

}
