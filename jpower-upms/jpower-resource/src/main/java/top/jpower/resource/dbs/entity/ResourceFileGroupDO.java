package top.jpower.resource.dbs.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.keygen.KeyGenerators;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.jpower.common.validated.group.Validation;
import top.jpower.core.dbs.dbs.entity.base.BaseEntity;

/**
 * 文件
 *
 * @author mr.g
 */
@Data
@Table("tb_resource_file_group")
@EqualsAndHashCode(callSuper = true)
public class ResourceFileGroupDO extends BaseEntity {

	@Schema(description = "文件分组ID")
	@Id(keyType = KeyType.Generator, value = KeyGenerators.flexId)
	@NotNull(message = "主键不能为空", groups = {Validation.Update.class})
	private Long id;
	@Schema(description = "文件分组名称")
	@NotBlank(message = "文件分组名称不能为空", groups = {Validation.Create.class, Validation.Update.class})
    private String name;

}
