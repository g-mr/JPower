package top.jpower.system.dbs.entity.dict;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.keygen.KeyGenerators;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.jpower.core.dbs.dictbind.annotation.Dict;
import top.jpower.core.dbs.tenant.entity.TenantEntity;

/**
 * 字典
 * 
 * @author mr.g
 */
@Data
@Table("tb_core_dict")
@EqualsAndHashCode(callSuper = true)
public class CoreDict extends TenantEntity {

	@Schema(description = "主键")
	@Id(keyType = KeyType.Generator, value = KeyGenerators.flexId)
	private Long id;
    @Schema(description = "字典类型编码")
	@NotBlank(message = "字典类型编码不能为空")
    private String dictTypeCode;
    @Schema(description = "字典编码")
	@NotBlank(message = "字典编码不能为空")
    private String code;
    @Schema(description = "字典名称")
	@NotBlank(message = "字典名称不能为空")
    private String name;
    @Schema(description = "语言类型 字典YYZL")
    @Dict(name = "YYZL")
    private String locale;
    @Schema(description = "是否停用")
    private Boolean isStop;
    @Schema(description = "备注")
    private String note;
    @Schema(description = "排序")
    private Integer sortNum;
    @Schema(description = "父级ID")
    private Long parentId;
    @Schema(description = "级别")
    private Integer dictLevel;

}
