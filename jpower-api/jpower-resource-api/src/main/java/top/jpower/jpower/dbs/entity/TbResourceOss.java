package top.jpower.jpower.dbs.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.jpower.core.dbs.dictbind.annotation.Dict;
import top.jpower.core.dbs.dbs.entity.base.BaseEntity;

import javax.validation.constraints.NotBlank;

/**
 * @author mr.g
 * @date 2024/4/21 4:54 PM
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TbResourceOss extends BaseEntity {
    private static final long serialVersionUID = -1613026851565115477L;

    @ApiModelProperty("OSS类型 字典：OSS_CATEGORY")
    @Dict(name = "OSS_CATEGORY")
    @NotBlank(message = "OSS类型 不可为空")
    private String category;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("编码")
    private String code;

    @ApiModelProperty("accessKey")
    private String accessKey;

    @ApiModelProperty("secretKey")
    private String secretKey;

    @ApiModelProperty("内网地址")
    private String internalAddress;

    @ApiModelProperty("外网地址")
    private String externalAddress;

    @ApiModelProperty("空间名称")
    @NotBlank(message = "空间名称 不可为空")
    private String bucketName;

    @ApiModelProperty("区域")
    private String region;
}
