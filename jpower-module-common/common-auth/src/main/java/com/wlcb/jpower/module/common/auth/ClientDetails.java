package com.wlcb.jpower.module.common.auth;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName TbCoreClient
 * @Description TODO 客户端表
 * @Author 郭丁志
 * @Date 2020-07-31 12:55
 * @Version 1.0
 */
@Data
public class ClientDetails implements Serializable {

    @ApiModelProperty("客户端code")
    private String clientCode;
    @ApiModelProperty("客户端密钥")
    private String clientSecret;
    @ApiModelProperty("token有效时长 单位秒")
    private Long accessTokenValidity;
    @ApiModelProperty("刷新token有效时长 单位秒")
    private Long refreshTokenValidity;

}
