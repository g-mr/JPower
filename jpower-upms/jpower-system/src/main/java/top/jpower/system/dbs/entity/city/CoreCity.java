package top.jpower.system.dbs.entity.city;

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
import top.jpower.core.dbs.dictbind.annotation.Dict;

/**
 * 城市地区
 * 
 * @author mr.g
 */
@Data
@Table("tb_core_city")
@EqualsAndHashCode(callSuper = true)
public class CoreCity extends BaseEntity {

    @Schema(description = "主键")
    @Id(keyType = KeyType.Generator, value = KeyGenerators.flexId)
	@NotNull(message = "主键不可为空", groups = {Validation.Update.class})
    private Long id;
    @Schema(description = "编码")
	@NotBlank(message = "编码不可为空", groups = {Validation.Create.class, Validation.Update.class})
    private String code;
    @Schema(description = "父级编码")
    private String pcode;
	@Schema(description = "城市区号")
	private String cityCode;
    @Schema(description = "名称")
	@NotBlank(message = "名称不可为空", groups = {Validation.Create.class, Validation.Update.class})
    private String name;
    @Schema(description = "全称")
    private String fullname;
    @Schema(description = "级别")
	@NotNull(message = "级别不可为空", groups = {Validation.Create.class, Validation.Update.class})
    private Integer rankd;
    @Schema(description = "经度")
    private Double lng;
    @Schema(description = "纬度")
    private Double lat;
    @Schema(description = "国家编码")
    private String countryCode;
    @Schema(description = "城市类型 字典CITY_TYPE")
    @Dict(name = "CITY_TYPE")
    private String cityType;
    @Schema(description = "备注")
    private String note;
    @Schema(description = "排序")
    private Integer sortNum;

}
