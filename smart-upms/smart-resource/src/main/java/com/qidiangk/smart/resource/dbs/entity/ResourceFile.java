package com.qidiangk.smart.resource.dbs.entity;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.keygen.KeyGenerators;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.jpower.core.dbs.dbs.entity.base.BaseEntity;
import top.jpower.core.dbs.dictbind.annotation.Dict;

/**
 * 文件
 *
 * @author mr.g
 */
@Data
@Table("tb_resource_file")
@EqualsAndHashCode(callSuper = true)
public class ResourceFile extends BaseEntity {

	@Schema(description = "文件ID")
	@Id(keyType = KeyType.Generator, value = KeyGenerators.flexId)
	@NotNull(message = "主键不能为空")
	private Long id;
	@Schema(description = "文件分组ID")
	private Long groupId;
	@Schema(description = "文件名称")
    private String name;
    @Schema(description = "文件大小")
    private Long fileSize;
    @Schema(description = "文件类型")
    private String fileType;
    @Schema(description = "文件路径")
    private String path;
    @Schema(description = "文件内容")
    @Column(isLarge = true)
    private byte[] content;
	/**
	 * 文件ID加密字符串
	 */
    @Schema(description = "文件标识")
    private String mark;
    @Schema(description = "存储类型")
    @Dict(name = "FILE_STORAGE_TYPE")
    private String storageType;
    @Schema(description = "备注")
    private String note;

}
