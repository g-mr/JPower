package com.wlcb.jpower.dbs.entity.client;

import com.wlcb.jpower.module.dbs.entity.base.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName TbCoreClient
 * @Description TODO 客户端表
 * @Author 郭丁志
 * @Date 2020-07-31 12:55
 * @Version 1.0
 */
@Data
public class TbCoreClient extends BaseEntity {

    private static final long serialVersionUID = -4522536489931346102L;

    @ApiModelProperty("客户端名称")
    private String name;
    @ApiModelProperty("客户端编码")
    private String clientCode;
    @ApiModelProperty("客户端密钥")
    private String clientSecret;
    @ApiModelProperty("token有效时长 单位秒")
    private Long accessTokenValidity;
    @ApiModelProperty("刷新token有效时长 单位秒")
    private Long refreshTokenValidity;
    @ApiModelProperty("排序")
    private String sortNum;
    @ApiModelProperty("备注")
    private String note;

}
