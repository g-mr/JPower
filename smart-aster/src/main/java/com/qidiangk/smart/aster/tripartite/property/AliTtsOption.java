package com.qidiangk.smart.aster.tripartite.property;

import lombok.Data;

import java.io.Serializable;

/**
 * 阿里TTS配置类
 *
 * @author mr.g
 */
@Data
public class AliTtsOption implements Serializable {

    /**
     * accessKeyId
     */
    private String accessKeyId;
    /**
     * accessKeySecret
     */
    private String accessKeySecret;

    /**
     * appKey
     */
    private String appKey;

    /**
     * 音色
     */
    private String voice;
    /**
     * 音量，范围是0~100，可选，默认50。
     */
    private Integer volume;
    /**
     * 语调，范围是-500~500，可选，默认是0。
     */
    private Integer pitchRate;
    /**
     * 语速，范围是-500~500，默认是0。
     */
    private Integer speechRate;
}
