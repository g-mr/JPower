package com.qidiangk.smart.system.dbs.entity.client;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.keygen.KeyGenerators;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.jpower.core.dbs.dbs.entity.base.BaseEntity;
import top.jpower.core.dbs.dictbind.annotation.Dict;

/**
 * 客户端信息
 * 
 * @author mr.g
 */
@Data
@Table("tb_core_client")
@EqualsAndHashCode(callSuper = true)
public class CoreClient extends BaseEntity {

    @Schema(description = "主键")
    @Id(keyType = KeyType.Generator, value = KeyGenerators.flexId)
    private Long id;
    @Schema(description = "客户端名称")
	@NotBlank(message = "客户端名称不可为空")
    private String name;
    @Schema(description = "客户端编码")
	@NotBlank(message = "客户端编码不可为空")
    private String clientCode;
    @Schema(description = "客户端密钥")
	@NotBlank(message = "客户端密钥不可为空")
    private String clientSecret;
    @Schema(description = "登录限制")
    @Dict(name = "LOGIN_LIMIT")
	@NotBlank(message = "登录限制不可为空")
    private String loginLimit;
    @Schema(description = "token有效时长 单位秒")
	@NotNull(message = "token有效时长不可为空")
    private Long accessTokenValidity;
    @Schema(description = "刷新token有效时长 单位秒")
	@NotNull(message = "刷新token有效时长不可为空")
    private Long refreshTokenValidity;
    @Schema(description = "排序")
    private Integer sortNum;
    @Schema(description = "备注")
    private String note;

}
