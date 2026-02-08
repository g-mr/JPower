package top.jpower.jpower.dbs.entity;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Table;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.jpower.core.dbs.dbs.entity.base.BaseEntity;
import top.jpower.core.dbs.dictbind.annotation.Dict;

/**
 * 文件
 *
 * @Author mr.g
 * @Date 2020-07-13 17:20
 */
@Data
@Table("tb_resource_file")
@EqualsAndHashCode(callSuper = true)
public class TbResourceFile extends BaseEntity {

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
    @Schema(description = "文件标识")
    private String mark;
    @Schema(description = "存储类型")
    @Dict(name = "FILE_STORAGE_TYPE")
    private String storageType;
    @Schema(description = "备注")
    private String note;

}
