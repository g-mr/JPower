package com.qidiangk.smart.aster.tripartite.property;

import lombok.Data;

import java.io.Serializable;

/**
 * 阿里ASR配置类
 *
 * @author mr.g
 */
@Data
public class DianxinAsrOption implements Serializable {

    /**
     * appId
     */
    private String appId;
    /**
     * appKey
     */
    private String appKey;

    /**
     * asrUrl
     */
    private String asrUrl;

    /**
     * 是否开启校勘（仅对多方言服务有效），默认：否
     */
    private Boolean enableEmendation;
    /**
     * 句尾静音阈值，单位ms，最小值为10，最大无限制
     */
    private Integer maxEndSilence;
}
