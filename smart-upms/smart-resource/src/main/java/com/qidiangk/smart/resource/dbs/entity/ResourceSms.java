package com.qidiangk.smart.resource.dbs.entity;

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
import top.jpower.core.dbs.dictbind.annotation.Dict;

/**
 * 短信配置对象
 *
 * @author mr.g
 */
@Data
@Table("tb_resource_sms")
@EqualsAndHashCode(callSuper = true)
public class ResourceSms extends BaseEntity {

	@Schema(description = "ID")
	@Id(keyType = KeyType.Generator, value = KeyGenerators.flexId)
	@NotNull(message = "ID 不可为空", groups = {Validation.Update.class})
	private Long id;
    @Schema(description = "名称")
    @NotBlank(message = "名称 不可为空", groups = {Validation.Create.class, Validation.Update.class})
    private String name;
    @Schema(description = "分类 字典：SMS_CATEGORY")
    @NotBlank(message = "分类 不能为空", groups = {Validation.Create.class, Validation.Update.class})
    @Dict(name = "SMS_CATEGORY")
    private String category;
    @Schema(description = "编号")
    @NotBlank(message = "编码 不能为空", groups = {Validation.Create.class, Validation.Update.class})
    private String code;
    @Schema(description = "模板ID")
    private String template;
    @Schema(description = "accessKey")
    private String accessKey;
    @Schema(description = "secretKey")
    private String secretKey;
    @Schema(description = "短信签名")
    private String sign;
    @Schema(description = "区域ID")
    private String regionId;
    @Schema(description = "发送参数")
    private String parameters;
}
