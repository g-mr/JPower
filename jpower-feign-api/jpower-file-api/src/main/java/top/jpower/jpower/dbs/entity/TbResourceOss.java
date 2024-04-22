package top.jpower.jpower.dbs.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.jpower.jpower.module.dbs.entity.base.BaseEntity;

/**
 * @author mr.g
 * @date 2024/4/21 4:54 PM
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TbResourceOss extends BaseEntity {
    private static final long serialVersionUID = -1613026851565115477L;

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
    private String bucketName;
}
