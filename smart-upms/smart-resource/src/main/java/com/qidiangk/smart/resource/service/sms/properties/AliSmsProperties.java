package com.qidiangk.smart.resource.service.sms.properties;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 阿里短信配置属性类
 * <p>
 * 包含阿里云短信服务的配置信息
 * </p>
 *
 * @author mr.g
 */
@Data
public class AliSmsProperties implements Serializable {
    @Serial
	private static final long serialVersionUID = -8271991827279425816L;

    /**
     * 模板ID
     */
    private String template;

    /**
     * Access Key
     */
    private String accessKey;

    /**
     * Secret Key
     */
    private String secretKey;

    /**
     * 短信签名
     */
    private String sign;

    /**
     * 区域ID
     */
    private String regionId;

    /**
     * 模板参数
     */
    private String parameters;
}
