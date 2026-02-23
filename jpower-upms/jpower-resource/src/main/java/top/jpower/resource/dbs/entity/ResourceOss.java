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
import top.jpower.core.dbs.dictbind.annotation.Dict;


/**
 * OSS对象存储
 *
 * @author mr.g
 */
@Data
@Table("tb_resource_oss")
@EqualsAndHashCode(callSuper = true)
public class ResourceOss extends BaseEntity {

	@Schema(description = "ID")
	@Id(keyType = KeyType.Generator, value = KeyGenerators.flexId)
	@NotNull(message = "ID 不可为空", groups = {Validation.Update.class})
	private Long id;

    @Schema(description = "OSS类型 字典：OSS_CATEGORY")
    @Dict(name = "OSS_CATEGORY")
    @NotBlank(message = "OSS类型 不可为空", groups = {Validation.Create.class, Validation.Update.class})
    private String category;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "编码")
    private String code;

    @Schema(description = "accessKey")
    private String accessKey;

    @Schema(description = "secretKey")
    private String secretKey;

    @Schema(description = "内网地址")
    private String internalAddress;

    @Schema(description = "外网地址")
    private String externalAddress;

    @Schema(description = "空间名称")
    @NotBlank(message = "空间名称 不可为空", groups = {Validation.Create.class, Validation.Update.class})
    private String bucketName;

    @Schema(description = "区域")
    private String region;
}
