package top.jpower.jpower.config.sms.properties;

import lombok.Data;

import java.io.Serializable;

/**
 * @author mr.g
 * @date 2024/3/6 4:08 PM
 */
@Data
public class AliSmsProperties implements Serializable {
    private static final long serialVersionUID = -8271991827279425816L;

    /**
     * 模板ID
     **/
    private String template;

    /**
     * accessKey
     **/
    private String accessKey;

    /**
     * secretKey
     **/
    private String secretKey;

    /**
     * 签名
     **/
    private String sign;

    /**
     * 区域ID
     **/
    private String regionId;
}
