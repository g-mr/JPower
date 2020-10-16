package com.wlcb.jpower.dbs.entity;

import com.wlcb.jpower.module.dbs.entity.base.BaseEntity;
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

    private String name;
    private Long fileSize;
    private String fileType;
    private String path;
    private byte[] content;
    private Integer sortNum;
    private String note;
}
