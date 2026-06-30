package com.qidiangk.smart.system.dbs.entity.dict;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.keygen.KeyGenerators;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.qidiangk.smart.common.validated.group.Validation;
import top.jpower.core.dbs.dbs.entity.base.BaseEntity;

/**
 * 字典类型表
 * 
 * @author mr.g
 */
@Data
@Table("tb_core_dict_type")
@EqualsAndHashCode(callSuper = true)
public class CoreDictType extends BaseEntity {

	@Schema(description = "主键")
	@Id(keyType = KeyType.Generator, value = KeyGenerators.flexId)
	@NotNull(message = "id不能为空", groups = Validation.Update.class)
	private Long id;
    @Schema(description = "字典类型编码")
	@NotBlank(message = "字典类型编码不能为空", groups = Validation.Create.class)
    private String dictTypeCode;
    @Schema(description = "字典类型名称")
	@NotBlank(message = "字典类型名称不能为空", groups = Validation.Create.class)
    private String dictTypeName;
    @Schema(description = "备注")
    private String note;
    @Schema(description = "是否允许删除")
    private Boolean delEnabled;
    @Schema(description = "排序")
    private Integer sortNum;
    @Schema(description = "父级ID")
    private Long parentId;
    @Schema(description = "是否树形结构")
    private Boolean isTree;

}
