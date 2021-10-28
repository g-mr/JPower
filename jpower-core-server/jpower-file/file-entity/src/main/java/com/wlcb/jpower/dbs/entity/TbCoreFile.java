package com.wlcb.jpower.dbs.entity;

import com.wlcb.jpower.module.base.annotation.Dict;
import com.wlcb.jpower.module.dbs.entity.base.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @ClassName TbCoreFile
 * @Description TODO 文件
 * @Author 郭丁志
 * @Date 2020-07-13 17:20
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TbCoreFile extends BaseEntity {

    private static final long serialVersionUID = -6889020017108014146L;

    @ApiModelProperty("文件名称")
    private String name;
    @ApiModelProperty("文件大小")
    private Long fileSize;
    @ApiModelProperty("文件类型")
    private String fileType;
    @ApiModelProperty("文件路径")
    private String path;
    @ApiModelProperty("文件内容")
    private byte[] content;
    @ApiModelProperty("文件标识")
    private String mark;
    @ApiModelProperty("存储类型")
    @Dict(name = "FILE_STORAGE_TYPE")
    private String storageType;
    @ApiModelProperty("备注")
    private String note;
}
